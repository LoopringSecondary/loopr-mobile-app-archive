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
    
    func getInformation(completion: @escaping (_ response: [News]?, _ error: Error?) -> Void) {
        CrawlerAPIRequest.getInformation(token: "ALL_CURRENCY", language: SettingDataManager.shared.getCurrentLanguage()) { (news, error) in
            self.informationItems = news ?? []
            completion(news, error)
        }
    }
}
