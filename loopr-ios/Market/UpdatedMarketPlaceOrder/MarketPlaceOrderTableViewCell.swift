//
//  MarketPlaceOrderTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class MarketPlaceOrderTableViewCell: UITableViewCell {
    
    var market: Market!
    var type: TradeType!
    
    @IBOutlet weak var nextButton: GradientButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        theme_backgroundColor = ColorPicker.cardBackgroundColor
    }
    
    func update() {
        if type == .buy {
            nextButton.title = LocalizedString("Buy", comment: "") + " " + market.tradingPair.tradingA
            nextButton.setGreen()
        } else {
            nextButton.title = LocalizedString("Sell", comment: "") + " " + market.tradingPair.tradingA
            nextButton.setRed()
        }
    }

    class func getCellIdentifier() -> String {
        return "MarketPlaceOrderTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 500
    }
}
