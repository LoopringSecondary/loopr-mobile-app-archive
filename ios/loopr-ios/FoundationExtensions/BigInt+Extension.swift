//
//  BigInt+Extension.swift
//  loopr-ios
//
//  Created by kenshin on 2018/11/21.
//  Copyright © 2018 Loopring. All rights reserved.
//
import Foundation
import BigInt
import Geth

extension BigInt {
    func toEth() -> GethBigInt {
        let hexString = String.init(self, radix: 16)
        let gethAmount = GethBigInt.init(0)!
        gethAmount.setString(hexString, base: 16)
        return gethAmount
    }
    
    func toHexWithPrefix() -> String {
        return "0x" + String.init(self, radix: 16)
    }
    
    func toHexWithoutPrefix() -> String {
        return String.init(self, radix: 16).drop0x()
    }
    
    func toHexWithPrefixZero(size: Int) -> String? {
        return toHexZeroPadded(size: size, withPrefix: true)
    }
    
    func toHexWithoutPrefixZero(size: Int) -> String? {
        return toHexZeroPadded(size: size, withPrefix: false)
    }
    
    func toHexZeroPadded(size: Int, withPrefix: Bool) -> String? {
        var result = self.toHexWithoutPrefix()
        let length = size - result.count
        if length >= 0 && self.signum() >= 0 {
            result = String(repeating: "0", count: length) + result
        } else {
            return nil
        }
        if withPrefix {
            result = "0x" + result
        }
        return result;
    }
    
    func toDouble(by decimal: Int) -> Double {
        return Double(self) / pow(10.0, Double(decimal))
    }
    
    static func generate(from valueInEth: String, by decimal: Int) -> BigInt? {
        var result: BigInt?
        if let double = Double(valueInEth) {
            result = generate(from: double, by: decimal)
        }
        return result
    }
    
    static func generate(from valueInEth: Double, by decimal: Int) -> BigInt {
        return BigInt(valueInEth * pow(10.0, Double(decimal)))
    }
}
