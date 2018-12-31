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
        
        layer.masksToBounds = false
        layer.cornerRadius = config.cardRadius
        layer.shadowOffset = config.cardShadowOffset
        layer.shadowColor = config.cardShadowColor.cgColor
        layer.shadowOpacity = config.cardShadowOpacity
        layer.shadowRadius = config.cardShadowRadius
        
        baseView.image = UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())
        baseView.contentMode = .scaleToFill
        baseView.backgroundColor = .clear
        
        // TODO: need to update to the font size
        titleLabel.font = FontConfigManager.shared.getCharactorFont(size: 28)
        titleLabel.textColor = UIColor.white
    }

}
