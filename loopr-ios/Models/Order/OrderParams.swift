//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class OrderParams {

    // 随机钱包地址
    let dualAuthAddr: String

    // 随机钱包私钥
    var dualAuthPrivateKey: String

    // 手续费收款地址
    let wallet: String

    var status: OrderStatus

    // int value e.g. 1548422323
    var validUntil: Int

    // false -- 可以被成交多次， true -- 必须被一次成交
    var allOrNone: Bool = false

    var sig: String = ""

    init(json: JSON) {
        self.dualAuthAddr = json["dualAuthAddr"].stringValue
        self.dualAuthPrivateKey = json["dualAuthPrivateKey"].stringValue
        self.wallet = json["wallet"].stringValue
        self.status = OrderStatus(rawValue: json["status"].stringValue) ?? .unknown
        self.validUntil = json["validUntil"].intValue
    }

    func toJson() -> JSON {
        var json = JSON()
        json["validUntil"] = JSON(validUntil)
        json["allOrNone"] = JSON(allOrNone)
        json["sig"] = JSON(sig)
        json["dualAuthAddr"] = JSON(dualAuthAddr)
        json["dualAuthPrivateKey"] = JSON(dualAuthPrivateKey)
        return json
    }
}
