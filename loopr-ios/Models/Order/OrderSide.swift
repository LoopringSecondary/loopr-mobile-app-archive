//
//  OrderSide.swift
//  loopr-ios
//
//  Created by xiaoruby on 7/30/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

enum OrderSide: String, CustomStringConvertible {
    case buy = "BUY"
    case sell = "SELL"

    var description: String {
        switch self {
        case .buy: return LocalizedString("Buy", comment: "")
        case .sell: return LocalizedString("Sell", comment: "")
        }
    }
}
