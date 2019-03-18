//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class TokenInfo {
    let symbol: String
    let circulatingSupply: Int
    let totalSupply: Int
    let maxSupply: Int
    let cmcRank: Int
    let icoRateWithEth: Double
    let websiteURL: String

    init(json: JSON) {
        self.symbol = json["symbol"].stringValue
        self.circulatingSupply = json["circulatingSupply"].intValue
        self.totalSupply = json["totalSupply"].intValue
        self.maxSupply = json["maxSupply"].intValue
        self.cmcRank = json["cmcRank"].boolValue
        self.icoRateWithEth = json["icoRateWithEth"].doubleValue
        self.websiteUrl = json["websiteUrl"].stringValue
    }
}
