//
//  Token.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation
import UIKit
import BigInt

class Token: Equatable {

    let metadata: TokenMetadata
    let info: TokenInfo
    let ticker: TokenTicker
    var icon: UIImage?

    init(json: JSON) {
        self.metadata = TokenMetadata(json: json["symbol"])
        self.info = TokenInfo(json: json["source"])
        self.ticker = TokenTicker(json: json["isMarket"])
        self.icon = UIImage(named: "Token-\(self.info.symbol)-\(Themes.getTheme())")
    }

    var precision: Int {
        return self.metadata.precision
    }

    var symbol: String {
        return self.metadata.symbol
    }

    var decimals: Int {
        return self.metadata.decimals
    }

    var source: String {
        return self.metadata.name
    }

    static func getAmount(fromWeiAmount weiAmount: String, of decimals: Int) -> Double? {
        var index: String.Index
        var result: Double?
        // hex string
        if weiAmount.lowercased().starts(with: "0x") {
            let hexString = weiAmount.dropFirst(2)
            let decString = BigUInt(hexString, radix: 16)!.description
            return getAmount(fromWeiAmount: decString, of: decimals)
        } else {
            var amount = weiAmount
            guard decimals < 100 || decimals >= 0 else {
                return result
            }
            if amount == "0" {
                return 0
            }
            if decimals >= amount.count {
                let prepend = String(repeating: "0", count: decimals - amount.count + 1)
                amount = prepend + amount
            }
            index = amount.index(amount.endIndex, offsetBy: -decimals)
            amount.insert(".", at: index)
            result = Double(amount)
        }
        return result
    }

    static func == (lhs: Token, rhs: Token) -> Bool {
        if lhs.metadata.symbol.lowercased() == rhs.metadata.symbol.lowercased() &&
                   lhs.metadata.address.lowercased() == rhs.metadata.address.lowercased() {
            return true
        } else {
            return false
        }
    }
}
