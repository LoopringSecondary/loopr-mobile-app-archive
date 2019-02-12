//
//  NewsList.swift
//  loopr-ios
//
//  Created by ruby on 1/26/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class NewsList {
    
    let key: String
    
    var informationHasMoreData: Bool = true
    var informationItems: [News] = []
    
    var flashHasMoreData: Bool = true
    var flashItems: [News] = []
    
    var currentIndex: Int = 0
    
    init(key: String) {
        self.key = key
    }
    
    func overwriteInformationItems(news: [News]) {
        if news.count < NewsDataManager.shared.pageSize {
            self.informationHasMoreData = false
        } else {
            self.informationHasMoreData = true
        }
        informationItems = news
    }
    
    func appendInformationItems(news: [News]) {
        if news.count < NewsDataManager.shared.pageSize {
            self.informationHasMoreData = false
        } else {
            self.informationHasMoreData = true
        }
        informationItems += news
    }
    
    func overwriteFlashItems(news: [News]) {
        if news.count < NewsDataManager.shared.pageSize {
            self.flashHasMoreData = false
        } else {
            self.flashHasMoreData = true
        }
        flashItems = news
    }
    
    func appendFlashItems(news: [News]) {
        if news.count < NewsDataManager.shared.pageSize {
            self.flashHasMoreData = false
        } else {
            self.flashHasMoreData = true
        }
        flashItems += news
    }

}
