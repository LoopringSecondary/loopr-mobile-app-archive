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
    
    /*
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
    */

}
