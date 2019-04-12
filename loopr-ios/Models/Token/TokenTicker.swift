//
//  Depth.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/6.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class TokenTicker {
    let token: String
    let price: Double
    
    let volume24H: Double
    
    let percentChange1H: Double
    let percentChange24H: Double
    let percentChange7D: Double
    
    var percentChange1HString: String = ""
    var percentChange24HString: String = ""
    var percentChange7DString: String = ""

    init(json: JSON) {
        self.token = json["token"].stringValue
        self.price = json["price"].doubleValue
        
        self.volume24H = json["volume24H"].doubleValue
        
        self.percentChange1H = json["percentChange1H"].doubleValue
        self.percentChange24H = json["percentChange24H"].doubleValue
        self.percentChange7D = json["percentChange7D"].doubleValue
        
        self.percentChange1HString = toString(json: json["percentChange1H"])
        self.percentChange24HString = toString(json: json["percentChange24H"])
        self.percentChange7DString = toString(json: json["percentChange7D"])
    }

    func toString(json: JSON) -> String {
        var result: String
        let value = json.doubleValue
        let numberFormatter = NumberFormatter()
        if value > 0 {
            result = "↑\(value)\(numberFormatter.percentSymbol)"
        } else {
            result = "↓\(value)\(numberFormatter.percentSymbol)"
        }
        return result
    }
}
