//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class ERC1400Params {

    var tokenStandardS: Int = 0

    var tokenStandardB: Int = 0

    var tokenStandardFee: Int = 0

    var trancheS: String = ""

    var trancheB: String = ""

    var transferDataS: String = ""

    init() {

    }

    init(json: JSON) {
        self.tokenStandardS = json["tokenStandardS"].intValue
        self.tokenStandardB = json["tokenStandardB"].intValue
        self.tokenStandardFee = json["tokenStandardFee"].intValue
        self.trancheS = json["trancheS"].stringValue
        self.trancheB = json["trancheB"].stringValue
        self.transferDataS = json["transferDataS"].stringValue
    }

    func toJson() -> JSON {
        return JSON()
    }
}
