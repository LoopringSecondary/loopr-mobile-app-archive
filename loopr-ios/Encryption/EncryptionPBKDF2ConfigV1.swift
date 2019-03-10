//
//  EncryptionPBKDF2ConfigV1.swift
//  loopr-ios
//
//  Created by ruby on 3/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation
import CryptoSwift

class EncryptionPBKDF2ConfigV1 {
    
    let salt: [UInt8]
    let iterations: Int
    let variant: HMAC.Variant
    
    init() {
        // The following values won't be changed in EncryptionConfigV1
        salt = Array("u_p_w_a_8_0_l_l_e_t".utf8)
        iterations = 8091
        variant = .sha256
    }

}
