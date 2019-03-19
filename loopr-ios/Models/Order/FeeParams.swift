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
    var tokenF: String = ""

    // big integer hex string e.g. "0x34f07768a92a83d00000"
    let amountFee: String

    // double value e.g. 0.02
    var amountF: Double = 0

    let tokenBFeePercentage: Int

    let tokenSFeePercentage: Int

    let walletSplitPercentage: Int

    init(json: JSON) {
        self.tokenFee = json["tokenFee"].stringValue
        self.tokenF = TokenDataManager.shared.getTokenByAddress(tokenFee)?.symbol ?? ""
        self.amountFee = json["amountFee"].stringValue
        self.amountF = json["amountFee"].doubleValue  sdfsf
        self.tokenBFeePercentage = json["tokenBFeePercentage"].intValue
        self.tokenSFeePercentage = json["tokenSFeePercentage"].intValue
        self.walletSplitPercentage = json["walletSplitPercentage"].intValue
    }
}
