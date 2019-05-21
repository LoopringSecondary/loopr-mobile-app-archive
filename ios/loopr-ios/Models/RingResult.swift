//
//  RingResult.swift
//  loopr-ios
//
//  Created by ruby on 4/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class RingResult {
    
    let rings: [Ring]
    let total: Int
    
    init(rings: [Ring], total: Int) {
        self.rings = rings
        self.total = total
    }
    
}
