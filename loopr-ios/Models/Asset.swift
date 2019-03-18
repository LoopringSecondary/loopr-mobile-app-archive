//
//  Asset.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit
import BigInt

class Asset: CustomStringConvertible, Equatable {

    let symbol: String
    var name: String
    var icon: UIImage?
    var enable: Bool
    var balance: Double  // Data from Relay is Hex. Need to transform to decimals
    var display: String
    var allowance: Double
    var total: Double  // total and currency are for the same value.
    var currency: String
    var description: String

    // Get it from \(symbol)-USDT in loopring_getTickerBySource endpoint.
    var decimals: Int

    init(json: JSON) {
        self.enable = true
        self.balance = 0.0
        self.allowance = 0.0
        self.display = "0.0"
        self.total = 0.0
        self.currency = Double(0).currency

        self.symbol = json["symbol"].string ?? ""
        self.decimals = MarketDataManager.shared.getDecimals(tokenSymbol: self.symbol)

        self.name = TokenDataManager.shared.getTokenBySymbol(symbol)?.source.capitalized ?? ""
        if self.symbol == "WETH" {
            self.name = "Wrapped ETH"
        }
        self.icon = UIImage(named: "Token-\(self.symbol)-\(Themes.getTheme())") ?? nil
        self.description = self.name

        if let balance = Asset.getAmount(of: symbol, fromWeiAmount: json["balance"].stringValue) {
            self.balance = balance
            let length = self.decimals
            // Displaying "0" in the WalletViewController doesn't look good.
            if self.balance.truncatingRemainder(dividingBy: 1) == 0 && self.balance > 0 {
                self.display = self.balance.withCommas(2)
            } else if self.balance > 0 {
                self.display = self.balance.withCommas(length).trailingZero()
            } else {
                self.display = self.balance.withCommas(length)
            }
        }

        if let allowance = Asset.getAmount(of: symbol, fromWeiAmount: json["allowance"].stringValue) {
            self.allowance = allowance
        }
    }

    init(token: TokenV1) {
        self.symbol = token.symbol
        self.name = token.source
        self.enable = true
        self.description = self.symbol
        self.balance = 0.0
        self.allowance = 0.0
        self.display = "0.0"
        self.total = 0.0
        self.currency = Double(0).currency
        self.icon = UIImage(named: "Token-\(self.symbol)-\(Themes.getTheme())") ?? nil
        self.decimals = MarketDataManager.shared.getDecimals(tokenSymbol: self.symbol)
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

    static func getAmount(of symbol: String, fromWeiAmount weiAmount: String) -> Double? {
        var result: Double?
        if let token = TokenDataManager.shared.getTokenBySymbol(symbol) {
            result = getAmount(fromWeiAmount: weiAmount, of: token.decimals)
        }
        return result
    }

    static func == (lhs: Asset, rhs: Asset) -> Bool {
        return lhs.symbol == rhs.symbol
    }
}
