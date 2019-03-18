//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

enum TokenType: CustomStringConvertible {

    case erc20 = "TOKEN_TYPE_ERC20"
    case erc1400 = "TOKEN_TYPE_ERC1400"
    case eth = "TOKEN_TYPE_ETH"
    case unknown = "UNKNOWN"

    var description: String {
        switch self {
        case .erc20:
            return "erc20"
        case .erc1400:
            return "erc1400"
        case .eth:
            return "eth"
        case .unknown:
            return "unknown"
        }
    }
}
