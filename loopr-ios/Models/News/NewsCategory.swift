//
//  NewsCategory.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/24.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

enum NewsCategory: String, CustomStringConvertible {
    
    case information
    case flash
    case unknown
    
    var description: String {
        switch self {
        case .information: return LocalizedString("News_information", comment: "")
        case .flash: return LocalizedString("News_flash", comment: "")
        case .unknown: return "unknown"
        }
    }
}
