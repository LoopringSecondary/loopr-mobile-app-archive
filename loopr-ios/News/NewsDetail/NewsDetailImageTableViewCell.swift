//
//  NewsDetailImageTableViewCell.swift
//  loopr-ios
//
//  Created by Ruby on 1/13/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NewsDetailImageTableViewCell: UITableViewCell {

    @IBOutlet weak var backgroundImageView: UIImageView!
    @IBOutlet weak var imageBottomLayoutConstraint: NSLayoutConstraint!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        selectionStyle = .none
        theme_backgroundColor = ColorPicker.cardBackgroundColor

        backgroundImageView.contentMode = .scaleAspectFill
        backgroundImageView.cornerRadius = 8
        backgroundImageView.clipsToBounds = true

        imageBottomLayoutConstraint.constant = 4 + NewsUIStyleConfig.shared.newsDetailPadding
    }

    class func getCellIdentifier() -> String {
        return "NewsDetailImageTableViewCell"
    }

    class func getHeight(image: UIImage?) -> CGFloat {
        if image != nil {
            let width: CGFloat = UIScreen.main.bounds.width - 15*2
            let height = image!.size.height/image!.size.width*width
            return height + NewsUIStyleConfig.shared.newsDetailPadding
        } else {
            return 0
        }
    }
}
