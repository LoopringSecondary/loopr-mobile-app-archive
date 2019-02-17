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

class UpdatedMarketPlaceOrderViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, NumericKeyboardDelegate, NumericKeyboardProtocol {

    var market: Market!
    
    var initialType: TradeType = .buy
    var initialPrice: String?
    
    private var types: [TradeType] = [.buy, .sell]
    
    // TODO: needs to update buys and sells in Relay 2.0
    var buys: [Depth] = []
    var sells: [Depth] = []
    
    @IBOutlet weak var tableView: UITableView!
    
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
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setBackButton()
        navigationItem.title = PlaceOrderDataManager.shared.market?.description ?? LocalizedString("Trade", comment: "")
        view.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.theme_backgroundColor = ColorPicker.backgroundColor
        
        tableView.dataSource = self
        tableView.delegate = self
        
        tableView.tableFooterView = UIView(frame: .zero)
        tableView.separatorStyle = .none
        
        let nib = Bundle.main.loadNibNamed("MarketPlaceOrderTableViewCell", owner: self, options: nil)
        marketPlaceOrderTableViewCell = nib![0] as! MarketPlaceOrderTableViewCell
        
        numericKeyboardBaseView.theme_backgroundColor = ColorPicker.backgroundColor
        numericKeyboardBaseView.backgroundColor = .clear
        
        blurVisualEffectView.backgroundColor = UIColor.black.withAlphaComponent(0.8)
        blurVisualEffectView.alpha = 1
        blurVisualEffectView.frame = UIScreen.main.bounds
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return MarketPlaceOrderTableViewCell.getHeight()
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        marketPlaceOrderTableViewCell.updatedMarketPlaceOrderViewController = self
        marketPlaceOrderTableViewCell.market = market
        marketPlaceOrderTableViewCell.type = initialType
        
        marketPlaceOrderTableViewCell.setBuys(buys)
        marketPlaceOrderTableViewCell.setSells(sells)
        
        marketPlaceOrderTableViewCell.update()
        marketPlaceOrderTableViewCell.updateUI()
        return marketPlaceOrderTableViewCell
    }
    
    func showNumericKeyboard(textField: UITextField) {
        if !isNumericKeyboardShow {
            let width = view.frame.width
            let height = tableView.height
            let window = UIApplication.shared.keyWindow
            let bottomPadding = (window?.safeAreaInsets.bottom ?? 0)

            numericKeyboardView = DefaultNumericKeyboard(frame: CGRect(x: 0, y: 0, width: width, height: DefaultNumericKeyboard.height))
            numericKeyboardView.collectionView.theme_backgroundColor = ColorPicker.backgroundColor
            numericKeyboardView.delegate = self
            
            numericKeyboardBaseView.frame = CGRect(x: 0, y: height, width: width, height: DefaultNumericKeyboard.height + bottomPadding)
            numericKeyboardBaseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
            numericKeyboardBaseView.addSubview(numericKeyboardView)
            tableView.addSubview(numericKeyboardBaseView)

            let destinateY = height - bottomPadding - DefaultNumericKeyboard.height
            
            UIView.animate(withDuration: 0.5, delay: 0, options: .curveEaseInOut, animations: {
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
            let height = tableView.height
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
            setupLabels()
            // syncAllTextFieldsAndStepSlider()
        }
        return isValid
    }
    
    func validateTokenPrice() -> Bool {
        if let value = Double(marketPlaceOrderTableViewCell.priceTextField.text!.removeComma()) {
            let validate = value > 0.0
            if validate {
                let tokenBPrice = PriceDataManager.shared.getPrice(of: PlaceOrderDataManager.shared.tokenB.symbol)!
                let estimateValue: Double = value * tokenBPrice
                marketPlaceOrderTableViewCell.priceTipLabel.text = "≈ \(estimateValue.currency)"
                marketPlaceOrderTableViewCell.priceTipLabel.isHidden = false
                marketPlaceOrderTableViewCell.priceTipLabel.textColor = .text1
            } else {
                marketPlaceOrderTableViewCell.priceTipLabel.text = LocalizedString("Please input a valid price", comment: "")
                marketPlaceOrderTableViewCell.priceTipLabel.isHidden = false
                marketPlaceOrderTableViewCell.priceTipLabel.textColor = .fail
                marketPlaceOrderTableViewCell.priceTipLabel.shake()
            }
            return validate
        } else {
            if marketPlaceOrderTableViewCell.activeTextFieldTag == marketPlaceOrderTableViewCell.priceTextField.tag {
                marketPlaceOrderTableViewCell.priceTipLabel.isHidden = false
                marketPlaceOrderTableViewCell.priceTipLabel.text = "≈ 0"
            }
            return false
        }
    }
    
    func validateAmount() -> Bool {
        if let value = Double(marketPlaceOrderTableViewCell.amountTextField.text!.removeComma()) {
            let validate = value > 0.0
            if validate {
                setupLabels()
            } else {
                /*
                 tipLabel.isHidden = false
                 tipLabel.textColor = .fail
                 tipLabel.text = LocalizedString("Please input a valid amount", comment: "")
                 tipLabel.shake()
                 */
            }
            return validate
        } else {
            if marketPlaceOrderTableViewCell.activeTextFieldTag == marketPlaceOrderTableViewCell.amountTextField.tag {
                if type == .buy {
                    /*
                     tipLabel.isHidden = true
                     */
                } else {
                    setupLabels()
                }
            }
            return false
        }
    }
    
    func setupLabels() {
        
    }

    func pressedPlaceOrderButton() {
        print("pressedPlaceOrderButton")
        hideNumericKeyboard()
        marketPlaceOrderTableViewCell.priceTextField.resignFirstResponder()
        marketPlaceOrderTableViewCell.amountTextField.resignFirstResponder()
        
        let isPriceValid = validateTokenPrice()
        let isAmountValid = validateAmount()
        
        // Need to call validate()
        if isPriceValid && isAmountValid && validate() {
            self.pushController()
        }
        if !isPriceValid {
            marketPlaceOrderTableViewCell.priceTipLabel.textColor = .fail
            marketPlaceOrderTableViewCell.priceTipLabel.isHidden = false
            marketPlaceOrderTableViewCell.priceTipLabel.text = LocalizedString("Please input a valid price", comment: "")
            marketPlaceOrderTableViewCell.priceTipLabel.shake()
        }

        if !isAmountValid {
            /*
            tipLabel.textColor = .fail
            tipLabel.isHidden = false
            tipLabel.text = LocalizedString("Please input a valid amount", comment: "")
            tipLabel.shake()
            */
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

    func getBalance() -> Double? {
        if let asset = CurrentAppWalletDataManager.shared.getAsset(symbol: tokenS) {
            return asset.balance
        }
        return nil
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

extension UpdatedMarketPlaceOrderViewController {

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
    
    func getMaxPossibleAmount(side: TradeType, tokenBuy: String, tokenSell: String) -> Double {
        var maxPossibleAmount: Double = 0
        
        if side == .buy {
            /*
             if tokenBuy.uppercased() == "LRC" {
             
             } else {
             
             }
             */
            maxPossibleAmount = CurrentAppWalletDataManager.shared.getAsset(symbol: tokenSell)?.balance ?? 0
            
        } else {
            /*
             if tokenSell.uppercased() == "LRC" {
             // This line of code will trigger a Relay API call
             let lrcFrozen = PlaceOrderDataManager.shared.getFrozenLRCFeeFromServer()
             let lrcBlance = CurrentAppWalletDataManager.shared.getBalance(of: "LRC")!
             
             let lrcFee = getLrcFee(lrcBlance-lrcFrozen, tokenSell)
             
             maxPossibleAmount = lrcBlance - lrcFrozen - lrcFee
             
             } else {
             maxPossibleAmount = CurrentAppWalletDataManager.shared.getAsset(symbol: tokenSell)?.balance ?? 0
             }
             */
            maxPossibleAmount = CurrentAppWalletDataManager.shared.getAsset(symbol: tokenSell)?.balance ?? 0
        }
        
        if maxPossibleAmount < 0 {
            maxPossibleAmount = 0
        }
        
        return maxPossibleAmount
    }

}
