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

    // we need to a method to strengthen the key
    // https://stackoverflow.com/questions/18122192/custom-string-to-128-bit-string
    class func strengthenKey(plaintext: String) -> [UInt8] {
        let key = "eda32f751456e33195f1f499cf2dc7c97ea127b6d488f211ccc5126fbb24afa6"
        let nonce = "a544218dadd3c1"
        let tagLength  = 64
        let additionalAuthenticatedData = ""
        
        let plaintextUInt8 = Array<UInt8>(hex: plaintext)

        let aes = try! AES(key: Array<UInt8>(hex: key), blockMode: CCM(iv: Array<UInt8>(hex: nonce), tagLength: tagLength, messageLength: plaintextUInt8.count, additionalAuthenticatedData: Array<UInt8>(hex: additionalAuthenticatedData)), padding: .noPadding)
        let encrypted = try! aes.encrypt(plaintextUInt8)
        return encrypted
    }
    

    class func encryptEncoded(plainText: String, secretKey: [UInt8]) -> ([UInt8], [UInt8]) {
        guard plainText.hexString() != "" && secretKey.count > 0 else {
            return ([], [])
        }

        // AES-128, AES-192, AES-256 is defined by the length of thkey
        let encGCM = GCM(iv: iv, mode: .detached)
        let aes = try! AES(key: secretKey, blockMode: encGCM, padding: .noPadding)
        let encrypted = try! aes.encrypt(plainText.hexString().hexBytes)
        print(encrypted)
        return (encrypted, encGCM.authenticationTag!)
    }

    class func decryptEncoded(cipherText: [UInt8], authenticationTag: [UInt8], secretKey: [UInt8]) -> String? {
        let decGCM = GCM(iv: iv, authenticationTag: authenticationTag, mode: .detached)
        let aes = try! AES(key: secretKey, blockMode: decGCM, padding: .noPadding)
        return try! aes.decrypt(cipherText).hexString
    }

}
