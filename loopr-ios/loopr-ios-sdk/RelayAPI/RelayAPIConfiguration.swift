//
//  RelayAPIConfiguration.swift
//  loopr-ios
//
//  Created by xiaoruby on 4/10/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class RelayAPIConfiguration {

    static let baseURL = "https://relay1.loopr.io"
    static let rpcURL = URL(string: baseURL + "/rpc/v2")!
    static let ethURL = URL(string: baseURL + "/eth")!
    static let socketURL = URL(string: baseURL)!

    // Deployment on Ethereum https://github.com/Loopring/token-listing/blob/master/ethereum/deployment.md#protocol
    static let delegateAddress = "0x17233e07c67d086464fD408148c3ABB56245FA64"
    static let protocolAddress = "0x8d8812b72d1e4ffCeC158D25f56748b7d67c1e78"
    static let neoProtocolAddress = "0xbf78B6E180ba2d1404c92Fc546cbc9233f616C42"
}
