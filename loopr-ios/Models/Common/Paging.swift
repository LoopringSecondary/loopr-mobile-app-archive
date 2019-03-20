//
//  Paging.swift
//  loopr-ios
//
//  Created by ruby on 3/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class Paging {

    let skip: UInt
    let size: UInt

    init(skip: UInt, size: UInt) {
        self.skip = skip
        self.size = size
    }

    init(json: JSON) {
        self.skip = json["skip"].uIntValue
        self.size = json["size"].uIntValue
    }

    func toJSON() -> JSON {
        var json: JSON = JSON()
        json["skip"] = JSON(skip)
        json["size"] = JSON(size)
        return json
    }
}
