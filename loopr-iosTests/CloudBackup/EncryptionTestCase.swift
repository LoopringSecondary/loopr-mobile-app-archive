//
//  EncryptionTestCase.swift
//  loopr-iosTests
//
//  Created by ruby on 3/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import XCTest
@testable import loopr_ios

class EncryptionTestCase: XCTestCase {

    func testCase1() {
        // The size of the key has to be 16 bytes (AES-128), 24 bytes (AES-192), 32 bytes (AES-256)
        let text1 = "loopringloopringloopringloopring123"
        let secretKey1 = Encryption.strengthenKey(plaintext: text1)
        XCTAssertEqual(secretKey1.count, 32)
        
        let (encrypted1, authenticationTag1) = Encryption.encryptEncoded(plainText: text1, userEnteredSecretKey: secretKey1)
        let decrypted1 = Encryption.decryptEncoded(cipherText: encrypted1, authenticationTag: authenticationTag1, userEnteredSecretKey: secretKey1)!.hexToString()
        XCTAssertEqual(decrypted1!, text1)
        
        let text2 = "hello world"
        let secretKey2 = Encryption.strengthenKey(plaintext: text2)
        XCTAssertEqual(secretKey2.count, 32)

        XCTAssertNotEqual(secretKey1, secretKey2)
        
        let (encrypted2, authenticationTag2) = Encryption.encryptEncoded(plainText: text2, userEnteredSecretKey: secretKey2)
        let decrypted2 = Encryption.decryptEncoded(cipherText: encrypted2, authenticationTag: authenticationTag2, userEnteredSecretKey: secretKey2)!.hexToString()
        XCTAssertEqual(decrypted2!, text2)
    }

}
