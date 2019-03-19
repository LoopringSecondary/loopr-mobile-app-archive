//
//  RawOrder.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class RawOrder: Equatable {

    let hash: String

    let version: Int

    let owner: String

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    let tokenB: String

    // token name e.g. lrc
    var tokenBuy: String?

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    let tokenS: String

    // token name e.g. lrc
    var tokenSell: String?

    // big integer hex string e.g. "0x34f07768a92a83d00000"
    let amountB: String

    // double value e.g. 0.02
    var amountBuy: Double?

    // big integer hex string e.g. 0x34f07768a92a83d00000
    let amountS: String

    // double value e.g. 0.02
    var amountSell: Double?

    // hex string e.g. 0x5be8e179
    let validSince: String

    // int value e.g. 3562653865313739
    var validS: Int = 0

    let params: OrderParams

    let feeParams: FeeParams

    let state: OrderState

    var priceB: String?

    var priceBuy: Double?

    var priceS: String?

    var priceSell: Double?

    // e.g. 10.50%
    var filled: String = ""

    init(json: JSON) {
        self.hash = json["hash"].stringValue
        self.version = json["version"].intValue
        self.owner = json["owner"].stringValue
        self.tokenB = json["tokenB"].stringValue
        self.tokenS = json["tokenS"].stringValue
        self.amountB = json["amountB"].stringValue
        self.amountS = json["amountS"].stringValue
        self.validSince = json["validSince"].stringValue
        self.params = OrderParams(json: json["params"])
        self.feeParams = FeeParams(json: json["feeParams"])
        self.state = OrderState(json: json["state"])

        self.tokenBuy = TokenDataManager.shared.getTokenByAddress(tokenB)?.symbol
        self.tokenSell = TokenDataManager.shared.getTokenByAddress(tokenS)?.symbol
        self.amountBuy = TokenDataManager.shared.get

        initPrice();

    }

    func initPrice() {


        self.amountBuy =

        var value =
    }

    static func ==(lhs: Order, rhs: Order) -> Bool {
        return lhs.originalOrder.hash == rhs.originalOrder.hash
    }

}
