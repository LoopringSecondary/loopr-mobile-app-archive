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
    
    func setEmptyUI() {
        isUserInteractionEnabled = false

        priceLabel.isHidden = true
        amountLabel.isHidden = true
    }
    
    func update(indexPath: IndexPath, orderbookItem: OrderbookItem) {
        isUserInteractionEnabled = true

        priceLabel.isHidden = false
        priceLabel.textAlignment = .left
        priceLabel.font = FontConfigManager.shared.getMediumFont(size: 12)

        amountLabel.isHidden = false
        amountLabel.textAlignment = .right
        amountLabel.font = FontConfigManager.shared.getMediumFont(size: 12)
        amountLabel.theme_textColor = GlobalPicker.textLightColor
        
        if indexPath.section == 0 {
            priceLabel.textColor = UIColor.fail
        } else {
            priceLabel.textColor = UIColor.success
        }
        
        priceLabel.text = orderbookItem.price.withCommas(8)
        amountLabel.text = orderbookItem.amount.withCommas(2).trailingZero()
    }
    
    class func getCellIdentifier() -> String {
        return "MarketPlaceOrderbookTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 27
    }

}
