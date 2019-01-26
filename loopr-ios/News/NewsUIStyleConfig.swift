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
    
    var newsDescriptionNormalFont: CGFloat = 12
    var newsDescriptionExpandedFont: CGFloat = 14
    
    let newsViewControllerPresentAnimationDuration: TimeInterval = 1
    let newsViewControllerPresentAnimationDelay: TimeInterval = 0.1
    let newsViewControllerPresentAnimationSpringWithDamping: CGFloat = 0.75
    let newsViewControllerPresentAnimationInitialSpringVelocity: CGFloat = 2

    private var newsDetailFontType: String = "isSmall"
    var newsDetailTitleFont: CGFloat = 20
    var newsDetailSubtitleFont: CGFloat = 12
    var newsDetailBodyFont: CGFloat = 16
    var newsDetailPadding: CGFloat = 20
    var newsDetailTextViewLineSpacing: CGFloat = 1
    
    init() {
        getNewsDetailFontType()
        updateFontSize(isSmall: isNewsDetailFontTypeSmall())
        scrollingDistance  = -UIScreen.main.bounds.height * 0.12
    }
    
    func getNewsDetailFontType() {
        // TODO: use String value rather than a boolean
        newsDetailFontType = UserDefaults.standard.string(forKey: UserDefaultsKeys.newsDetailFontType.rawValue) ?? "isSmall"
    }
    
    func isNewsDetailFontTypeSmall() -> Bool {
        if newsDetailFontType == "isSmall" {
            return true
        } else {
            return false
        }
    }
    
    func updateFontSize(isSmall: Bool) {
        if isSmall {
            newsDetailTitleFont = 22
            newsDetailSubtitleFont = 12
            newsDetailBodyFont = 16
            newsDetailPadding = 30
            newsDetailTextViewLineSpacing = 1
        } else {
            newsDetailTitleFont = 28
            newsDetailSubtitleFont = 16
            newsDetailBodyFont = 22
            newsDetailPadding = 40
            newsDetailTextViewLineSpacing = 2
        }
    }
    
    func setNewsDetailBodyFont(isSmall: Bool) {
        updateFontSize(isSmall: isSmall)
        if isSmall {
            newsDetailFontType = "isSmall"
        } else {
            newsDetailFontType = "isLarge"
        }
        UserDefaults.standard.set(newsDetailFontType, forKey: UserDefaultsKeys.newsDetailFontType.rawValue)
        NotificationCenter.default.post(name: .adjustFontInNewsDetailViewController, object: nil)
    }

}
