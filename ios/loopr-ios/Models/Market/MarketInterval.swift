//
//  TrendInterval.swift
//  loopr-ios
//
//  Created by xiaoruby on 12/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

enum MarketInterval: String, CustomStringConvertible {

    case one_minute = "OHLC_INTERVAL_ONE_MINUTES"
    case five_minutes = "OHLC_INTERVAL_FIVE_MINUTES"
    case fifteen_minutes = "OHLC_INTERVAL_FIFTEEN_MINUTES"
    case thirty_minutes = "OHLC_INTERVAL_THIRTY_MINUTES"
    case one_hour = "OHLC_INTERVAL_ONE_HOUR"
    case two_hours = "OHLC_INTERVAL_TWO_HOURS"
    case four_hours = "OHLC_INTERVAL_FOUR_HOURS"
    case twelve_hours = "OHLC_INTERVAL_TWELVE_HOURS"
    case one_day = "OHLC_INTERVAL_ONE_DAY"
    case three_days = "OHLC_INTERVAL_THREE_DAYS"
    case five_days = "OHLC_INTERVAL_FIVE_DAYS"
    case one_week = "OHLC_INTERVAL_ONE_WEEK"

    var description: String {
        switch self {
        case .one_minute: return "1M"
        case .five_minutes: return "5M"
        case .fifteen_minutes: return "15M"
        case .thirty_minutes: return "30M"
        case .one_hour: return "1H"
        case .two_hours: return "2H"
        case .four_hours: return "4H"
        case .twelve_hours: return "12H"
        case .one_day: return "1D"
        case .three_days: return "3D"
        case .five_days: return "5D"
        case .one_week: return "1W"
        }
    }
}
