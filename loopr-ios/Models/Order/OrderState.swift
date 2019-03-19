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
    let actualAmountS: Amount

    let actualAmountB: Amount

    let actualAmountFee: Amount

    // 订单未成交数量，计算fill
    let outstandingAmountS: Amount

    var outstandingAmountSell: Double = 0

    let outstandingAmountB: Amount

    var outstandingAmountBuy: Double = 0

    let outstandingAmountFee: Amount

    var outstandingAmountF: Double = 0

    init(json: JSON) {
        self.status = OrderStatus(rawValue: json["status"].stringValue) ?? .unknown
        self.actualAmountS = Amount(json: json["actualAmountS"])
        self.actualAmountB = Amount(json: json["actualAmountB"])
        self.actualAmountFee = Amount(json: json["actualAmountFee"])

        self.outstandingAmountS = Amount(json: json["outstandingAmountS"])
        self.outstandingAmountB = Amount(json: json["outstandingAmountB"])
        self.outstandingAmountFee = Amount(json: json["outstandingAmountFee"])
    }
}
