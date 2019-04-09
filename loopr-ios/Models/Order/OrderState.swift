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
    let actualAmountS: String

    let actualAmountB: String

    let actualAmountFee: String

    // 订单未成交数量，计算fill
    let outstandingAmountS: String

    var outstandingAmountSell: Double?

    let outstandingAmountB: String

    var outstandingAmountBuy: Double?

    let outstandingAmountFee: String

    var outstandingAmountF: Double?

    init(json: JSON) {
        self.status = OrderStatus(rawValue: json["status"].stringValue) ?? .unknown
        self.actualAmountS = json["actualAmountS"].stringValue
        self.actualAmountB = json["actualAmountB"].stringValue
        self.actualAmountFee = json["actualAmountFee"].stringValue
        self.outstandingAmountS = json["outstandingAmountS"].stringValue
        self.outstandingAmountB = json["outstandingAmountB"].stringValue
        self.outstandingAmountFee = json["outstandingAmountFee"].stringValue
    }
}
