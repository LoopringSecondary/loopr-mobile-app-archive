//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class FeeParams {

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    let tokenFee: String

    // token name e.g. lrc
    var tokenF: String?

    // big integer hex string e.g. "0x34f07768a92a83d00000"
    let amountFee: String

    // double value e.g. 0.02
    var amountF: Double?

    let tokenBFeePercentage: Int

    let tokenSFeePercentage: Int

    let walletSplitPercentage: Int

    init(json: JSON) {
        self.tokenFee = json["tokenFee"].stringValue
        self.amountFee = json["amountFee"].stringValue
        self.tokenBFeePercentage = json["tokenBFeePercentage"].intValue
        self.tokenSFeePercentage = json["tokenSFeePercentage"].intValue
        self.walletSplitPercentage = json["walletSplitPercentage"].intValue

        if let tokenF = TokenDataManager.shared.getTokenByAddress(tokenFee) {
            self.tokenF = tokenF.symbol
            self.amountF = TokenDataManager.shared.getAmount(fromWeiAmount: tokenF.symbol, of: tokenF.decimals)
        }
    }
}
