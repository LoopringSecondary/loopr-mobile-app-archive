//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

enum TokenStatus: String, CustomStringConvertible {

    case valid = "VALID"
    case invalid = "INVALID"
    case unknown = "UNKNOWN"

    var description: String {
        switch self {
        case .valid:
            return "valid"
        case .invalid:
            return "invalid"
        case .unknown:
            return "unknown"
        }
    }
}
