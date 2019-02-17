//
//  MarketPlaceOrderTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class MarketPlaceOrderTableViewCell: UITableViewCell, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate {

    var market: Market!
    var type: TradeType!
    
    let decimalsValue: Int = 8
    
    // TODO: needs to update buys and sells in Relay 2.0
    private var buys: [Depth] = []
    private var sells: [Depth] = []
    
    var activeTextFieldTag = -1
    
    @IBOutlet weak var baseView: UIView!
    @IBOutlet weak var buyTabButton: UIButton!
    @IBOutlet weak var sellTabButton: UIButton!
    
    @IBOutlet weak var latestPriceLabel: UILabel!

    @IBOutlet weak var textFieldHeightLayoutConstraint: NSLayoutConstraint!
    
    // Price
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
        
        sellTabButton.title = LocalizedString("Sell", comment: "")
        sellTabButton.backgroundColor = UIColor.clear
        sellTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        sellTabButton.clipsToBounds = true
        sellTabButton.round(corners: [.topRight, .bottomRight], radius: viewCornerRadius)

        latestPriceLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        // latestPriceLabel.theme_textColor = GlobalPicker.textColor
        latestPriceLabel.textColor = UIColor.theme

        let textFieldWidth = (UIScreen.main.bounds.width - 16*2)*0.5 - 4*2 - 4*2
        textFieldHeightLayoutConstraint.constant = (textFieldWidth - 2*3)/4
        
        // Price
        minusPriceStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        minusPriceStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)

        plusPriceStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        plusPriceStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)

        priceTextField.delegate = self
        priceTextField.tag = 0
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
        priceTipLabel.text = "≈ $100"
        
        // Amount
        minusAmountStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        minusAmountStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)
        
        plusAmountStepperButton.setBackgroundColor(UIColor.dark3, for: .normal)
        plusAmountStepperButton.setBackgroundColor(UIColor.dark4, for: .highlighted)

        amountTextField.delegate = self
        amountTextField.tag = 1
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

        percentage50Button.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        percentage50Button.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 10)
        percentage50Button.setBackgroundColor(UIColor.dark3, for: .normal)
        percentage50Button.setBackgroundColor(UIColor.dark4, for: .highlighted)

        percentage75Button.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        percentage75Button.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 10)
        percentage75Button.setBackgroundColor(UIColor.dark3, for: .normal)
        percentage75Button.setBackgroundColor(UIColor.dark4, for: .highlighted)

        percentage100Button.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        percentage100Button.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 10)
        percentage100Button.setBackgroundColor(UIColor.dark3, for: .normal)
        percentage100Button.setBackgroundColor(UIColor.dark4, for: .highlighted)
        percentage100Button.clipsToBounds = true
        percentage100Button.round(corners: [.topRight, .bottomRight], radius: viewCornerRadius)

        // Info
        availableAmountInfoLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        availableAmountInfoLabel.theme_textColor = GlobalPicker.textLightColor
        
        availableAmountLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        availableAmountLabel.theme_textColor = GlobalPicker.textLightColor
        availableAmountLabel.textAlignment = .right

        totalAmountInforLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        totalAmountInforLabel.theme_textColor = GlobalPicker.textLightColor
        
        totalAmountLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        totalAmountLabel.theme_textColor = GlobalPicker.textLightColor
        totalAmountLabel.textAlignment = .right

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
        self.sells = Array(sells[0..<5])
    }
    
    func update() {
        latestPriceLabel.text = "最新成交价\n\(market.balanceWithDecimals) \(market.tradingPair.tradingB) ≈ \(market.display.description)"

        if type == .buy {
            buyTabButton.setBackgroundColor(UIColor.init(rgba: "#5ED279"), for: .normal)

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
    
    // TextField
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        print("textFieldShouldBeginEditing")
        activeTextFieldTag = textField.tag
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
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0 {
            return 30
        } else if section == 1 {
            return 8
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 0 {
            let screenWidth = (UIScreen.main.bounds.width - 16*2)*0.5
            let height: CGFloat = 30
            let labelWidth = (screenWidth - 8*2)*0.5
            
            let headerView = UIView(frame: CGRect(x: 0, y: 0, width: screenWidth, height: height))
            headerView.backgroundColor = .clear

            let label1 = UILabel(frame: CGRect(x: 8, y: 0, width: labelWidth, height: height))
            label1.theme_textColor = GlobalPicker.textLightColor
            label1.font = FontConfigManager.shared.getMediumFont(size: 12)
            label1.text = "\(LocalizedString("Price", comment: ""))(\(market.tradingPair.tradingB))"
            label1.textAlignment = .left
            headerView.addSubview(label1)

            let label4 = UILabel(frame: CGRect(x: label1.frame.maxX, y: 0, width: labelWidth, height: height))
            label4.theme_textColor = GlobalPicker.textLightColor
            label4.font = FontConfigManager.shared.getMediumFont(size: 12)
            label4.text = "\(LocalizedString("Amount", comment: ""))(\(market.tradingPair.tradingA))"
            label4.textAlignment = .right
            headerView.addSubview(label4)
            
            let seperateLine1 = UIView(frame: CGRect(x: 8, y: 0, width: headerView.width - 2*8, height: 0.5))
            seperateLine1.theme_backgroundColor = ColorPicker.cardHighLightColor
            headerView.addSubview(seperateLine1)
            
            let seperateLine2 = UIView(frame: CGRect(x: 8, y: height-0.5, width: headerView.width - 2*8, height: 0.5))
            seperateLine2.theme_backgroundColor = ColorPicker.cardHighLightColor
            headerView.addSubview(seperateLine2)
            
            return headerView

        } else if section == 1 {
            let screenWidth = (UIScreen.main.bounds.width - 16*2)*0.5
            let headerView = UIView(frame: CGRect(x: 0, y: 0, width: screenWidth, height: 8))
            let seperateLine = UIView(frame: CGRect(x: 8, y: headerView.height*0.5, width: headerView.width - 2*8, height: 0.5))
            seperateLine.theme_backgroundColor = ColorPicker.cardHighLightColor
            headerView.addSubview(seperateLine)
            return headerView
        } else {
            return nil
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return MarketPlaceOrderbookTableViewCell.getHeight()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 5
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: MarketPlaceOrderbookTableViewCell.getCellIdentifier()) as? MarketPlaceOrderbookTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("MarketPlaceOrderbookTableViewCell", owner: self, options: nil)
            cell = nib![0] as? MarketPlaceOrderbookTableViewCell
        }

        if indexPath.section == 0 {
            let depth = sells[sells.count-1-indexPath.row]
            cell?.update(indexPath: indexPath, depth: depth)
        } else if indexPath.section == 1 {
            let depth = buys[indexPath.row]
            cell?.update(indexPath: indexPath, depth: depth)
        }

        return cell!
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }

    class func getCellIdentifier() -> String {
        return "MarketPlaceOrderTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 363
    }

}
