//
//  TokenDataManager.swift
//  loopr-ios
//
//  Created by xiaoruby on 4/11/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import BigInt

private let blackList: [String] = ["BAR", "FOO", "EOS"]

class TokenDataManager {

    static let shared = TokenDataManager()
    private var tokens: [Token]

    private init() {
        self.tokens = []
        self.loadTokens()
    }

    func loadTokens() {
        loadTokensFromJson()
        loadTokensFromServer(completionHandler: {})
    }

    // load tokens from json file to avoid http request
    func loadTokensFromJson() {
        if let path = Bundle.main.path(forResource: "tokens", ofType: "json") {
            let jsonString = try? String(contentsOfFile: path, encoding: String.Encoding.utf8)
            let json = JSON(parseJSON: jsonString!)
            for subJson in json.arrayValue {
                let token = Token(json: subJson)
                if !blackList.contains(token.symbol.uppercased()) {
                    tokens.append(token)
                }
            }
            tokens.sort(by: { (a, b) -> Bool in
                return a.symbol < b.symbol
            })
        }
    }

    private func loadTokensFromServer(completionHandler: @escaping () -> Void) {
        LoopringAPIRequest.getTokens(requireMetadata: true, requireInfo: true, requirePrice: true, quoteCurrencyForPrice: Currency.init(name: "USD")) { (tokens, error) in
            guard let tokens = tokens, error == nil else {
                completionHandler()
                return
            }
            for token in tokens {
                // Check if the token exists in self.tokens.
                if !self.tokens.contains(where: { (element) -> Bool in
                    return element.symbol.lowercased() == token.symbol.lowercased()
                }) {
                    if !blackList.contains(token.symbol.uppercased()) {
                        self.tokens.append(token)
                    }
                }
            }
            completionHandler()
        }
    }

    /*
    func loadCustomTokensForCurrentWallet(completionHandler: @escaping () -> Void) {
        if let wallet = CurrentAppWalletDataManager.shared.getCurrentAppWallet() {
            LoopringAPIRequest.getCustomTokens(owner: wallet.address) { (tokens, error) in
                guard let tokens = tokens, error == nil else {
                    completionHandler()
                    return
                }
                for token in tokens {
                    // Check if the token exists in self.tokens.
                    if !self.tokens.contains(where: { (element) -> Bool in
                        return element.symbol.lowercased() == token.symbol.lowercased()
                    }) {
                        if !blackList.contains(token.symbol.uppercased()) {
                            self.tokens.append(token)
                        }
                    }
                }
                completionHandler()
            }
        }
    }

    func loadCustomTokens(completionHandler: @escaping () -> Void) {
        let wallets = AppWalletDataManager.shared.getWallets()
        for wallet in wallets {
            LoopringAPIRequest.getCustomTokens(owner: wallet.address) { (tokens, error) in
                guard let tokens = tokens, error == nil else {
                    completionHandler()
                    return
                }
                for token in tokens {
                    // Check if the token exists in self.tokens.
                    if !self.tokens.contains(where: { (element) -> Bool in
                        return element.symbol.lowercased() == token.symbol.lowercased()
                    }) {
                        if !blackList.contains(token.symbol.uppercased()) {
                            self.tokens.append(token)
                        }
                    }
                }
                completionHandler()
            }
        }
    }
    */

    // Get a list of tokens
    func getTokens() -> [Token] {
        return tokens
    }

    private func getTokensExcept(for symbols: [String]) -> [Token] {
        return tokens.filter({ (token) -> Bool in
            return !symbols.contains(token.symbol.uppercased())
        })
    }

    func getErcTokens() -> [Token] {
        return getTokensExcept(for: ["ETH"])
    }

    func getErcTokensExcept(for symbols: [String]) -> [Token] {
        let list = symbols + ["ETH"]
        return tokens.filter({ (token) -> Bool in
            return !list.contains(token.symbol.uppercased())
        })
    }

    func getTokensToAdd() -> [Token] {
        return tokens
    }

    func getTokenBySymbol(_ symbol: String) -> Token? {
        var result: Token?
        for case let token in tokens where token.symbol.lowercased() == symbol.lowercased() {
            result = token
            break
        }
        return result
    }

    func getTokenByAddress(_ address: String) -> Token? {
        var result: Token?
        for case let token in tokens where token.address.lowercased() == address.lowercased() {
            result = token
            break
        }
        return result
    }

    func getAddress(by symbol: String) -> String? {
        if let token = getTokenBySymbol(symbol) {
            return token.address
        } else {
            return nil
        }
    }

    func getAmount(fromWeiAmount weiAmount: String, of decimals: Int) -> Double? {
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
}
