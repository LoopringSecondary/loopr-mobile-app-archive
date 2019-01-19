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

}
