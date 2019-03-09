//
//  ASE_GCM.swift
//  loopr-ios
//
//  Created by ruby on 3/8/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation
import CryptoSwift

class ASE_GCM {

    static let iv = Array<UInt8>(hex: "0xcafebabefacedbaddecaf888")

    class func encryptEncoded(plainText: String, key: [UInt8]) -> ([UInt8], [UInt8]) {
        guard plainText.hexString() != "" else {
            return ([], [])
        }

        // AES-128, AES-192, AES-256 is defined by the length of thkey
        let encGCM = GCM(iv: iv, mode: .detached)
        let aes = try! AES(key: key, blockMode: encGCM, padding: .noPadding)
        let encrypted = try! aes.encrypt(plainText.hexString().hexBytes)
        print(encrypted)
        return (encrypted, encGCM.authenticationTag!)
    }

    class func decryptEncoded(cipherText: [UInt8], authenticationTag: [UInt8], key: [UInt8]) -> String? {
        let decGCM = GCM(iv: iv, authenticationTag: authenticationTag, mode: .detached)
        let aes = try! AES(key: key, blockMode: decGCM, padding: .noPadding)
        return try! aes.decrypt(cipherText).hexString
    }

}
