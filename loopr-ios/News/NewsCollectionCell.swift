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
    @IBOutlet weak var titleTextView: UITextView!
    @IBOutlet weak var titleTextViewHeightLayout: NSLayoutConstraint!
    @IBOutlet weak var titleTextViewTrailingLayoutConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var descriptionTextViewLeadingLayoutConstraint: NSLayoutConstraint!
    @IBOutlet weak var descriptionTextViewTrailingLayoutConstraint: NSLayoutConstraint!
    @IBOutlet weak var descriptionTextView: UITextView!
    @IBOutlet weak var informationImageView: UIImageView!
    @IBOutlet weak var informationImageViewHeightLayoutConstraint: NSLayoutConstraint!
    
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
        
        layer.cornerRadius = 6
        layer.shadowOffset = CGSize(width: 0, height: 2)
        layer.shadowColor = UIColor.black.cgColor
        layer.shadowOpacity = 0.3
        
        layer.shouldRasterize = true
        layer.rasterizationScale = UIScreen.main.scale
        
        cornerRadius = 6
        
        // update UI
        dateLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        // dateLabel.theme_textColor = GlobalPicker.textLightColor
        dateLabel.textColor = UIColor.theme
        
        sourceLabel.font = FontConfigManager.shared.getRegularFont(size: 12)
        sourceLabel.theme_textColor = GlobalPicker.textLightColor
        sourceLabel.textAlignment = .left
        
        let padding = descriptionTextView.textContainer.lineFragmentPadding
        titleTextView.font = FontConfigManager.shared.getMediumFont(size: 16)
        titleTextView.theme_textColor = GlobalPicker.textColor
        titleTextView.backgroundColor = .clear
        titleTextView.isUserInteractionEnabled = false
        titleTextView.isScrollEnabled = false
        titleTextView.showsVerticalScrollIndicator = false
        titleTextView.showsHorizontalScrollIndicator = false
        titleTextView.textContainerInset = UIEdgeInsetsMake(0, -padding, 0, -padding)

        descriptionTextView.backgroundColor = .clear
        descriptionTextView.isUserInteractionEnabled = false
        descriptionTextView.isScrollEnabled = false
        descriptionTextView.showsVerticalScrollIndicator = false
        descriptionTextView.showsHorizontalScrollIndicator = false
        descriptionTextView.textContainer.lineBreakMode = .byTruncatingTail
        descriptionTextView.textContainerInset = UIEdgeInsetsMake(0, -padding, 0, -padding)
        
        upvoteButton.setTitle(LocalizedString("News_Up", comment: ""), for: .normal)
        // upInChart color is better than up color here
        upvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        
        // upvoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
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

        shareButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        shareButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        shareButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        shareButton.addTarget(self, action: #selector(pressedshareButton), for: .touchUpInside)
        shareButton.set(image: UIImage.init(named: "News-share")?.alpha(0.4), title: LocalizedString("Share", comment: ""), titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
        
        informationImageViewHeightLayoutConstraint.constant = 100
        informationImageView.cornerRadius = 4
        informationImageView.contentMode = .scaleAspectFill
        informationImageView.clipsToBounds = true
    }
    
    func updateUIStyle(news: News, isExpanded: Bool) {
        self.news = news

        // parse data
        dateLabel.text = news.publishTime
        sourceLabel.text = news.source
        titleTextView.text = news.title

        if news.category == .information && news.newsImage != nil {
            informationImageView.image = news.newsImage?.image
            titleTextViewTrailingLayoutConstraint.constant = 118
            // descriptionTextViewLeadingLayoutConstraint.constant = 135
            descriptionTextViewTrailingLayoutConstraint.constant = 118
            informationImageView.isHidden = false
            if news.newsImage!.isLoading == true {
                news.newsImage!.downloadImage { (image) in
                    DispatchQueue.main.async {
                        self.informationImageView.image = image
                    }
                }
            }
        } else {
            titleTextViewTrailingLayoutConstraint.constant = 10
            descriptionTextViewTrailingLayoutConstraint.constant = 10
            informationImageView.isHidden = true
        }
        titleTextViewTrailingLayoutConstraint.constant += 4

        // TODO: move to other places?
        let width: CGFloat = UIScreen.main.bounds.width - 15*2
        let maxHeight: CGFloat = UIScreen.main.bounds.height * 0.7
        let titleTextViewWidth: CGFloat = width - 10 - titleTextViewTrailingLayoutConstraint.constant

        let localTextView: UITextView = UITextView(frame: CGRect(x: 0, y: 0, width: titleTextViewWidth, height: maxHeight))
        let padding = localTextView.textContainer.lineFragmentPadding
        localTextView.textContainerInset = UIEdgeInsetsMake(0, -padding, 0, -padding)
        localTextView.font = titleTextView.font
        localTextView.text = news.title
        
        let rawLineNumber = NewsCollectionCell.numberOfLines(textView: localTextView)
        let numLines = CGFloat(rawLineNumber)
        
        // TODO: 4 will break the measure of lines
        titleTextViewHeightLayout.constant = titleTextView.font!.lineHeight*numLines + 4

        // TODO: this causes slow scrolling
        descriptionTextView.attributedText = news.descriptionAttributedText
        
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
    
    class func numberOfLines(textView: UITextView) -> Int {
        let layoutManager = textView.layoutManager
        let numberOfGlyphs = layoutManager.numberOfGlyphs
        var lineRange: NSRange = NSMakeRange(0, 1)
        var index = 0
        var numberOfLines = 0
        
        while index < numberOfGlyphs {
            layoutManager.lineFragmentRect(forGlyphAt: index, effectiveRange: &lineRange)
            index = NSMaxRange(lineRange)
            numberOfLines += 1
        }
        return numberOfLines
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
        // shareButton.shake(direction: "y", withTranslation: 6)
        didPressedShareButtonClosure?(news)
    }
    
    func updateVoteButtons() {
        let language = SettingDataManager.shared.getCurrentLanguage()
        let upvoteSelectedImageName: String
        let downvoteSelectedImageName: String
        if language == Language(name: "zh-Hans") {
            upvoteSelectedImageName = "Upvote-selected-red"
            downvoteSelectedImageName = "Downvote-selected-green"
        } else {
            upvoteSelectedImageName = "Upvote-selected-green"
            downvoteSelectedImageName = "Downvote-selected-red"
        }

        let vote = NewsDataManager.shared.getVote(uuid: news.uuid)
        if vote > 0 {
            // upvoteButton.setTitleColor(UIColor.upInChart, for: .normal)
            upvoteButton.setTitleColor(UIColor.up, for: .normal)
            upvoteButton.set(image: UIImage.init(named: upvoteSelectedImageName), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
            downvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
            downvoteButton.set(image: UIImage.init(named: "Downvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
        } else if vote < 0 {
            upvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
            upvoteButton.set(image: UIImage.init(named: "Upvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
            
            // downvoteButton.setTitleColor(UIColor.downInChart, for: .normal)
            downvoteButton.setTitleColor(UIColor.down, for: .normal)
            downvoteButton.set(image: UIImage.init(named: downvoteSelectedImageName), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
        } else {
            upvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
            upvoteButton.set(image: UIImage.init(named: "Upvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)

            downvoteButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
            downvoteButton.set(image: UIImage.init(named: "Downvote"), title: "", titlePosition: .right, additionalSpacing: iconTitlePadding, state: .normal)
        }
        
        upvoteButton.imageEdgeInsets = UIEdgeInsets.init(top: -2, left: 0, bottom: 2, right: 0)
        downvoteButton.imageEdgeInsets = UIEdgeInsets.init(top: 2, left: 0, bottom: -2, right: 0)

        upvoteButton.setTitle("\(LocalizedString("News_Up", comment: "")) \(news.bullIndex)", for: .normal)
        downvoteButton.setTitle("\(LocalizedString("News_Down", comment: "")) \(news.bearIndex)", for: .normal)
        
        if language == Language(name: "en") {
            
        }
    }
    
    static let informationMinHeight: CGFloat = 190
    static let flashMinHeight: CGFloat = 190
    
    class func getSize(news: News, isExpanded: Bool) -> CGSize {
        let width: CGFloat = UIScreen.main.bounds.width - 15*2
        
        if isExpanded {
            let maxHeight: CGFloat = UIScreen.main.bounds.height * 0.7
            let descriptionTextView: UITextView = UITextView(frame: CGRect(x: 0, y: 0, width: width-10*2, height: maxHeight))
            descriptionTextView.font = FontConfigManager.shared.getRegularFont(size: 14)
            descriptionTextView.text = news.description
            let padding = descriptionTextView.textContainer.lineFragmentPadding
            descriptionTextView.textContainerInset = UIEdgeInsetsMake(0, -padding, 0, -padding)
            let numLines = CGFloat(numberOfLines(textView: descriptionTextView)) + 1
            let textViewheight = CGFloat((numLines)) * descriptionTextView.font!.lineHeight + CGFloat((numLines) - 1) * descriptionTextViewLineSpacing
            
            let titleTextView = UITextView(frame: CGRect(x: 0, y: 0, width: width-10*2, height: maxHeight))
            titleTextView.font = FontConfigManager.shared.getMediumFont(size: 16)
            titleTextView.text = news.title
            titleTextView.textContainerInset = UIEdgeInsetsMake(0, -padding, 0, -padding)
            let rawLineNumber = CGFloat(numberOfLines(textView: titleTextView))
            let titleHeight = titleTextView.font!.lineHeight*rawLineNumber
            
            let otherHeight: CGFloat = 74
            var height = textViewheight + otherHeight + titleHeight
            if height < flashMinHeight {
                height = flashMinHeight
            }
            return CGSize(width: width, height: height)
        }

        if news.category == .information {
            return CGSize(width: width, height: informationMinHeight)
        } else {
            return CGSize(width: width, height: flashMinHeight)
        }
    }
    
    class func getCellIdentifier() -> String {
        return "NewsCollectionCell"
    }

}
