//
//  OrderType.swift
//  loopr-ios
//
//  Created by kenshin on 2018/5/28.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

enum IndexType: String, CustomStringConvertible {
    
    case bullIndex = "bull_index"
    case bearIndex = "bear_index"
    case forwardNum = "forward_num"
    case unknown = "unknown"
    
    var description: String {
        switch self {
        case .bullIndex: return "bull_index"
        case .bearIndex: return "bear_index"
        case .forwardNum: return "forward_num"
        case .unknown: return "unknown"
        }
    }
}
