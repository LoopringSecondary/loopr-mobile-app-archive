//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class MarketTicker {

    let baseToken: String
    let baseTokenSymbol: String = ""
    let quoteToken: String
    let quoteTokenSymbol: String = ""
    let exchangeRate: Double
    let volume24H: Double
    let percentChange1H: String
    let percentChange24H: String
    let percentChange7D: String

    init(json: JSON) {
        self.baseToken = json["baseToken"].stringValue
        self.quoteToken = json["quoteToken"].stringValue
        self.exchangeRate = json["exchangeRate"].doubleValue
        self.volume24H = json["volume24H"].stringValue
        self.baseTokenSymbol = TokenDataManager.shared.getTokenByAddress(baseToken)
        self.quoteTokenSymbol = TokenDataManager.shared.getTokenByAddress(quoteToken)
        self.percentChange1H = toString(json: json["percentChange1H"])
        self.percentChange24H = toString(json: json["percentChange24H"])
        self.percentChange7D = toString(json: json["percentChange7D"])
    }

    func toString(json: JSON) -> String {
        var result: String
        var value = json.doubleValue
        let numberFormatter = NumberFormatter()
        if value > 0 {
            result = "↑\(value)\(numberFormatter.percentSymbol)"
        } else {
            result = "↓\(value)\(numberFormatter.percentSymbol)"
        }
        return result
    }
}
