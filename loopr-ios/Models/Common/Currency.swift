//
//  Currency.swift
//  loopr-ios
//
//  Created by kenshin on 2018/4/21.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation
import UIKit

class Currency: Equatable, CustomStringConvertible {
    
    var locale: String
    let name: String
    let icon: UIImage?
    var description: String
    
    // Remove "zh_HK": "HKD" for now
    // currently support for "USD", "RMB"
    let map = [
        "en_US": "USD",
        "zh_CN": "RMB"
    ]
    
    init(name: String) {
        if Array(map.values).contains(name) {
            self.name = name
        } else {
            if SettingDataManager.shared.getCurrentLanguage() == Language(name: "zh-Hans") {
                self.name = "RMB"
                SettingDataManager.shared.setCurrentCurrency(Currency(name: "RMB"), syncToServer: false)
            } else {
                self.name = "USD"
                SettingDataManager.shared.setCurrentCurrency(Currency(name: "USD"), syncToServer: false)
            }
        }

        self.icon = UIImage(named: name)
        self.locale = "en_US"
        self.description = LocalizedString(name, comment: "")
        map.forEach { (k, v) in
            if v == name {
                self.locale = k
            }
        }
    }

    static func == (lhs: Currency, rhs: Currency) -> Bool {
        return lhs.name == rhs.name
    }
}
