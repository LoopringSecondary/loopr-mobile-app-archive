//
//  UpdatedMarketPlaceOrderViewController.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit
import Geth
import BigInt
import NotificationBannerSwift

class MarketPlaceOrderViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, NumericKeyboardDelegate, NumericKeyboardProtocol {

    var market: MarketV1!
    
    var initialType: TradeType = .buy
    var initialPrice: String?
    
    private var types: [TradeType] = [.buy, .sell]
    
    // TODO: needs to update buys and sells in Relay 2.0
    var buys: [Depth] = []
    var sells: [Depth] = []
    
    // TODO: needs to change tableView1 to a UIView
    @IBOutlet weak var tableView1: UITableView!
    @IBOutlet weak var tableView2: UITableView!

    // keyboard
    var marketPlaceOrderTableViewCell: MarketPlaceOrderTableViewCell!
    
    var isNumericKeyboardShow: Bool = false
    var numericKeyboardView: DefaultNumericKeyboard!
    var numericKeyboardBaseView: UIView = UIView()
    
    var orderAmount: Double = 0
    
    // config
    var type: TradeType = .buy
    var tokenS: String = ""
    var tokenB: String = ""
    
    // Expires
    var orderIntervalTime = SettingDataManager.shared.getOrderIntervalTime()
    
    var blurVisualEffectView = UIView(frame: .zero)
    
    // Data source
    var orders: [Order] = []
    
    var isLaunching: Bool = true

    var previousOrderCount: Int = 0
    var pageIndex: UInt = 1
    var hasMoreData: Bool = true
    
    let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setBackButton()
        navigationItem.title = PlaceOrderDataManager.shared.market?.description ?? LocalizedString("Trade", comment: "")
        view.theme_backgroundColor = ColorPicker.backgroundColor
        tableView1.theme_backgroundColor = ColorPicker.backgroundColor
        
        tableView1.dataSource = self
        tableView1.delegate = self
        
        tableView1.tableFooterView = UIView(frame: .zero)
        tableView1.separatorStyle = .none
        tableView1.isScrollEnabled = false
        tableView1.tag = 1

        tableView2.theme_backgroundColor = ColorPicker.backgroundColor
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: 320, height: 0))
        tableView2.tableHeaderView = headerView

        tableView2.dataSource = self
        tableView2.delegate = self
        tableView2.separatorStyle = .none
        tableView2.tag = 2

        let window = UIApplication.shared.keyWindow
        let bottomPadding = (window?.safeAreaInsets.bottom ?? 0)
        let footerView = UIView(frame: CGRect(x: 0, y: 0, width: 200, height: bottomPadding))
        footerView.backgroundColor = .clear
        tableView2.tableFooterView = footerView
        
        tableView2.refreshControl = refreshControl
        refreshControl.updateUIStyle(withTitle: RefreshControlDataManager.shared.get(type: .orderHistoryViewController))
        refreshControl.addTarget(self, action: #selector(refreshData), for: .valueChanged)
        
        // let tap = UITapGestureRecognizer(target: self, action: #selector(handleTap(_:)))
        // tableView2.addGestureRecognizer(tap)
        
        getDataFromRelay()

        let nib = Bundle.main.loadNibNamed("MarketPlaceOrderTableViewCell", owner: self, options: nil)
        marketPlaceOrderTableViewCell = nib![0] as! MarketPlaceOrderTableViewCell
        
        numericKeyboardBaseView.theme_backgroundColor = ColorPicker.backgroundColor
        numericKeyboardBaseView.backgroundColor = .clear
        
        blurVisualEffectView.backgroundColor = UIColor.black.withAlphaComponent(0.8)
        blurVisualEffectView.alpha = 1
        blurVisualEffectView.frame = UIScreen.main.bounds
    }
    
    @objc private func refreshData() {
        pageIndex = 1
        hasMoreData = true
        getDataFromRelay()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        // Update the orderbook after users place a order successfully.
        getDataFromRelay()
    }

    // This will conflict to tableview2 click
    @objc func handleTap(_ sender: UITapGestureRecognizer?) {
        hideNumericKeyboard()
        marketPlaceOrderTableViewCell.priceTextField.resignFirstResponder()
        marketPlaceOrderTableViewCell.amountTextField.resignFirstResponder()
    }

    private func getDataFromRelay() {
        OrderDataManager.shared.getOrdersFromServer(pageIndex: pageIndex, status: OrderStatus.pending_active.rawValue, completionHandler: { _ in
            DispatchQueue.main.async {
                if self.isLaunching {
                    self.isLaunching = false
                }
                self.orders = OrderDataManager.shared.getOrders(type: .open)
                if self.previousOrderCount != self.orders.count {
                    self.hasMoreData = true
                } else {
                    self.hasMoreData = false
                }
                self.previousOrderCount = self.orders.count
                self.refreshControl.endRefreshing(refreshControlType: .orderHistoryViewController)
                
                self.tableView2.reloadData()
            }
        })
        
        MarketDepthDataManager.shared.getDepthFromServer(market: market.name, completionHandler: { buys, sells, _ in
            self.buys = buys
            self.sells = sells

            DispatchQueue.main.async {
                // Update the orderbook on the right
                self.marketPlaceOrderTableViewCell.setBuys(buys)
                self.marketPlaceOrderTableViewCell.setSells(sells)
                self.marketPlaceOrderTableViewCell.orderbookTableView.reloadData()
            }
        })
    }
    
    private func isTableEmpty() -> Bool {
        return orders.count == 0 && !isLaunching
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if tableView.tag == tableView1.tag {
            return 0
            
        } else {
            if orders.count == 0 {
                return 0
            }
            return 30+0.5
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if tableView.tag == tableView1.tag {
            return nil
        } else {
            return UIView.getOrderHistoryHeaderView()
        }
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableView.tag == tableView1.tag {
            return 1
        } else {
            return isTableEmpty() ? 1 : orders.count
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if tableView.tag == tableView1.tag {
            return MarketPlaceOrderTableViewCell.getHeight()
        } else {
            if isTableEmpty() {
                return OrderNoDataTableViewCell.getHeight() / 1.8
            } else {
                return OrderTableViewCell.getHeight()
            }
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if tableView.tag == tableView1.tag {
            marketPlaceOrderTableViewCell.marketPlaceOrderViewController = self
            marketPlaceOrderTableViewCell.market = market
            marketPlaceOrderTableViewCell.type = initialType
            if Double(initialPrice ?? "") != nil {
                marketPlaceOrderTableViewCell.setPriceTextField(priceValue: Double(initialPrice!)!)
            }
            
            marketPlaceOrderTableViewCell.setBuys(buys)
            marketPlaceOrderTableViewCell.setSells(sells)
            
            marketPlaceOrderTableViewCell.update()
            marketPlaceOrderTableViewCell.updateUI()
            return marketPlaceOrderTableViewCell
        } else {
            if isTableEmpty() {
                var cell = tableView.dequeueReusableCell(withIdentifier: OrderNoDataTableViewCell.getCellIdentifier()) as? OrderNoDataTableViewCell
                if cell == nil {
                    let nib = Bundle.main.loadNibNamed("OrderNoDataTableViewCell", owner: self, options: nil)
                    cell = nib![0] as? OrderNoDataTableViewCell
                }
                cell?.noDataLabel.text = LocalizedString("No-opened-order", comment: "")
                cell?.noDataImageView.image = UIImage(named: "No-data-order")
                return cell!
            } else {
                var cell = tableView.dequeueReusableCell(withIdentifier: OrderTableViewCell.getCellIdentifier()) as? OrderTableViewCell
                if cell == nil {
                    let nib = Bundle.main.loadNibNamed("OrderTableViewCell", owner: self, options: nil)
                    cell = nib![0] as? OrderTableViewCell
                }
                let order: Order
                order = orders[indexPath.row]

                cell?.order = order
                cell?.pressedCancelButtonClosure = {
                    let title = LocalizedString("You are going to cancel the order.", comment: "")
                    let alert = UIAlertController(title: title, message: nil, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: LocalizedString("Confirm", comment: ""), style: .default, handler: { _ in
                        SendCurrentAppWalletDataManager.shared._cancelOrder(order: order.originalOrder, completion: { (txHash, error) in
                            // TODO: if the page index is not 1, it may have some bugs
                            self.refreshData()
                            self.completion(txHash, error)
                        })
                    }))
                    alert.addAction(UIAlertAction(title: LocalizedString("Cancel", comment: ""), style: .cancel, handler: { _ in
                    }))
                    self.present(alert, animated: true, completion: nil)
                }
                cell?.update()
                
                // Pagination
                if hasMoreData && indexPath.row == orders.count - 1 {
                    pageIndex += 1
                    getDataFromRelay()
                }
                
                return cell!
            }
        }
    }
    
    func showNumericKeyboard(textField: UITextField) {
        if !isNumericKeyboardShow {
            let width = view.frame.width
            let height = view.height
            let window = UIApplication.shared.keyWindow
            let bottomPadding = (window?.safeAreaInsets.bottom ?? 0)

            numericKeyboardView = DefaultNumericKeyboard(frame: CGRect(x: 0, y: 0, width: width, height: DefaultNumericKeyboard.height))
            numericKeyboardView.collectionView.theme_backgroundColor = ColorPicker.backgroundColor
            numericKeyboardView.delegate = self
            
            numericKeyboardBaseView.frame = CGRect(x: 0, y: height, width: width, height: DefaultNumericKeyboard.height + bottomPadding)
            numericKeyboardBaseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
            numericKeyboardBaseView.addSubview(numericKeyboardView)
            view.addSubview(numericKeyboardBaseView)

            let destinateY = height - bottomPadding - DefaultNumericKeyboard.height
            
            UIView.animate(withDuration: 0.3, delay: 0, options: .curveEaseInOut, animations: {
                self.numericKeyboardBaseView.frame = CGRect(x: 0, y: destinateY, width: width, height: DefaultNumericKeyboard.height + bottomPadding)
            }, completion: { finished in
                self.isNumericKeyboardShow = true
            })
        } else {
            
        }
    }
    
    func hideNumericKeyboard() {
        if isNumericKeyboardShow {
            let width = view.frame.width
            let height = view.height
            let destinateY = height
            
            let window = UIApplication.shared.keyWindow
            let bottomPadding = (window?.safeAreaInsets.bottom ?? 0)
            
            UIView.animate(withDuration: 0.3, delay: 0, options: .curveEaseInOut, animations: {
                self.numericKeyboardBaseView.frame = CGRect(x: 0, y: destinateY, width: width, height: DefaultNumericKeyboard.height + bottomPadding)
            }, completion: { finished in
                self.isNumericKeyboardShow = false
                if finished {
                }
            })
        } else {
            
        }
    }
    
    func numericKeyboard(_ numericKeyboard: NumericKeyboard, itemTapped item: NumericKeyboardItem, atPosition position: Position) {
        print("pressed keyboard: (\(position.row), \(position.column))")
        let activeTextField = getActiveTextField()
        guard activeTextField != nil else {
            return
        }
        var currentText = activeTextField!.text ?? ""
        switch (position.row, position.column) {
        case (3, 0):
            if !currentText.contains(".") {
                if currentText == "" {
                    activeTextField!.text = "0."
                } else {
                    activeTextField!.text = currentText + "."
                }
            }
        case (3, 1):
            activeTextField!.text = currentText + "0"
        case (3, 2):
            if currentText.count > 0 {
                currentText = String(currentText.dropLast())
                if currentText == "0" {
                    currentText = ""
                }
            }
            activeTextField!.text = currentText
        default:
            let itemValue = position.row * 3 + position.column + 1
            activeTextField!.text = currentText + String(itemValue)
        }
        _ = validate()
    }
    
    func numericKeyboard(_ numericKeyboard: NumericKeyboard, itemLongPressed item: NumericKeyboardItem, atPosition position: Position) {
        print("Long pressed keyboard: (\(position.row), \(position.column))")
        
        let activeTextField = getActiveTextField()
        guard activeTextField != nil else {
            return
        }
        var currentText = activeTextField!.text ?? ""
        if (position.row, position.column) == (3, 2) {
            if currentText.count > 0 {
                currentText = String(currentText.dropLast())
            }
            activeTextField!.text = currentText
        }
    }
    
    func getActiveTextField() -> UITextField? {
        if marketPlaceOrderTableViewCell.activeTextFieldTag == marketPlaceOrderTableViewCell.priceTextField.tag {
            return marketPlaceOrderTableViewCell.priceTextField
        } else if marketPlaceOrderTableViewCell.activeTextFieldTag == marketPlaceOrderTableViewCell.amountTextField.tag {
            return marketPlaceOrderTableViewCell.amountTextField
        } else {
            return nil
        }
    }
    
    func validate() -> Bool {
        var isValid = false
        if marketPlaceOrderTableViewCell.activeTextFieldTag == marketPlaceOrderTableViewCell.priceTextField.tag {
            isValid = validateTokenPrice()
        } else if marketPlaceOrderTableViewCell.activeTextFieldTag == marketPlaceOrderTableViewCell.amountTextField.tag {
            isValid = validateAmount()
        } else {
            isValid = validateTokenPrice() && validateAmount()
        }
        guard isValid else {
            return false
        }
        if validateTokenPrice() && validateAmount() {
            isValid = true
            var total: Double
            total = Double(marketPlaceOrderTableViewCell.priceTextField.text!.removeComma())! * Double(marketPlaceOrderTableViewCell.amountTextField.text!.removeComma())!
            self.orderAmount = total
        }
        return isValid
    }
    
    func validateTokenPrice(withErrorNotification: Bool = true) -> Bool {
        if let value = Double(marketPlaceOrderTableViewCell.priceTextField.text!.removeComma()) {
            let validate = value > 0.0
            if validate {
                let tokenBPrice = PriceDataManager.shared.getPrice(of: PlaceOrderDataManager.shared.tokenB.symbol)!
                let estimateValue: Double = value * tokenBPrice
                marketPlaceOrderTableViewCell.priceTipLabel.text = "≈ \(estimateValue.currency)"
                marketPlaceOrderTableViewCell.priceTipLabel.isHidden = false
                marketPlaceOrderTableViewCell.priceTipLabel.theme_textColor = GlobalPicker.textLightColor
            } else {
                if withErrorNotification {
                    marketPlaceOrderTableViewCell.priceTipLabel.text = LocalizedString("Please input a valid price", comment: "")
                    marketPlaceOrderTableViewCell.priceTipLabel.isHidden = false
                    marketPlaceOrderTableViewCell.priceTipLabel.textColor = .fail
                    marketPlaceOrderTableViewCell.priceTipLabel.shake()
                }
            }
            return validate
        } else {
            if marketPlaceOrderTableViewCell.activeTextFieldTag == marketPlaceOrderTableViewCell.priceTextField.tag {
                marketPlaceOrderTableViewCell.priceTipLabel.isHidden = false
                marketPlaceOrderTableViewCell.priceTipLabel.theme_textColor = GlobalPicker.textLightColor
                marketPlaceOrderTableViewCell.priceTipLabel.text = "≈ \(0.0.currency)"
            }
            return false
        }
    }
    
    func validateAmount(withErrorNotification: Bool = true) -> Bool {
        if let value = Double(marketPlaceOrderTableViewCell.amountTextField.text!.removeComma()) {
            let validate = value > 0.0
            return validate
        } else {
            return false
        }
    }

    func switchToBuy() {
        type = .buy
        marketPlaceOrderTableViewCell.type = .buy
        marketPlaceOrderTableViewCell.update()
    }

    func switchToSell() {
        type = .sell
        marketPlaceOrderTableViewCell.type = .sell
        marketPlaceOrderTableViewCell.update()
    }

    func pressedPlaceOrderButton() {
        print("pressedPlaceOrderButton")
        marketPlaceOrderTableViewCell.priceTextField.resignFirstResponder()
        marketPlaceOrderTableViewCell.amountTextField.resignFirstResponder()
        
        let isPriceValid = validateTokenPrice()
        let isAmountValid = validateAmount()
        
        // Need to call validate()
        if isPriceValid && isAmountValid && validate() {
            hideNumericKeyboard()
            self.pushController()
        }
        if !isPriceValid {
            marketPlaceOrderTableViewCell.priceTipLabel.textColor = .fail
            marketPlaceOrderTableViewCell.priceTipLabel.isHidden = false
            marketPlaceOrderTableViewCell.priceTipLabel.text = LocalizedString("Please input a valid price", comment: "")
            marketPlaceOrderTableViewCell.priceView.shake(withTranslation: 5)
        }

        if !isAmountValid {
            marketPlaceOrderTableViewCell.amountView.shake(withTranslation: 5)
        }
    }

    func pushController() {
        if let order = constructOrder() {
            let viewController = PlaceOrderConfirmationViewController()
            viewController.order = order
            viewController.price = marketPlaceOrderTableViewCell.priceTextField.text
            
            // viewController.transitioningDelegate = self
            viewController.modalPresentationStyle = .overFullScreen
            viewController.dismissClosure = {
                UIView.animate(withDuration: 0.2, animations: {
                    self.blurVisualEffectView.alpha = 0.0
                }, completion: {(_) in
                    self.blurVisualEffectView.removeFromSuperview()
                })
            }

            self.present(viewController, animated: true) {
                // self.dismissInteractor.attachToViewController(viewController: viewController, withView: viewController.containerView, presentViewController: nil, backgroundView: self.blurVisualEffectView)
            }
            
            self.navigationController?.view.addSubview(self.blurVisualEffectView)
            UIView.animate(withDuration: 0.3, animations: {
                self.blurVisualEffectView.alpha = 1.0
            }, completion: {(_) in
                
            })
            
            viewController.parentNavController = self.navigationController
        }
    }

    func constructOrder() -> OriginalOrder? {
        var buyNoMoreThanAmountB: Bool
        var side, tokenSell, tokenBuy: String
        var amountBuy, amountSell, lrcFee: Double
        var amountB, amountS: BigInt
        if self.type == .buy {
            side = "buy"
            tokenBuy = PlaceOrderDataManager.shared.tokenA.symbol
            tokenSell = PlaceOrderDataManager.shared.tokenB.symbol
            buyNoMoreThanAmountB = true
            amountBuy = Double(marketPlaceOrderTableViewCell.amountTextField.text!.removeComma())!
            amountSell = self.orderAmount
            amountB = BigInt.generate(from: amountBuy, by: PlaceOrderDataManager.shared.tokenA.decimals)
            amountS = BigInt.generate(from: amountSell, by: PlaceOrderDataManager.shared.tokenB.decimals)
        } else {
            side = "sell"
            tokenBuy = PlaceOrderDataManager.shared.tokenB.symbol
            tokenSell = PlaceOrderDataManager.shared.tokenA.symbol
            buyNoMoreThanAmountB = false
            amountBuy = self.orderAmount
            amountSell = Double(marketPlaceOrderTableViewCell.amountTextField.text!.removeComma())!
            amountB = BigInt.generate(from: amountBuy, by: PlaceOrderDataManager.shared.tokenB.decimals)
            amountS = BigInt.generate(from: amountSell, by: PlaceOrderDataManager.shared.tokenA.decimals)
        }
        
        lrcFee = getLrcFee(amountSell, tokenSell)
        let delegate = RelayAPIConfiguration.delegateAddress
        let address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
        let since = Int64(Date().timeIntervalSince1970)
        let until = Int64(Calendar.current.date(byAdding: orderIntervalTime.intervalUnit, value: orderIntervalTime.intervalValue, to: Date())!.timeIntervalSince1970)
        var order = OriginalOrder(delegate: delegate, address: address, side: side, tokenS: tokenSell, tokenB: tokenBuy, validSince: since, validUntil: until, amountBuy: amountBuy, amountSell: amountSell, lrcFee: lrcFee, buyNoMoreThanAmountB: buyNoMoreThanAmountB, amountS: amountS, amountB: amountB)
        PlaceOrderDataManager.shared.completeOrder(&order)
        return order
    }

}

extension MarketPlaceOrderViewController {

    // TODO: this will be out of date in Relay 2.0
    func getLrcFee(_ amountS: Double, _ tokenS: String) -> Double {
        var result: Double = 0
        let pair = tokenS + "/LRC"
        let ratio = SettingDataManager.shared.getLrcFeeRatio()
        if let market = MarketDataManager.shared.getMarket(byTradingPair: pair) {
            result = market.balance * amountS * ratio
        } else if let price = PriceDataManager.shared.getPrice(of: tokenS),
            let lrcPrice = PriceDataManager.shared.getPrice(of: "LRC") {
            result = price * amountS * ratio / lrcPrice
        }
        // do not know what this logic for. temp annotation
        let minLrc = GasDataManager.shared.getGasAmount(by: "eth_transfer", in: "LRC")
        return max(result, minLrc)
    }

}

// TODO: needs to merge MarketPlaceOrderViewController and OrderHistoryViewController
extension MarketPlaceOrderViewController {
    
    func completion(_ txHash: String?, _ error: Error?) {
        var title: String = ""
        guard error == nil && txHash != nil else {
            DispatchQueue.main.async {
                title = NSLocalizedString("Order cancellation Failed, Please try again.", comment: "")
                let banner = NotificationBanner.generate(title: title, style: .danger)
                banner.duration = 5
                banner.show()
            }
            return
        }
        DispatchQueue.main.async {
            print(txHash!)
            title = NSLocalizedString("Order cancellation Successful.", comment: "")
            let banner = NotificationBanner.generate(title: title, style: .success)
            banner.duration = 2
            banner.show()
        }
    }
}
