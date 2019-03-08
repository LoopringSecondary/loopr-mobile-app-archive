//
//  CloudBackDataManager.swift
//  loopr-ios
//
//  Created by ruby on 3/7/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class CloudBackDataManager {
    
    static let shared = CloudBackDataManager()
    
    private init() {
        
    }

    func set() {
        let keyStore = NSUbiquitousKeyValueStore()
        keyStore.set("John Appleseed", forKey: "userName")
        keyStore.synchronize()
    }
    
    func get() -> String? {
        let keyStore = NSUbiquitousKeyValueStore()
        let storedUserName = keyStore.string(forKey: "userName")
        return storedUserName
    }
    
}
