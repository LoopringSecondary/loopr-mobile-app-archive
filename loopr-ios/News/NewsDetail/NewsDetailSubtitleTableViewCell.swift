//
//  NewsDetailSubtitleTableViewCell.swift
//  loopr-ios
//
//  Created by Ruby on 1/13/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NewsDetailSubtitleTableViewCell: UITableViewCell {

    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var sourceLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        selectionStyle = .none
        theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        dateLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        dateLabel.theme_textColor = GlobalPicker.textLightColor

        sourceLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        sourceLabel.theme_textColor = GlobalPicker.textLightColor
    }
    
    func update(news: News) {
        dateLabel.text = news.publishTime
        sourceLabel.text = news.source
    }

    class func getCellIdentifier() -> String {
        return "NewsDetailSubtitleTableViewCell"
    }

    class func getHeight() -> CGFloat {
        return 30
    }
}
