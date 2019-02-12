//
//  NewsParams.swift
//  loopr-ios
//
//  Created by Ruby on 1/1/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

struct NewsParams {
    
    let token: String
    let category: NewsCategory
    let title: String
    
    init(token: String, category: NewsCategory) {
        self.token = token
        self.category = category
        
        if token == "ALL_CURRENCY" {
            title = category.description
        } else {
            title = token.uppercased()
        }
    }

}
