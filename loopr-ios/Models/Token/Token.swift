//
//  Token.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation
import UIKit

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

    static func == (lhs: Token, rhs: Token) -> Bool {
        if lhs.metadata.symbol.lowercased() == rhs.metadata.symbol.lowercased() &&
                   lhs.metadata.address.lowercased() == rhs.metadata.address.lowercased() {
            return true
        } else {
            return false
        }
    }
}
