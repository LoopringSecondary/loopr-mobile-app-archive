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
    
    let scrollingDistance: CGFloat = -130
    
    let newsViewControllerPresentAnimationDuration: TimeInterval = 1
    let newsViewControllerPresentAnimationDelay: TimeInterval = 0.1
    let newsViewControllerPresentAnimationSpringWithDamping: CGFloat = 0.75
    let newsViewControllerPresentAnimationInitialSpringVelocity: CGFloat = 2

    
    // The values in html are different from ones in iOS native.
    // css variables
    public var fontFamily: String = "PingFangSC-Regular"
    public var backgroundColor: String = "#21203A"
    public var textColor: String = "#ffffffcc"
    public var titleFontSize: String = "10pt"
    public var subTitleFontSize: String = "6px"
    public var fontSize: String = "6pt"
    public var imageCornerRadius: Int = 8
    
}
