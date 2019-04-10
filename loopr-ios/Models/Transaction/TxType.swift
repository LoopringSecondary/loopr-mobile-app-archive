//
//  TxType.swift
//  loopr-ios
//
//  Created by xiaoruby on 6/2/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension Transaction {

    enum TxType: String, CustomStringConvertible {
        case token_auth = "TOKEN_AUTH"
        case eth_out = "ETHER_TRANSFER_OUT"
        case token_out = "TOKEN_TRANSFER_OUT"
        case eth_in = "ETHER_TRANSFER_IN"
        case token_in = "TOKEN_TRANSFER_IN"
        case trade_sell = "TRADE_SELL"
        case trade_buy = "TRADE_BUY"
        case ether_wrap = "ETHER_WRAP"
        case ether_unwrap = "ETHER_UNWRAP"
        case order_cancel = "ORDER_CANCEL"
        case other = "other"

        var description: String {
            switch self {
            case .token_auth: return "Enable"
            case .eth_out: return LocalizedString("Sent", comment: "")
            case .token_out: return LocalizedString("Sent", comment: "")
            case .eth_in: return LocalizedString("Received", comment: "")
            case .token_in: return LocalizedString("Received", comment: "")
            case .trade_sell: return LocalizedString("Sold", comment: "")
            case .trade_buy: return LocalizedString("Bought", comment: "")
            case .ether_wrap: return LocalizedString("Convert", comment: "")
            case .ether_unwrap: return LocalizedString("Convert", comment: "")
            case .order_cancel: return LocalizedString("Cancel", comment: "")
            case .other: return LocalizedString("Unknown", comment: "")
            }
        }
    }
}
