//
//  NewsUIStyleConfig.swift
//  loopr-ios
//
//  Created by xiaoruby on 1/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class NewsUIStyleConfig {
    
    public static var shared = NewsUIStyleConfig()
    
    var scrollingDistance: CGFloat = -120
    
    let newsViewControllerPresentAnimationDuration: TimeInterval = 1
    let newsViewControllerPresentAnimationDelay: TimeInterval = 0.1
    let newsViewControllerPresentAnimationSpringWithDamping: CGFloat = 0.75
    let newsViewControllerPresentAnimationInitialSpringVelocity: CGFloat = 2

    private var newsDetailFontType: String = "isSmall"
    var newsDetailTitleFont: CGFloat = 20
    var newsDetailSubtitleFont: CGFloat = 12
    var newsDetailBodyFont: CGFloat = 14
    
    init() {
        scrollingDistance  = -UIScreen.main.bounds.height * 0.18
    }
    
    func isNewsDetailFontTypeSmall() -> Bool {
        if newsDetailFontType == "isSmall" {
            return true
        } else {
            return false
        }
    }
    
    func setNewsDetailBodyFont(isSmall: Bool) {
        if isSmall {
            newsDetailTitleFont = 20
            newsDetailSubtitleFont = 12
            newsDetailBodyFont = 14
        } else {
            newsDetailTitleFont = 28
            newsDetailSubtitleFont = 14
            newsDetailBodyFont = 20
        }
        NotificationCenter.default.post(name: .adjustFontInNewsDetailViewController, object: nil)
    }
}
