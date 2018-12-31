//
//  NewsDataManager.swift
//  loopr-ios
//
//  Created by Ruby on 12/28/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class NewsDataManager {
    
    static let shared = NewsDataManager()

    var informationItems: [News] = []
    var flashItems: [News] = []

    private init() {
        
    }
    
    func get(category: NewsCategory, pageIndex: UInt = 0, pageSize: UInt = 50, completion: @escaping (_ response: [News]?, _ error: Error?) -> Void)  {
        CrawlerAPIRequest.get(token: "ALL_CURRENCY", language: SettingDataManager.shared.getCurrentLanguage(), category: category) { (news, error) in
            if category == .information {
                self.informationItems = news ?? []
            } else if category == .flash {
                self.flashItems = news ?? []
            }
            completion(news, error)
        }
    }

}
