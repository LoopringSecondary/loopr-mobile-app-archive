//
//  MarketPlaceOrderTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit
import ValueStepper

class MarketPlaceOrderTableViewCell: UITableViewCell {
    
    var market: Market!
    var type: TradeType!
    
    @IBOutlet weak var buyTabButton: UIButton!
    @IBOutlet weak var sellTabButton: UIButton!
    @IBOutlet weak var priceValueStepper: ValueStepper!
    
    @IBOutlet weak var nextButton: GradientButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        backgroundColor = .clear
        // theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        buyTabButton.title = LocalizedString("Buy", comment: "")
        buyTabButton.backgroundColor = UIColor.clear
        buyTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        
        sellTabButton.title = LocalizedString("Sell", comment: "")
        sellTabButton.backgroundColor = UIColor.clear
        sellTabButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        
        priceValueStepper.tintColor = .white
        priceValueStepper.minimumValue = 0
        priceValueStepper.maximumValue = 1000
        priceValueStepper.stepValue = 100
        // priceValueStepper.enableManualEditing = true
        priceValueStepper.backgroundButtonColor = UIColor.red
        priceValueStepper.labelTextColor = UIColor.dark1
    }
    
    func update() {
        if type == .buy {
            // buyTabButton.setTitleColor(UIColor.theme, for: .normal)
            buyTabButton.setBackgroundColor(UIColor.theme, for: .normal)
            sellTabButton.theme_backgroundColor = ColorPicker.cardBackgroundColor
            nextButton.title = LocalizedString("Buy", comment: "") + " " + market.tradingPair.tradingA
            nextButton.setGreen()
        } else {
            // sellTabButton.setTitleColor(UIColor.theme, for: .normal)
            buyTabButton.theme_backgroundColor = ColorPicker.cardBackgroundColor
            sellTabButton.setBackgroundColor(UIColor.theme, for: .normal)
            nextButton.title = LocalizedString("Sell", comment: "") + " " + market.tradingPair.tradingA
            nextButton.setRed()
        }
        nextButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
    }

    class func getCellIdentifier() -> String {
        return "MarketPlaceOrderTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 500
    }
}
