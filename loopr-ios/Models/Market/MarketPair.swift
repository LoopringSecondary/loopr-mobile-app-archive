//
//  Paging.swift
//  loopr-ios
//
//  Created by ruby on 3/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class MarketPair: Equatable, CustomStringConvertible {
    
    var description: String

    let baseToken: String

    var baseSymbol: String?

    let quoteToken: String

    var quoteSymbol: String?

    init(baseToken: String, quoteToken: String) {
        self.baseToken = baseToken
        self.quoteToken = quoteToken
        self.baseSymbol = TokenDataManager.shared.getTokenByAddress(baseToken)?.symbol
        self.quoteSymbol = TokenDataManager.shared.getTokenByAddress(baseToken)?.symbol
        if let baseSymbol = self.baseSymbol, let quoteSymbol = self.quoteSymbol {
            self.description = baseSymbol + "/" + quoteSymbol
        } else {
            self.description = ""
        }
    }

    init(json: JSON) {
        self.baseToken = json["baseToken"].stringValue
        self.quoteToken = json["quoteToken"].stringValue
        self.baseSymbol = TokenDataManager.shared.getTokenByAddress(baseToken)?.symbol
        self.quoteSymbol = TokenDataManager.shared.getTokenByAddress(baseToken)?.symbol
        if let baseSymbol = self.baseSymbol, let quoteSymbol = self.quoteSymbol {
            self.description = baseSymbol + "/" + quoteSymbol
        } else {
            self.description = ""
        }
    }

    func toJSON() -> JSON {
        var json: JSON = JSON()
        json["baseToken"] = JSON(baseToken)
        json["quoteToken"] = JSON(quoteToken)
        return json
    }

    static func ==(lhs: MarketPair, rhs: MarketPair) -> Bool {
        if lhs.baseToken == rhs.baseToken && lhs.quoteToken == rhs.quoteToken {
            return true
        } else {
            return false
        }
    }
}
