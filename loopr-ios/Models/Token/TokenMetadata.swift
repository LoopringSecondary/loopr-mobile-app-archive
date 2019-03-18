//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class TokenMetadata {
    let type: TokenType
    let status: TokenStatus
    let symbol: String
    let name: String
    let address: String
    let unit: String
    let decimals: Int
    let precision: Int
    let burnRate: BurnRate

    init(json: JSON) {
        self.type = TokenType(rawValue: json["type"].stringValue) ?? .unknown
        self.status = TokenStatus(rawValue: json["status"].stringValue) ?? .unknown
        self.symbol = json["symbol"].stringValue
        self.name = json["name"].stringValue
        self.address = json["address"].stringValue
        self.unit = json["unit"].stringValue
        self.decimals = json["decimals"].intValue
        self.precision = json["precision"].intValue
        self.burnRate = BurnRate(json: json["burnRate"])
    }
}
