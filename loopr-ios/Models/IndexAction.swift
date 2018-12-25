//
//  CancelType.swift
//  loopr-ios
//
//  Created by kenshin on 2018/6/13.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

enum IndexAction: Int, CustomStringConvertible {
    
    case confirm = 1
    case cancel = -1
    
    var description: String {
        switch self {
        case .confirm: return "confirm"
        case .cancel: return "cancel"
        }
    }
}
