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

    let pageSize: UInt = 50
    
    var informationHasMoreData: Bool = true
    var informationItems: [News] = []

    var flashHasMoreData: Bool = true
    var flashItems: [News] = []

    var votes = [String: Int]()
    var localUpvoteUpdates = [String: Int]()
    var localDownvoteUpdates = [String: Int]()

    private init() {
        if let votes = UserDefaults.standard.dictionary(forKey: UserDefaultsKeys.newsUpvoteAndDownvote.rawValue) as? [String: Int] {
            self.votes = votes
        }
    }

    func getVote(uuid: String) -> Int {
        if let voteValue = votes[uuid] {
            return voteValue
        } else {
            return 0
        }
    }
    
    func updateVote(uuid: String, updateBullValue: Int, updateBearValue: Int) {
        
    }

    func get(category: NewsCategory, pageIndex: UInt, completion: @escaping (_ newsItems: [News], _ error: Error?) -> Void) {
        CrawlerAPIRequest.get(token: "ALL_CURRENCY", language: SettingDataManager.shared.getCurrentLanguage(), category: category, pageIndex: pageIndex, pageSize: pageSize) { (news, error) in
            if category == .information {
                if pageIndex == 0 {
                    self.localUpvoteUpdates.removeAll()
                    self.localDownvoteUpdates.removeAll()
                    self.informationItems = news
                } else {
                    self.informationItems += news
                }
                if news.count < self.pageSize {
                    self.informationHasMoreData = false
                } else {
                    self.informationHasMoreData = true
                }
            } else {
                if pageIndex == 0 {
                    self.localUpvoteUpdates.removeAll()
                    self.localDownvoteUpdates.removeAll()
                    self.flashItems = news
                } else {
                    self.flashItems += news
                }
                if news.count < self.pageSize {
                    self.flashHasMoreData = false
                } else {
                    self.flashHasMoreData = true
                }
            }

            completion(news, error)
        }
    }

}
