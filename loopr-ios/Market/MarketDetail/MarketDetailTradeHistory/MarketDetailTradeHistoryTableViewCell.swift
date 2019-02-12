//
//  MarketDetailTradeHistoryTableViewCell.swift
//  loopr-ios
//
//  Created by xiaoruby on 7/30/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class MarketDetailTradeHistoryTableViewCell: UITableViewCell {

    var orderFill: OrderFill?
    
    var baseViewBuy: UIView = UIView()
    
    var label1: UILabel = UILabel()
    var label2: UILabel = UILabel()
    var label4: UILabel = UILabel()

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        clipsToBounds = true
        selectionStyle = .none
        theme_backgroundColor = ColorPicker.backgroundColor
        
        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        
        baseViewBuy.frame = CGRect(x: 15, y: 0, width: (screenWidth - 15*2), height: 33+10)
        baseViewBuy.theme_backgroundColor = ColorPicker.cardBackgroundColor
        addSubview(baseViewBuy)
        
        label1 = UILabel(frame: CGRect(x: 10, y: 0, width: (baseViewBuy.width-30)*0.3, height: 33))
        label1.theme_textColor = GlobalPicker.textColor
        label1.font = FontConfigManager.shared.getMediumFont(size: 12)
        label1.textAlignment = .left
        baseViewBuy.addSubview(label1)
        
        label2 = UILabel(frame: CGRect(x: 10 + 10 + (baseViewBuy.width-30)*0.3, y: 0, width: (baseViewBuy.width-30)*0.22, height: 33))
        label2.theme_textColor = GlobalPicker.textColor
        label2.font = FontConfigManager.shared.getMediumFont(size: 12)
        label2.textAlignment = .right
        label2.lineBreakMode = .byCharWrapping
        baseViewBuy.addSubview(label2)
    
        label4 = UILabel(frame: CGRect(x: 15, y: 0, width: (baseViewBuy.width-30), height: 33))
        label4.theme_textColor = GlobalPicker.textColor
        label4.font = FontConfigManager.shared.getMediumFont(size: 12)
        label4.textAlignment = .right
        label4.lineBreakMode = .byClipping
        baseViewBuy.addSubview(label4)
    }
    
    func update() {
        if let orderFill = orderFill {
            label1.text = "\(orderFill.price.withCommas(8))"
            label2.text = "\(orderFill.amount)"
            label4.text = DateUtil.convertToDate(orderFill.createTime, format: "MM-dd HH:mm")
        } else {
            label1.text = ""
            label2.text = ""
        }
    }
    
    class func getCellIdentifier() -> String {
        return "MarketDetailTradeHistoryTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 32
    }

}
