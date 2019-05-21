//
//  OrderbookItem.swift
//  loopr-ios
//
//  Created by ruby on 4/7/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

// Used in ordrebook
class OrderbookItem {
    
    let amount: Double
    let price: Double
    let total: Double
    
    init(amount: Double, price: Double, total: Double) {
        self.amount = amount
        self.price = price
        self.total = total
    }
    
    init(json: JSON) {
        self.amount = json["amount"].doubleValue
        self.price = json["price"].doubleValue
        self.total = json["total"].doubleValue
    }

}
