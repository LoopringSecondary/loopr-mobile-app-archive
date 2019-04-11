//
//  OrderStatus.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/5/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import BigInt

class OrderState {

    var status: OrderStatus = .pending

    // tokens可用于订单数量，暂时无用
    var actualAmountS: String = ""

    var actualAmountB: String = ""

    var actualAmountFee: String = ""

    // 订单未成交数量，计算fill
    var outstandingAmountS: String = ""

    var outstandingAmountSell: Double

    var outstandingAmountB: String = ""

    var outstandingAmountBuy: Double

    var outstandingAmountFee: String = ""

    var outstandingAmountF: Double

    init() {

    }

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
