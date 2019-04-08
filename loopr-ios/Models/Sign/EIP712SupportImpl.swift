//
//  EIP712Support.swift
//  loopr-ios
//
//  Created by kenshin on 2019/4/8.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

func findTypeDependencies(targetType: String, allTypes: Types, results: inout [Type712]) -> [Type712] {
    if let typeDef = allTypes.types[targetType] {
        return typeDef.typeItems.flatMap { (item) -> [Type712] in
            results.insert(typeDef, at: 0)
            var typeDeps = findTypeDependencies(targetType: item.itemType, allTypes: allTypes, results: &results)
            typeDeps.insert(typeDef, at: 0)
            return typeDeps
        }.unique
    } else {
        return results
    }
}
