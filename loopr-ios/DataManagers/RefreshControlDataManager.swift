//
//  RefreshControllerDataManager.swift
//  loopr-ios
//
//  Created by Ruby on 12/31/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class RefreshControlDataManager {
    
    static let shared = RefreshControlDataManager()
    
    private init() {
        
    }

    func set(type: RefreshControlType) {
        UserDefaults.standard.setValue(Date(), forKey: "\(UserDefaultsKeys.refreshControl.rawValue)\(type.rawValue)")
    }
    
    func get(type: RefreshControlType) -> String {
        if let date = UserDefaults.standard.object(forKey: "\(UserDefaultsKeys.refreshControl.rawValue)\(type.rawValue)") as? Date {
            let stringDate: String
            let dateFormatter = DateFormatter()
            dateFormatter.amSymbol = LocalizedString("AM", comment: "")
            dateFormatter.pmSymbol = LocalizedString("PM", comment: "")
            if SettingDataManager.shared.getCurrentLanguage().name == "zh-Hans" || SettingDataManager.shared.getCurrentLanguage().name  == "zh-Hant" {
                dateFormatter.dateFormat = "a h:mm"
                stringDate = dateFormatter.string(from: date).replacingOccurrences(of: " ", with: "")
            } else {
                dateFormatter.dateFormat = "h:mm a"
                stringDate = dateFormatter.string(from: date)
            }
            return "\(LocalizedString("Updated", comment: "")) \(stringDate)"
        } else {
            return ""
        }
    }

}
