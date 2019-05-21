//
//  Ring.swift
//  loopr-ios
//
//  Created by ruby on 4/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class Ring {
    
    var ringHash: String = ""
    
    var ringIndex = 0

    var fillsAmount = 0
    
    var miner: String = ""
    
    var txHash: String = ""
    
    var fees: [FeeParams] = []
    
    var blockHeight = 0
    
    var blockTimestamp: Int = 0

    init(json: JSON) {
        ringHash = json["ringHash"].stringValue
        ringIndex = json["ringIndex"].intValue
        fillsAmount = json["fillsAmount"].intValue
        miner = json["miner"].stringValue
        txHash = json["txHash"].stringValue
        fees = []
        let arrayData = json["fees"].arrayValue
        for subJson in arrayData {
            let fee = FeeParams(json: subJson)
            fees.append(fee)
        }
        blockHeight = json["blockHeight"].intValue
        blockTimestamp = json["blockTimestamp"].intValue
    }

}
