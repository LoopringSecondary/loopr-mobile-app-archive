//
//  NewsCategory.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/24.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

enum NewsCategory: String, CustomStringConvertible {
    
    case information = "information"
    case flash = "flash"
    case unknown = "unknown"
    
    var description: String {
        switch self {
        case .information: return "information"
        case .flash: return "flash"
        case .unknown: return "unknown"
        }
    }
}
