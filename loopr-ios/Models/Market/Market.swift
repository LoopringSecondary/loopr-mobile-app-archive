//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class Market {

    let metadata: Metadata
    let ticker: Ticker

    init(json: JSON) {
        self.metadata = Metadata(json: json["metadata"])
        self.ticker = Ticker(json: json["ticker"])
    }
}
