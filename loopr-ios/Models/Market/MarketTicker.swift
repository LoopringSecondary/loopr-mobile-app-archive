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
    var baseSymbol: String = ""
    let quoteToken: String
    var quoteSymbol: String = ""
    let exchangeRate: Double
    let price: Double
    let volume24H: String
    var percentChange1H: String = ""
    var percentChange24H: String = ""
    var percentChange7D: String = ""

    init?(json: JSON) {
        let baseToken = json["baseToken"].stringValue
        let quoteToken = json["quoteToken"].stringValue
        guard TokenDataManager.shared.getTokenByAddress(baseToken) != nil && TokenDataManager.shared.getTokenByAddress(quoteToken) != nil else {
            return nil
        }
        self.baseToken = baseToken
        self.quoteToken = quoteToken
        self.exchangeRate = json["exchangeRate"].doubleValue
        self.baseSymbol = TokenDataManager.shared.getTokenByAddress(baseToken)!.source
        self.quoteSymbol = TokenDataManager.shared.getTokenByAddress(quoteToken)!.source
        self.price = json["price"].doubleValue
        self.volume24H = json["volume24H"].stringValue
        self.percentChange1H = toString(json: json["percentChange1H"])
        self.percentChange24H = toString(json: json["percentChange24H"])
        self.percentChange7D = toString(json: json["percentChange7D"])
    }

    func toString(json: JSON) -> String {
        var result: String
        let value = json.doubleValue
        let numberFormatter = NumberFormatter()
        if value > 0 {
            result = "↑\(value)\(numberFormatter.percentSymbol)"
        } else {
            result = "↓\(value)\(numberFormatter.percentSymbol)"
        }
        return result
    }
}
