//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class Market {

    let metadata: MarketMetadata
    let ticker: MarketTicker

    init(json: JSON) {
        self.metadata = MarketMetadata(json: json["metadata"])
        self.ticker = MarketTicker(json: json["ticker"])
    }
}
