//
//  LoopringLightcoreAPIRequest.swift
//  loopr-ios
//
//  Created by ruby on 3/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class LoopringLightcoreAPIRequest {
    
    static func newJSON() -> JSON {
        var body: JSON = JSON()
        body["jsonrpc"] = "2.0"
        body["id"] = JSON(UUID().uuidString)
        return body
    }
    
    // Not ready
    public static func getAccounts(addresses: [String], tokens: [String], allTokens: Bool, completionHandler: @escaping (_ assets: [Asset], _ error: Error?) -> Void) {
        var body = newJSON()

        body["method"] = "get_accounts"
        body["params"] = [[
            "addresses": addresses,
            "tokens": tokens,
            "allTokens": allTokens
        ]]

        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler([], error)
                return
            }
            let json = JSON(data)
            var assets: [Asset] = []
            let offerData = json["result"]["tokens"]
            // print(offerData)
            for subJson in offerData.arrayValue {
                let asset = Asset(json: subJson)
                assets.append(asset)
            }
            
            completionHandler(assets, error)
        }
    }

    // Not ready
    public static func getAccountNonce(address: String) {
        var body = newJSON()

        body["method"] = "get_account_nonce"
        body["params"] = [[
            "address": address
        ]]
        
        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            
        }
    }
    
    // Not ready
    public static func getUserFills(owner: String, marketPair: MarketPair, sort: Sort, paging: Paging) {
        var body = newJSON()

        body["method"] = "get_user_fills"
        body["params"] = [[
            "owner": owner,
            "marketPair": marketPair.toJSON(),
            "sort": sort.rawValue,
            "paging": paging.toJSON()
        ]]

        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            
        }
    }
    
    // Not ready
    public static func getMarketFills(marketPair: MarketPair) {
        var body = newJSON()
        
        body["method"] = "get_market_fills"
        body["params"] = [[
            "marketPair": marketPair.toJSON()
        ]]

        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            
        }
    }
    
    // Not ready
    public static func getOrderBook(level: Int, size: Int, marketPair: MarketPair) {
        var body = newJSON()
        
        body["method"] = "get_order_book"
        body["params"] = [[
            "level": level,
            "size": size,
            "marketPair": marketPair.toJSON()
        ]]
        
        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            
        }
    }
    
    // Not ready
    public static func getRings(sort: Sort, paging: Paging) {
        var body = newJSON()
        
        body["method"] = "get_rings"
        body["params"] = [[
            "sort": sort.rawValue,
            "paging": paging.toJSON()
        ]]
        
        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            
        }
    }
    
    // Not ready
    public static func getActivities(owner: String, token: String, paging: Paging) {
        var body = newJSON()
        
        body["method"] = "get_activities"
        body["params"] = [[
            "owner": owner,
            "token": token,
            "paging": paging.toJSON()
        ]]
        
        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            
        }
    }
    
    // Not ready
    public static func getMarketHistory(marketPair: MarketPair, interval: String) {
        var body = newJSON()
        
        body["method"] = "get_market_history"
        body["params"] = [[
            "marketPair": marketPair.toJSON(),
            "interval": interval
        ]]
        
        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            
        }
    }

}
