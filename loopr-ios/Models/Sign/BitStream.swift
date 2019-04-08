//
// Created by kenshin on 2019-04-04.
// Copyright (c) 2019 Loopring. All rights reserved.
//

import Foundation
import BigInt
import web3

class BitStream {

    let ADDRESS_LENGTH = 20
    let Uint256Max = BigInt(String(repeating: "f", count: 64), radix: 16)

    var data: String

    init(data: String) {
        self.data = data
    }

    func getData() -> String {
        return data.isEmpty ? "0x0" : "0x" + data;
    }

    func getBytes() -> [UInt8] {
        return data.hexBytes
    }

    func getLength() -> Int {
        return data.count / 2
    }

    func addAddress(x: String, numBytes: Int, forceAppend: Bool) -> Int {
        let _x = x.count == 0 ? "0" : x
        return insert(_x.toBigInt?.toHexWithoutPrefixZero(size: numBytes * 2), forceAppend)
    }

    private func insert(_ x: String?, _ forceAppend: Bool) -> Int {
        let offset = getLength()
        if !forceAppend {
            var start = data.index
            start.encodedOffset
        }
    }
}
