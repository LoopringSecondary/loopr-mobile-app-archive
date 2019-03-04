//
//  ERC20Functions.swift
//  web3swift
//
//  Created by Matt Marshall on 13/04/2018.
//  Copyright © 2018 Argent Labs Limited. All rights reserved.
//

import Foundation
import BigInt

enum ERC20Functions {
    struct name: ABIFunction {
        static let name = "name"
        let gasPrice: BigUInt? = nil
        let gasLimit: BigUInt? = nil
        var contract: EthereumAddress
        let from: EthereumAddress? = nil
        
        func encode(to encoder: ABIFunctionEncoder) throws {
        }
    }
    
    struct symbol: ABIFunction {
        static let name = "symbol"
        let gasPrice: BigUInt? = nil
        let gasLimit: BigUInt? = nil
        var contract: EthereumAddress
        let from: EthereumAddress? = nil
        
        func encode(to encoder: ABIFunctionEncoder) throws { }
    }
    
    struct decimals: ABIFunction {
        static let name = "decimals"
        let gasPrice: BigUInt? = nil
        let gasLimit: BigUInt? = nil
        var contract: EthereumAddress
        let from: EthereumAddress? = nil
        
        func encode(to encoder: ABIFunctionEncoder) throws { }
    }
    
    struct balanceOf: ABIFunction {
        static let name = "balanceOf"
        let gasPrice: BigUInt? = nil
        let gasLimit: BigUInt? = nil
        var contract: EthereumAddress
        let account: EthereumAddress
        let from: EthereumAddress? = nil
        
        func encode(to encoder: ABIFunctionEncoder) throws {
            try encoder.encode(account)
        }
    }

    struct bind: ABIFunction {
        static let name = "getBindingAddress"
        let gasPrice: BigUInt? = nil
        let gasLimit: BigUInt? = nil
        var contract: EthereumAddress
        let account: EthereumAddress
        let projectId: String
        let from: EthereumAddress? = nil
        
        func encode(to encoder: ABIFunctionEncoder) throws {
            do {
                try encoder.encode(account)
                try encoder.encode(type: ABIRawType.FixedUInt(8), value: projectId)
            } catch {
                print(11111)
            }
        }
    }

}

