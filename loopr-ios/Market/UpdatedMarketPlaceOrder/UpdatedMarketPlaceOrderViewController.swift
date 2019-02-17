//
//  UpdatedMarketPlaceOrderViewController.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

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
        numericKeyboardBaseView.backgroundColor = .white
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
            let height = view.frame.height
            let destinateY = height
            
            UIView.animate(withDuration: 0.3, delay: 0, options: .curveEaseInOut, animations: {
                self.numericKeyboardView.frame = CGRect(x: 0, y: destinateY, width: width, height: DefaultNumericKeyboard.height)
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
                marketPlaceOrderTableViewCell.priceTipLabel.isHidden = true
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

}
