//
//  NewsListHeaderView.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit

class NewsListHeaderView: UIView {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var baseView: UIImageView!

    open override func awakeFromNib() {
        super.awakeFromNib()
        
        let config = GarlandConfig.shared
        frame.size = config.headerSize
        
        theme_backgroundColor = ColorPicker.cardBackgroundColor
        layer.masksToBounds = true
        layer.cornerRadius = config.cardRadius
        layer.shadowOffset = config.cardShadowOffset
        layer.shadowColor = config.cardShadowColor.cgColor
        layer.shadowOpacity = config.cardShadowOpacity
        layer.shadowRadius = config.cardShadowRadius

        baseView.contentMode = .scaleAspectFill
        baseView.cornerRadius = 6
        baseView.backgroundColor = UIColor.clear
        baseView.clipsToBounds = true
        baseView.layer.masksToBounds = true

        // TODO: need to update to the font size
        titleLabel.font = FontConfigManager.shared.getCharactorFont(size: 28)
        titleLabel.textColor = UIColor.white
        titleLabel.isHidden = true
    }

}
