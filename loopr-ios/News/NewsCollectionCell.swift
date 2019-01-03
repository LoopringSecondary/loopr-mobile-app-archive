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
    static let descriptionTextViewLineSpacing: CGFloat = 3

    @IBOutlet weak var buttonView: UIView!
    @IBOutlet weak var upvoteButton: UIButton!
    @IBOutlet weak var downvoteButton: UIButton!
    @IBOutlet weak var shareButton: UIButton!
    
    var didClickedCollectionCellClosure: ((News) -> Void)?
    var didPressedShareButtonClosure: ((News) -> Void)?

    let iconTitlePadding: CGFloat = 8

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
        layer.shadowRadius = 4
        
        layer.shouldRasterize = true
        layer.rasterizationScale = UIScreen.main.scale
        
        cornerRadius = config.cardRadius
        
        // update UI
        dateLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        // dateLabel.theme_textColor = GlobalPicker.textLightColor
        dateLabel.textColor = UIColor.theme
        
        sourceLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        sourceLabel.theme_textColor = GlobalPicker.textLightColor
        sourceLabel.textAlignment = .right
        
        titleLabel.font = FontConfigManager.shared.getMediumFont(size: 16)
        titleLabel.theme_textColor = GlobalPicker.textColor

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
        upvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        
        upvoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        upvoteButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        upvoteButton.addTarget(self, action: #selector(pressedUpvoteButton), for: .touchUpInside)
        upvoteButton.set(image: UIImage.init(named: "Upvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
        
        downvoteButton.setTitle(LocalizedString("News_Down", comment: ""), for: .normal)
        // downInChart color is better than down color here
        downvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        downvoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        downvoteButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        downvoteButton.addTarget(self, action: #selector(pressedDownvoteButton), for: .touchUpInside)
        downvoteButton.set(image: UIImage.init(named: "Downvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)

        // shareButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        shareButton.setTitleColor(UIColor.theme, for: .normal)
        shareButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        shareButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        shareButton.addTarget(self, action: #selector(pressedshareButton), for: .touchUpInside)
        shareButton.setTitle(LocalizedString("Share", comment: ""), for: .normal)
        shareButton.set(image: UIImage.init(named: "News-share"), title: LocalizedString("Share", comment: ""), titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
    }
    
    func updateUIStyle(news: News, isExpanded: Bool) {
        self.news = news

        // parse data
        dateLabel.text = news.publishTime
        sourceLabel.text = news.source
        titleLabel.text = news.title

        let style = NSMutableParagraphStyle()
        style.lineSpacing = NewsCollectionCell.descriptionTextViewLineSpacing
        let attributes = [NSAttributedStringKey.paragraphStyle: style]
        descriptionTextView.attributedText = NSAttributedString(string: news.description, attributes: attributes)
        
        // TODO: We have two ways to show an expanded cell
        /*
        descriptionTextView.theme_textColor = GlobalPicker.textLightColor
        if isExpanded {
            descriptionTextView.font = FontConfigManager.shared.getRegularFont(size: 14)
        } else {
            descriptionTextView.font = FontConfigManager.shared.getRegularFont(size: 10)
        }
        */
        
        descriptionTextView.font = FontConfigManager.shared.getRegularFont(size: 14)
        if isExpanded {
            descriptionTextView.theme_textColor = GlobalPicker.textColor
        } else {
            descriptionTextView.theme_textColor = GlobalPicker.textLightColor
        }

        updateVoteButtons()
    }
    
    @IBAction func clickedCollectionCell(_ sender: Any) {
        didClickedCollectionCellClosure?(news)
    }
    
    @objc func pressedUpvoteButton(_ sender: Any) {
        let vote = NewsDataManager.shared.getVote(uuid: news.uuid)
        if vote > 0 {
            CrawlerAPIRequest.cancelBull(uuid: news.uuid) { (_, _) in }
            
            news.bullIndex -= 1
            NewsDataManager.shared.updateVote(updatedNews: news)
            
            NewsDataManager.shared.votes[news.uuid] = 0
        } else if vote < 0 {
            CrawlerAPIRequest.confirmBull(uuid: news.uuid) { (_, _) in}

            news.bullIndex += 1
            news.bearIndex -= 1
            NewsDataManager.shared.updateVote(updatedNews: news)
            
            NewsDataManager.shared.votes[news.uuid] = 1

            CrawlerAPIRequest.cancelBear(uuid: news.uuid) { (_, _) in }
            
        } else if vote == 0 {
            CrawlerAPIRequest.confirmBull(uuid: news.uuid) { (_, _) in }
            
            news.bullIndex += 1
            NewsDataManager.shared.updateVote(updatedNews: news)
            
            NewsDataManager.shared.votes[news.uuid] = 1
        }

        UserDefaults.standard.set(NewsDataManager.shared.votes, forKey: UserDefaultsKeys.newsUpvoteAndDownvote.rawValue)

        upvoteButton.shake(direction: "y", withTranslation: 6)
        updateVoteButtons()
    }
    
    @objc func pressedDownvoteButton(_ sender: Any) {
        let vote = NewsDataManager.shared.getVote(uuid: news.uuid)
        if vote < 0 {
            CrawlerAPIRequest.cancelBear(uuid: news.uuid) { (_, _) in }
            
            news.bearIndex -= 1
            NewsDataManager.shared.updateVote(updatedNews: news)
            
            NewsDataManager.shared.votes[news.uuid] = 0
        } else if vote > 0 {
            CrawlerAPIRequest.confirmBear(uuid: news.uuid) { (_, _) in }

            news.bullIndex -= 1
            news.bearIndex += 1
            NewsDataManager.shared.updateVote(updatedNews: news)
            
            NewsDataManager.shared.votes[news.uuid] = -1

            CrawlerAPIRequest.cancelBull(uuid: news.uuid) { (_, _) in }

        } else if vote == 0 {
            CrawlerAPIRequest.confirmBear(uuid: news.uuid) { (_, _) in }

            news.bearIndex += 1
            NewsDataManager.shared.updateVote(updatedNews: news)
            
            NewsDataManager.shared.votes[news.uuid] = -1
        }

        UserDefaults.standard.set(NewsDataManager.shared.votes, forKey: UserDefaultsKeys.newsUpvoteAndDownvote.rawValue)

        downvoteButton.shake(direction: "y", withTranslation: 6)
        updateVoteButtons()
    }
    
    @objc func pressedshareButton(_ sender: Any) {
        didPressedShareButtonClosure?(news)
    }
    
    func updateVoteButtons() {
        let vote = NewsDataManager.shared.getVote(uuid: news.uuid)
        if vote > 0 {
            // upvoteButton.setTitleColor(UIColor.upInChart, for: .normal)
            upvoteButton.setTitleColor(UIColor.down, for: .normal)
            upvoteButton.set(image: UIImage.init(named: "Upvote-selected"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
            downvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
            downvoteButton.set(image: UIImage.init(named: "Downvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
        } else if vote < 0 {
            upvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
            upvoteButton.set(image: UIImage.init(named: "Upvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
            
            // downvoteButton.setTitleColor(UIColor.downInChart, for: .normal)
            downvoteButton.setTitleColor(UIColor.up, for: .normal)
            downvoteButton.set(image: UIImage.init(named: "Downvote-selected"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
        } else {
            upvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
            upvoteButton.set(image: UIImage.init(named: "Upvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)

            downvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
            downvoteButton.set(image: UIImage.init(named: "Downvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
        }

        upvoteButton.setTitle("\(LocalizedString("News_Up", comment: "")) \(news.bullIndex)", for: .normal)
        downvoteButton.setTitle("\(LocalizedString("News_Down", comment: "")) \(news.bearIndex)", for: .normal)
    }
    
    class func getSize(news: News, isExpanded: Bool) -> CGSize {
        let minHeight: CGFloat = 190
        
        if isExpanded {
            let width: CGFloat = UIScreen.main.bounds.width - 15*2
            let maxHeight: CGFloat = UIScreen.main.bounds.height * 0.7
            let descriptionTextView: UITextView = UITextView(frame: CGRect(x: 0, y: 0, width: width, height: maxHeight))
            descriptionTextView.font = FontConfigManager.shared.getRegularFont(size: 12)
            descriptionTextView.text = news.description
            let numLines = (descriptionTextView.contentSize.height / descriptionTextView.font!.lineHeight) as CGFloat
            let textViewheight = CGFloat(ceil(numLines)) * descriptionTextView.font!.lineHeight + CGFloat(ceil(numLines) - 1) * descriptionTextViewLineSpacing
            let otherHeight: CGFloat = 109
            var height = textViewheight + otherHeight
            if height <  minHeight {
                height = minHeight
            }
            return CGSize(width: UIScreen.main.bounds.width - 15*2, height: height)
        }

        if news.category == .information {
            return CGSize(width: UIScreen.main.bounds.width - 15*2, height: minHeight)
        } else {
            return CGSize(width: UIScreen.main.bounds.width - 15*2, height: minHeight)
        }
    }
    
    class func getCellIdentifier() -> String {
        return "NewsCollectionCell"
    }

}
