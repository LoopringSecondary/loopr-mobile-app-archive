//
//  MinedRing.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

// TODO: remove?
class MinedRing {

    let id: UInt
    let ringhash: String
    let tradeAmount: UInt
    let miner: String
    let feeRecepient: String
    let txHash: String
    let blockNumber: UInt
    let totalLrcFee: String
    let address: String
    let isRinghashReserved: Bool
    let ringIndex: String
    let timestamp: UInt

    init(json: JSON) {
        self.id = json["id"].uIntValue
        self.ringhash = json["ringhash"].stringValue
        self.tradeAmount = json["tradeAmount"].uIntValue
        self.miner = json["miner"].stringValue
        self.feeRecepient = json["feeRecepient"].stringValue
        self.txHash = json["txHash"].stringValue
        self.blockNumber = json["blockNumber"].uIntValue
        self.totalLrcFee = json["totalLrcFee"].stringValue
        self.address = json["address"].stringValue
        self.isRinghashReserved = json["isRinghashReserved"].boolValue
        self.ringIndex = json["ringIndex"].stringValue
        self.timestamp = json["timestamp"].uIntValue
    }
}
