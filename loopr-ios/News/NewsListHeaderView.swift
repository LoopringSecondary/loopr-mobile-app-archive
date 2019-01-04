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
    
    var blog: Blog?
    var didClickedClosure: ((Blog) -> Void)?
    
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
        
        if let blog = NewsDataManager.shared.getNextBlog() {
            self.blog = blog
            baseView.image = blog.image ?? UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())
            let singleTap = UITapGestureRecognizer(target: self, action: #selector(tapDetected))
            baseView.isUserInteractionEnabled = true
            baseView.addGestureRecognizer(singleTap)
            
        } else {
            baseView.image = UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())
        }
        
        baseView.contentMode = .scaleAspectFill
        baseView.cornerRadius = 6
        baseView.clipsToBounds = true
        baseView.backgroundColor = .clear
        
        // TODO: need to update to the font size
        titleLabel.font = FontConfigManager.shared.getCharactorFont(size: 28)
        titleLabel.textColor = UIColor.white
        titleLabel.isHidden = true
    }

    //Action
    @objc func tapDetected() {
        print("Imageview Clicked")
        if blog != nil {
            didClickedClosure?(blog!)
        }
    }

}
