//
//  NewsDetailStringTableViewCell.swift
//  loopr-ios
//
//  Created by Ruby on 1/13/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NewsDetailStringTableViewCell: UITableViewCell {

    @IBOutlet weak var contentTextView: UITextView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        selectionStyle = .none
        theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        let padding = contentTextView.textContainer.lineFragmentPadding
        
        contentTextView.backgroundColor = .clear
        contentTextView.isUserInteractionEnabled = false
        contentTextView.isScrollEnabled = false
        contentTextView.showsVerticalScrollIndicator = false
        contentTextView.showsHorizontalScrollIndicator = false
        contentTextView.textContainerInset = UIEdgeInsetsMake(0, -padding, 0, -padding)
    }
    
    func update(content: String) {
        // TODO: this causes slow scrolling
        let style = NSMutableParagraphStyle()
        style.lineSpacing = NewsDetailStringTableViewCell.textViewLineSpacing
        let attributes = [NSAttributedStringKey.paragraphStyle: style]
        contentTextView.attributedText = NSAttributedString(string: content, attributes: attributes)

        contentTextView.font = FontConfigManager.shared.getRegularFont(size: 14)
        contentTextView.theme_textColor = GlobalPicker.textColor
    }
    
    class func getCellIdentifier() -> String {
        return "NewsDetailStringTableViewCell"
    }
    
    static let textViewLineSpacing: CGFloat = 3

    class func getHeight(content: String) -> CGFloat {
        let width: CGFloat = UIScreen.main.bounds.width - 15*2

        let maxHeight: CGFloat = UIScreen.main.bounds.height * 0.7
        let textView: UITextView = UITextView(frame: CGRect(x: 0, y: 0, width: width, height: maxHeight))
        textView.font = FontConfigManager.shared.getRegularFont(size: 14)
        textView.text = content
        let numLines = CGFloat(NewsCollectionCell.numberOfLines(textView: textView))
        var textViewheight = CGFloat((numLines)) * textView.font!.lineHeight
        
        if numLines > 0 {
            textViewheight += CGFloat((numLines)) * textViewLineSpacing
        }
        let otherHeight: CGFloat = 8
        let height = textViewheight + otherHeight

        return height        
    }

}
