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
    
    func testGetTime() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getTime { (error) in
            expectation.fulfill()

        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetMarket_USD() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getMarkets(requireMetadata: true, requireTicker: true, quoteCurrencyForTicker: Currency.init(name: "USD"), marketPairs: []) { (markets, error) in
            
            XCTAssertTrue(markets!.count > 1)
            for market in markets! {
                XCTAssertTrue(market.metadata.priceDecimals > 0)
                XCTAssertTrue(market.metadata.orderbookAggLevels > 0)
                XCTAssertTrue(market.metadata.precisionForAmount > 0)
                XCTAssertTrue(market.metadata.precisionForTotal > 0)
            }
            
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetMarket_RMB() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getMarkets(requireMetadata: true, requireTicker: true, quoteCurrencyForTicker: Currency.init(name: "RMB"), marketPairs: []) { (markets, error) in
            
            XCTAssertTrue(markets!.count > 1)
            for market in markets! {
                XCTAssertTrue(market.metadata.priceDecimals > 0)
                XCTAssertTrue(market.metadata.orderbookAggLevels > 0)
                XCTAssertTrue(market.metadata.precisionForAmount > 0)
                XCTAssertTrue(market.metadata.precisionForTotal > 0)
            }
            
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetTokens_USD() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getTokens(requireMetadata: true, requireInfo: true, requirePrice: true, quoteCurrencyForPrice: Currency.init(name: "USD")) { (tokens, error) in
            XCTAssertTrue(tokens!.count > 1)
            for token in tokens! {
                XCTAssertTrue(token.metadata.decimals > 0)
                XCTAssertTrue(token.metadata.precision > 0)
                XCTAssertTrue(token.metadata.burnRate.forMarket > 0)
                XCTAssertTrue(token.metadata.burnRate.forP2P >= 0)
                
                XCTAssertTrue(token.info.circulatingSupply >= 0)
                XCTAssertTrue(token.info.totalSupply >= 0)
                XCTAssertTrue(token.info.maxSupply >= 0)
                XCTAssertTrue(token.info.cmcRank >= 0)
                XCTAssertTrue(token.info.icoRateWithEth >= 0)
                
                XCTAssertTrue(token.ticker.price >= 0)
                XCTAssertTrue(token.ticker.volume24H >= 0)
                XCTAssertTrue(token.ticker.percentChange1H >= 0)
                XCTAssertTrue(token.ticker.percentChange24H >= 0)
                XCTAssertTrue(token.ticker.percentChange7D >= 0)
            }
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetOrders() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getOrders(owner: "0xd65a23388d5d6f0b1ec52a7fc07a291c132d57ed") { (orderResult, error) in
            XCTAssertTrue(orderResult!.total > 0)
            let orders = orderResult!.orders
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetAccounts() {
        let addresses = ["0xd65a23388d5d6f0b1ec52a7fc07a291c132d57ed"]
        let tokens: [String] = []
        
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getAccounts(addresses: addresses, tokens: tokens, allTokens: true) { (assets, error) in
            
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetAccountNonce() {
        let expectation = XCTestExpectation()
        LoopringAPIRequest.getAccountNonce(address: "0xd65a23388d5d6f0b1ec52a7fc07a291c132d57ed") { (nonce, error) in
            XCTAssertTrue(nonce! >= 0)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }
    
    func testGetUserFills() {
        let expectation = XCTestExpectation()
        let marketPair = MarketPair(baseToken: "0xef68e7c694f40c8202821edf525de3782458639f", quoteToken: "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2")
        LoopringAPIRequest.getUserFills(owner: "0xd65a23388d5d6f0b1ec52a7fc07a291c132d57ed", marketPair: marketPair, paging: Paging(cursor: 0, size: 50)) { (userFills, error) in
            XCTAssertTrue(userFills.count >= 0)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 10.0)
    }

    func testGetMarketFills() {
        let expectation = XCTestExpectation()
        let marketPair = MarketPair(baseToken: "0xef68e7c694f40c8202821edf525de3782458639f", quoteToken: "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2")
        LoopringAPIRequest.getMarketFills(marketPair: marketPair) { (userFills, error) in
            XCTAssertTrue(userFills.count >= 0)
            expectation.fulfill()
        }
        wait(for: [expectation], timeout: 60)
    }
    
}
