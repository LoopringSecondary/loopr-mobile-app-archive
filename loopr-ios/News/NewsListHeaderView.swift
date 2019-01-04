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
    @IBOutlet weak var blogBackgroundView: UIView!
    @IBOutlet weak var blogTitleLabel: UILabel!
    @IBOutlet weak var blogIndexLabel: UILabel!

    var blog: Blog?
    var index: Int = 0
    var didClickedClosure: ((Blog) -> Void)?
    
    let animationDuration: TimeInterval = 1
    let switchingInterval: TimeInterval = 10
    
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
        
        switchToNextBlog()
        
        baseView.contentMode = .scaleAspectFill
        baseView.cornerRadius = 6
        baseView.backgroundColor = UIColor.clear
        baseView.clipsToBounds = true
        baseView.layer.masksToBounds = true
        
        // blogBackgroundView.backgroundColor = UIColor(rgba: "#2B2C47").withAlphaComponent(0.9)
        blogBackgroundView.backgroundColor = UIColor.black.withAlphaComponent(0.9)
        blogTitleLabel.font = FontConfigManager.shared.getMediumFont(size: 16)
        blogTitleLabel.theme_textColor = GlobalPicker.textColor
        
        blogIndexLabel.font = FontConfigManager.shared.getMediumFont(size: 16)
        blogIndexLabel.theme_textColor = GlobalPicker.textColor
        
        // TODO: need to update to the font size
        titleLabel.font = FontConfigManager.shared.getCharactorFont(size: 28)
        titleLabel.textColor = UIColor.white
        titleLabel.isHidden = true
    }

    func animateImageView(blog: Blog, currentIndex: Int) {
        CATransaction.begin()
        
        CATransaction.setAnimationDuration(animationDuration)
        CATransaction.setCompletionBlock {
            DispatchQueue.main.asyncAfter(deadline: .now() + self.switchingInterval) {
                self.switchToNextBlog()
            }
        }
        
        let transition = CATransition()
        // transition.type = kCATransitionFade
        transition.type = kCATransitionPush
        transition.subtype = kCATransitionFromRight
        
        baseView.layer.add(transition, forKey: kCATransition)
        baseView.image = blog.image ?? UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())!
        
        blogBackgroundView.layer.add(transition, forKey: kCATransition)
        blogTitleLabel.text = blog.title
        blogIndexLabel.text = "\(currentIndex+1)/\(NewsDataManager.shared.blogs.count)"

        CATransaction.commit()
    }

    @objc func tapDetected() {
        print("Imageview Clicked")
        if blog != nil {
            didClickedClosure?(blog!)
        }
    }

    func switchToNextBlog() {
        let next = NewsDataManager.shared.getNextBlog()
        if next.0 != nil {
            blog = next.0
            index = next.1
            // baseView.image = blog.image ?? UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())
            let singleTap = UITapGestureRecognizer(target: self, action: #selector(tapDetected))
            baseView.isUserInteractionEnabled = true
            baseView.addGestureRecognizer(singleTap)
            
            animateImageView(blog: blog!, currentIndex: index)
            
        } else {
            baseView.image = UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())
        }
    }

}
