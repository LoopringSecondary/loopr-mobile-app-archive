//
//  KeyStrengtheningTestCase.swift
//  loopr-iosTests
//
//  Created by ruby on 3/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import XCTest
@testable import loopr_ios

class KeyStrengtheningTestCase: XCTestCase {

    func testCase1() {
        let encryptedKey = Encryption.strengthenKey(plaintext: "loopringDEX")

        XCTAssertEqual(encryptedKey.count, 32)
        
        let expectedEncryptedKey: [UInt8] = [172, 123, 139, 19, 99, 92, 139, 131, 129, 29, 183, 103, 195, 15, 145, 177, 198, 116, 143, 219, 104, 227, 65, 131, 196, 115, 19, 82, 164, 194, 238, 46]
        XCTAssertEqual(encryptedKey, expectedEncryptedKey)
    }
    
    func testCase2() {
        let encryptedKey = Encryption.strengthenKey(plaintext: "ToTheMoon")
        
        XCTAssertEqual(encryptedKey.count, 32)
        print(encryptedKey)
        
        let expectedEncryptedKey: [UInt8] = [213, 144, 195, 31, 210, 185, 95, 133, 56, 17, 160, 56, 219, 22, 99, 139, 190, 55, 143, 138, 223, 193, 38, 225, 17, 83, 114, 24, 185, 224, 38, 143]
        XCTAssertEqual(encryptedKey, expectedEncryptedKey)
    }

}
