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

    func addAddress(_ x: String, forceAppend: Bool = true) -> Int {
        return addAddress(x, ADDRESS_LENGTH, forceAppend: forceAppend);
    }
    
    func addAddress(_ x: String, _ numBytes: Int, forceAppend: Bool = true) -> Int {
        let _x = x.count == 0 ? "0" : x
        return insert(_x.toBigInt!.toHexWithoutPrefixZero(size: numBytes * 2)!, forceAppend)
    }

    func addUint16(_ num: BigInt, forceAppend: Bool = true) -> Int {
        return addBigInt(num, 2, forceAppend)
    }

    func addInt16(_ num: BigInt, forceAppend: Bool = true) -> Int {
        if num.signum() == -1 {
            let negUint256 = Uint256Max + num + 1
            let int16Str = negUint256.toHexWithoutPrefix()[60..<64]
            return addHex(int16Str, forceAppend)
        } else {
            return addBigInt(num, 2, forceAppend)
        }
    }

    func addUint32(_ num: BigInt, forceAppend: Bool = true) -> Int {
        return addBigInt(num, 4, forceAppend)
    }

    func addUint(_ num: BigInt, forceAppend: Bool = true) -> Int {
        return addBigInt(num, 32, forceAppend)
    }

    func addNumber(_ num: BigInt, _ numBytes: Int, forceAppend: Bool = true) -> Int {
        return addBigInt(num, numBytes, forceAppend)
    }

    func addBool(_ b: Bool, forceAppend: Bool = true) -> Int {
        let _b = b ? BigInt(1) : BigInt(0)
        return addBigInt(_b, 1, forceAppend)
    }

    func addBytes32(_ x: String, forceAppend: Bool) throws -> Int {
        let strWithoutPrefix = x.drop0x()
        if strWithoutPrefix.count > 64 {
            throw NSException(name: NSExceptionName.invalidArgumentException ,reason: "invalid bytes32 str: too long, str: \(strWithoutPrefix)", userInfo: nil) as! Error
        }
        let strPadded = strWithoutPrefix + String(repeating: "0", count: 64 - strWithoutPrefix.count)
        return insert(strPadded, forceAppend)
    }

    func addHex(_ x: String, forceAppend: Bool = true) -> Int {
        return insert(x.drop0x(), forceAppend)
    }

    func addRawBytes(data: Data, forceAppend: Bool = true) -> Int {
        return insert(data.hexStringWithNoPrefix, forceAppend)
    }

    func addBigInt(_ num: BigInt, _ numBytes: Int, _ forceAppend: Bool = true) -> Int {
        let x = num.toHexWithoutPrefixZero(size: numBytes * 2) ?? ""
        return insert(x, forceAppend)
    }

    private func insert(_ x: String, _ forceAppend: Bool) -> Int {
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
