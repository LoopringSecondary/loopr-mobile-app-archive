//
//  MarketPlaceOrderTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class MarketPlaceOrderTableViewCell: UITableViewCell, UITableViewDelegate, UITableViewDataSource {

    var market: Market!
    var type: TradeType!
    
    @IBOutlet weak var baseView: UIView!
    @IBOutlet weak var buyTabButton: UIButton!
    @IBOutlet weak var sellTabButton: UIButton!
    
    @IBOutlet weak var latestPriceLabel: UILabel!

    // Price
    @IBOutlet weak var minusPriceStepperButton: UIButton!
    @IBOutlet weak var plusPriceStepperButton: UIButton!

    // Amount
    @IBOutlet weak var minusAmountStepperButton: UIButton!
    @IBOutlet weak var plusAmountStepperButton: UIButton!
    
    @IBOutlet weak var nextButton: GradientButton!
    
    @IBOutlet weak var orderbookTableView: UITableView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        backgroundColor = .clear
        baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        buyTabButton.title = LocalizedString("Buy", comment: "")
        buyTabButton.backgroundColor = UIColor.clear
        buyTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        
        sellTabButton.title = LocalizedString("Sell", comment: "")
        sellTabButton.backgroundColor = UIColor.clear
        sellTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        
        latestPriceLabel.font = FontConfigManager.shared.getRegularFont(size: 14)
        latestPriceLabel.theme_textColor = GlobalPicker.textColor
        latestPriceLabel.text = "最新成交价\n0.00045460 WETH"
        
        minusPriceStepperButton.theme_backgroundColor = ColorPicker.cardHighLightColor
        plusPriceStepperButton.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        minusAmountStepperButton.theme_backgroundColor = ColorPicker.cardHighLightColor
        plusAmountStepperButton.theme_backgroundColor = ColorPicker.cardHighLightColor
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
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return UITableViewCell()
    }

    class func getCellIdentifier() -> String {
        return "MarketPlaceOrderTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 400
    }
}
