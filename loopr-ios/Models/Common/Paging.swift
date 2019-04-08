//
//  Paging.swift
//  loopr-ios
//
//  Created by ruby on 3/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class Paging {

    let cursor: UInt
    let size: UInt

    init(cursor: UInt, size: UInt) {
        self.cursor = cursor
        self.size = size
    }

    init(json: JSON) {
        self.cursor = json["skip"].uIntValue
        self.size = json["size"].uIntValue
    }

    func toJSON() -> JSON {
        var json: JSON = JSON()
        json["skip"] = JSON(cursor)
        json["size"] = JSON(size)
        return json
    }
}
