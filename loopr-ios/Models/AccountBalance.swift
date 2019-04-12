//
//  AccountBalance.swift
//  loopr-ios
//
//  Created by ruby on 4/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class AccountBalance {
    
    let address: String
    
    init(json: JSON) {
        address = json["address"].stringValue
        // TODO: Add tokenBalanceMap
    }
}
