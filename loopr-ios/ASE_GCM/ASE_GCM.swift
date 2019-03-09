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

    class func encryptEncoded(plainText: String) -> ([UInt8], [UInt8]) {
        guard plainText.hexString() != "" else {
            return ([], [])
        }
        let key = Array<UInt8>(hex: "0xfeffe9928665731c6d6a8f9467308308")
        let iv = Array<UInt8>(hex: "0xcafebabefacedbaddecaf888")
        
        let encGCM = GCM(iv: iv, mode: .detached)
        let aes = try! AES(key: key, blockMode: encGCM, padding: .noPadding)
        let encrypted = try! aes.encrypt(plainText.hexString().hexBytes)
        print(encrypted)
        return (encrypted, encGCM.authenticationTag!)
    }
    
    class func decryptEncoded(cipherText: [UInt8], authenticationTag: [UInt8]) -> String? {
        let key = Array<UInt8>(hex: "0xfeffe9928665731c6d6a8f9467308308")
        let iv = Array<UInt8>(hex: "0xcafebabefacedbaddecaf888")

        let decGCM = GCM(iv: iv, authenticationTag: authenticationTag, mode: .detached)
        let aes = try! AES(key: key, blockMode: decGCM, padding: .noPadding)
        return try! aes.decrypt(cipherText).hexString
    }

}
