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

    // hex string e.g. 0x5be8e179
    let validUntil: String

    // int value e.g. 3562653865313739
    var validU: Int = 0

    init(json: JSON) {
        self.dualAuthAddr = json["dualAuthAddr"].stringValue
        self.dualAuthPrivateKey = json["dualAuthPrivateKey"].stringValue
        self.wallet = json["wallet"].stringValue
        self.status = OrderStatus(rawValue: json["status"].stringValue) ?? .unknown
        self.validUntil = json["validUntil"].stringValue
        self.validU = validUntil.hexToInteger ?? 0
    }
}
