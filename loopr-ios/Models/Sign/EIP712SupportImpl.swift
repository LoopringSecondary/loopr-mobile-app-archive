//
//  EIP712Support.swift
//  loopr-ios
//
//  Created by kenshin on 2019/4/8.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation
import web3

class EIP712SupportImpl: EIP712Support {
    
    static let EIP191HeaderHex = "1901"
    
    func jsonToTypeData(json: String) -> EIP712TypedData {
        let root = JSON(json)
        var typesMap: [String: Type712] = [:]
        root["types"].dictionaryValue.forEach { (param) in
            let (key, value) = param
            let typeItems = value.arrayValue.map({ (item) -> TypeItem in
                return TypeItem(name: item["name"].stringValue, itemType: item["type"].stringValue)
            })
            let type = Type712(name: key, typeItems: typeItems)
            typesMap[key] = type
        }
        let types = Types(types: typesMap)
        let primaryType = root["primaryType"].stringValue
        let domain = root["domain"].dictionaryObject ?? [:]
        let message = root["message"].dictionaryObject ?? [:]
        return EIP712TypedData(types: types, primaryType: primaryType, domain: domain, message: message)
    }
    
    func getEIP712Message(typedData: EIP712TypedData) -> String {
        let domainHash = hashStruct("EIP712Domain", typedData.domain, typedData.types)
        let messageHash = hashStruct(typedData.primaryType, typedData.message, typedData.types)
        let source = [EIP712SupportImpl.EIP191HeaderHex, domainHash, messageHash]
            .map { (s) -> String in return s.drop0x() }.joined()
        return source.sha3(.keccak256)
    }
    
    func hashStruct(_ primaryType: String, _ data: [String: Any], _ typeDefs: Types) -> String {
        let encodedString: String = encodeData(dataType: primaryType, data: data, typeDefs: typeDefs)
        return encodedString.sha3(.keccak256)
    }
    
    func encodeData(dataType: String, data: [String: Any], typeDefs: Types) -> String {
        let dataTypeHash = hashType(dataType: dataType, allTypes: typeDefs).hexString
        
        var encodedValues = [dataTypeHash]
        
        let dataTypeItems = typeDefs.types[dataType]?.typeItems
        
        dataTypeItems?.forEach({ (typeItem) in
            if let value = data[typeItem.name] {
                var stringValue: String
                switch value {
                case let s as String:
                    stringValue = s.count > 0 ? s : "0x0"
                    break
                default:
                    stringValue = "0x0"
                    break
                }
                switch typeItem.itemType {
                case "string":
                    let valueHash = stringValue.sha3(.keccak256).hexString
                    encodedValues.insert(valueHash, at: 0)
                    break
                case "bytes":
                    let valueHash = stringValue.sha3(.keccak256)
                    encodedValues.insert(valueHash, at: 0)
                    break
                case let bytesn where bytesn.starts(with: "bytes") && bytesn.count > 5:
                    do {
                        let hexString = try ABIEncoder.encode(stringValue, forType: .FixedBytes(32)).hexString
                        encodedValues.insert(hexString, at: 0)
                    } catch {}
                    break
                case let uintType where uintType.starts(with: "uint"):
                    do {
                        let hexString = try ABIEncoder.encode(stringValue, forType: .FixedUInt(stringValue.count)).hexString
                        encodedValues.insert(hexString, at: 0)
                    } catch {}
                    break
                case let intType where intType.starts(with: "int"):
                    do {
                        let hexString = try ABIEncoder.encode(stringValue, forType: .FixedInt(stringValue.count)).hexString
                        encodedValues.insert(hexString, at: 0)
                    } catch {}
                    break
                case "address":
                    do {
                        let hexString = try ABIEncoder.encode(stringValue, forType: .FixedAddress).hexString
                        encodedValues.insert(hexString, at: 0)
                    } catch {}
                    break
                case "bool":
                    do {
                        let hexString = try ABIEncoder.encode(value as! String, forType: .FixedBool).hexString
                        encodedValues.insert(hexString, at: 0)
                    } catch {}
                    break
                case let structType where typeDefs.types.contains(where: { (arg) -> Bool in structType == arg.key }):
                    let a = value as! JSON
                    let s = encodeData(dataType: structType, data: a.dictionaryObject!, typeDefs: typeDefs)
                    encodedValues.insert(s, at: 0)
                    break
                default:
                    break
                }
            } else {
                print("error")
            }
        })
        return encodedValues.reversed().map({ $0.drop0x() }).joined()
    }
    
    func hashType(dataType: String, allTypes: Types) -> Data {
        let encodedTypeStr = encodeType(dataType: dataType, allTypes: allTypes)
        return encodedTypeStr.sha3(.keccak256).dataWithHexString()
    }
    
    func encodeType(dataType: String, allTypes: Types) -> String {
        var result: [Type712] = []
        let deps = findTypeDependencies(targetType: dataType, allTypes: allTypes, results: &result)
        let depsSorted = deps
            .sorted { $0.name > $1.name }
            .map { (t) -> String in
                t.name + "(" + t.typeItems
                    .map({ (item) -> String in
                        "\(item.itemType) \(item.name)"
                    }).joined(separator: ",") + ")"
            }.joined()
        return depsSorted
    }
    
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
}
