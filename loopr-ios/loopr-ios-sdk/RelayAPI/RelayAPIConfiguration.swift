//
//  RelayAPIConfiguration.swift
//  loopr-ios
//
//  Created by xiaoruby on 4/10/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class RelayAPIConfiguration {

    static let baseURL = "https://relay1.loopring.io"
    static let rpcURL = URL(string: baseURL + "/rpc/v2")!
    static let ethURL = URL(string: baseURL + "/eth")!
    static let neoURL = URL(string: baseURL + "/neo")!
    static let crawlerURL = URL(string: baseURL + "/news")!
    static let socketURL = URL(string: baseURL)!

    // Deployment on Ethereum https://github.com/Loopring/token-listing/blob/master/ethereum/deployment.md#protocol
    static let delegateAddress = "0x17233e07c67d086464fD408148c3ABB56245FA64"
    static let protocolAddress = "0x8d8812b72d1e4ffCeC158D25f56748b7d67c1e78"
    static let neoProtocolAddress = "0xbf78B6E180ba2d1404c92Fc546cbc9233f616C42"
    static let loopringAddress = "0x8E63Bb7Af326de3fc6e09F4c8D54A75c6e236abA"
}
