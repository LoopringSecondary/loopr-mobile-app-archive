//
//  MarketPair.swift
//  loopr-ios
//
//  Created by ruby on 3/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
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
    
    func toJSON() -> JSON {
        var json: JSON = JSON()
        json["baseToken"] = JSON(baseToken)
        json["quoteToken"] = JSON(quoteToken)
        return json
    }

}
