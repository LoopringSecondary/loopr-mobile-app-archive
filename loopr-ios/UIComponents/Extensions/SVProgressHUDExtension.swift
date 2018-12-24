//
//  SVProgressHUDExtension.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/24.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import SVProgressHUD

extension SVProgressHUD {
    
    open class func show(_ status: String, maxTime: TimeInterval) {
        SVProgressHUD.show(withStatus: status)
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + maxTime) {
            SVProgressHUD.dismiss()
        }
    }
}
