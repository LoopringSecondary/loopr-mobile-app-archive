//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class Amount {

    let value: String

    var block: Int

    init(json: JSON) {
        self.value = json["value"].stringValue
        self.block = json["block"].intValue
    }
}
