//
//  IndexResult.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/24.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class IndexResult {
    var uuid: String
    var bullIndex: Int
    var bearIndex: Int
    var forwardNum: Int
    
    init(json: JSON) {
        self.uuid = json["uuid"].stringValue
        self.bullIndex = json["bullIndex"].intValue
        self.bearIndex = json["bearIndex"].intValue
        self.forwardNum = json["forwardNum"].intValue
    }
}
