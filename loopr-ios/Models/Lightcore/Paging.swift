//
//  Paging.swift
//  loopr-ios
//
//  Created by ruby on 3/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class Paging {
    
    let skip: Int
    let size: Int
    
    init(skip: Int, size: Int) {
        self.skip = skip
        self.size = size
    }
    
    func toJSON() -> JSON {
        var json: JSON = JSON()
        json["skip"] = JSON(skip)
        json["size"] = JSON(size)
        return json
    }
    
}
