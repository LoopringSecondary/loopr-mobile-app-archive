//
//  OrderResult.swift
//  loopr-ios
//
//  Created by xiaoruby on 3/6/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class Order {

    let total: UInt

    var orders: [RawOrder]

    init(json: JSON) {
        self.orders = []
        self.total = json["total"].uIntValue
        let orders = json["orders"].arrayValue
        for order in orders {
            let rawOrder = RawOrder(json: order)
            self.orders.append(rawOrder)
        }
    }
}
