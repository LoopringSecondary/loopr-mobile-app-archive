//
//  Trend.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/19.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class Trend {
    let amount: Double
    let close: Double
    let market: String
    let start: UInt
    let end: UInt
    let intervals: String
    let open: Double
    var low: Double
    let createTime: UInt
    var high: Double
    let vol: Double

	init(json: JSON) {
		self.amount = json["amount"].doubleValue
		self.close = json["close"].doubleValue
		self.market = json["market"].stringValue
		self.intervals = json["intervals"].stringValue
		self.open = json["open"].doubleValue
		self.low = json["low"].doubleValue
        self.high = json["high"].doubleValue
        self.vol = json["vol"].doubleValue
        self.start = json["start"].uIntValue
        self.end = json["end"].uIntValue
        self.createTime = json["createTime"].uIntValue
        
        // Have to tune the data to avoid abnormal market orders.
        if high > 1.25 * max(open, close) {
            high = 1.25 * max(open, close)
        }
        
        if low < 0.8 * min(open, close) {
            low = 0.8 * min(open, close)
        }
	}
}
