//
//  eth_JSON_RPC.swift
//  loopr-iosTests
//
//  Created by xiaoruby on 2/9/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import XCTest
@testable import loopr_ios

class EthereumAPIRequestTests: XCTestCase {
    
    let testAddress = "0x8311804426a24495bd4306daf5f595a443a52e32"
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testCall() {
        let expectation = XCTestExpectation()
        EthereumAPIRequest.eth_call(from: nil, to: "0x98C9D14a894d19a38744d41CD016D89Cf9699a51", gas: nil, gasPrice: nil, value: nil, data: "0x70a082310000000000000000000000004c44d51cf0d35172fce9d69e2beac728de980e9d", block: BlockTag.latest) { data, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            XCTAssertNotNil(data)
            print("\nresult: \(data!.respond)\n")
            expectation.fulfill()
        }
        
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetTransactionCount() {
        let expectation = XCTestExpectation()
        EthereumAPIRequest.eth_getTransactionCount(data: testAddress, block: BlockTag.pending) { data, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            XCTAssertNotNil(data)
            print("\nresult: \(data!.respond)\n")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGasPrice() {
        let expectation = XCTestExpectation()
        EthereumAPIRequest.eth_gasPrice { data, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            XCTAssertNotNil(data)
            print("\nresult: \(data!.respond)\n")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testEstimateGas() {
        let expectation = XCTestExpectation()
        EthereumAPIRequest.eth_estimateGas(from: nil, to: testAddress, gas: nil, gasPrice: nil, value: nil, data: "0x095ea7b30000000000000000000000004c44d51cf0d35172fce9d69e2beac728de980e9d0000000000000000000000000000000000000000000000000de0b6b3a7640000") { data, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(data)
            print("\nresult: \(data!.respond)\n")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    // valid data makes test pass, otherwise return nonce too low error
    func _testSendRawTransaction() {
        let expectation = XCTestExpectation()
        EthereumAPIRequest.eth_sendRawTransaction(data: "0xf8698201798504e3b292008252089444b97fc8befe2ce2f2a776c648e33da4816b01f6018083d8e4e9a0966828a54a0a68aa5dcdd250da3545bd1130511369a3cef86e3a35e4f1fcd752a07acf6701331a8719beafa6484ee35173fd997c9b60e538a638b447cd92c2e06c") { data, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            XCTAssertNotNil(data)
            print("\nresult: \(data!.respond)\n")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetTransactionByHash() {
        let expectation = XCTestExpectation()
        EthereumAPIRequest.eth_getTransactionByHash(data: "0x9e47f07489795b1eada67765e52bd300af1616bb7aa5f0cd0c134ec6ad100b39") { data, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(data)
            let transaction = data! as ETH_Transaction
            print("\ntransaction_hash: \(transaction.hash)\n")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
}
