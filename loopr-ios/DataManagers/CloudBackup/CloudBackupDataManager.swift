//
//  CloudBackDataManager.swift
//  loopr-ios
//
//  Created by ruby on 3/7/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class CloudBackupDataManager {
    
    static let shared = CloudBackupDataManager()
    
    private init() {
        
    }

    func set() {
        let keyStore = NSUbiquitousKeyValueStore()
        keyStore.set("John Appleseed", forKey: "userName")
        keyStore.synchronize()
    }
    
    func get() -> String? {
        let keyStore = NSUbiquitousKeyValueStore()
        let storedUserName = keyStore.string(forKey: "upwallet")
        return storedUserName
    }
    
}
