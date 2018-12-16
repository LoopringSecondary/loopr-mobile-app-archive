//
//  loopr_iosTests.swift
//  loopr-iosTests
//
//  Created by xiaoruby on 1/31/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import XCTest
@testable import loopr_ios

class LoopringAPIRequestTests: XCTestCase {
    
    let testAddress = "0x87c6117ef0935b1Df3f9D93D9b39516eB8141870"
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    // its ok to pass nil or real value to method, e.g. owner, same as below
    func testGetBalance() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getBalance(owner: testAddress) { assets, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            XCTAssertNotNil(assets)
            XCTAssertNotEqual(assets.count, 0)
            for asset in assets {
                print("\(asset.symbol): \(asset.balance)")
            }
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetOrders() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getOrders(owner: testAddress, orderType: "market_order") { orders, error in
            XCTAssertNotNil(orders)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetOrderByHash() {
        let expectation = XCTestExpectation()
        let orderHash = "0xa5e664b32e0e517537230fea1a0a07fb201589e1a5dc207c757b22de9a20a33f"
        LoopringAPIRequest.getOrderByHash(orderHash: orderHash) { order, error in
            XCTAssertNotNil(order)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }

    func testGetDepth() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getDepths(market: "LRC-WETH", length: 10) { buyDepths, sellDepths, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            XCTAssertNotNil(buyDepths)
            XCTAssertNotNil(sellDepths)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetMarkets() {
//        let expectation = XCTestExpectation()
//        LoopringAPIRequest.getMarkets() { markets, error in
//            guard error == nil else {
//                print("error=\(String(describing: error))")
//                return
//            }
//            XCTAssertNotEqual(markets.count, 0)
//            expectation.fulfill()
//        }
//        wait(for: [expectation], timeout: 10.0)
    }

    func testGetFills() {
        let expectation = XCTestExpectation()
        
        LoopringAPIRequest.getFills(market: "LRC-WETH", owner: nil, orderHash: nil, ringHash: nil) { trades, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(trades)
            XCTAssertNotEqual(trades!.count, 0)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetLatestFills() {
        let expectation = XCTestExpectation()
        
        LoopringAPIRequest.getLatestFills(market: "LRC-WETH") { orderFills, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(orderFills)
            XCTAssertNotEqual(orderFills!.count, 0)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    // getTrend may be updated.
    func _testGetTrend() {
        
    }

    func testGetRingMined() {
        let expectation = XCTestExpectation()
        let pageSize: UInt = 20
        LoopringAPIRequest.getRingMined(ringHash: nil, pageIndex: 0, pageSize: pageSize) { minedRings, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(minedRings)
            XCTAssertEqual(minedRings!.count, Int(pageSize))
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetCutoff() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getCutoff(address: testAddress) { date, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(date)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetPriceQuote() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getPriceQuote(currency: "USD") { price, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(price)
            price?.tokens.forEach({ (token) in
                print(token.symbol + " " + token.price.description)
            })
            print("\nprice.currency:\(price!.currency)\n")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetEstimatedAllocatedAllowance() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getEstimatedAllocatedAllowance(owner: testAddress, token: "WETH") { amount, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(amount)
            print(amount!)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetSupportedMarket() {
        let expectation = XCTestExpectation()
        
        LoopringAPIRequest.getSupportedMarket() { pairs, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            // If the relay support more tokens, we will know.
            XCTAssertNotNil(pairs)
            XCTAssertNotEqual(pairs!.count, 0)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetSupportedTokens() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getSupportedTokens(completionHandler: { (tokens, error) in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            // If the relay support more tokens, we will know.
            XCTAssertNotNil(tokens)
            XCTAssertNotEqual(tokens!.count, 0)
            expectation.fulfill()
        })
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetTransactions() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getTransactions(owner: testAddress, symbol: "WETH", txHash: nil, pageIndex: 1, pageSize: 20) { transactions, error in
            guard error == nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(transactions)
            XCTAssertNotEqual(transactions!.count, 0)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testUnlockWallet() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.unlockWallet(owner: "0xeE8b2b3aFDE5Fd69E7E0581EE702c202693e7B03") { result, error in
            guard error == nil && result != nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertEqual(result!, "unlock_notice_success")
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetPortfolio() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getPortfolio(owner: "0x329B97A3E8eC8a8c23D4bD54803e74A9eE66CbcB") { result, error in
            guard error == nil && result != nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            print(result!)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetFrozenLRCFee() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getFrozenLRCFee(owner: "0x329B97A3E8eC8a8c23D4bD54803e74A9eE66CbcB") { (frozenLRCFee, error) in
            guard error == nil && frozenLRCFee != nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(frozenLRCFee)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetEstimateGasPrice() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getEstimateGasPrice { (gasPrice, error) in
            guard error == nil && gasPrice != nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            XCTAssertNotNil(gasPrice)
            print(gasPrice!)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testCreatePartner() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.createPartner(owner: testAddress) { (result, error) in
            guard error == nil && result != nil else {
                print("error=\(String(describing: error))")
                XCTFail()
                return
            }
            print(result!)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testAddCustomToken() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.addCustomToken(owner: testAddress, tokenContractAddress: testAddress, symbol: "TEST", decimals: 18) { (result, error) in
            guard error == nil && result != nil else {
                print("error=\(String(describing: error))")
                expectation.fulfill()
                return
            }
            print(result!)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
}
