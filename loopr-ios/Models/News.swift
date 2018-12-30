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
    
    // not from API
    var paragraphs: [String] = []
    var description: String
    
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
        
        var paragraphs = self.content.split(separator: "\n")
        if paragraphs.count > 1 {
            paragraphs = Array(paragraphs.dropFirst())
        }
        self.description = paragraphs.joined(separator: "\n")
        
        let options: [NSAttributedString.DocumentReadingOptionKey: Any] = [
            .documentType: NSAttributedString.DocumentType.html,
            .characterEncoding: String.Encoding.utf8.rawValue
        ]
        let attributed = try! NSAttributedString(data: self.description.data(using: .unicode)!, options: options, documentAttributes: nil)
        self.description = attributed.string
    }
}
