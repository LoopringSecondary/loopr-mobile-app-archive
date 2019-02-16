//
//  MarketPlaceOrderbookTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 2/16/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class MarketPlaceOrderbookTableViewCell: UITableViewCell {

    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var amountLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        backgroundColor = .clear
    }
    
    func update(index: Int) {
        priceLabel.textAlignment = .left
        priceLabel.font = FontConfigManager.shared.getMediumFont(size: 12)

        amountLabel.textAlignment = .right
        amountLabel.font = FontConfigManager.shared.getMediumFont(size: 12)
        amountLabel.theme_textColor = GlobalPicker.textColor
        
        if index < 5 {
            priceLabel.textColor = UIColor.success
        } else {
            priceLabel.textColor = UIColor.fail
        }
        priceLabel.text = "0.00044289"
        amountLabel.text = "1081.76"
    }
    
    class func getCellIdentifier() -> String {
        return "MarketPlaceOrderbookTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 28
    }

}
