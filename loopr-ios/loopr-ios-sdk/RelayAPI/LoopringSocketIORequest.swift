//
//  LoopringWebSocketCaller.swift
//  loopr-ios
//
//  Created by kenshin on 2018/3/11.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation
import SocketIO

public class LoopringSocketIORequest {
    
    static let manager = SocketManager(socketURL: RelayAPIConfiguration.socketURL, config: [.compress, .forceWebsockets(true), .reconnects(true)])
    static let socket = manager.defaultSocket
    static var handlers: [String: [(JSON) -> Void]] = [:]
    
    static func setup() {
        if handlers.isEmpty {
            // add more requests using socketio here
            // handlers["balance_res"] = [CurrentAppWalletDataManager.shared.onBalanceResponse]
            handlers["marketcap_res"] = [PriceDataManager.shared.onPriceQuoteResponse]
            handlers["tickersOfSource_req"] = [MarketDataManager.shared.onTickerResponse]
            handlers["trends_res"] = [MarketDataManager.shared.onTrendResponse]
            handlers["orderTracing_res"] = [TradeDataManager.shared.onOrderResponse]
            addHandlers(handlers)
        }
        connect()
    }
    
    static func tearDown() {
        handlers = [:]
        socket.removeAllHandlers()
        disconnect()
    }
    
    static func connect() {
        if socket.status != .connected {
            socket.connect()
        }
    }
    
    static func disconnect() {
        if socket.status != .disconnected {
            socket.disconnect()
        }
    }
    
    static func addHandlers(_ handlers: [String: [(JSON) -> Void]]) {
        for (key, methods) in handlers {
            for method in methods {
                socket.on(key, callback: { (data, _) in
                    if let string = data[0] as? String {
                        if let json = try? JSON(data: string.data(using: .utf8)!) {
                            method(json["data"])
                        }
                    }
                })
            }
        }
    }
    
    // TODO: It's disable due to the socket io may cause race condition.
    static func getBalance(owner: String) {
        /*
        var body: JSON = JSON()
        body["owner"] = JSON(owner)
        body["delegateAddress"] = JSON(RelayAPIConfiguration.delegateAddress)
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("balance_req", body.rawString()!)
            }
        } else {
            self.socket.emit("balance_req", body.rawString()!)
        }
        */
    }

    static func endBalance() {
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("balance_end")
            }
        } else {
            self.socket.emit("balance_end")
        }
    }
    
    // TODO: It's disable due to the socket io may cause race condition.
    static func getPriceQuote(currency: String) {
        /*
        var body: JSON = JSON()
        body["currency"] = JSON(currency)
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("marketcap_req", body.rawString()!)
            }
        } else {
            self.socket.emit("marketcap_req", body.rawString()!)
        }
        */
    }
    
    static func endPriceQuote() {
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("marketcap_end")
            }
        } else {
            self.socket.emit("marketcap_end")
        }
    }

    // TODO: It's disable due to API is changed. not work anymore
    static func getTiker() {
        /*
        var body: JSON = JSON()
        body["delegateAddress"] = JSON(RelayAPIConfiguration.delegateAddress)
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("tickersOfSource_req", body.rawString()!)
            }
        } else {
            self.socket.emit("tickersOfSource_req", body.rawString()!)
        }
        */
    }
    
    static func endTicker() {
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("tickersOfSource_end")
            }
        } else {
            self.socket.emit("tickersOfSource_end")
        }
    }
    
    static func getTrend(market: String, interval: String) {
        var body: JSON = JSON()
        body["market"] = JSON(market)
        body["interval"] = JSON(interval)
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("trends_req", body.rawString()!)
            }
        } else {
            self.socket.emit("trends_req", body.rawString()!)
        }
    }
    
    static func endTrend() {
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("trends_end")
            }
        } else {
            self.socket.emit("trends_end")
        }
    }
    
    static func getOrderStatus(orderHash: String) {
        var body: JSON = JSON()
        body["orderHash"] = JSON(orderHash)
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("orderTracing_req", body.rawString()!)
            }
        } else {
            self.socket.emit("orderTracing_req", body.rawString()!)
        }
    }
    
    static func endOrderStatus() {
        if socket.status != .connected {
            socket.on(clientEvent: .connect) {_, _ in
                self.socket.emit("orderTracing_end")
            }
        } else {
            self.socket.emit("orderTracing_end")
        }
    }
}
