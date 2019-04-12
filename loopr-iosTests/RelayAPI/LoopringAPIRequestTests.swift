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
    
    func testGetMarket() {
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

}
