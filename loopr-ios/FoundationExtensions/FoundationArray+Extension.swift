//
//  FoundationArray+Extension.swift
//  loopr-ios
//
//  Created by xiaoruby on 4/19/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

func unique<S: Sequence, T: Hashable >(_ source: S) -> [T] where S.Iterator.Element == T {
    var buffer = [T]()
    var added = Set<T>()
    for elem in source {
        if !added.contains(elem) {
            buffer.append(elem)
            added.insert(elem)
        }
    }
    return buffer
}

extension MutableCollection {
    /// Shuffles the contents of this collection.
    mutating func shuffle() {
        let c = count
        guard c > 1 else { return }
        
        for (firstUnshuffled, unshuffledCount) in zip(indices, stride(from: c, to: 1, by: -1)) {
            // Change `Int` in the next line to `IndexDistance` in < Swift 4.1
            let d: IndexDistance = numericCast(arc4random_uniform(numericCast(unshuffledCount)))
            let i = index(firstUnshuffled, offsetBy: d)
            swapAt(firstUnshuffled, i)
        }
    }
}

extension Sequence {
    /// Returns an array with the contents of this sequence, shuffled.
    func shuffled() -> [Element] {
        var result = Array(self)
        result.shuffle()
        return result
    }
}

extension Array where Element == UInt8 {
    public var hexString: String {
        return self.map { return String(format: "%02x", $0) }.joined()
    }
    
    public var hexStringWithPrefix: String {
        return "0x\(hexString)"
    }
    
    func toWordArray() -> [UInt32] {
        return arrayUtil_convertArray(self, to: UInt32.self)
    }
    
    mutating public func removeTrailingZeros() {
        for i in (0..<self.endIndex).reversed() {
            guard self[i] == 0 else {
                break
            }
            self.remove(at: i)
        }
    }
    
    func xor(other: [UInt8]) -> [UInt8] {
        assert(self.count == other.count)
        
        var result: [UInt8] = []
        for i in 0..<self.count {
            result.append(self[i] ^ other[i])
        }
        return result
    }
    
    public var base58EncodedString: String {
        guard !self.isEmpty else { return "" }
        return Base58Util.base58FromBytes(self)
    }
    
    public var base58CheckEncodedString: String {
        var bytes = self
        let checksum = [UInt8](bytes.sha256.sha256[0..<4])
        
        bytes.append(contentsOf: checksum)
        
        return Base58Util.base58FromBytes(bytes)
    }
    
    public var sha256: [UInt8] {
        let bytes = self
        
        let mutablePointer = UnsafeMutablePointer<UInt8>.allocate(capacity: Int(CC_SHA256_DIGEST_LENGTH))
        
        CC_SHA256(bytes, CC_LONG(bytes.count), mutablePointer)
        
        let mutableBufferPointer = UnsafeMutableBufferPointer<UInt8>.init(start: mutablePointer, count: Int(CC_SHA256_DIGEST_LENGTH))
        let sha256Data = Data(buffer: mutableBufferPointer)
        
        mutablePointer.deallocate()
        
        return sha256Data.bytes
    }
}

extension Array where Element == UInt32 {
    func toByteArrayFast() -> [UInt8] {
        return arrayUtil_convertArray(self, to: UInt8.self)
    }
    
    func toByteArray() -> [UInt8] {
        return arrayUtil_convertArray(self, to: UInt8.self)
    }
}

func arrayUtil_convertArray<S, T>(_ source: [S], to: T.Type) -> [T] {
    let count = source.count * MemoryLayout<S>.stride/MemoryLayout<T>.stride
    return source.withUnsafeBufferPointer {
        $0.baseAddress!.withMemoryRebound(to: T.self, capacity: count) {
            Array(UnsafeBufferPointer(start: $0, count: count))
        }
    }
}
