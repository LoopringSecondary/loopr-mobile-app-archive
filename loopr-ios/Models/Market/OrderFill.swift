//
//  OrderFill.swift
//  loopr-ios
//
//  Created by ruby on 4/10/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class OrderFill {
    
    var owner: String = ""

    var orderHash: String = ""

    var ringHash: String = ""

    var ringIndex = 0

    var fillIndex = 0

    var txHash: String = ""

    // 0xde0b6b3a7640000
    var amountS: String = ""

    var amountSell: Double = 0.0

    // 0xde0b6b3a7640000
    var amountB: String = ""

    var amountBuy: Double = 0.0

    // 0xef68e7c694f40c8202821edf525de3782458639f
    var tokenS: String = ""

    // LRC
    var tokenSell: String = ""

    // 0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2
    var tokenB: String = ""

    // LRC
    var tokenBuy: String = ""

    // 0x2f424dff26d7f20f088c429075b73a70182d0f5d
    var marketKey: String = ""

    // 0xde0b6b3a7640000
    var split: String = ""

    var fee: FeeParams

    var wallet: String = ""

    var miner: String = ""

    var blockHeight: Int = 0

    // 1547782995
    var blockTimestamp: Int = 0

    init() {
        self.fee = FeeParams()
    }

    init(json: JSON) {
        self.owner = json["owner"].stringValue
        self.orderHash = json["orderHash"].stringValue
        self.ringHash = json["ringHash"].stringValue
        self.ringIndex = json["ringIndex"].intValue
        self.fillIndex = json["fillIndex"].intValue
        self.txHash = json["txHash"].stringValue
        self.amountS = json["amountS"].stringValue
        self.amountB = json["amountB"].stringValue
        self.tokenS = json["tokenS"].stringValue
        self.tokenB = json["tokenB"].stringValue
        self.marketKey = json["marketKey"].stringValue
        self.split = json["split"].stringValue
        self.fee = FeeParams(json: json["fee"])
        self.wallet = json["wallet"].stringValue
        self.miner = json["miner"].stringValue
        self.blockHeight = json["blockHeight"].intValue
        self.blockTimestamp = json["blockTimestamp"].intValue

        self.tokenBuy = TokenDataManager.shared.getTokenByAddress(self.tokenB)?.symbol ?? ""
        self.tokenSell = TokenDataManager.shared.getTokenByAddress(self.tokenS)?.symbol ?? ""
        self.amountBuy = Asset.getAmount(of: self.tokenBuy, fromWeiAmount: self.amountB) ?? 0.0
        self.amountSell = Asset.getAmount(of: self.tokenSell, fromWeiAmount: self.amountS) ?? 0.0
    }
}
