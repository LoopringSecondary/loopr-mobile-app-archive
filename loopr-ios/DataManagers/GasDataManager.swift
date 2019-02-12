//
//  GasDataManager.swift
//  loopr-ios
//
//  Created by kenshin on 2018/5/4.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation
import Geth

struct GasLimit {
    let type: String
    let gasLimit: Int64
    init(json: JSON) {
        self.type = json["type"].stringValue
        self.gasLimit = json["gasLimit"].int64Value
    }
}

class GasDataManager {
    
    static let shared = GasDataManager()
    
    private var gasRecommendedPrice: Double // gwei
    private var gasPrice: Double // gwei
    private var gasLimits: [GasLimit]
    private var gasAmount: Double
    
    private init() {
        self.gasRecommendedPrice = 10
        self.gasPrice = 10  // Set the default value to 10
        self.gasAmount = 0
        self.gasLimits = []
        self.loadGasLimitsFromJson()
    }

    func getGasLimits() -> [GasLimit] {
        return gasLimits
    }
    
    // TODO: Why we need to load gas_limit from a json file instead of writing as code.
    // load
    func loadGasLimitsFromJson() {
        gasLimits = []
        if let path = Bundle.main.path(forResource: "gas_limit", ofType: "json") {
            let jsonString = try? String(contentsOfFile: path, encoding: String.Encoding.utf8)
            let json = JSON(parseJSON: jsonString!)
            for subJson in json.arrayValue {
                let gas = GasLimit(json: subJson)
                gasLimits.append(gas)
            }
        }
    }
    
    func getGasLimit(by type: String) -> Int64? {
        var gasLimit: Int64?
        for case let gas in gasLimits where gas.type.lowercased() == type.lowercased() {
            gasLimit = gas.gasLimit
            break
        }
        return gasLimit
    }
    
    func getGasAmountInETH(by type: String) -> Double {
        var result: Double = 0
        if let limit = getGasLimit(by: type) {
            result = self.gasPrice * Double(limit)
        } else {
            result = self.gasPrice * 20000
        }
        return result / 1000000000
    }
    
    func getGasAmount(by type: String, in token: String) -> Double {
        let gasInETH = getGasAmountInETH(by: type)
        let tradingPair = "\(token)-WETH"
        let price = MarketDataManager.shared.getBalance(of: tradingPair)
        return gasInETH / price
    }
    
    // TODO: This part should be an async API call.
    func getEstimateGasPrice(completionHandler: @escaping (_ gasPrice: Double, _ error: Error?) -> Void) {
        LoopringAPIRequest.getEstimateGasPrice { (gasPrice, error) in
            guard error == nil && gasPrice != nil else {
                completionHandler(self.gasPrice, nil)
                return
            }
            self.gasPrice = gasPrice! * 1000000000
            self.gasPrice.round()
            print("Estimate gas price: \(self.gasPrice)")
            
            // set gasRecommendedPrice
            self.gasRecommendedPrice = self.gasPrice

            let copyGasPrice = self.gasPrice
            completionHandler(copyGasPrice, nil)
        }
    }

    func getGasRecommendedPrice() -> Double {
        return self.gasRecommendedPrice
    }

    func getGasPriceInGwei() -> Double {
        return self.gasPrice
    }
    
    func setGasPrice(in gwei: Double) {
        self.gasPrice = gwei
    }

    func getGasPriceInWei() -> GethBigInt {
        let amountInWei = GethBigInt.convertGweiToWei(from: self.gasPrice)!
        return amountInWei
    }
}
