//
//  News.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/24.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class News {
    var uuid: String
    var token: String
    var language: Language
    var category: NewsCategory
    var title: String
    var content: String
    var url: String
    var publishTime: String
    var source: String
    var author: String
    var imageUrl: String
    var bullIndex: Int
    var bearIndex: Int
    var forwardNum: Int
    
    init(json: JSON) {
        self.uuid = json["uuid"].stringValue
        self.token = json["token"].stringValue
        self.language = Language(name: json["language"].stringValue)
        self.category = NewsCategory(rawValue: json["category"].stringValue) ?? .unknown
        self.title = json["title"].stringValue
        self.content = json["content"].stringValue
        self.url = json["url"].stringValue
        self.publishTime = json["publishTime"].stringValue
        self.source = json["source"].stringValue
        self.author = json["author"].stringValue
        self.imageUrl = json["imageUrl"].stringValue
        self.bullIndex = json["bullIndex"].intValue
        self.bearIndex = json["bearIndex"].intValue
        self.forwardNum = json["forwardNum"].intValue
    }
}
