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
    
    @IBOutlet weak var baseView: UIView!
    @IBOutlet weak var buyTabButton: UIButton!
    @IBOutlet weak var sellTabButton: UIButton!
    
    @IBOutlet weak var latestPriceLabel: UILabel!

    // Price
    @IBOutlet weak var minusPriceStepperButton: UIButton!
    @IBOutlet weak var plusPriceStepperButton: UIButton!
    @IBOutlet weak var priceTextField: UITextField!
    @IBOutlet weak var priceTipLabel: UILabel!

    // Amount
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
    @IBOutlet weak var seperateLine: UIView!
    @IBOutlet weak var orderbookTableView: UITableView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        backgroundColor = .clear
        selectionStyle = .none
        baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        buyTabButton.title = LocalizedString("Buy", comment: "")
        buyTabButton.backgroundColor = UIColor.clear
        buyTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        buyTabButton.clipsToBounds = true
        buyTabButton.round(corners: [.topLeft, .bottomLeft], radius: 6)
        
        sellTabButton.title = LocalizedString("Sell", comment: "")
        sellTabButton.backgroundColor = UIColor.clear
        sellTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        sellTabButton.clipsToBounds = true
        sellTabButton.round(corners: [.topRight, .bottomRight], radius: 6)

        latestPriceLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        // latestPriceLabel.theme_textColor = GlobalPicker.textColor
        latestPriceLabel.textColor = UIColor.theme
        latestPriceLabel.text = "最新成交价\n0.00045460 WETH"
        
        // Price
        minusPriceStepperButton.theme_backgroundColor = ColorPicker.cardHighLightColor
        plusPriceStepperButton.theme_backgroundColor = ColorPicker.cardHighLightColor
        priceTextField.delegate = self
        priceTextField.tag = 1
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
        minusAmountStepperButton.theme_backgroundColor = ColorPicker.cardHighLightColor
        plusAmountStepperButton.theme_backgroundColor = ColorPicker.cardHighLightColor
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
        percentage25Button.round(corners: [.topLeft, .bottomLeft], radius: 6)

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
        percentage100Button.round(corners: [.topRight, .bottomRight], radius: 6)

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
        decimalInfoLabel.text = "8\(LocalizedString("Decimals", comment: ""))"
        
        seperateLine.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        orderbookTableView.dataSource = self
        orderbookTableView.delegate = self
        orderbookTableView.separatorStyle = .none
        orderbookTableView.backgroundColor = .clear
        orderbookTableView.isScrollEnabled = false
    }
    
    func update() {
        if type == .buy {
            // buyTabButton.setTitleColor(UIColor.theme, for: .normal)
            buyTabButton.setBackgroundColor(UIColor.init(rgba: "#5ED279"), for: .normal)
            sellTabButton.theme_backgroundColor = ColorPicker.cardHighLightColor
            nextButton.title = LocalizedString("Buy", comment: "") + " " + market.tradingPair.tradingA
            nextButton.setGreen()
        } else {
            // sellTabButton.setTitleColor(UIColor.theme, for: .normal)
            buyTabButton.theme_backgroundColor = ColorPicker.cardHighLightColor
            sellTabButton.setBackgroundColor(UIColor.init(rgba: "#DD5252"), for: .normal)
            nextButton.title = LocalizedString("Sell", comment: "") + " " + market.tradingPair.tradingA
            nextButton.setRed()
        }
        nextButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0 {
            return 24
        } else if section == 1 {
            return 8
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 0 {
            let screenWidth = (UIScreen.main.bounds.width - 16*2)*0.5
            let labelWidth = (screenWidth - 8*2)*0.5
            
            let headerView = UIView(frame: CGRect(x: 0, y: 0, width: screenWidth, height: MarketPlaceOrderbookTableViewCell.getHeight()))
            headerView.backgroundColor = .clear
            
            let label1 = UILabel(frame: CGRect(x: 8, y: 0, width: labelWidth, height: MarketPlaceOrderbookTableViewCell.getHeight()))
            label1.theme_textColor = GlobalPicker.textLightColor
            label1.font = FontConfigManager.shared.getMediumFont(size: 12)
            label1.text = LocalizedString("Price", comment: "")
            label1.textAlignment = .left
            headerView.addSubview(label1)

            let label4 = UILabel(frame: CGRect(x: label1.frame.maxX, y: 0, width: labelWidth, height: MarketPlaceOrderbookTableViewCell.getHeight()))
            label4.theme_textColor = GlobalPicker.textLightColor
            label4.font = FontConfigManager.shared.getMediumFont(size: 12)
            label4.text = LocalizedString("Amount", comment: "")
            label4.textAlignment = .right
            headerView.addSubview(label4)
            
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
        cell?.update(indexPath: indexPath)
        return cell!
    }

    class func getCellIdentifier() -> String {
        return "MarketPlaceOrderTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 363
    }

}
