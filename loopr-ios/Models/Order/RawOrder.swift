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
    var validS: Int?

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
        self.validS = validSince.hexToInteger
        self.params = OrderParams(json: json["params"])
        self.feeParams = FeeParams(json: json["feeParams"])
        self.state = OrderState(json: json["state"])
        self.initPrice();
    }

    func initPrice() {
        if let tokenB = TokenDataManager.shared.getTokenByAddress(tokenB),
           let tokenS = TokenDataManager.shared.getTokenByAddress(tokenS) {
            self.tokenBuy = tokenB.symbol
            self.tokenSell = tokenS.symbol
            self.amountBuy = TokenDataManager.shared.getAmount(fromWeiAmount: amountB, of: tokenB.decimals)
            self.amountSell = TokenDataManager.shared.getAmount(fromWeiAmount: amountS, of: tokenS.decimals)
            if let amountBuy = self.amountBuy, let amountSell = self.amountSell {
                self.priceBuy = amountBuy / amountSell
                self.priceSell = amountSell / amountBuy
                self.priceB = "≈\(priceBuy) \(tokenB.symbol)/\(tokenS.symbol)"  //TODO: market precision
                self.priceS = "≈\(priceSell) \(tokenS.symbol)/\(tokenB.symbol)"  //TODO: market precision
            }
            if let symbol = feeParams.tokenF, let tokenF = TokenDataManager.shared.getTokenBySymbol(symbol),
               let outstandingAmountBuy = TokenDataManager.shared.getAmount(fromWeiAmount: amountB, of: tokenB.decimals),
               let outstandingAmountSell = TokenDataManager.shared.getAmount(fromWeiAmount: amountS, of: tokenS.decimals) {
                self.state.outstandingAmountBuy = outstandingAmountBuy
                self.state.outstandingAmountSell = outstandingAmountSell
                self.state.outstandingAmountF = TokenDataManager.shared.getAmount(fromWeiAmount: feeParams.tokenFee, of: tokenF.decimals)
            }
            if let amountBuy = self.amountBuy, let outstandingAmountBuy = self.state.outstandingAmountBuy {
                let numberFormatter = NumberFormatter()
                let rate = (amountBuy - outstandingAmountBuy) / amountBuy * 100
                self.filled = rate.withCommas(0) + numberFormatter.percentSymbol
            }
        }
    }

    static func ==(lhs: RawOrder, rhs: RawOrder) -> Bool {
        return lhs.hash == rhs.hash
    }

}
