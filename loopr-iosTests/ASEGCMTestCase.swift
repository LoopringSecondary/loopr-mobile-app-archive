//
//  ASEGCMTestCase.swift
//  loopr-iosTests
//
//  Created by ruby on 3/4/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import XCTest
import CryptoSwift

class ASEGCMTestCase: XCTestCase {

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    func testAESGCMTestCase1() {
        // Test Case 1
        let key = Array<UInt8>(hex: "0x00000000000000000000000000000000")
        let iv = Array<UInt8>(hex: "0x000000000000000000000000")
        
        let gcm = GCM(iv: iv, mode: .detached)
        let aes = try! AES(key: key, blockMode: gcm, padding: .noPadding)
        let encrypted = try! aes.encrypt([UInt8]())
        XCTAssertEqual(Array(encrypted), [UInt8](hex: "")) // C
        XCTAssertEqual(gcm.authenticationTag, [UInt8](hex: "58e2fccefa7e3061367f1d57a4e7455a")) // T (128-bit)
    }
    
    func testAESGCMTestCase2() {
        // Test Case 2
        let key = Array<UInt8>(hex: "0x00000000000000000000000000000000")
        let plaintext = Array<UInt8>(hex: "0x00000000000000000000000000000000")
        let iv = Array<UInt8>(hex: "0x000000000000000000000000")
        
        let gcm = GCM(iv: iv, mode: .detached)
        let aes = try! AES(key: key, blockMode: gcm, padding: .noPadding)
        let encrypted = try! aes.encrypt(plaintext)
        XCTAssertEqual(Array(encrypted), [UInt8](hex: "0388dace60b6a392f328c2b971b2fe78")) // C
        XCTAssertEqual(gcm.authenticationTag, [UInt8](hex: "ab6e47d42cec13bdf53a67b21257bddf")) // T (128-bit)
    }
    
    func testAESGCMTestCase3() {
        // Test Case 3
        let key = Array<UInt8>(hex: "0xfeffe9928665731c6d6a8f9467308308")
        let plaintext = Array<UInt8>(hex: "0xd9313225f88406e5a55909c5aff5269a86a7a9531534f7da2e4c303d8a318a721c3c0c95956809532fcf0e2449a6b525b16aedf5aa0de657ba637b391aafd255")
        let iv = Array<UInt8>(hex: "0xcafebabefacedbaddecaf888")
        
        let encGCM = GCM(iv: iv, mode: .detached)
        let aes = try! AES(key: key, blockMode: encGCM, padding: .noPadding)
        let encrypted = try! aes.encrypt(plaintext)
        
        XCTAssertNotNil(encGCM.authenticationTag)
        XCTAssertEqual(Array(encrypted), [UInt8](hex: "0x42831ec2217774244b7221b784d0d49ce3aa212f2c02a4e035c17e2329aca12e21d514b25466931c7d8f6a5aac84aa051ba30b396a0aac973d58e091473f5985")) // C
        XCTAssertEqual(encGCM.authenticationTag, [UInt8](hex: "0x4d5c2af327cd64a62cf35abd2ba6fab4")) // T (128-bit)
        // decrypt
        func decrypt(_ encrypted: Array<UInt8>, tag: Array<UInt8>) -> Array<UInt8> {
            let decGCM = GCM(iv: iv, authenticationTag: tag, mode: .detached)
            let aes = try! AES(key: key, blockMode: decGCM, padding: .noPadding)
            return try! aes.decrypt(encrypted)
        }
        
        let decrypted = decrypt(encrypted, tag: encGCM.authenticationTag!)
        XCTAssertEqual(decrypted, plaintext)
    }

}
