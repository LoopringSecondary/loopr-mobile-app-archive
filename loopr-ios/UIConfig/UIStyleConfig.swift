//
//  UIStyleConfig.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/10/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit

enum UIStyleConfig {
      
    static func getChangeColor(change: String, down: Bool? = nil) -> UIColor {
        let firstChar = change.first?.description ?? ""
        // if change == "0.00%", use update
        if firstChar == "" {
            return UIColor.clear
        }
        
        // HongKong has the same color as Unite States.
        let language = SettingDataManager.shared.getCurrentLanguage()
        if language == Language(name: "zh-Hans") {
            if firstChar == "↓" || firstChar == "-" || down == true {
                return UIColor(named: "Color-green")!
            } else {
                return UIColor(named: "Color-red")!
            }
        } else {
            if firstChar == "↓" || firstChar == "-" || down == false {
                return UIColor(named: "Color-red")!
            } else {
                return UIColor(named: "Color-green")!
            }
        }
    }
}
