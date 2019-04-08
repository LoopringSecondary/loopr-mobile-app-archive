//
//  EIP712Support.swift
//  loopr-ios
//
//  Created by kenshin on 2019/4/8.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

struct TypeItem {
    var name: String
    var itemType: String
}

struct Type712: Hashable {

    var name: String
    var typeItems: [TypeItem]
    
    static func == (lhs: Type712, rhs: Type712) -> Bool {
        return lhs.name == rhs.name
    }
    
    var hashValue: Int {
        return name.description.hashValue
    }
}

struct Types {
    var types: [String: Type712]
}

struct EIP712TypedData {
    var types: [Types]
    var primaryType: String
    var domain: [String: Any]
    var message: [String: Any]
}

protocol EIP712Support: class {
    func jsonToTypeData(json: String) -> EIP712TypedData
    func getEIP712Message(typedData: EIP712TypedData) -> String
}
