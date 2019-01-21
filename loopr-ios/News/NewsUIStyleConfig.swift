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
    
    var scrollingDistance: CGFloat = -100
    
    let newsViewControllerPresentAnimationDuration: TimeInterval = 1
    let newsViewControllerPresentAnimationDelay: TimeInterval = 0.1
    let newsViewControllerPresentAnimationSpringWithDamping: CGFloat = 0.75
    let newsViewControllerPresentAnimationInitialSpringVelocity: CGFloat = 2

    private var newsDetailFontType: String = "isSmall"
    var newsDetailTitleFont: CGFloat = 20
    var newsDetailSubtitleFont: CGFloat = 12
    var newsDetailBodyFont: CGFloat = 14
    var newsDetailPadding: CGFloat = 10
    var newsDetailTextViewLineSpacing: CGFloat = 1
    
    init() {
        scrollingDistance  = -UIScreen.main.bounds.height * 0.14
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
            newsDetailPadding = 10
            newsDetailTextViewLineSpacing = 1
        } else {
            newsDetailTitleFont = 24
            newsDetailSubtitleFont = 16
            newsDetailBodyFont = 18
            newsDetailPadding = 20
            newsDetailTextViewLineSpacing = 2
        }
        NotificationCenter.default.post(name: .adjustFontInNewsDetailViewController, object: nil)
    }
}
