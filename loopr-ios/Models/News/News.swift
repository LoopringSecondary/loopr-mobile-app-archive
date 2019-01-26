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
    var publishTime: String = ""
    var publishTimeDate: Date = Date()
    var source: String
    var author: String
    var imageUrl: String
    var newsImage: NewsImage?
    var bullIndex: Int
    var bearIndex: Int
    var forwardNum: Int
    
    // not from API
    var paragraphs: [NewsParagraph] = []
    var description: String
    var descriptionAttributedText: NSAttributedString

    init?(json: JSON, category: NewsCategory) {
        self.uuid = json["uuid"].stringValue
        self.token = json["token"].stringValue
        self.language = Language(name: json["language"].stringValue)
        
        // TODO: category is not returned in json
        // self.category = NewsCategory(rawValue: json["category"].stringValue) ?? .unknown
        // print(category)
        self.category = category

        self.title = json["title"].stringValue
        self.content = json["content"].stringValue
        self.url = json["url"].stringValue

        self.source = json["source"].stringValue
        self.author = json["author"].stringValue
        self.imageUrl = json["imageUrl"].stringValue
        self.newsImage = NewsImage(imageFolderName: "NewsThumbnail", imageUrl: self.imageUrl)

        self.bullIndex = json["bullIndex"].intValue
        self.bearIndex = json["bearIndex"].intValue
        self.forwardNum = json["forwardNum"].intValue

        // Remove all line breaks at the beginning of content
        self.content = self.content.replacingOccurrences(of: "^\\s*", with: "", options: .regularExpression)

        let contentParagraphs = self.content.split(separator: "\n").map({ (paragraph) -> String in
            return paragraph.replacingOccurrences(of: "^\\s*", with: "", options: .regularExpression)
        })
        
        for contentParagraph in contentParagraphs {
            if let newsParagraph = NewsParagraph(content: contentParagraph) {
                paragraphs.append(newsParagraph)
            }
        }
        
        // Description
        self.description = contentParagraphs.joined(separator: "\n")
        let options: [NSAttributedString.DocumentReadingOptionKey: Any] = [
            .documentType: NSAttributedString.DocumentType.html,
            .characterEncoding: String.Encoding.utf8.rawValue
        ]
        
        do {
            let attributed = try NSAttributedString(data: self.description.data(using: .unicode)!, options: options, documentAttributes: nil)
            self.description = attributed.string
        } catch {
            return nil
        }
        
        if description.count > 150 && category == .information {
            self.description = self.description.substring(toIndex: 150) + "..."
        }
        
        let style = NSMutableParagraphStyle()
        style.lineSpacing = NewsCollectionCell.descriptionTextViewLineSpacing
        let attributes = [NSAttributedStringKey.paragraphStyle: style]
        descriptionAttributedText = NSAttributedString(string: self.description, attributes: attributes)
        
        // TODO: how to change the line distance in html and css?
        self.content = self.content.replacingOccurrences(of: "\n\n", with: "\n")
        self.content = self.content.replacingOccurrences(of: "\n", with: "\n\n")
        
        let filteredParagraphs = paragraphs.filter { !$0.isString && $0.newsImage != nil }
        if filteredParagraphs.count > 0 && newsImage == nil {
            newsImage = NewsImage(imageFolderName: "NewsThumbnail", imageUrl: filteredParagraphs[0].newsImage!.imageUrl)
        }
        
        let publichTimeInUTC = json["publishTime"].stringValue
        self.publishTime = self.utcToCurrent(publishTimeInUTC: publichTimeInUTC, currentDateFormat: "MM-dd HH:mm:ss")
        self.publishTimeDate = self.utcToCurrent(publishTimeInUTC: publichTimeInUTC)
    }
    
    func utcToCurrent(publishTimeInUTC: String, currentDateFormat: String) -> String {
        // UTC timezone
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        let date = dateFormatter.date(from: publishTimeInUTC)!
        
        // device time zone
        let dateFormatter2 = DateFormatter()
        dateFormatter2.timeZone = TimeZone.current
        dateFormatter2.dateFormat = currentDateFormat
        
        return dateFormatter2.string(from: date)
    }
    
    func utcToCurrent(publishTimeInUTC: String) -> Date {
        // UTC timezone
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        dateFormatter.timeZone = TimeZone(abbreviation: "UTC")
        let date = dateFormatter.date(from: publishTimeInUTC)!
        return date
    }

}
