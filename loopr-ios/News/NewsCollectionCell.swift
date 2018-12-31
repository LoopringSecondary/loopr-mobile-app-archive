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
    
    var news: News!

    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var sourceLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var descriptionTextView: UITextView!

    @IBOutlet weak var buttonView: UIView!
    @IBOutlet weak var upvoteButton: UIButton!
    @IBOutlet weak var downvoteButton: UIButton!
    @IBOutlet weak var shareButton: UIButton!
    
    var didClickedCollectionCellClosure: ((News) -> Void)?

    open override func awakeFromNib() {
        super.awakeFromNib()
        
        theme_backgroundColor = ColorPicker.cardBackgroundColor
        contentView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        buttonView.backgroundColor = .clear
        
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
        
        descriptionTextView.font = FontConfigManager.shared.getRegularFont(size: 12)
        descriptionTextView.theme_textColor = GlobalPicker.textLightColor
        descriptionTextView.backgroundColor = .clear
        descriptionTextView.isUserInteractionEnabled = false
        descriptionTextView.isScrollEnabled = false
        // contentTextView.textContainerInset = UIEdgeInsets.init(top: 0, left: 0, bottom: 0, right: 0)
        descriptionTextView.showsVerticalScrollIndicator = false
        descriptionTextView.showsHorizontalScrollIndicator = false
        let padding = descriptionTextView.textContainer.lineFragmentPadding
        descriptionTextView.textContainerInset = UIEdgeInsetsMake(0, -padding, 0, -padding)
        
        upvoteButton.setTitle(LocalizedString("News_Up", comment: ""), for: .normal)
        // upInChart color is better than up color here
        upvoteButton.setTitleColor(UIColor.upInChart, for: .normal)
        upvoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        upvoteButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        upvoteButton.addTarget(self, action: #selector(pressedUpvoteButton), for: .touchUpInside)
        
        downvoteButton.setTitle(LocalizedString("News_Down", comment: ""), for: .normal)
        // downInChart color is better than down color here
        downvoteButton.setTitleColor(UIColor.downInChart, for: .normal)
        downvoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        downvoteButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        upvoteButton.addTarget(self, action: #selector(pressedDownvoteButton), for: .touchUpInside)
        
        shareButton.setTitle(LocalizedString("Share", comment: ""), for: .normal)
        // shareButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        shareButton.setTitleColor(UIColor.theme, for: .normal)
        shareButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        shareButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
    }
    
    func updateUIStyle(news: News) {
        self.news = news

        // parse data
        dateLabel.text = news.publishTime
        sourceLabel.text = news.source
        titleLabel.text = news.title
        descriptionTextView.text = news.description
        
        upvoteButton.setTitle("\(LocalizedString("News_Up", comment: "")) \(news.bullIndex)", for: .normal)
        downvoteButton.setTitle("\(LocalizedString("News_Down", comment: "")) \(news.bearIndex)", for: .normal)
    }
    
    @IBAction func clickedCollectionCell(_ sender: Any) {
        didClickedCollectionCellClosure?(news)
    }
    
    @objc func pressedUpvoteButton(_ sender: Any) {
        CrawlerAPIRequest.confirmBull(uuid: news.uuid) { (_, _) in
            
        }
    }
    
    @objc func pressedDownvoteButton(_ sender: Any) {
        CrawlerAPIRequest.confirmBear(uuid: news.uuid) { (_, _) in
            
        }
    }
    
    class func getSize() -> CGSize {
        return CGSize(width: UIScreen.main.bounds.width - 15*2, height: 166)
    }
    
    class func getCellIdentifier() -> String {
        return "NewsCollectionCell"
    }

}
