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
    var dualAuthAddr: String = ""

    // 随机钱包私钥
    var dualAuthPrivateKey: String = ""

    var broker: String = ""

    var orderInterceptor: String = ""

    // 手续费收款地址
    var wallet: String = ""

    var status: OrderStatus = .pending

    // int value e.g. 1548422323
    var validUntil: Int = 0

    // false -- 可以被成交多次， true -- 必须被一次成交
    var allOrNone: Bool = false

    var sig: String = ""

    init() {
        let (dualAuthPrivateKey, dualAuthAddr) = Wallet.generateRandomWallet()
        self.dualAuthAddr = dualAuthAddr
        self.dualAuthPrivateKey = dualAuthPrivateKey
        self.wallet = RelayAPIConfiguration.loopringAddress
    }

    init(json: JSON) {
        self.dualAuthAddr = json["dualAuthAddr"].stringValue
        self.dualAuthPrivateKey = json["dualAuthPrivateKey"].stringValue
        self.broker = json["broker"].stringValue
        self.orderInterceptor = json["orderInterceptor"].stringValue
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
