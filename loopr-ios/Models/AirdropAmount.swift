//
//  AirdropAmount.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/20.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class AirdropAmount {
    var script: String
    var state: String
    var gasConsumed: String
    var stack: [Stack]
    
    init(json: JSON) {
        self.script = json["script"].stringValue
        self.state = json["state"].stringValue
        self.gasConsumed = json["gas_consumed"].stringValue
        self.stack = []
        json["stack"].arrayValue.forEach { (subjson) in
            stack.append(Stack(json: subjson))
        }
    }
}

class Stack {
    var type: String
    var value: String
    
    init(json: JSON) {
        self.type = json["type"].stringValue
        self.value = json["value"].stringValue
    }
}
