//
//  ASE_GCM.swift
//  loopr-ios
//
//  Created by ruby on 3/8/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation
import CryptoSwift

class Encryption {

    // Why we need to a method to strengthen the key https://stackoverflow.com/questions/18122192/custom-string-to-128-bit-string
    // It should always return 32 bytes array
    class func strengthenKey(plaintext: String) -> [UInt8] {
        let password: Array<UInt8> = Array(plaintext.utf8)
        let key = try! PKCS5.PBKDF2(password: password, salt: EncryptionConfigV1.PBKDF2ConfigV1.salt, iterations: EncryptionConfigV1.PBKDF2ConfigV1.iterations, variant: EncryptionConfigV1.PBKDF2ConfigV1.variant).calculate()
        return key
    }

    class func encryptEncoded(plainText: String, userEnteredSecretKey secretKey: [UInt8]) -> ([UInt8], [UInt8]) {
        guard plainText.hexString() != "" && secretKey.count > 0 else {
            return ([], [])
        }

        // AES-128, AES-192, AES-256 is defined by the length of thkey
        let encGCM = GCM(iv: EncryptionConfigV1.AESGCMConfigV1.iv, mode: EncryptionConfigV1.AESGCMConfigV1.mode)
        let aes = try! AES(key: secretKey, blockMode: encGCM, padding: .noPadding)
        let encrypted = try! aes.encrypt(plainText.hexString().hexBytes)
        print(encrypted)
        return (encrypted, encGCM.authenticationTag!)
    }

    class func decryptEncoded(cipherText: [UInt8], authenticationTag: [UInt8], userEnteredSecretKey secretKey: [UInt8]) -> String? {
        let decGCM = GCM(iv: EncryptionConfigV1.AESGCMConfigV1.iv, authenticationTag: authenticationTag, mode: EncryptionConfigV1.AESGCMConfigV1.mode)
        let aes = try! AES(key: secretKey, blockMode: decGCM, padding: .noPadding)
        return try! aes.decrypt(cipherText).hexString
    }

}
