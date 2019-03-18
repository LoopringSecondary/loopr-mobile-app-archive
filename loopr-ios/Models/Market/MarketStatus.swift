//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

enum MarketStatus: CustomStringConvertible {

    case active = "ACTIVE"
    case readonly = "READONLY"
    case terminated = "TERMINATED"
    case unknown = "UNKNOWN"

    var description: String {
        switch self {
        case .active:
            return "active"
        case .readonly:
            return "readonly"
        case .terminated:
            return "terminated"
        case .unknown:
            return "unknown"
        }
    }
}
