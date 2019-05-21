//
//  TokenBalance.swift
//  loopr-ios
//
//  Created by ruby on 4/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class TokenBalance {
    
    let token: String
    let balance: String
    let allowance: String
    let availableBalance: String
    let availableAlloawnce: String
    
    init(json: JSON) {
        token = json["token"].stringValue
        balance = json["balance"].stringValue
        allowance = json["allowance"].stringValue
        availableBalance = json["availableBalance"].stringValue
        availableAlloawnce = json["availableAlloawnce"].stringValue
    }

}
