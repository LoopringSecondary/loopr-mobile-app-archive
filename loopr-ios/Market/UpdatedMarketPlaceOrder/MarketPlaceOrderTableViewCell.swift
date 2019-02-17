//
//  MarketPlaceOrderTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class MarketPlaceOrderTableViewCell: UITableViewCell, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate {

    weak var updatedMarketPlaceOrderViewController: UpdatedMarketPlaceOrderViewController!
    var market: Market!
    var type: TradeType!
    
    private var amountValueDecimal: Int = 8
    private var amountValue: Double = 0
    
    private var totalValueDecimal: Int = 8
    private var totalValue: Double = 0
    
    // Setting
    let decimalsValue: Int = 8
    
    // TODO: needs to update buys and sells in Relay 2.0
    var buys: [Depth] = []
    var sells: [Depth] = []
    
    var activeTextFieldTag = -1
    
    @IBOutlet weak var baseView: UIView!
    @IBOutlet weak var buyTabButton: UIButton!
    @IBOutlet weak var sellTabButton: UIButton!
    
    @IBOutlet weak var latestPriceButton: UIButton!

    @IBOutlet weak var textFieldHeightLayoutConstraint: NSLayoutConstraint!
    
    // Price
    @IBOutlet weak var priceView: UIView!
    @IBOutlet weak var minusPriceStepperButton: UIButton!
    @IBOutlet weak var plusPriceStepperButton: UIButton!
    @IBOutlet weak var priceTextField: UITextField!
    @IBOutlet weak var priceTipLabel: UILabel!

    // Amount
    @IBOutlet weak var amountView: UIView!
    @IBOutlet weak var minusAmountStepperButton: UIButton!
    @IBOutlet weak var plusAmountStepperButton: UIButton!
    @IBOutlet weak var amountTextField: UITextField!
    
    // Percentage
    @IBOutlet weak var percentage25Button: UIButton!
    @IBOutlet weak var percentage50Button: UIButton!
    @IBOutlet weak var percentage75Button: UIButton!
    @IBOutlet weak var percentage100Button: UIButton!
    
    // Info
    @IBOutlet weak var availableAmountInfoLabel: UILabel!
    @IBOutlet weak var availableAmountLabel: UILabel!
    @IBOutlet weak var totalAmountInforLabel: UILabel!
    @IBOutlet weak var totalAmountLabel: UILabel!
    
    @IBOutlet weak var nextButton: GradientButton!
    
    // Orderbook
    @IBOutlet weak var decimalInfoLabel: UILabel!
    @IBOutlet weak var orderbookTableView: UITableView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        backgroundColor = .clear
        selectionStyle = .none

        baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        // baseView.applyShadow()
        baseView.cornerRadius = 8
        baseView.clipsToBounds = true
        
        let viewCornerRadius: CGFloat = 6
        
        buyTabButton.title = LocalizedString("Buy", comment: "")
        buyTabButton.backgroundColor = UIColor.clear
        buyTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        buyTabButton.clipsToBounds = true
        buyTabButton.round(corners: [.topLeft, .bottomLeft], radius: viewCornerRadius)
        buyTabButton.addTarget(self, action: #selector(pressedBuyTabButton), for: .touchUpInside)
        
        sellTabButton.title = LocalizedString("Sell", comment: "")
        sellTabButton.backgroundColor = UIColor.clear
        sellTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        sellTabButton.clipsToBounds = true
        sellTabButton.round(corners: [.topRight, .bottomRight], radius: viewCornerRadius)
        sellTabButton.addTarget(self, action: #selector(pressedSellTabButton), for: .touchUpInside)

        latestPriceButton.contentHorizontalAlignment = .left
        latestPriceButton.setTitleColor(UIColor.theme, for: .normal)
        latestPriceButton.setTitleColor(UIColor.theme.withAlphaComponent(0.6), for: .highlighted)
        latestPriceButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        latestPriceButton.title = ""
        latestPriceButton.titleLabel?.numberOfLines = 2
        latestPriceButton.addTarget(self, action: #selector(pressedLatestPriceButton), for: .touchUpInside)

        let textFieldWidth = (UIScreen.main.bounds.width - 16*2)*0.5 - 4*2 - 4*2
        textFieldHeightLayoutConstraint.constant = (textFieldWidth - 2*3)/4
        
        // Price
        minusPriceStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        minusPriceStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)

        plusPriceStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        plusPriceStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)

        priceTextField.delegate = self
        priceTextField.tag = 0
        priceTextField.inputView = UIView(frame: .zero)
        priceTextField.theme_tintColor = GlobalPicker.textColor
        priceTextField.theme_textColor = GlobalPicker.textColor
        priceTextField.theme_backgroundColor = ColorPicker.cardHighLightColor
        priceTextField.keyboardAppearance = Themes.isDark() ? .dark : .default
        priceTextField.textAlignment = .center
        priceTextField.font = FontConfigManager.shared.getRegularFont(size: 12)
        priceTextField.placeholder = LocalizedString("Price", comment: "")
        priceTextField.setValue(UIColor.init(white: 1, alpha: 0.4), forKeyPath: "_placeholderLabel.textColor")
        priceTextField.contentMode = UIViewContentMode.bottom
        
        priceTipLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        priceTipLabel.theme_textColor = GlobalPicker.textLightColor
        priceTipLabel.text = "≈ \(0.0.currency)"

        // Amount
        minusAmountStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        minusAmountStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
        
        plusAmountStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        plusAmountStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)

        amountTextField.delegate = self
        amountTextField.tag = 1
        amountTextField.inputView = UIView(frame: .zero)
        amountTextField.theme_tintColor = GlobalPicker.textColor
        amountTextField.theme_textColor = GlobalPicker.textColor
        amountTextField.theme_backgroundColor = ColorPicker.cardHighLightColor
        amountTextField.keyboardAppearance = Themes.isDark() ? .dark : .default
        amountTextField.textAlignment = .center
        amountTextField.font = FontConfigManager.shared.getRegularFont(size: 12)
        amountTextField.placeholder = LocalizedString("Amount", comment: "")
        amountTextField.setValue(UIColor.init(white: 1, alpha: 0.4), forKeyPath: "_placeholderLabel.textColor")
        amountTextField.contentMode = UIViewContentMode.bottom
        
        percentage25Button.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        percentage25Button.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 10)
        percentage25Button.setBackgroundColor(UIColor.dark3, for: .normal)
        percentage25Button.setBackgroundColor(UIColor.dark4, for: .highlighted)
        percentage25Button.clipsToBounds = true
        percentage25Button.round(corners: [.topLeft, .bottomLeft], radius: viewCornerRadius)
        percentage25Button.addTarget(self, action: #selector(pressedPercentage25Button), for: .touchUpInside)

        percentage50Button.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        percentage50Button.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 10)
        percentage50Button.setBackgroundColor(UIColor.dark3, for: .normal)
        percentage50Button.setBackgroundColor(UIColor.dark4, for: .highlighted)
        percentage50Button.addTarget(self, action: #selector(pressedPercentage50Button), for: .touchUpInside)

        percentage75Button.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        percentage75Button.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 10)
        percentage75Button.setBackgroundColor(UIColor.dark3, for: .normal)
        percentage75Button.setBackgroundColor(UIColor.dark4, for: .highlighted)
        percentage75Button.addTarget(self, action: #selector(pressedPercentage75Button), for: .touchUpInside)
        
        percentage100Button.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        percentage100Button.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 10)
        percentage100Button.setBackgroundColor(UIColor.dark3, for: .normal)
        percentage100Button.setBackgroundColor(UIColor.dark4, for: .highlighted)
        percentage100Button.clipsToBounds = true
        percentage100Button.round(corners: [.topRight, .bottomRight], radius: viewCornerRadius)
        percentage100Button.addTarget(self, action: #selector(pressedPercentage100Button), for: .touchUpInside)

        // Info
        availableAmountInfoLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        availableAmountInfoLabel.theme_textColor = GlobalPicker.textLightColor
         availableAmountInfoLabel.text = LocalizedString("Available", comment: "")
        
        availableAmountLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        availableAmountLabel.theme_textColor = GlobalPicker.textLightColor
        availableAmountLabel.textAlignment = .right

        totalAmountInforLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        totalAmountInforLabel.theme_textColor = GlobalPicker.textLightColor
        
        totalAmountLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        totalAmountLabel.theme_textColor = GlobalPicker.textLightColor
        totalAmountLabel.textAlignment = .right
        
        nextButton.addTarget(self, action: #selector(pressedNextButton), for: .touchUpInside)

        // Orderbook
        decimalInfoLabel.font = FontConfigManager.shared.getMediumFont(size: 12)
        decimalInfoLabel.theme_textColor = GlobalPicker.textLightColor
        decimalInfoLabel.text = "\(decimalsValue)\(LocalizedString("Decimals", comment: ""))"

        orderbookTableView.dataSource = self
        orderbookTableView.delegate = self
        orderbookTableView.separatorStyle = .none
        orderbookTableView.backgroundColor = .clear
        orderbookTableView.isScrollEnabled = false
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            self.updateUI()
        }
    }

    func setBuys(_ buys: [Depth]) {
        self.buys = buys
    }
    
    func setSells(_ sells: [Depth]) {
        // get first 5
        if sells.count >= 5 {
            self.sells = Array(sells[0..<5])
        } else {
            self.sells = sells
        }
    }
    
    func update() {
        latestPriceButton.title = "\(LocalizedString("Market Price", comment: ""))\n\(market.balanceWithDecimals) \(market.tradingPair.tradingB) ≈ \(market.display.description)"
        
        updateAvailableLabel()
        updateTotalLabel()

        if type == .buy {
            buyTabButton.setBackgroundColor(UIColor.init(rgba: "#5ED279"), for: .normal)
            buyTabButton.setTitleColor(UIColor.white, for: .normal)

            sellTabButton.setBackgroundColor(UIColor.dark3, for: .normal)
            sellTabButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
            sellTabButton.setTitleColor(UIColor.text2, for: .normal)
            
            nextButton.title = LocalizedString("Buy", comment: "") + " " + market.tradingPair.tradingA
            nextButton.setGreen()
        } else {
            buyTabButton.setBackgroundColor(UIColor.dark3, for: .normal)
            buyTabButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
            buyTabButton.setTitleColor(UIColor.text2, for: .normal)

            sellTabButton.setBackgroundColor(UIColor.init(rgba: "#DD5252"), for: .normal)
            sellTabButton.setTitleColor(UIColor.white, for: .normal)

            nextButton.title = LocalizedString("Sell", comment: "") + " " + market.tradingPair.tradingA
            nextButton.setRed()
        }
        nextButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
    }
    
    func updateUI() {
        minusPriceStepperButton.clipsToBounds = true
        minusPriceStepperButton.round(corners: [.topLeft, .bottomLeft], radius: 6)
        
        plusPriceStepperButton.clipsToBounds = true
        plusPriceStepperButton.round(corners: [.topRight, .bottomRight], radius: 6)

        minusAmountStepperButton.clipsToBounds = true
        minusAmountStepperButton.round(corners: [.topLeft, .bottomLeft], radius: 6)

        plusAmountStepperButton.clipsToBounds = true
        plusAmountStepperButton.round(corners: [.topRight, .bottomRight], radius: 6)
    }
    
    // Prefill textFields
    @objc func pressedLatestPriceButton() {
        priceTextField.text = market.balanceWithDecimals
        priceTipLabel.text = "≈ \(market.display.description)"
        updateTotalLabel()
    }
    
    func pressedDepthCell(depth: Depth) {
        priceTextField.text = depth.price.toDecimalPlaces(8)
        
        if let value = Double(priceTextField.text!.removeComma()) {
            let tokenBPrice = PriceDataManager.shared.getPrice(of: PlaceOrderDataManager.shared.tokenB.symbol)!
            let estimateValue: Double = value * tokenBPrice
            priceTipLabel.text = "≈ \(estimateValue.currency)"
        }

        amountTextField.text = depth.amountA.toDecimalPlaces(2).trailingZero()
        
        updateTotalLabel()
    }
    
    // Only update at init method and switch buy and sell type
    func updateAvailableLabel() {
        var message: String = ""
        var tokenB: String = ""
        var tokenS: String = ""

        if self.type == .buy {
            tokenB = PlaceOrderDataManager.shared.tokenA.symbol
            tokenS = PlaceOrderDataManager.shared.tokenB.symbol
        } else {
            tokenB = PlaceOrderDataManager.shared.tokenB.symbol
            tokenS = PlaceOrderDataManager.shared.tokenA.symbol
        }
        if let asset = CurrentAppWalletDataManager.shared.getAsset(symbol: tokenS) {
            // Have to drop last chars due to the size
            
            amountValue = asset.balance
            amountValueDecimal = asset.decimals
            if amountValue > 100 {
                amountValueDecimal = 4
            } else if amountValue > 1000 {
                amountValueDecimal = 2
            }
            
            message = "\(asset.display) \(tokenS)"
        } else {
            message = "0.0 \(tokenS)"
        }
        availableAmountLabel.text = message
    }
    
    // Update when priceTextField is changed.
    func updateTotalLabel() {
        var message: String = ""
        var tokenB: String = ""
        var tokenS: String = ""
        
        if self.type == .buy {
            tokenB = PlaceOrderDataManager.shared.tokenA.symbol
            tokenS = PlaceOrderDataManager.shared.tokenB.symbol
            totalAmountInforLabel.text = LocalizedString("Can Buy", comment: "")
        } else {
            tokenB = PlaceOrderDataManager.shared.tokenB.symbol
            tokenS = PlaceOrderDataManager.shared.tokenA.symbol
            totalAmountInforLabel.text = LocalizedString("Can Sell", comment: "")
        }

        if updatedMarketPlaceOrderViewController.validateTokenPrice(withErrorNotification: false), let asset = CurrentAppWalletDataManager.shared.getAsset(symbol: tokenS) {
            let priceValue = Double(priceTextField.text!.removeComma())!
            if self.type == .buy {
                totalValue = asset.balance/priceValue
            } else {
                totalValue = priceValue * asset.balance
            }
            totalValueDecimal = asset.decimals
            if totalValue > 100 {
                totalValueDecimal = 4
            } else if totalValue > 1000 {
                totalValueDecimal = 2
            }
            message = "\(totalValue.withCommas(totalValueDecimal).trailingZero()) \(tokenB)"
        } else {
            message = "-- \(tokenB)"
        }

        totalAmountLabel.text = message
    }
    
    // Percentage buttons
    @objc func pressedPercentage25Button() {
        updateAmountTextFieldAfterPressedPercentageButton(percentage: 0.25)
    }

    @objc func pressedPercentage50Button() {
        updateAmountTextFieldAfterPressedPercentageButton(percentage: 0.5)
    }

    @objc func pressedPercentage75Button() {
        updateAmountTextFieldAfterPressedPercentageButton(percentage: 0.75)
    }

    @objc func pressedPercentage100Button() {
        // TODO: how to calculate 100%
        updateAmountTextFieldAfterPressedPercentageButton(percentage: 0.98)
    }
    
    private func updateAmountTextFieldAfterPressedPercentageButton(percentage: Double) {
        if self.type == .buy {
            amountTextField.text = (totalValue*percentage).withCommas(totalValueDecimal).trailingZero()
        } else {
            amountTextField.text = (amountValue*percentage).withCommas(amountValueDecimal).trailingZero()
        }
    }

    @objc func pressedBuyTabButton() {
        guard type != .buy else {
            return
        }
        updatedMarketPlaceOrderViewController.switchToBuy()
    }

    @objc func pressedSellTabButton() {
        guard type != .sell else {
            return
        }
        updatedMarketPlaceOrderViewController.switchToSell()
    }

    @objc func pressedNextButton() {
        updatedMarketPlaceOrderViewController.pressedPlaceOrderButton()
    }
    
    // TextField
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        print("textFieldShouldBeginEditing")
        activeTextFieldTag = textField.tag
        updatedMarketPlaceOrderViewController.showNumericKeyboard(textField: textField)
        _ = updatedMarketPlaceOrderViewController.validate()
        return true
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        print("textFieldDidBeginEditing")
        if activeTextFieldTag == 0 {
            priceTextField.placeholder = nil
            amountTextField.placeholder = LocalizedString("Amount", comment: "")
        } else if activeTextFieldTag == 1 {
            priceTextField.placeholder = LocalizedString("Price", comment: "")
            amountTextField.placeholder = nil
        }
    }

    func textFieldDidEndEditing(_ textField: UITextField) {
        print("textFieldDidEndEditing")
        if activeTextFieldTag == 0 {
            priceTextField.placeholder = LocalizedString("Price", comment: "")
        } else if activeTextFieldTag == 1 {
            amountTextField.placeholder = LocalizedString("Amount", comment: "")
        }
    }
    
    class func getCellIdentifier() -> String {
        return "MarketPlaceOrderTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 363 + 5 + 4
    }

}
