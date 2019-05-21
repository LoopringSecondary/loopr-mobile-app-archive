//
//  P2POrderHistoryDataManager.swift
//  loopr-ios
//
//  Created by xiaoruby on 5/17/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

// TODO: consider share code with OrderDataManager.
// It's to all P2P orders of an address.
class P2POrderHistoryDataManager {

    static let shared = P2POrderHistoryDataManager()

    private var orders: [RawOrder]

    // changed to true when a P2P order is submitted.
    var shouldReloadData: Bool = false

    private init() {
        orders = []
    }

    func getOrderDataFromLocal(order: RawOrder) -> String? {
        let defaults = UserDefaults.standard
        return defaults.string(forKey: order.hash) ?? nil
    }

    func getOrders(orderStatuses: [OrderStatus]? = nil) -> [RawOrder] {
        guard let orderStatuses = orderStatuses else {
            return orders
        }
        let filteredOrder = orders.filter { (order) -> Bool in
            orderStatuses.contains(order.state.status)
        }
        return filteredOrder
    }

    func getOrders(token: String? = nil) -> [RawOrder] {
        guard let token = token else {
            return orders
        }
        return orders.filter { (order) -> Bool in
            let pair = order.market.components(separatedBy: "-")
            return pair[0].lowercased() == token.lowercased()
        }
    }

    func getOrdersFromServer(cursor: UInt, size: UInt = 50, completionHandler: @escaping (_ orders: [RawOrder], _ error: Error?) -> Void) {
        if let owner = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.address {
            LoopringAPIRequest.getOrders(owner: owner, cursor: cursor, size: size) { result, error in
                guard let result = result, error == nil else {
                    self.orders = []
                    completionHandler([], error)
                    return
                }
                let p2pOrders = result.orders.filter { $0.orderType == .p2pOrder }  // TODO no type from relay2.0 now
                if cursor == 1 {
                    self.orders = p2pOrders
                } else {
                    self.orders += p2pOrders
                }
                completionHandler(p2pOrders, error)
            }
        }
    }
}
