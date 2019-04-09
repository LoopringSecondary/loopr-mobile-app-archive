//
//  OrderBookDataManager.swift
//  loopr-ios
//
//  Created by xiaoruby on 5/7/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit

// Get a list of orders for a market and group them by price.
// It's not binding to an address.
class MarketDepthDataManager {

    static let shared = MarketDepthDataManager()

    var market: String?
    private var sells: [OrderbookItem] = []
    private var buys: [OrderbookItem] = []
    
    private init() {
    }
    
    func getSells() -> [OrderbookItem] {
        return sells
    }
    
    func getBuys() -> [OrderbookItem] {
        return buys
    }

    func getOrderbookFromServer(market: String, completionHandler: @escaping (_ buyDepths: [OrderbookItem], _ sellDepths: [OrderbookItem], _ error: Error?) -> Void) {
        LoopringAPIRequest.getOrderBook(level: 8, size: 100, marketPair: MarketPair(baseToken: "0xef68e7c694f40c8202821edf525de3782458639f", quoteToken: "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2")) { (buyDepths, sellDepths, error) in
            guard buyDepths != nil && sellDepths != nil && error == nil else { return }
            self.market = market
            self.buys = buyDepths
            self.sells = sellDepths
            completionHandler(self.buys, self.sells, nil)
        }
    }

}
