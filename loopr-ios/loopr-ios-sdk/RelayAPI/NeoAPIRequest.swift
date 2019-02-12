//
//  eth_JSON_RPC.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/8/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class NeoAPIRequest {
    
    static var param: String = {
        let path = Bundle.main.path(forResource: "neo", ofType: "json")!
        let jsonString = try? String(contentsOfFile: path, encoding: String.Encoding.utf8)
        let json = JSON(parseJSON: jsonString!)
        return json["param"].stringValue
    }()
    
    static var method: String = {
        let path = Bundle.main.path(forResource: "neo", ofType: "json")!
        let jsonString = try? String(contentsOfFile: path, encoding: String.Encoding.utf8)
        let json = JSON(parseJSON: jsonString!)
        return json["method"].stringValue
    }()

    static func invoke<T: Initable>(method: String, withBody body: inout JSON, _ completionHandler: @escaping (_ response: T?, _ error: Error?) -> Void) {
        body["method"] = JSON(method)
        body["jsonrpc"] = JSON("2.0")
        body["id"] = JSON(UUID().uuidString)
        
        Request.post(body: body, url: RelayAPIConfiguration.neoURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completionHandler(nil, error)
                return
            }
            
            // TODO: need to check the status code.
            var json = JSON(data)
            if json["result"] != JSON.null {
                completionHandler(T.init(json["result"]), nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: method, code: 0, userInfo: userInfo)
                completionHandler(nil, error)
            }
        }
    }
    
    // READY
    static func neo_getAmount(bindAddress: String, completion: @escaping (_ response: AirdropAmount?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        let key = "14\(bindAddress.hashFromAddress())\(param)"
        body["params"] = [JSON(key)]
        body["method"] = JSON("invokescript")
        body["jsonrpc"] = JSON("2.0")
        body["id"] = JSON(UUID().uuidString)
        
        Request.post(body: body, url: RelayAPIConfiguration.neoURL, showFailureBannerNotification: true) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                let airdropAmount = AirdropAmount(json: offerData)
                completion(airdropAmount, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: method, code: 0, userInfo: userInfo)
                completion(nil, error)
            }
        }
    }

    // READY
    static func neo_claimAmount(bindAddress: String, completion: @escaping (_ response: String?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        
        let stamp = UInt(Date().timeIntervalSince1970)
        let id = DateUtil.convertToDate(stamp, format: "yyyyMMddHHmm")
        let key = "d1013d1c\(id)0000\(bindAddress.hashFromAddress())\(method)"
        body["params"] = [JSON(key)]
        body["method"] = JSON("sendrawtransaction")
        body["jsonrpc"] = JSON("2.0")
        body["id"] = JSON(UUID().uuidString)
        Request.post(body: body, url: RelayAPIConfiguration.neoURL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                completion(offerData["txid"].string, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: method, code: 0, userInfo: userInfo)
                completion(nil, error)
            }
        }
    }
}
