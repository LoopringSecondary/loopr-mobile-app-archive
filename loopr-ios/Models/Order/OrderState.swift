//
//  OrderStatus.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/5/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class OrderState {

    let status: OrderStatus

    // tokens可用于订单数量，暂时无用
    let actualStringS: String

    let actualStringB: String

    let actualStringFee: String

    // 订单未成交数量，计算fill
    let outstandingStringS: String

    var outstandingStringSell: Double?

    let outstandingStringB: String

    var outstandingStringBuy: Double?

    let outstandingStringFee: String

    var outstandingStringF: Double?

    init(json: JSON) {
        self.status = OrderStatus(rawValue: json["status"].stringValue) ?? .unknown
        self.actualStringS = json["actualStringS"].stringValue
        self.actualStringB = json["actualStringB"].stringValue
        self.actualStringFee = json["actualStringFee"].stringValue

        self.outstandingStringS = json["outstandingStringS"].stringValue
        self.outstandingStringB = json["outstandingStringB"].stringValue
        self.outstandingStringFee = json["outstandingStringFee"].stringValue
    }
}
