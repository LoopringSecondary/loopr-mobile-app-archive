//
//  MarketPlaceOrderTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class MarketPlaceOrderTableViewCell: UITableViewCell, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate {

    weak var marketPlaceOrderViewController: MarketPlaceOrderViewController!
    var market: Market!
    var type: OrderSide!

    private var amountValueDecimal: Int = 8
    private var amountValue: Double = 0

    private var totalValueDecimal: Int = 8
    private var totalValue: Double = 0

    // Setting
    let decimalsSettingValue: Int = 8

    // TODO: needs to update buys and sells in Relay 2.0
    var buys: [OrderbookItem] = []
    var sells: [OrderbookItem] = []

    var activeTextFieldTag = -1

    @IBOutlet weak var baseView: UIView!
    @IBOutlet weak var leftBaseView: UIView!

    @IBOutlet weak var buyTabButton: UIButton!
    @IBOutlet weak var sellTabButton: UIButton!

    @IBOutlet weak var latestPriceInfoLabel: UILabel!
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
    @IBOutlet weak var availableAmountInfoLabelWidthLayoutConstraint: NSLayoutConstraint!
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

        let tap = UITapGestureRecognizer(target: self, action: #selector(handleTap(_:)))
        tap.delegate = self
        baseView.addGestureRecognizer(tap)

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

        latestPriceInfoLabel.text = "\(LocalizedString("Market Price", comment: ""))"
        latestPriceInfoLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        latestPriceInfoLabel.theme_textColor = GlobalPicker.textLightColor

        latestPriceButton.contentHorizontalAlignment = .left
        latestPriceButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        latestPriceButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .highlighted)
        latestPriceButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        latestPriceButton.title = ""
        latestPriceButton.titleLabel?.numberOfLines = 1
        latestPriceButton.addTarget(self, action: #selector(pressedLatestPriceButton), for: .touchUpInside)

        let textFieldWidth = (UIScreen.main.bounds.width - 16*2)*0.5 - 4*2 - 4*2
        textFieldHeightLayoutConstraint.constant = (textFieldWidth - 2*3)/4

        // Price
        minusPriceStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        minusPriceStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
        minusPriceStepperButton.addTarget(self, action: #selector(pressedMinusPriceStepperButton), for: .touchUpInside)
        minusPriceStepperButton.setImage(UIImage(named: "Minus-button-dark")?.alpha(0.9), for: .normal)

        plusPriceStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        plusPriceStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
        plusPriceStepperButton.addTarget(self, action: #selector(pressedPlusPriceStepperButton), for: .touchUpInside)
        plusPriceStepperButton.setImage(UIImage(named: "Add-button-dark")?.alpha(0.9), for: .normal)

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
        minusAmountStepperButton.addTarget(self, action: #selector(pressedMinusAmountStepperButton), for: .touchUpInside)
        minusAmountStepperButton.setImage(UIImage(named: "Minus-button-dark")?.alpha(0.9), for: .normal)

        plusAmountStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        plusAmountStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
        plusAmountStepperButton.addTarget(self, action: #selector(pressedPlusAmountStepperButton), for: .touchUpInside)
        plusAmountStepperButton.setImage(UIImage(named: "Add-button-dark")?.alpha(0.9), for: .normal)

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
        availableAmountInfoLabelWidthLayoutConstraint.constant = availableAmountInfoLabel.text?.widthOfString(usingFont: availableAmountLabel.font) ?? 30

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
        decimalInfoLabel.text = "\(decimalsSettingValue) \(LocalizedString("Decimals", comment: ""))"

        orderbookTableView.dataSource = self
        orderbookTableView.delegate = self
        orderbookTableView.separatorStyle = .none
        orderbookTableView.backgroundColor = .clear
        orderbookTableView.isScrollEnabled = false

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            self.updateUI()
        }
    }

    func setBuys(_ buys: [OrderbookItem]) {
        self.buys = buys
    }

    func setSells(_ sells: [OrderbookItem]) {
        // get first 5
        if sells.count >= 5 {
            self.sells = Array(sells[0..<5])
        } else {
            self.sells = sells
        }
    }

    func update() {
        latestPriceButton.setAttributedTitle("\(market.ticker.price.withCommas()) \(market.metadata.marketPair.quoteToken) ≈ \(market.ticker.price.withCommas())".higlighted(words: [market.ticker.price.withCommas()], attributes: [NSAttributedStringKey.foregroundColor: UIColor.theme]), for: .normal)
        latestPriceButton.setAttributedTitle("\(market.ticker.price.withCommas()) \(market.metadata.marketPair.baseToken) ≈ \(market.ticker.price.withCommas())".higlighted(words: [market.ticker.price.withCommas()], attributes: [NSAttributedStringKey.foregroundColor: UIColor.theme.withAlphaComponent(0.6)]), for: .highlighted)

        updateAvailableLabel()
        updateTotalLabels()

        if type == .buy {
            buyTabButton.setBackgroundColor(UIColor.init(rgba: "#5ED279"), for: .normal)
            buyTabButton.setTitleColor(UIColor.white, for: .normal)

            sellTabButton.setBackgroundColor(UIColor.dark3, for: .normal)
            sellTabButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
            sellTabButton.setTitleColor(UIColor.text2, for: .normal)

            nextButton.title = LocalizedString("Buy", comment: "") + " " + market.metadata.marketPair.baseToken
            nextButton.setGreen()
        } else {
            buyTabButton.setBackgroundColor(UIColor.dark3, for: .normal)
            buyTabButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
            buyTabButton.setTitleColor(UIColor.text2, for: .normal)

            sellTabButton.setBackgroundColor(UIColor.init(rgba: "#DD5252"), for: .normal)
            sellTabButton.setTitleColor(UIColor.white, for: .normal)

            nextButton.title = LocalizedString("Sell", comment: "") + " " + market.metadata.marketPair.baseToken
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

    @objc func handleTap(_ sender: UITapGestureRecognizer?) {
        print("handleTap")
        marketPlaceOrderViewController.hideNumericKeyboard()
        priceTextField.resignFirstResponder()
        amountTextField.resignFirstResponder()
    }

    override func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        if touch.view?.isDescendant(of: self.orderbookTableView) == true {
            return false
        }
        return true
    }

    // Prefill textFields
    @objc func pressedLatestPriceButton() {
        priceTextField.text = market.balanceWithDecimals.trailingZero()
        priceTipLabel.text = "≈ \(market.display.description)"
        updateTotalLabels()
    }

    // Update textFields
    @objc func pressedMinusPriceStepperButton() {
        if marketPlaceOrderViewController.validateTokenPrice(withErrorNotification: false) {
            var priceValue = Double(priceTextField.text!.removeComma())!
            if priceValue - getPriceValueStep() > 0 {
                priceValue -= getPriceValueStep()
                setPriceTextField(priceValue: priceValue)
            } else {
                setPriceTextField(priceValue: 0)
            }
        }
    }

    @objc func pressedPlusPriceStepperButton() {
        if marketPlaceOrderViewController.validateTokenPrice(withErrorNotification: false) {
            var priceValue = Double(priceTextField.text!.removeComma())!
            priceValue += getPriceValueStep()
            setPriceTextField(priceValue: priceValue)
        } else {
            let priceValue = getPriceValueStep()
            setPriceTextField(priceValue: priceValue)
        }
    }

    func setPriceTextField(priceValue: Double) {
        if priceValue == 0 {
            priceTextField.text = ""
            priceTipLabel.text = "≈ \(0.0.currency)"
            updateTotalLabels()
        } else {
            priceTextField.text = priceValue.withCommas(14).trailingZero()
            let tokenBPrice = PriceDataManager.shared.getPrice(of: OrderDataManager.shared.quoteToken.symbol)!
            let estimateValue: Double = priceValue * tokenBPrice
            priceTipLabel.text = "≈ \(estimateValue.currency)"
            updateTotalLabels()
        }
    }

    // TODO: value step will be defined in Relay API
    func getPriceValueStep() -> Double {
        switch decimalsSettingValue {
        case 8:
            return 0.000001
        default:
            return 0.00000001
        }
    }

    func getAmountValueStep() -> Double {
        if type == .buy {
            if totalValue > 1000 {
                return 100
            } else if totalValue > 100 {
                return 10
            } else if totalValue > 10 {
                return 1
            } else if totalValue > 1 {
                return 0.1
            } else if totalValue > 0.1 {
                return 0.01
            } else {
                return 0
            }
        } else {
            if amountValue > 1000 {
                return 100
            } else if amountValue > 100 {
                return 10
            } else if amountValue > 10 {
                return 1
            } else if amountValue > 1 {
                return 0.1
            } else if amountValue > 0.1 {
                return 0.01
            } else {
                return 0
            }
        }
    }

    @objc func pressedMinusAmountStepperButton() {
        if marketPlaceOrderViewController.validateAmount(withErrorNotification: false) {
            var amountValue = Double(amountTextField.text!.removeComma())!
            if amountValue - getAmountValueStep() > 0 {
                amountValue -= getAmountValueStep()
                setAmountTextField(amountValue: amountValue)
            } else {
                setAmountTextField(amountValue: 0)
            }
        }
    }

    @objc func pressedPlusAmountStepperButton() {
        if marketPlaceOrderViewController.validateAmount(withErrorNotification: false) {
            var amountValue = Double(amountTextField.text!.removeComma())!
            amountValue += getAmountValueStep()
            setAmountTextField(amountValue: amountValue)
        } else {
            let amountValue = getAmountValueStep()
            setAmountTextField(amountValue: amountValue)
        }
    }

    func setAmountTextField(amountValue: Double) {
        if amountValue == 0 {
            amountTextField.text = ""
        } else {
            amountTextField.text = amountValue.withCommas(14).trailingZero()
        }
        // No need to update available label
        // updateAvailableLabel()
    }

    func pressedDepthCell(depth: Depth) {
        // TODO: Should put it to func setPriceTextField(priceValue: Double)
        priceTextField.text = depth.price.toDecimalPlaces(decimalsSettingValue)
        if let value = Double(priceTextField.text!.removeComma()) {
            setPriceTextField(priceValue: value)
        }

        // amountTextField.text = depth.amountA.toDecimalPlaces(2).trailingZero()
        // doesn't have commas
        // The following implementation does have commas
        amountTextField.text = depth.amountAInDouble.withCommas(2).trailingZero()

        // No need to update available label
        // updateAvailableLabel()
        updateTotalLabels()
    }

    // Only update at init method and switch buy and sell type
    func updateAvailableLabel() {
        var message: String = ""
        var tokenB: String = ""
        var tokenS: String = ""

        if self.type == .buy {
            tokenB = OrderDataManager.shared.baseToken.symbol
            tokenS = OrderDataManager.shared.quoteToken.symbol
        } else {
            tokenB = OrderDataManager.shared.quoteToken.symbol
            tokenS = OrderDataManager.shared.baseToken.symbol
        }
        if let asset = CurrentAppWalletDataManager.shared.getAsset(symbol: tokenS) {
            amountValue = asset.balance
            amountValueDecimal = asset.decimals
            if amountValue > 1000 {
                amountValueDecimal = 0
            } else if amountValue > 100 {
                amountValueDecimal = 2
            } else if amountValue > 10 {
                amountValueDecimal = 4
            } else {
                amountValueDecimal = 6
            }

            message = "\(asset.balance.withCommas(amountValueDecimal).trailingZero()) \(tokenS)"
        } else {
            message = "0.0 \(tokenS)"
        }
        availableAmountLabel.text = message
    }

    // Update when priceTextField is changed. It's not related to amountTextField change
    func updateTotalLabels() {
        var message: String = ""
        var tokenB: String = ""
        var tokenS: String = ""

        if self.type == .buy {
            tokenB = OrderDataManager.shared.baseToken.symbol
            tokenS = OrderDataManager.shared.quoteToken.symbol
            totalAmountInforLabel.text = LocalizedString("Can Buy", comment: "")
        } else {
            tokenB = OrderDataManager.shared.quoteToken.symbol
            tokenS = OrderDataManager.shared.baseToken.symbol
            totalAmountInforLabel.text = LocalizedString("Can Sell", comment: "")
        }

        if marketPlaceOrderViewController.validateTokenPrice(withErrorNotification: false), let asset = CurrentAppWalletDataManager.shared.getAsset(symbol: tokenS) {
            let priceValue = Double(priceTextField.text!.removeComma())!
            if self.type == .buy {
                totalValue = asset.balance/priceValue
            } else {
                totalValue = priceValue * asset.balance
            }
            totalValueDecimal = asset.decimals

            if totalValue > 1000 {
                totalValueDecimal = 0
            } else if totalValue > 100 {
                totalValueDecimal = 2
            } else if totalValue > 10 {
                totalValueDecimal = 4
            } else {
                totalValueDecimal = 6
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
        marketPlaceOrderViewController.switchToBuy()
    }

    @objc func pressedSellTabButton() {
        guard type != .sell else {
            return
        }
        marketPlaceOrderViewController.switchToSell()
    }

    @objc func pressedNextButton() {
        marketPlaceOrderViewController.pressedPlaceOrderButton()
    }

    // TextField
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        print("textFieldShouldBeginEditing")
        activeTextFieldTag = textField.tag
        marketPlaceOrderViewController.showNumericKeyboard(textField: textField)
        _ = marketPlaceOrderViewController.validate()
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
