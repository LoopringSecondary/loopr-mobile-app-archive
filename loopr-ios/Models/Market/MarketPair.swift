//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class MarketPair {

    let baseToken: String
    let quoteToken: String

    init(baseToken: String, quoteToken: String) {
        self.baseToken = baseToken
        self.quoteToken = quoteToken
    }

    init(json: JSON) {
        self.baseToken = json["baseToken"].stringValue ?? ""
        self.quoteToken = json["quoteToken"].stringValue ?? ""
    }

}
