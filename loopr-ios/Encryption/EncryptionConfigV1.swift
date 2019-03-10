//
//  EncryptionConfigV1.swift
//  loopr-ios
//
//  Created by ruby on 3/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class EncryptionConfigV1 {
    static let version = "1"
    
    // encryption
    static let PBKDF2ConfigV1 = EncryptionPBKDF2ConfigV1()
    static let AESGCMConfigV1 = EncryptionAESGCMConfigV1()
    
    // icloud key
}
