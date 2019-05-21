//
//  TokenTransfer.swift
//  loopr-ios
//
//  Created by ruby on 4/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class TokenTransfer {

    let address: String
    let token: String
    let amount: String
    
    init(address: String, token: String, amount: String) {
        self.address = address
        self.token = token
        self.amount = amount
    }

}
