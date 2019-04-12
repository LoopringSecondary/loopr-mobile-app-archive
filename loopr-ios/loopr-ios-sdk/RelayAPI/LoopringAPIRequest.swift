//
//  JSON_RPC.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/4/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class LoopringAPIRequest {
    
    static func newJSON() -> JSON {
        var body: JSON = JSON()
        body["jsonrpc"] = "2.0"
        body["id"] = JSON(UUID().uuidString)
        return body
    }

    static func invoke<T: Initable>(method: String, withBody body: inout JSON, _ completionHandler: @escaping (_ response: T?, _ error: Error?) -> Void) {
        body["method"] = JSON(method)
        body["id"] = JSON(UUID().uuidString)
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler(nil, error)
                return
            }
            var json = JSON(data)
            if json["result"] != JSON.null {
                completionHandler(T.init(json["result"]), nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                let code = json["error"]["code"].intValue
                userInfo["message"] = json["error"]["message"].stringValue
                let error = NSError(domain: method, code: code, userInfo: userInfo)
                completionHandler(nil, error)
            }
        }
    }

    static func getTime(completionHandler: @escaping (_ error: Error?) -> Void) {
        var body: JSON = newJSON()
        body["method"] = "get_time"
        body["params"] = JSON()

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler(error)
                return
            }
            let json = JSON(data)
            print(json)
            completionHandler(error)
        }
    }

    static func getMarkets(requireMetadata: Bool, requireTicker: Bool, quoteCurrencyForTicker: Currency, marketPairs: [MarketPair], completionHandler: @escaping (_ result: [Market]?, _ error: Error?) -> Void) {
        var body: JSON = newJSON()
        body["method"] = "get_markets"

        // TODO: add marketPairs
        body["params"] = ["requireMetadata": requireMetadata, "requireTicker": requireTicker, "quoteCurrencyForTicker": quoteCurrencyForTicker.name]

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler([], error)
                return
            }
            let json = JSON(data)
            var markets: [Market] = []
            let offerData = json["result"]["markets"]
            for subJson in offerData.arrayValue {
                if let market = Market(json: subJson) {
                    markets.append(market)
                }
            }
            completionHandler(markets, error)
        }
    }

    static func getTokens(requireMetadata: Bool, requireInfo: Bool, requirePrice: Bool, quoteCurrencyForPrice: Currency, completionHandler: @escaping (_ result: [Token]?, _ error: Error?) -> Void) {
        var body: JSON = newJSON()
        body["method"] = "get_tokens"
        body["params"] = ["requireMetadata": requireMetadata, "requireInfo": requireInfo, "requirePrice": requirePrice, "quoteCurrencyForPrice": quoteCurrencyForPrice.name]

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler([], error)
                return
            }
            let json = JSON(data)
            var tokens: [Token] = []
            let offerData = json["result"]["tokens"]
            for subJson in offerData.arrayValue {
                let token = Token(json: subJson)
                tokens.append(token)
            }
            completionHandler(tokens, error)
        }
    }

    static func getOrders(owner: String, statuses: [OrderStatus] = [], marketPair: MarketPair? = nil, side: OrderSide = .both, sort: Sort = .ASC, cursor: UInt = 0, size: UInt = 20, completionHandler: @escaping (_ order: OrderResult?, _ error: Error?) -> Void) {
        var body: JSON = newJSON()
        body["method"] = "get_orders"
        body["params"] = ["owner": owner, "status": statuses.map { $0.rawValue }, "side": side.rawValue, "sort": sort.rawValue, "paging": Paging(cursor: cursor, size: size).toJSON()]
        if marketPair != nil {
            body["params"]["marketPair"] = marketPair!.toJSON()
        }

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler(nil, error)
                return
            }
            let json = JSON(data)["result"]
            let result = OrderResult(json: json)
            completionHandler(result, nil)
        }
    }

    // not test
    static func getOrdersByHash(hashes: [String], completionHandler: @escaping (_ orders: [RawOrder]?, _ error: Error?) -> Void) {
        var body = newJSON()
        body["method"] = "get_orders_by_hash"
        body["params"] = ["hashes": JSON(arrayLiteral: hashes)]

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler(nil, error)
                return
            }
            var orders: [RawOrder] = []
            let ordersJson = JSON(data)["result"]["orders"]
            for json in ordersJson.arrayValue {
                let order = RawOrder(json: json)
                orders.append(order)
            }
            completionHandler(orders, nil)
        }
    }

    // not test
    static func submitOrder(order: RawOrder, completionHandler: @escaping (_ result: String?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["params"] = ["rawOrder": order.toJson()]
        self.invoke(method: "submit_order", withBody: &body) { (_ data: SimpleRespond?, _ error: Error?) in
            guard error == nil && data != nil else {
                completionHandler(nil, error!)
                return
            }
            completionHandler(data!.respond, nil)
        }
    }

    // not test
    static func cancelOrders(param: OrderCancelParam, completionHandler: @escaping (_ result: String?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["params"] = param.toJson()
        self.invoke(method: "cancel_orders", withBody: &body) { (_ data: SimpleRespond?, _ error: Error?) in
            guard error == nil && data != nil else {
                completionHandler(nil, error!)
                return
            }
            completionHandler(data!.respond, nil)
        }
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

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler([], error)
                return
            }
            let json = JSON(data)
            var assets: [Asset] = []
            let offerData = json["result"]["accountBalances"]
            print(offerData)
            for subJson in offerData.arrayValue {
                let asset = Asset(json: subJson)
                assets.append(asset)
            }

            completionHandler(assets, error)
        }
    }

    public static func getAccountNonce(address: String, completionHandler: @escaping (_ nonce: Int?, _ error: Error?) -> Void) {
        var body = newJSON()
        body["method"] = "get_account_nonce"
        body["params"] = [[
            "address": address
            ]]

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler(0, error)
                return
            }
            let json = JSON(data)
            let nonce = json["result"]["nonce"].intValue
            completionHandler(nonce, error)
        }
    }

    public static func getUserFills(owner: String, marketPair: MarketPair, sort: Sort = .ASC, paging: Paging, completionHandler: @escaping (_ userFills: [UserFill], _ error: Error?) -> Void) {
        var body = newJSON()
        body["method"] = "get_user_fills"
        body["params"] = [[
            "owner": owner,
            "marketPair": marketPair.toJSON(),
            "sort": sort.rawValue,
            "paging": paging.toJSON()
            ]]

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler([], error)
                return
            }
            let json = JSON(data)
            var userFills: [UserFill] = []
            let arrayData = json["result"]["fills"]
            print(arrayData)
            for subJson in arrayData.arrayValue {
                let userFill = UserFill(json: subJson)
                userFills.append(userFill)
            }
            
            completionHandler(userFills, error)
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
    public static func getOrderBook(level: Int, size: Int, marketPair: MarketPair, completionHandler: @escaping (_ sells: [OrderbookItem], _ buys: [OrderbookItem], _ error: Error?) -> Void) {
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
    public static func getMarketHistory(marketPair: MarketPair, interval: MarketInterval) {
        var body = newJSON()

        body["method"] = "get_market_history"
        body["params"] = [[
            "marketPair": marketPair.toJSON(),
            "interval": interval.rawValue
            ]]

        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in

        }
    }

    public static func getGasPrice(completionHandler: @escaping (_ gasPrice: Double, _ error: Error?) -> Void) {
        var body = newJSON()

        body["method"] = "get_gas_price"
        body["params"] = [[
            ]]

        // TOOD
        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            completionHandler(0, error)
        }
    }

}
