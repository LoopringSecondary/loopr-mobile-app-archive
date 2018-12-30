//
//  NewsCollectionCell.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit

class NewsCollectionCell: UICollectionViewCell {

    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var sourceLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var contentTextView: UITextView!

    @IBOutlet weak var upVoteButton: UIButton!
    @IBOutlet weak var downVoteButton: UIButton!
    @IBOutlet weak var shareButton: UIButton!

    open override func awakeFromNib() {
        super.awakeFromNib()
        
        theme_backgroundColor = ColorPicker.cardBackgroundColor
        contentView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        contentView.layer.masksToBounds = false
        layer.masksToBounds = false
        
        let config = GarlandConfig.shared
        layer.cornerRadius  = config.cardRadius
        layer.shadowOffset = config.cardShadowOffset
        layer.shadowColor = config.cardShadowColor.cgColor
        layer.shadowOpacity = config.cardShadowOpacity
        layer.shadowRadius = config.cardShadowRadius
        
        layer.shouldRasterize = true
        layer.rasterizationScale = UIScreen.main.scale
        
        cornerRadius = config.cardRadius
        
        // update UI
        dateLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        dateLabel.theme_textColor = GlobalPicker.textLightColor
        
        sourceLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        sourceLabel.theme_textColor = GlobalPicker.textLightColor
        sourceLabel.textAlignment = .right
        
        titleLabel.font = FontConfigManager.shared.getMediumFont(size: 14)
        titleLabel.theme_textColor = GlobalPicker.textColor
        
        contentTextView.font = FontConfigManager.shared.getRegularFont(size: 12)
        contentTextView.theme_textColor = GlobalPicker.textLightColor
        contentTextView.backgroundColor = .clear
        contentTextView.isUserInteractionEnabled = false
        contentTextView.isScrollEnabled = false
        // contentTextView.textContainerInset = UIEdgeInsets.init(top: 0, left: 0, bottom: 0, right: 0)
        contentTextView.showsVerticalScrollIndicator = false
        contentTextView.showsHorizontalScrollIndicator = false
        let padding = contentTextView.textContainer.lineFragmentPadding
        contentTextView.textContainerInset = UIEdgeInsetsMake(0, -padding, 0, -padding)
        
        upVoteButton.setTitle(LocalizedString("Up 20", comment: ""), for: .normal)
        upVoteButton.setTitleColor(UIColor.up, for: .normal)
        upVoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        upVoteButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        
        downVoteButton.setTitle(LocalizedString("Down 20", comment: ""), for: .normal)
        downVoteButton.setTitleColor(UIColor.down, for: .normal)
        downVoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        downVoteButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        
        shareButton.setTitle(LocalizedString("Share", comment: ""), for: .normal)
        shareButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        shareButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        shareButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
    }
    
    func updateUIStyle(news: News) {
        // parse data
        dateLabel.text = news.publishTime
        sourceLabel.text = news.source
        titleLabel.text = news.title
        contentTextView.text = news.content
    }
    
    class func getSize() -> CGSize {
        return CGSize(width: UIScreen.main.bounds.width - 15*2, height: 166)
    }
    
    class func getCellIdentifier() -> String {
        return "NewsCollectionCell"
    }

}
