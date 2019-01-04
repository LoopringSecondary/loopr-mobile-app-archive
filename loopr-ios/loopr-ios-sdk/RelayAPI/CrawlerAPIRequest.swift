//
//  eth_JSON_RPC.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/8/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class CrawlerAPIRequest {
    
    // READY  获取  资讯  内容
    static func get(token: String, language: Language, category: NewsCategory, pageIndex: UInt = 0, pageSize: UInt = 50, completion: @escaping (_ response: [News], _ error: Error?) -> Void) {
        
        var body: JSON = JSON()
        body["id"] = JSON(UUID().uuidString)
        body["jsonrpc"] = JSON("2.0")
        body["method"] = JSON("queryNews")
        body["params"] = [["currency": token, "language": language.name, "category": category.rawValue, "pageIndex": pageIndex, "pageSize": pageSize]]
        
        Request.post(body: body, url: RelayAPIConfiguration.crawlerURL, showFailureBannerNotification: true) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                completion([], error)
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                var newsArray: [News] = []
                for subJson in offerData["data"].arrayValue {
                    if let news = News(json: subJson, category: category) {
                        newsArray.append(news)
                    }
                }
                completion(newsArray, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: NewsCategory.information.description, code: 0, userInfo: userInfo)
                completion([], error)
            }
        }
    }
    
    // READY  点击利好按钮
    static func confirmBull(uuid: String, completion: @escaping (_ response: IndexResult?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = JSON("updateIndex")
        body["id"] = JSON(UUID().uuidString)
        body["jsonrpc"] = JSON("2.0")
        body["params"] = [["uuid": uuid, "indexName": IndexType.bullIndex.description, "direction": IndexAction.confirm.rawValue]]
        
        Request.post(body: body, url: RelayAPIConfiguration.crawlerURL, showFailureBannerNotification: true) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                let indexResult = IndexResult(json: offerData)
                completion(indexResult, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: "confirmBull", code: 0, userInfo: userInfo)
                completion(nil, error)
            }
        }
    }
    
    // READY  取消利好按钮
    static func cancelBull(uuid: String, completion: @escaping (_ response: IndexResult?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = JSON("updateIndex")
        body["id"] = JSON(UUID().uuidString)
        body["jsonrpc"] = JSON("2.0")
        body["params"] = [["uuid": uuid, "indexName": IndexType.bullIndex.description, "direction": IndexAction.cancel.rawValue]]
        
        Request.post(body: body, url: RelayAPIConfiguration.crawlerURL, showFailureBannerNotification: true) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                let indexResult = IndexResult(json: offerData)
                completion(indexResult, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: "cancelBull", code: 0, userInfo: userInfo)
                completion(nil, error)
            }
        }
    }
    
    // READY  点击利空按钮
    static func confirmBear(uuid: String, completion: @escaping (_ response: IndexResult?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = JSON("updateIndex")
        body["id"] = JSON(UUID().uuidString)
        body["jsonrpc"] = JSON("2.0")
        body["params"] = [["uuid": uuid, "indexName": IndexType.bearIndex.description, "direction": IndexAction.confirm.rawValue]]
        
        Request.post(body: body, url: RelayAPIConfiguration.crawlerURL, showFailureBannerNotification: true) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                let indexResult = IndexResult(json: offerData)
                completion(indexResult, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: "confirmBear", code: 0, userInfo: userInfo)
                completion(nil, error)
            }
        }
    }
    
    // READY  取消利空按钮
    static func cancelBear(uuid: String, completion: @escaping (_ response: IndexResult?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = JSON("updateIndex")
        body["id"] = JSON(UUID().uuidString)
        body["jsonrpc"] = JSON("2.0")
        body["params"] = [["uuid": uuid, "indexName": IndexType.bearIndex.description, "direction": IndexAction.cancel.rawValue]]
        
        Request.post(body: body, url: RelayAPIConfiguration.crawlerURL, showFailureBannerNotification: true) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                let indexResult = IndexResult(json: offerData)
                completion(indexResult, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: "cancelBear", code: 0, userInfo: userInfo)
                completion(nil, error)
            }
        }
    }
    
    // READY  确认转发按钮
    static func confirmForward(uuid: String, completion: @escaping (_ response: IndexResult?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = JSON("updateIndex")
        body["id"] = JSON(UUID().uuidString)
        body["jsonrpc"] = JSON("2.0")
        body["params"] = [["uuid": uuid, "indexName": IndexType.forwardNum.description, "direction": IndexAction.confirm.rawValue]]
        
        Request.post(body: body, url: RelayAPIConfiguration.crawlerURL, showFailureBannerNotification: true) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                let indexResult = IndexResult(json: offerData)
                completion(indexResult, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: "confirmForward", code: 0, userInfo: userInfo)
                completion(nil, error)
            }
        }
    }
    
    // READY  获取blog信息 最多每次5条博客，官网爬取
    static func getBlogs(completion: @escaping (_ response: [Blog]?, _ error: Error?) -> Void) {
        var body: JSON = JSON()
        body["method"] = JSON("queryScrollingInfo")
        body["id"] = JSON(UUID().uuidString)
        body["jsonrpc"] = JSON("2.0")
        body["params"] = []
        
        Request.post(body: body, url: RelayAPIConfiguration.crawlerURL, showFailureBannerNotification: true) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let offerData = json["result"]
            if offerData != JSON.null {
                var blogArray: [Blog] = []
                for subJson in offerData["data"].arrayValue {
                    let blog = Blog(json: subJson)
                    blogArray.append(blog)
                }
                completion(blogArray, nil)
            } else if json["error"] != JSON.null {
                var userInfo: [String: Any] = [:]
                userInfo["code"] = json["error"]["code"]
                userInfo["message"] = json["error"]["message"]
                let error = NSError(domain: "confirmForward", code: 0, userInfo: userInfo)
                completion(nil, error)
            }
        }
    }
    
}
