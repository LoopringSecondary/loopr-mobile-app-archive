//
//  NewsDetailUIStyleConfig.swift
//  loopr-ios
//
//  Created by Ruby on 12/29/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class NewsDetailUIStyleConfig {
    
    public static var shared = NewsDetailUIStyleConfig()
    
    public var presentAnimationDuration: Double = 0.3
    public var dismissAnimationDuration: Double = 0.3
    public var webAlphaAnimationDuration: Double = 0.5
    
    // css variables
    public var fontFammily: String = "PingFangSC-Regular"
    public var backgroundColor: String = "#16162A"
    public var textColor: String = "#ffffffcc"
    public var fontSize: Int = 6
    
}
