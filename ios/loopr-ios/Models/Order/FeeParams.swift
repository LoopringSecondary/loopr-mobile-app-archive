//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation
import BigInt

class FeeParams {

    // token protocol e.g. 0xef68e7c694f40c8202821edf525de3782458639f
    var tokenFee: String = ""

    // token name e.g. lrc
    var tokenF: String? = "LRC"

    // big integer hex string e.g. "0x34f07768a92a83d00000"
    var amountFee: String = ""

    // big int value e.g. 0.02
    var amountF: Double = 0.0

    var tokenRecipient: String = ""

    var tokenBFeePercentage: Int = 0

    var tokenSFeePercentage: Int = 0

    var walletSplitPercentage: Int = 0

    init() {

    }

    init(json: JSON) {
        self.tokenFee = json["tokenFee"].stringValue
        self.amountFee = json["amountFee"].stringValue
        self.tokenRecipient = json["tokenRecipient"].stringValue
        self.tokenBFeePercentage = json["tokenBFeePercentage"].intValue
        self.tokenSFeePercentage = json["tokenSFeePercentage"].intValue
        self.walletSplitPercentage = json["walletSplitPercentage"].intValue

        if let tokenF = TokenDataManager.shared.getTokenByAddress(tokenFee) {
            self.tokenF = tokenF.symbol
            self.amountF = TokenDataManager.shared.getAmount(fromWeiAmount: amountFee, of: tokenF.decimals) ?? 0.0
        }
    }

    func toJson() -> JSON {
        var json = JSON()
        json["tokenFee"] = JSON(tokenFee)
        json["amountFee"] = JSON(amountFee)
        json["tokenRecipient"] = JSON(tokenRecipient)
        return json
    }
}
