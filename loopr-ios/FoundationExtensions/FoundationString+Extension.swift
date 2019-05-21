//
//  String+Extension.swift
//  loopr-ios
//
//  Created by xiaoruby on 4/15/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import CommonCrypto
import BigInt

extension String {
    
    func trim() -> String {
        return self.trimmingCharacters(in: NSCharacterSet.whitespacesAndNewlines)
    }
    
    func widthOfString(usingFont font: UIFont) -> CGFloat {
        let fontAttributes = [NSAttributedStringKey.font: font]
        let size = self.size(withAttributes: fontAttributes)
        return size.width
    }
    
    func heightOfString(usingFont font: UIFont) -> CGFloat {
        let fontAttributes = [NSAttributedStringKey.font: font]
        let size = self.size(withAttributes: fontAttributes)
        return size.height
    }
    
    func sizeOfString(usingFont font: UIFont) -> CGSize {
        let fontAttributes = [NSAttributedStringKey.font: font]
        return self.size(withAttributes: fontAttributes)
    }
    
    subscript (i: Int) -> String {
        return self[i ..< i + 1]
    }
    
    func substring(fromIndex: Int) -> String {
        return self[min(fromIndex, count) ..< count]
    }
    
    func substring(toIndex: Int) -> String {
        return self[0 ..< max(0, toIndex)]
    }
    
    subscript (r: Range<Int>) -> String {
        let range = Range(uncheckedBounds: (lower: max(0, min(count, r.lowerBound)),
                                            upper: min(count, max(0, r.upperBound))))
        let start = index(startIndex, offsetBy: range.lowerBound)
        let end = index(start, offsetBy: range.upperBound - range.lowerBound)
        return String(self[start ..< end])
    }
    
    //Checks if the given string is an address in hexidecimal encoded form.
    public func isHexAddress() -> Bool {
        if !Set([40, 42]).contains(self.count) {
            return false
        } else if isHex() {
            return true
        }
        return false
    }
    
    public func isHex() -> Bool {
        let lowerCasedSample = self.lowercased()
        if lowerCasedSample.contains("0x") {
            return true
        }
        let unprefixedValue = lowerCasedSample.remove0xPrefix()
        let hexRegex = "^[0-9a-f]+$"
        let regex = try! NSRegularExpression(pattern: hexRegex, options: [])
        let matches = regex.matches(in: unprefixedValue, options: [], range: NSRange(location: 0, length: unprefixedValue.count))
        
        if matches.count > 0 {
            return true
        }
        return false
    }
    
    public func is0xPrefixed() -> Bool {
        return self.hasPrefix("0x") || self.hasPrefix("0X")
    }
    
    public func remove0xPrefix() -> String {
        if is0xPrefixed() {
            let index2 = self.index(self.startIndex, offsetBy: 2)
            return String(self[index2...])
        }
        return self
    }
    
    public func toDecimalPlaces(_ value: Int) -> String {
        let charset: Character = "."
        if let index = self.index(of: charset) {
            let distanceToDecimal = self.distance(from: self.startIndex, to: index).advanced(by: value + 1)
            if let lastIndex = self.index(self.startIndex, offsetBy: distanceToDecimal, limitedBy: self.endIndex) {
                return String(self[..<lastIndex])
            } else {
                return self
            }
        }
        return self
    }
    
    var wholeValue: String {
        if let decimalIndex = self.index(of: ".") {
            return String(self[..<decimalIndex])
        } else {
            return self
        }
    }
    
    public func trimmingFirstConsecutiveCharacters(_ character: String) -> String? {
        let pattern = "^\(character)+(?!$)"
        do {
            let regex = try NSRegularExpression(pattern: pattern, options: [])
            let match = regex.firstMatch(in: self, options: [], range: NSRange(location: 0, length: self.count))
            
            if let match = match {
                let startIndex = String.Index(encodedOffset: match.range.length)
                let endIndex = String.Index(encodedOffset: self.count)
                let result = self[startIndex..<endIndex]
                return String(result)
            } else {
                return self
            }
        } catch {
            print("Failed to create regex pattern for given character")
            return nil
        }
    }
    
    // Reference: https://stackoverflow.com/questions/43360747/how-to-convert-hexadecimal-string-to-an-array-of-uint8-bytes-in-swift
    // compactMap and flatMap https://developer.apple.com/documentation/swift/sequence/2950916-compactmap
    // If use compactMap, get an compiler error Value of type 'StrideTo<String.IndexDistance>' (aka 'StrideTo<Int>') has no member 'compactMap'
    var hexBytes: [UInt8] {
        let hex = Array(self)
        return stride(from: 0, to: count, by: 2).flatMap { UInt8(String(hex[$0..<$0.advanced(by: 2)]), radix: 16) }
    }
    
    var isDouble: Bool {
        if Double(self) != nil {
            return true
        } else {
            return false
        }
    }
    
    var toInt: Int? {
        if self.isHex() {
            return Int(self.drop0x(), radix: 16)
        }
        return nil
    }
    
    var toBigInt: BigInt? {
        if self.isHex() {
            return BigInt(self.drop0x(), radix: 16)
        }
        return nil
    }
    
    func higlighted(words: [String], attributes: [NSAttributedStringKey: Any]) -> NSMutableAttributedString {
        
        let allAttributedText = NSMutableAttributedString.init(string: self)
        var ranges = [NSRange]()
        
        for word in words {
            var string = allAttributedText.string as NSString
            var i = 0
            while true {
                var range = string.range(of: word)
                if range.location == NSNotFound {
                    break
                }
                i += range.location + word.count
                string = string.substring(from: range.location + range.length) as NSString
                range.location = i - word.count
                print("\(range)  XX \(word)" )
                
                ranges.append(range)
            }
        }
        for range in ranges {
            allAttributedText.addAttributes(attributes, range: range)
        }
        return allAttributedText
    }
    
    func getAddressFormat(length: Int = 6) -> String {
        let header = String(self.prefix(length))
        let footer = String(self.suffix(length))
        return "\(header)...\(footer)"
    }
    
    func textWidth(font: UIFont) -> CGFloat {
        let attributes = [NSAttributedStringKey.font: font]
        return self.size(withAttributes: attributes).width
    }
    
    func trailingZero() -> String {
        if self.contains(".") {
            var value = self
            while true {
                if value.last == "0" {
                    value = String(value.dropLast())
                } else {
                    break
                }
            }
            if value.last == "." {
                value = String(value.dropLast())
            }
            return value
        } else {
            return self
        }
    }
    
    func removeComma() -> String {
        return self.replacingOccurrences(of: ",", with: "", options: NSString.CompareOptions.literal, range:nil)
    }

    func drop0x() -> String {
        if hasPrefix("0x") {
            return String(dropFirst(2))
        }
        return self
    }
    
    public var base58EncodedString: String {
        return [UInt8](utf8).base58EncodedString
    }
    
    public var base58DecodedData: Data? {
        let bytes = Base58Util.bytesFromBase58(self)
        return Data(bytes)
    }
    
    public var base58CheckDecodedData: Data? {
        guard let bytes = self.base58CheckDecodedBytes else { return nil }
        return Data(bytes)
    }
    
    public var base58CheckDecodedBytes: [UInt8]? {
        var bytes = Base58Util.bytesFromBase58(self)
        guard 4 <= bytes.count else { return nil }
        
        let checksum = [UInt8](bytes[bytes.count-4..<bytes.count])
        bytes = [UInt8](bytes[0..<bytes.count-4])
        
        let calculatedChecksum = [UInt8](bytes.sha256.sha256[0...3])
        if checksum != calculatedChecksum { return nil }
        
        return bytes
    }
    
    public var littleEndianHexToUInt: UInt {
        let str = self.dataWithHexString().bytes.reversed().hexString
        return UInt(str, radix: 16)!
    }
    
    public var sha256: Data? {
        return self.data(using: String.Encoding.utf8)?.sha256
    }
    
    public var hexString: String {
        return self.data(using: .utf8)?.hexString ?? ""
    }
    
    func hash160() -> String? {
        //NEO Address hash160
        //skip the first byte which is 0x17, revert it then convert to full hex
        let bytes = self.base58CheckDecodedBytes!
        let reverse = Data(bytes: bytes[1...bytes.count - 1].reversed())
        return reverse.hexString
    }
    
    func hashFromAddress() -> String {
        let bytes = self.base58CheckDecodedBytes!
        let shortened = bytes[0...20] //need exactly twenty one bytes
        let substringData = Data(bytes: shortened)
        let hashOne = substringData.sha256
        let hashTwo = hashOne.sha256
        _ = [UInt8](hashTwo)
        let finalKeyData = Data(bytes: shortened[1...shortened.count - 1])
        return finalKeyData.hexString
    }
    
    func scriptHash() -> Data {
        let bytes = self.base58CheckDecodedBytes!
        let shortened = bytes[0...20] //need exactly twenty one bytes
        let substringData = Data(bytes: shortened)
        let hashOne = substringData.sha256
        let hashTwo = hashOne.sha256
        _ = [UInt8](hashTwo)
        let finalKeyData = Data(bytes: shortened[1...shortened.count - 1])
        return finalKeyData
    }
    
    func dataWithHexString() -> Data {
        var hex = self
        var data = Data()
        while hex.count > 0 {
            let c: String = String(hex[..<hex.index(hex.startIndex, offsetBy: 2)])
            hex = String(hex[hex.index(hex.startIndex, offsetBy: 2)...])
            var ch: UInt32 = 0
            Scanner(string: c).scanHexInt32(&ch)
            var char = UInt8(ch)
            data.append(&char, count: 1)
        }
        return data
    }
    
    func hexToString() -> String? {
        guard self.count % 2 == 0 else {
            return nil
        }
        
        var bytes = [CChar]()
        
        var startIndex = self.index(self.startIndex, offsetBy: 0)
        while startIndex < self.endIndex {
            let endIndex = self.index(startIndex, offsetBy: 2)
            let substr = self[startIndex..<endIndex]
            
            if let byte = Int8(substr, radix: 16) {
                bytes.append(byte)
            } else {
                return nil
            }
            
            startIndex = endIndex
        }
        
        bytes.append(0)
        return String(cString: bytes)
    }

}
