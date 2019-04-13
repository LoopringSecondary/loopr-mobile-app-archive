//
//  Activity.swift
//  loopr-ios
//
//  Created by ruby on 4/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class Activity {
    
    let owner: String
    let block: Int
    let txHash: String
    let activityType: ActivityType
    let timestamp: Int
    let fiatValue: Double
    let token: String
    let from: String
    let nonce: Int
    
    let txStatus: Transaction.TxStatus
    
    let tokenTransfer: TokenTransfer
    
    init(json: JSON) {
        owner = json["owner"].stringValue
        block = json["block"].intValue
        txHash = json["txHash"].stringValue
        
        // Avoid force wrap
        activityType = ActivityType(rawValue: json["activityType"].stringValue)!
        
        timestamp = json["timestamp"].intValue
        fiatValue = json["fiatValue"].doubleValue
        token = json["token"].stringValue
        from = json["from"].stringValue
        nonce = json["nonce"].intValue
        
        let txStatus = json["txStatus"].stringValue
        if txStatus == "TX_STATUS_PENDING" {
            self.txStatus = .pending
        } else if txStatus == "TX_STATUS_SUCCESS" {
            self.txStatus = .success
        } else {
            self.txStatus = .failed
        }
        
        let detail = json["detail"]
        tokenTransfer = TokenTransfer(address: detail["address"].stringValue, token: detail["token"].stringValue, amount: detail["amount"].stringValue)
    }
    
}
