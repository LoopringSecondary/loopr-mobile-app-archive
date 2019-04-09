//
//  OrderDataManager.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/5/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import BigInt
import web3

// It's to all orders of an address.
class OrderDataManager {

    static let shared = OrderDataManager()
    let Eip191Header = "\u{0019}\u{0001}"
    let Eip712DomainHash = "0xaea25658c273c666156bd427f83a666135fcde6887a6c25fc1cd1562bc4f3f34"
    let Eip712OrderSchemaHash = "0x40b942178d2a51f1f61934268590778feb8114db632db7d88537c98d2b05c5f2"

    var errorInfo: [String: Any] = [:]
    var balanceInfo: [String: Double] = [:]

    let gasManager = GasDataManager.shared
    let tokenManager = TokenDataManager.shared
    let walletManager = CurrentAppWalletDataManager.shared
    let sendManager = SendCurrentAppWalletDataManager.shared

    // Similar naming in Trade.swift
    var baseToken: String = "LRC"
    var quoteToken: String = "WETH"
    var market: Market?

    func new(baseToken: String, quoteToken: String, market: Market) {
        self.baseToken = baseToken
        self.quoteToken = quoteToken
        self.market = market
    }

    var orders: [RawOrder] = []

    init() {
        orders = []
    }

    func getOrders(orderStatuses: [OrderStatus]? = nil) -> [RawOrder] {
        guard let orderStatuses = orderStatuses else {
            return orders
        }
        let filteredOrder = orders.filter { (order) -> Bool in
            orderStatuses.contains(order.state.status)
        }
        return filteredOrder
    }

    func getOrders(token: String? = nil) -> [RawOrder] {
        guard let token = token else {
            return orders
        }
        return orders.filter { (order) -> Bool in
            return order.tokenSell == token || order.tokenBuy == token
        }
    }

    func getOrdersFromServer(cursor: UInt, size: UInt = 20, statuses: [OrderStatus]? = nil, completionHandler: @escaping (_ error: Error?) -> Void) {
        if let owner = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.address {
            LoopringAPIRequest.getOrders(owner: owner, statuses: statuses, cursor: cursor, size: size) { result, error in
                guard let orders = result?.orders, error == nil else {
                    self.orders = []
                    completionHandler(error)
                    return
                }
                if cursor == 1 {
                    self.orders = orders
                } else {
                    self.orders += orders
                }
                completionHandler(error)
            }
        }
    }

    func constructOrder(side: OrderSide, amountBuy: Double, amountSell: Double, validSince: Int, validUntil: Int) -> RawOrder? {

        var result: RawOrder?
        var tokenB, tokenBuy, tokenS, tokenSell, amountB, amountS: String
        let address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address

        if let baseToken = tokenManager.getTokenBySymbol(baseToken),
           let quoteToken = tokenManager.getTokenBySymbol(quoteToken) {
            if side == .buy {
                tokenBuy = baseToken.symbol; tokenB = baseToken.address
                tokenSell = quoteToken.symbol; tokenS = quoteToken.address
                amountB = BigInt.generate(from: amountBuy, by: baseToken.decimals).toHexWithPrefix()
                amountS = BigInt.generate(from: amountSell, by: quoteToken.decimals).toHexWithPrefix()
            } else {
                tokenBuy = quoteToken.symbol; tokenB = quoteToken.address
                tokenSell = baseToken.symbol; tokenS = baseToken.address
                amountB = BigInt.generate(from: amountBuy, by: quoteToken.decimals).toHexWithPrefix()
                amountS = BigInt.generate(from: amountSell, by: baseToken.decimals).toHexWithPrefix()
            }

            let orderParams = OrderParams()
            orderParams.validUntil = validUntil
            let erc1400Params = ERC1400Params()
            let feeParams = FeeParams()
            let lrcFee = getLrcFee(amountSell, tokenSell)
            feeParams.tokenF = "LRC"
            feeParams.tokenFee = tokenManager.getAddress(by: "LRC")!
            feeParams.amountF = lrcFee
            feeParams.amountFee = BigInt.generate(from: lrcFee, by: 18).toHexWithPrefix()
            feeParams.tokenRecipient = address

            var order = RawOrder()
            order.owner = address; order.version = 0
            order.tokenB = tokenB; order.tokenBuy = tokenBuy
            order.tokenS = tokenS; order.tokenSell = tokenSell
            order.amountBuy = amountBuy; order.amountB = amountB
            order.amountSell = amountSell; order.amountS = amountS
            order.validSince = validSince
            order.params = orderParams
            order.feeParams = feeParams
            order.erc1400Params = erc1400Params

            result = completeOrder(&order)
        }
        return result
    }

    func completeOrder(_ order: inout RawOrder) -> RawOrder? {
        let orderData = getOrderHash(order: order)
        SendCurrentAppWalletDataManager.shared._keystore()
        if case (let signature?, let hash?) = web3swift.sign(message: orderData) {
            order.hash = hash
            order.r = signature.r
            order.s = signature.s
            order.v = UInt(signature.v)!
        }
    }

    func getOrderHash(order: RawOrder) -> Data {
        var bitStream: BitStream = BitStream()
        let orderParams = order.params
        let feeParams = order.feeParams
        let erc1400Params = order.erc1400Params

        let transferDataHash = erc1400Params.transferDataS.sha3(.keccak256).hexString
        try! bitStream.addBytes32(Eip712OrderSchemaHash, forceAppend: true)
        bitStream.addUint(order.amountS.toBigInt!, forceAppend: true);
        bitStream.addUint(order.amountB.toBigInt!, forceAppend: true);
        bitStream.addUint(feeParams.amountFee.toBigInt!, forceAppend: true);
        bitStream.addUint(BigInt(order.validSince), forceAppend: true);
        bitStream.addUint(BigInt(order.params.validUntil), forceAppend: true);
        bitStream.addAddress(order.owner, 32, forceAppend: true);
        bitStream.addAddress(order.tokenS, 32, forceAppend: true);
        bitStream.addAddress(order.tokenB, 32, forceAppend: true);
        bitStream.addAddress(orderParams.dualAuthAddr, 32, forceAppend: true);
        bitStream.addAddress(orderParams.broker, 32, forceAppend: true);
        bitStream.addAddress(orderParams.orderInterceptor, 32, forceAppend: true);
        bitStream.addAddress(orderParams.wallet, 32, forceAppend: true);
        bitStream.addAddress(feeParams.tokenRecipient, 32, forceAppend: true);
        bitStream.addAddress(feeParams.tokenFee, forceAppend: true);
        bitStream.addUint(BigInt(feeParams.walletSplitPercentage), forceAppend: true);
        bitStream.addUint(BigInt(feeParams.tokenSFeePercentage), forceAppend: true);
        bitStream.addUint(BigInt(feeParams.tokenBFeePercentage), forceAppend: true);
        bitStream.addBool(orderParams.allOrNone, forceAppend: true);
        bitStream.addUint(BigInt(erc1400Params.tokenStandardS), forceAppend: true);
        bitStream.addUint(BigInt(erc1400Params.tokenStandardB), forceAppend: true);
        bitStream.addUint(BigInt(erc1400Params.tokenStandardFee), forceAppend: true);
        try! bitStream.addBytes32(erc1400Params.trancheS, forceAppend: true);
        try! bitStream.addBytes32(erc1400Params.trancheB, forceAppend: true);
        try! bitStream.addBytes32(transferDataHash, forceAppend: true);
        
        let orderDataHash: String = bitStream.getBytes().sha3(.keccak256).hexString
        let outerStream: BitStream = BitStream()
        outerStream.addHex(Eip191Header.hexString, forceAppend: true)
        try! outerStream.addBytes32(Eip712DomainHash, forceAppend: true)
        try! outerStream.addBytes32(orderDataHash, forceAppend: true)
        return outerStream.getBytes().sha3(.keccak256).hexString
    }

    func getLrcFee(_ amountS: Double, _ tokenS: String) -> Double {
        var result: Double = 0
        let ratio = SettingDataManager.shared.getLrcFeeRatio()
        if let price = PriceDataManager.shared.getPrice(of: tokenS),
           let lrcPrice = PriceDataManager.shared.getPrice(of: "LRC") {
            result = price * amountS * ratio / lrcPrice
        }
        let minLrc = GasDataManager.shared.getGasAmount(by: "eth_transfer", in: "LRC")
        return max(result, minLrc)
    }
}
