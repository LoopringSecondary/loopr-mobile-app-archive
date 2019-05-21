//
//  MarketHistoryItem.swift
//  loopr-ios
//
//  Created by ruby on 4/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class MarketHistoryItem {
    
    let startingPoint: Double
    let quality: Double
    let amount: Double
    let openingPrice: Double
    let closingPrice: Double
    let highestPrice: Double
    let lowestPrice: Double
    
    init(startingPoint: Double, quality: Double, amount: Double, openingPrice: Double, closingPrice: Double, highestPrice: Double, lowestPrice: Double) {
        self.startingPoint = startingPoint
        self.quality = quality
        self.amount = amount
        self.openingPrice = openingPrice
        self.closingPrice = closingPrice
        self.highestPrice = highestPrice
        self.lowestPrice = lowestPrice
    }
    
}
