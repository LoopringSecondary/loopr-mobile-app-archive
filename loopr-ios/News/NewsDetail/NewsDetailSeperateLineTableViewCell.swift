//
//  NewsDetailSeperateLineTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 1/24/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NewsDetailSeperateLineTableViewCell: UITableViewCell {

    var seperateLine: UIView = UIView()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        selectionStyle = .none
        theme_backgroundColor = ColorPicker.cardBackgroundColor

        seperateLine.frame = CGRect(x: UIScreen.main.bounds.width*0.2, y: (NewsDetailSeperateLineTableViewCell.getHeight()-4)*0.5, width: UIScreen.main.bounds.width*0.6, height: 4)
        
        seperateLine.theme_backgroundColor = ColorPicker.cardHighLightColor
        seperateLine.clipsToBounds = true
        seperateLine.round(corners: [.topLeft, .topRight, .bottomRight, .bottomLeft], radius: 2)
        
        addSubview(seperateLine)
    }

    class func getCellIdentifier() -> String {
        return "NewsDetailSeperateLineTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 20 + 4
    }
    
}
