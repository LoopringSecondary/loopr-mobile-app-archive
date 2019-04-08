//
//  TrendInterval.swift
//  loopr-ios
//
//  Created by xiaoruby on 12/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

enum TrendInterval: String, CustomStringConvertible {
    
    case oneMinutes = "OHLC_INTERVAL_ONE_MINUTES"
    case fiveMinutes = "OHLC_INTERVAL_FIVE_MINUTES"
    case fourHours = "4Hr"
    case oneDay = "1Day"
    case oneWeek = "1Week"
    
    var description: String {
        switch self {
        case .oneHour: return LocalizedString("1Hr", comment: "")
        case .twoHours: return LocalizedString("2Hr", comment: "")
        case .fourHours: return LocalizedString("4Hr", comment: "")
        case .oneDay: return LocalizedString("1Day", comment: "")
        case .oneWeek: return LocalizedString("1Week", comment: "")
        }
    }
    
}
