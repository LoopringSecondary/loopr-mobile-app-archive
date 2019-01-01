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
        upvoteButton.setTitleColor(UIColor.upInChart, for: .normal)
        upvoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        upvoteButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        upvoteButton.addTarget(self, action: #selector(pressedUpvoteButton), for: .touchUpInside)
        
        downvoteButton.setTitle(LocalizedString("News_Down", comment: ""), for: .normal)
        // downInChart color is better than down color here
        downvoteButton.setTitleColor(UIColor.downInChart, for: .normal)
        downvoteButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        downvoteButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        downvoteButton.addTarget(self, action: #selector(pressedDownvoteButton), for: .touchUpInside)
        
        shareButton.setTitle(LocalizedString("Share", comment: ""), for: .normal)
        // shareButton.theme_setTitleColor(GlobalPicker.textLightColor, forState: .normal)
        shareButton.setTitleColor(UIColor.theme, for: .normal)
        shareButton.setTitleColor(UIColor.init(white: 0.5, alpha: 1), for: .highlighted)
        shareButton.titleLabel?.font = FontConfigManager.shared.getRegularFont(size: 12)
        shareButton.addTarget(self, action: #selector(pressedshareButton), for: .touchUpInside)
    }
    
    func updateUIStyle(news: News) {
        self.news = news

        // parse data
        dateLabel.text = news.publishTime
        sourceLabel.text = news.source
        titleLabel.text = news.title

        let style = NSMutableParagraphStyle()
        style.lineSpacing = NewsCollectionCell.descriptionTextViewLineSpacing
        let attributes = [NSAttributedStringKey.paragraphStyle: style]
        descriptionTextView.attributedText = NSAttributedString(string: news.description, attributes: attributes)
        descriptionTextView.font = FontConfigManager.shared.getRegularFont(size: 14)
        
        descriptionTextView.theme_textColor = GlobalPicker.textLightColor

        updateVoteButtons()
    }
    
    @IBAction func clickedCollectionCell(_ sender: Any) {
        didClickedCollectionCellClosure?(news)
    }
    
    @objc func pressedUpvoteButton(_ sender: Any) {
        upvoteButton.shake(direction: "y", withTranslation: 6)
        NewsDataManager.shared.setVote(uuid: news.uuid, isUpvote: true)
        CrawlerAPIRequest.confirmBull(uuid: news.uuid) { (_, _) in
            
        }
        updateVoteButtons()
    }
    
    @objc func pressedDownvoteButton(_ sender: Any) {
        downvoteButton.shake(direction: "y", withTranslation: 6)
        NewsDataManager.shared.setVote(uuid: news.uuid, isUpvote: false)
        CrawlerAPIRequest.confirmBear(uuid: news.uuid) { (_, _) in
            
        }
        updateVoteButtons()
    }
    
    @objc func pressedshareButton(_ sender: Any) {
        didPressedShareButtonClosure?(news)
    }
    
    func updateVoteButtons() {
        let localVote = NewsDataManager.shared.getVote(uuid: news.uuid)
        var localUpvoteValue: Int = 0
        var localDownvoteValue: Int = 0
        if localVote > 0 {
            localUpvoteValue = localVote
        } else {
            localDownvoteValue = -localVote
        }
        upvoteButton.setTitle("\(LocalizedString("News_Up", comment: "")) \(news.bullIndex+localUpvoteValue)", for: .normal)
        downvoteButton.setTitle("\(LocalizedString("News_Down", comment: "")) \(news.bearIndex+localDownvoteValue)", for: .normal)
    }
    
    class func getSize(news: News) -> CGSize {
        if news.category == .information {
            return CGSize(width: UIScreen.main.bounds.width - 15*2, height: 190)
        } else {
            return CGSize(width: UIScreen.main.bounds.width - 15*2, height: 190)
            // GarlandView doesn't support different height of collection view cell between view controllers.
            /*
            let width: CGFloat = UIScreen.main.bounds.width - 15*2
            let maxHeight: CGFloat = UIScreen.main.bounds.height * 0.7
            let descriptionTextView: UITextView = UITextView(frame: CGRect(x: 0, y: 0, width: width, height: maxHeight))
            descriptionTextView.font = FontConfigManager.shared.getRegularFont(size: 12)
            descriptionTextView.text = news.description
            let numLines = (descriptionTextView.contentSize.height / descriptionTextView.font!.lineHeight) as CGFloat
            let height = CGFloat(ceil(numLines)) * descriptionTextView.font!.lineHeight + CGFloat(ceil(numLines) - 1) * descriptionTextViewLineSpacing
            let otherHeight: CGFloat = 109
            return CGSize(width: UIScreen.main.bounds.width - 15*2, height: height + otherHeight)
            */
        }
    }
    
    class func getCellIdentifier() -> String {
        return "NewsCollectionCell"
    }

}
