//
//  NewsParagraph.swift
//  loopr-ios
//
//  Created by Ruby on 1/13/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class NewsParagraph {

    var isString: Bool = true
    var content: String
    
    var newsImage: NewsImage?
    
    init?(content: String) {
        // string contains non-whitespace characters
        guard !content.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            return nil
        }

        self.content = content
        if content.starts(with: "<img src=") && content.contains("\">") {
            let tmps = self.content.components(separatedBy: "\">")
            if tmps.count < 0 {
                return nil
            }
            self.newsImage = NewsImage(imageFolderName: "NewsParagraph", imageUrl: tmps[0].replacingOccurrences(of: "<img src=\"", with: "").trim())
            self.isString = false
        } else {
            self.isString = true
        }
    }

}
