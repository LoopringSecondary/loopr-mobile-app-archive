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
        self.metadata = TokenMetadata(json: json["metadata"])
        self.info = TokenInfo(json: json["info"])
        self.ticker = TokenTicker(json: json["ticker"])
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

    var address: String {
        return self.metadata.address
    }

    static func == (lhs: Token, rhs: Token) -> Bool {
        if lhs.metadata.address.lowercased() == rhs.metadata.address.lowercased() {
            return true
        } else {
            return false
        }
    }
}
