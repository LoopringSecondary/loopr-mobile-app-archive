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
        case .one_minute: return LocalizedString("1M", comment: "")
        case .five_minutes: return LocalizedString("5M", comment: "")
        case .fifteen_minutes: return LocalizedString("15M", comment: "")
        case .thirty_minutes: return LocalizedString("30M", comment: "")
        case .one_hour: return LocalizedString("1H", comment: "")
        case .two_hours: return LocalizedString("2H", comment: "")
        case .four_hours: return LocalizedString("4H", comment: "")
        case .twelve_hours: return LocalizedString("12H", comment: "")
        case .one_day: return LocalizedString("1D", comment: "")
        case .three_days: return LocalizedString("3D", comment: "")
        case .five_days: return LocalizedString("5D", comment: "")
        case .one_week: return LocalizedString("1W", comment: "")
        }
    }
}
