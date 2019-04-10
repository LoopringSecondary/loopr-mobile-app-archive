//
//  MarketOrderDataManager.swift
//  loopr-ios
//
//  Created by xiaoruby on 3/10/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import Geth
import BigInt

class MarketOrderDataManager: OrderDataManager {

    static let instance = MarketOrderDataManager()

    func getAllowance(of token: String) -> Double {
        var result: Double = 0
        // TODO
        return result
    }

    override func constructOrder(side: OrderSide, amountBuy: Double, amountSell: Double, validSince: Int, validUntil: Int) -> RawOrder? {
        let order = super.constructOrder(side: side, amountBuy: amountBuy, amountSell: amountSell, validSince: validSince, validUntil: validUntil)
        order?.orderType = .marketOrder
        return order
    }

    func checkLRCEnough(of order: RawOrder) {
        var result: Double = 0
        let lrcFrozen = 0.0 // TODO
        let lrcBalance = walletManager.getBalance(of: "LRC")!
        result = lrcBalance - order.feeParams.amountF! - lrcFrozen
        if result < 0 {
            balanceInfo["MINUS_LRC"] = -result
        }
    }

    func checkGasEnough(of order: RawOrder, includingLRC: Bool = true) {
        var result: Double = 0
        if let ethBalance = walletManager.getBalance(of: "ETH"),
           let tokenGas = calculateGas(for: order.tokenSell!, to: order.amountSell!, lrcFee: order.feeParams.amountF!) {
            if includingLRC {
                if let lrcGas = calculateGas(for: "LRC", to: order.amountSell!, lrcFee: order.feeParams.amountF!) {
                    result = ethBalance - lrcGas - tokenGas
                }
            } else {
                result = ethBalance - tokenGas
            }
        }
        if result < 0 {
            balanceInfo["MINUS_ETH"] = -result
        }
    }

    func checkLRCGasEnough(of order: RawOrder) {
        var result: Double = 0
        if let ethBalance = walletManager.getBalance(of: "ETH"),
           let lrcGas = calculateGasForLRC(of: order) {
            result = ethBalance - lrcGas
        }
        if result < 0 {
            balanceInfo["MINUS_ETH"] = -result
        }
    }

    func calculateGas(for token: String, to amount: Double, lrcFee: Double) -> Double? {
        var result: Double?
        if let asset = walletManager.getAsset(symbol: token) {
            if token.uppercased() == "LRC" {
                let lrcFrozen = 0.0
                let sellingFrozen = getAllowance(of: "LRC")
                if asset.allowance >= lrcFee + lrcFrozen + sellingFrozen {
                    return 0
                }
            } else {
                let tokenFrozen = getAllowance(of: token)
                if asset.allowance >= amount + tokenFrozen {
                    return 0
                }
            }
            let gasAmount = gasManager.getGasAmountInETH(by: "approve")
            if asset.allowance == 0 {
                result = gasAmount
                balanceInfo["GAS_\(asset.symbol)"] = 1
            } else {
                result = gasAmount * 2
                balanceInfo["GAS_\(asset.symbol)"] = 2
            }
        }
        return result
    }

    func calculateGasForLRC(of order: RawOrder) -> Double? {
        var result: Double?
        if let asset = walletManager.getAsset(symbol: "LRC"),
           let lrcFee = order.feeParams.amountF,
           let amountSell = order.amountSell {
            let lrcAllowance = asset.allowance
            let lrcFrozen = 0.0  // TODO
            let sellingFrozen = getAllowance(of: "LRC")
            if lrcFee + lrcFrozen + sellingFrozen + amountSell > lrcAllowance {
                let gasAmount = gasManager.getGasAmountInETH(by: "approve")
                if lrcAllowance == 0 {
                    result = gasAmount
                    balanceInfo["GAS_LRC"] = 1
                } else {
                    result = gasAmount * 2
                    balanceInfo["GAS_LRC"] = 2
                }
            } else {
                return 0
            }
        }
        return result
    }

    /*
     1. LRC FEE 比较的是当前订单lrc fee + getFrozenLrcfee() >< 账户lrc 余额 不够失败
     2. 如果够了，看lrc授权够不够，够则成功，如果不够需要授权是否等于=0，如果不是，先授权lrc = 0， 再授权lrc = max，是则直接授权lrc = max。看两笔授权支付的eth gas够不够，如果eth够则两次授权，不够失败
     3. 比较当前订单amounts + loopring_getEstimatedAllocatedAllowance() >< 账户授权tokens，够则成功，不够则看两笔授权支付的eth gas够不够，如果eth够则两次授权，不够失败
     如果是sell lrc，需要lrc fee + getFrozenLrcfee() + amounts(lrc) + loopring_getEstimatedAllocatedAllowance() >< 账户授权lrc
     4. buy lrc不看前两点，只要3满足即可
     */
    func verify(order: RawOrder) -> [String: Double] {
        balanceInfo = [:]
        if order.orderSide == .buy {
            if order.tokenBuy?.uppercased() == "LRC" {
                checkGasEnough(of: order, includingLRC: false)
            } else {
                checkLRCEnough(of: order)
                checkGasEnough(of: order)
            }
        } else {
            if order.tokenSell?.uppercased() == "LRC" {
                checkLRCEnough(of: order)
                checkLRCGasEnough(of: order)
            } else {
                checkLRCEnough(of: order)
                checkGasEnough(of: order)
            }
        }
        return balanceInfo
    }

    func _submitOrder(_ order: RawOrder, completion: @escaping (String?, Error?) -> Void) {
        LoopringAPIRequest.submitOrder(order: order, completionHandler: completion)
    }
}
