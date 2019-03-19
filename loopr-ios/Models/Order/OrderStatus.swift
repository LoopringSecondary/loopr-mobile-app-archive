//
//  OrderStatus.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/5/18.
//  Copyright © 2018 Loopring. All rights reserved.
//
import UIKit

enum OrderStatus: String, CustomStringConvertible {

    case pending_active = "STATUS_PENDING_ACTIVE"
    case new = "STATUS_NEW"
    case pending = "STATUS_PENDING"
    case partially_filled = "STATUS_PARTIALLY_FILLED"
    case soft_cancelled_by_user = "STATUS_SOFT_CANCELLED_BY_USER"
    case onchain_cancelled_by_user = "STATUS_ONCHAIN_CANCELLED_BY_USER"
    case soft_cancelled_by_user_trading_pair = "STATUS_SOFT_CANCELLED_BY_USER_TRADING_PAIR"
    case onchain_cancelled_by_user_trading_pair = "STATUS_ONCHAIN_CANCELLED_BY_USER_TRADING_PAIR"
    case completely_filled = "STATUS_COMPLETELY_FILLED"
    case expired = "STATUS_EXPIRED"
    case unknown = "ORDER_UNKNOWN"

    var description: String {
        switch self {
        case .pending_active: return LocalizedString("Order Pending", comment: "")
        case .new: return LocalizedString("Order Submitted", comment: "")
        case .pending: return LocalizedString("Order Submitted", comment: "")
        case .partially_filled: return LocalizedString("Order Submitted", comment: "")
        case .soft_cancelled_by_user: return LocalizedString("Order Cancelled", comment: "")
        case .onchain_cancelled_by_user: return LocalizedString("Order Cancelled", comment: "")
        case .soft_cancelled_by_user_trading_pair: return LocalizedString("Order Cancelled", comment: "")
        case .onchain_cancelled_by_user_trading_pair: return LocalizedString("Order Cancelled", comment: "")
        case .completely_filled: return LocalizedString("Order Completed", comment: "")
        case .expired: return LocalizedString("Order Expired", comment: "")
        case .unknown: return LocalizedString("Order Unknown", comment: "")
        }
    }
}
