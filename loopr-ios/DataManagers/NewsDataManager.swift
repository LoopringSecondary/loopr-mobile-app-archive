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

    let pageSize: UInt = 20
    
    var currentIndex: Int = 0
    var currentNewsListKey = "ALL_CURRENCY"
    var newsLists = [String: NewsList]()

    var votes = [String: Int]()
    
    var isLaunching = false

    private init() {
        if let votes = UserDefaults.standard.dictionary(forKey: UserDefaultsKeys.newsUpvoteAndDownvote.rawValue) as? [String: Int] {
            self.votes = votes
        }
    }

    func isInformationEmpty() -> Bool {
        return getInformationItems().count == 0 && !isLaunching
    }

    func getInformationHasMoreData() -> Bool {
        if self.newsLists[self.currentNewsListKey] != nil {
            return newsLists[currentNewsListKey]!.informationHasMoreData
        } else {
            return true
        }
    }
    
    func getInformationItems() -> [News] {
        if self.newsLists[self.currentNewsListKey] != nil {
            print(self.newsLists[self.currentNewsListKey]!.informationItems)
            return self.newsLists[self.currentNewsListKey]!.informationItems
        } else {
            return []
        }
    }

    func isFlashEmpty() -> Bool {
        return getFlashItems().count == 0 && !isLaunching
    }

    func getFlashHasMoreData() -> Bool {
        if self.newsLists[self.currentNewsListKey] != nil {
            return newsLists[currentNewsListKey]!.flashHasMoreData
        } else {
            return true
        }
    }

    func getFlashItems() -> [News] {
        if self.newsLists[self.currentNewsListKey] != nil {
            return self.newsLists[self.currentNewsListKey]!.flashItems
        } else {
            return []
        }
    }
    
    func getCurrentInformationItem() -> News? {
        if let informationItems = newsLists[currentNewsListKey]?.informationItems {
            return informationItems[currentIndex]
        } else {
            return nil
        }
    }

    func getVote(uuid: String) -> Int {
        if let voteValue = votes[uuid] {
            return voteValue
        } else {
            return 0
        }
    }
    
    func updateVote(updatedNews: News) {
        if updatedNews.category == .information {
            if let firstNews = newsLists[currentNewsListKey]?.informationItems.index(where: { $0.uuid == updatedNews.uuid }) {
                newsLists[currentNewsListKey]?.informationItems[firstNews] = updatedNews
            }
        } else {
            if let firstNews = newsLists[currentNewsListKey]?.flashItems.index(where: { $0.uuid == updatedNews.uuid }) {
                newsLists[currentNewsListKey]?.flashItems[firstNews] = updatedNews
            }
        }
    }

    func get(category: NewsCategory, pageIndex: UInt, completion: @escaping (_ newsItems: [News], _ error: Error?) -> Void) {
        isLaunching = true
        CrawlerAPIRequest.get(token: currentNewsListKey, language: SettingDataManager.shared.getCurrentLanguage(), category: category, pageIndex: pageIndex, pageSize: pageSize) { (news, error) in
            if category == .information {
                if pageIndex == 0 {
                    if self.newsLists[self.currentNewsListKey] != nil {
                        self.newsLists[self.currentNewsListKey]!.overwriteInformationItems(news: news)
                    } else {
                        let newList = NewsList(key: self.currentNewsListKey)
                        newList.overwriteInformationItems(news: news)
                        self.newsLists[self.currentNewsListKey] = newList
                    }
                } else {
                    if self.newsLists[self.currentNewsListKey] != nil {
                        self.newsLists[self.currentNewsListKey]!.appendInformationItems(news: news)
                    } else {
                        let newList = NewsList(key: self.currentNewsListKey)
                        newList.overwriteInformationItems(news: news)
                        self.newsLists[self.currentNewsListKey] = newList
                    }
                }
                
            } else {
                if pageIndex == 0 {
                    if self.newsLists[self.currentNewsListKey] != nil {
                        self.newsLists[self.currentNewsListKey]!.overwriteFlashItems(news: news)
                    } else {
                        let newList = NewsList(key: self.currentNewsListKey)
                        newList.overwriteFlashItems(news: news)
                        self.newsLists[self.currentNewsListKey] = newList
                    }
                } else {
                    if self.newsLists[self.currentNewsListKey] != nil {
                        self.newsLists[self.currentNewsListKey]!.appendFlashItems(news: news)
                    } else {
                        let newList = NewsList(key: self.currentNewsListKey)
                        newList.overwriteFlashItems(news: news)
                        self.newsLists[self.currentNewsListKey] = newList
                    }
                }
            }

            self.isLaunching = false
            completion(news, error)
        }
    }

}
