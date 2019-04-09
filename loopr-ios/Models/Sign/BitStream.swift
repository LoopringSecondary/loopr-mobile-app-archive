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
        // Force wrap toBigInt here. If it crashes, we can debug it.
        return insert(_x.toBigInt!.toHexWithoutPrefixZero(size: numBytes * 2)!, forceAppend)
    }

    private func insert(_ x: String, _ forceAppend: Bool) -> Int {
        // let offset = getLength()
        if !forceAppend {
            var start = 0
            while true {
                let subData = data.substring(fromIndex: start)
                if let index = subData.index(of: x) {
                    let distance = data.distance(from: data.startIndex, to: index)
                    if (distance % 2 == 0) {
                        let offset = distance / 2
                        return offset
                    } else {
                        start += 1
                    }
                } else {
                    break
                }
            }
        }
        data.append(x)
        return getLength()
    }

}
