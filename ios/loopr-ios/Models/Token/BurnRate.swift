//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class BurnRate {
    let forMarket: Double
    let forP2P: Double

    init(json: JSON) {
        self.forMarket = json["forMarket"].doubleValue
        self.forP2P = json["forP2P"].doubleValue
    }
}
