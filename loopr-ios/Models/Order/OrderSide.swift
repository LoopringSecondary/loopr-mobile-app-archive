//
//  OrderSide.swift
//  loopr-ios
//
//  Created by xiaoruby on 7/30/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

enum OrderSide: String, CustomStringConvertible {

    case both = "both"
    case buy = "buy"
    case sell = "sell"

    var description: String {
        switch self {
        case .both: return LocalizedString("Both", comment: "")
        case .buy: return LocalizedString("Buy", comment: "")
        case .sell: return LocalizedString("Sell", comment: "")
        }
    }
}
