//
//  News.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/24.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class Blog {
    var title: String
    var url: String
    var imageUrl: String
    
    init(json: JSON) {
        self.title = json["title"].stringValue
        self.url = json["url"].stringValue
        self.imageUrl = json["imageUrl"].stringValue
    }
}
