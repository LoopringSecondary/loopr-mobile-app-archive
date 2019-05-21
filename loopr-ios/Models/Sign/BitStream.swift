//
// Created by kenshin on 2019-04-04.
// Copyright (c) 2019 Loopring. All rights reserved.
//

import Foundation
import BigInt
import web3

class BitStream {

    let ADDRESS_LENGTH = 20
    let Uint256Max = BigInt(String(repeating: "f", count: 64), radix: 16)!

    var data: String = ""

    init() {

    }

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

    func addAddress(_ x: String, forceAppend: Bool = true) {
        addAddress(x, ADDRESS_LENGTH, forceAppend: forceAppend);
    }
    
    func addAddress(_ x: String, _ numBytes: Int, forceAppend: Bool = true) {
        let _x = x.count == 0 ? "0" : x
        insert(_x.toBigInt!.toHexWithoutPrefixZero(size: numBytes * 2)!, forceAppend)
    }

    func addUint16(_ num: BigInt, forceAppend: Bool = true) {
        addBigInt(num, 2, forceAppend)
    }

    func addInt16(_ num: BigInt, forceAppend: Bool = true) {
        if num.signum() == -1 {
            let negUint256 = Uint256Max + num + 1
            let int16Str = negUint256.toHexWithoutPrefix()[60..<64]
            addHex(int16Str, forceAppend: forceAppend)
        } else {
            addBigInt(num, 2, forceAppend)
        }
    }

    func addUint32(_ num: BigInt, forceAppend: Bool = true) {
        addBigInt(num, 4, forceAppend)
    }

    func addUint(_ num: BigInt, forceAppend: Bool = true) {
        addBigInt(num, 32, forceAppend)
    }

    func addNumber(_ num: BigInt, _ numBytes: Int, forceAppend: Bool = true) {
        addBigInt(num, numBytes, forceAppend)
    }

    func addBool(_ b: Bool, forceAppend: Bool = true) {
        let _b = b ? BigInt(1) : BigInt(0)
        addBigInt(_b, 1, forceAppend)
    }

    func addBytes32(_ x: String, forceAppend: Bool) {
        let strWithoutPrefix = x.drop0x()
        if strWithoutPrefix.count <= 64 {
            let strPadded = strWithoutPrefix + String(repeating: "0", count: 64 - strWithoutPrefix.count)
            insert(strPadded, forceAppend)
        }
    }

    func addHex(_ x: String, forceAppend: Bool = true) {
        insert(x.drop0x(), forceAppend)
    }

    func addRawBytes(_ data: Data, forceAppend: Bool = true) {
        insert(data.hexStringWithNoPrefix, forceAppend)
    }

    func addBigInt(_ num: BigInt, _ numBytes: Int, _ forceAppend: Bool = true) {
        let x = num.toHexWithoutPrefixZero(size: numBytes * 2) ?? ""
        insert(x, forceAppend)
    }

    private func insert(_ x: String, _ forceAppend: Bool) {
        if !forceAppend {
            var start = 0
            while true {
                let subData = data.substring(fromIndex: start)
                if let index = subData.index(of: x) {
                    let distance = data.distance(from: data.startIndex, to: index)
                    if (distance % 2 == 0) {
                        return
                    } else {
                        start += 1
                    }
                } else {
                    break
                }
            }
        }
        data.append(x)
    }
}
