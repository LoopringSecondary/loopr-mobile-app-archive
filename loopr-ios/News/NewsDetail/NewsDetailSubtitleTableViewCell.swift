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
        
        dateLabel.font = FontConfigManager.shared.getRegularFont(size: NewsUIStyleConfig.shared.newsDetailSubtitleFont)
        dateLabel.theme_textColor = GlobalPicker.textLightColor

        sourceLabel.font = FontConfigManager.shared.getRegularFont(size: NewsUIStyleConfig.shared.newsDetailSubtitleFont)
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
        if NewsUIStyleConfig.shared.isNewsDetailFontTypeSmall() {
            return 30
        } else {
            return 36
        }
    }
}
