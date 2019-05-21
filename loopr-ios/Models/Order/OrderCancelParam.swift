//
//  OrderCancelParam.swift
//  loopr-ios
//
//  Created by kenshin on 2018/6/13.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class OrderCancelParam {

    var time: Int!
    var owner: String!
    var id: String?
    var marketPair: MarketPair?
    var sig: String!

    func toJson() -> JSON {
        var json = JSON()
        json["time"] = JSON(time)
        json["owner"] = JSON(owner)
        json["sig"] = JSON(sig)
        if let marketPair = marketPair {
            json["marketPair"] = marketPair.toJSON()
        }
        if let id = id {
            json["id"] = JSON(id)
        }
        return json
    }

    func isValid() -> Bool {
        guard self.owner != nil && time != nil else { return false }
        return true
    }
}
