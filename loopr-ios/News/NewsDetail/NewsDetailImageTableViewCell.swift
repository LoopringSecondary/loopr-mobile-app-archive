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

    override func awakeFromNib() {
        super.awakeFromNib()
        selectionStyle = .none
        theme_backgroundColor = ColorPicker.cardBackgroundColor

        backgroundImageView.contentMode = .scaleAspectFill
        backgroundImageView.cornerRadius = 8
        backgroundImageView.clipsToBounds = true
    }

    class func getCellIdentifier() -> String {
        return "NewsDetailImageTableViewCell"
    }

    class func getHeight(image: UIImage?) -> CGFloat {
        if image != nil {
            let width: CGFloat = UIScreen.main.bounds.width - 15*2
            let height = image!.size.height/image!.size.width*width
            return height + 8
        } else {
            return 0
        }
    }
}
