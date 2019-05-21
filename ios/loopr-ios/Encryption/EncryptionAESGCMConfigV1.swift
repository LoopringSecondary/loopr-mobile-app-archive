//
//  EncryptionAESGCMConfigV1.swift
//  loopr-ios
//
//  Created by ruby on 3/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation
import CryptoSwift

class EncryptionAESGCMConfigV1 {
    
    let iv: [UInt8]
    let mode: GCM.Mode
    
    init() {
        // The following values won't be changed in EncryptionConfigV1
        iv =  Array<UInt8>(hex: "0xcafebabefacedbaddecaf888")
        mode = .detached
    }

}
