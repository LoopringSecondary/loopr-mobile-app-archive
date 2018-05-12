//
//  Order.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class Order {
    
    let originalOrder: OriginalOrder
    let orderStatus: OrderStatus
    let dealtAmountB: Double
    let dealtAmountS: Double
    let price: Double
    let tradingPairDescription: String

    init(originalOrder: OriginalOrder, orderStatus: OrderStatus, dealtAmountB: String, dealtAmountS: String) {
        self.originalOrder = originalOrder
        self.orderStatus = orderStatus
        self.dealtAmountB = Asset.getAmount(of: originalOrder.tokenBuy, fromWeiAmount: dealtAmountB) ?? 0.0
        self.dealtAmountS = Asset.getAmount(of: originalOrder.tokenSell, fromWeiAmount: dealtAmountS) ?? 0.0
        if originalOrder.side == "sell" {
            price = originalOrder.amountBuy / originalOrder.amountSell
        } else if originalOrder.side == "buy" {
            price = originalOrder.amountSell / originalOrder.amountBuy
        } else {
            price = 0.0
        }
        
        if originalOrder.tokenBuy == "WETH" {
            tradingPairDescription = "\(originalOrder.tokenSell)/\(originalOrder.tokenBuy)"
        } else {
            tradingPairDescription = "\(originalOrder.tokenBuy)/\(originalOrder.tokenSell)"
        }
    }
}
