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

    // TODO: ruby should not return nil
    init?(json: JSON) {
        self.metadata = MarketMetadata(json: json["metadata"])
        let ticker = MarketTicker(json: json["ticker"])
        guard ticker != nil else {
            return nil
        }
        self.ticker = ticker!
    }

    var name: String {
        return self.metadata.marketPair.description
    }
}
