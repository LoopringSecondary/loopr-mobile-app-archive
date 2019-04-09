//
//  TrendRange.swift
//  loopr-ios
//
//  Created by xiaoruby on 12/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

enum MarketRange: CustomStringConvertible {

    case oneDay
    case oneWeek
    case oneMonth
    case threeMonths
    case oneYear
    case twoYears
    case all

    var description: String {
        switch self {
        case .oneDay: return LocalizedString("1H", comment: "")
        case .oneWeek: return LocalizedString("1W", comment: "")
        case .oneMonth: return LocalizedString("1M", comment: "")
        case .threeMonths: return LocalizedString("3M", comment: "")
        case .oneYear: return LocalizedString("1Y", comment: "")
        case .twoYears: return LocalizedString("2Y", comment: "")
        case .all: return LocalizedString("All", comment: "")
        }
    }

    func getTrendInterval() -> MarketInterval {
        switch self {
        case .oneDay:
            return MarketInterval.one_hour
        case .oneWeek:
            return MarketInterval.four_hours
        case .oneMonth:
            return MarketInterval.one_day
        case .threeMonths:
            return MarketInterval.one_day
        case .oneYear:
            return MarketInterval.one_week
        case .twoYears:
            return MarketInterval.one_week
        case .all:
            return MarketInterval.one_week
        }
    }

    func getCount() -> Int {
        switch self {
        case .oneDay:
            return 24
        case .oneWeek:
            return 42
        case .oneMonth:
            return 30
        case .threeMonths:
            return 90
        case .oneYear:
            return 52
        case .twoYears:
            return 100
        case .all:
            return 100
        }
    }

}
