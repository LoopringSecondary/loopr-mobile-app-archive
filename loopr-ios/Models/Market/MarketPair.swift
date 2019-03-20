//
//  Paging.swift
//  loopr-ios
//
//  Created by ruby on 3/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class MarketPair: Equatable, CustomStringConvertible {

    let baseToken: String
    let quoteToken: String
    var description: String

    init(baseToken: String, quoteToken: String) {
        self.baseToken = baseToken
        self.quoteToken = quoteToken
        self.description = baseToken + "/" + quoteToken
    }

    init(json: JSON) {
        self.baseToken = json["baseToken"].stringValue
        self.quoteToken = json["quoteToken"].stringValue
        self.description = baseToken + "/" + quoteToken
    }

    func toJSON() -> JSON {
        var json: JSON = JSON()
        json["baseToken"] = JSON(baseToken)
        json["quoteToken"] = JSON(quoteToken)
        return json
    }

    static func == (lhs: MarketPair, rhs: MarketPair) -> Bool {
        if lhs.baseToken == rhs.baseToken && lhs.quoteToken == rhs.quoteToken {
            return true
        } else {
            return false
        }
    }
}
