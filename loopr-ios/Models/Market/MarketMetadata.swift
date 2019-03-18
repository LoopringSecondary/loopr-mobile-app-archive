//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class MarketMetadata {
    let status: MarketStatus
    let priceDecimals: Int
    let orderbookAggLevels: Int
    let precisionForAmount: Int
    let precisionForTotal: Int
    let browsableInWallet: Bool
    let marketPair: MarketPair
    let marketHash: String

    init(json: JSON) {
        self.status = MarketStatus(rawValue: json["status"].stringValue) ?? .unknown
        self.priceDecimals = json["priceDecimals"].intValue
        self.orderbookAggLevels = json["orderbookAggLevels"].intValue
        self.precisionForAmount = json["precisionForAmount"].intValue
        self.precisionForTotal = json["precisionForTotal"].intValue
        self.browsableInWallet = json["browsableInWallet"].boolValue
        self.marketPair = marketPair(json: json["marketPair"])
        self.marketHash = json["marketHash"].stringValue
    }
}
