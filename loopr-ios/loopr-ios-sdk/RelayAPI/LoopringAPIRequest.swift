//
//  JSON_RPC.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/4/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class LoopringAPIRequest {

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
        var body: JSON = JSON()
        body["method"] = "get_time"
        body["id"] = JSON(UUID().uuidString)
        
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
    
    static func getMarkets(requireMetadata: Bool, requireTicker: Bool, quoteCurrencyForTicker: Currency, completionHandler: @escaping (_ result: [Market]?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = "get_markets"
        body["params"] = ["requireMetadata": requireMetadata, "requireTicker": requireTicker, "quoteCurrencyForTicker": quoteCurrencyForTicker.name]
        body["id"] = JSON(UUID().uuidString)

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
                let market = Market(json: subJson)
                markets.append(market)
            }
            completionHandler(markets, error)
        }
    }

    static func getTokens(requireMetadata: Bool, requireInfo: Bool, requirePrice: Bool, quoteCurrencyForTicker: Currency, completionHandler: @escaping (_ result: [Token]?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = "get_tokens"
        body["params"] = ["requireMetadata": requireMetadata, "requireInfo": requireInfo, "requirePrice": requirePrice, "quoteCurrencyForTicker": quoteCurrencyForTicker.name]
        body["id"] = JSON(UUID().uuidString)

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

    static func getOrders(owner: String, statuses: [OrderStatus]? = nil, marketPair: MarketPair? = nil, side: OrderSide? = nil, sort: Sort? = nil, cursor: UInt = 0, size: UInt = 20, completionHandler: @escaping (_ order: OrderResult?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = "get_orders"
        body["params"] = ["owner": owner, "status": statuses?.map { $0.rawValue }, "marketPair": marketPair?.toJSON(), "side": side?.rawValue, "sort": sort?.rawValue, "paging": Paging(cursor: cursor, size: size).toJSON()]
        body["id"] = JSON(UUID().uuidString)

        Request.post(body: body, url: RelayAPIConfiguration.rpcURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler(nil, error)
                return
            }
            let json = JSON(data)["result"]
            let order = Order(json: json)
            completionHandler(order, nil)
        }
    }

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

}
