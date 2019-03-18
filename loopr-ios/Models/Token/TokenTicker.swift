//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class TokenTicker {
    let token: String
    let price: Double
    let volume24H: Int
    let percentChange1H: Double
    let percentChange24H: Double
    let percentChange7D: Double

    init(json: JSON) {
        self.token = json["token"].stringValue
        self.price = json["price"].doubleValue
        self.volume24H = json["volume24H"].intValue
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
