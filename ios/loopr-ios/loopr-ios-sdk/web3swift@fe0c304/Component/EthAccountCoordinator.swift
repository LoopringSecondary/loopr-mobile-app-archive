//
//  EthAccountCoordinator.swift
//  web3swift
//
//  Created by Sameer Khavanekar on 1/23/18.
//  Copyright © 2018 Radical App LLC. All rights reserved.
//

import Foundation
import Geth

open class EthAccountConfiguration {
    public var namespace: String?
    public var password: String?
    
    public init(namespace: String?, password: String?) {
        self.namespace = namespace
        self.password = password
    }
    
    open static let `default`: EthAccountConfiguration = {
        return EthAccountConfiguration(namespace: "wallet", password: "qwerty")
    }()
    
}

open class EthAccountCoordinator {
    
    fileprivate var _keystore: GethKeyStore?
    fileprivate var _account: GethAccount?
    fileprivate var _gethContext: GethContext?
    
    open var defaultConfiguration: EthAccountConfiguration = EthAccountConfiguration.default
    
    open static let `default`: EthAccountCoordinator = {
        return EthAccountCoordinator()
    }()
    
    open var keystore: GethKeyStore? {
        return _keystore
    }
    
    open var account: GethAccount? {
        get {
            return _account
        }
        set {
            _account = newValue
        }
    }
    
    open func launch(_ configuration: EthAccountConfiguration) -> (GethKeyStore?, GethAccount?) {
        defaultConfiguration = configuration
        _keystore = _createKeystore(configuration.namespace)
        
        guard let keystore = _keystore else {
            print("Failed to create keystore")
            return (nil, nil)
        }
        if let password = configuration.password {
            _account = _createAccount(keystore, password: password)
            return (_keystore, _account)
        } else {
            print("Account not created as password is not set")
        }
        return (_keystore, nil)
    }
    
    open func launch(keystore: GethKeyStore, password: String) -> GethAccount? {
        _keystore = keystore
        let tmp = getAccount(password)
        if tmp == nil {
            print("nil")
        } else {
            print(tmp?.getAddress().getHex())
        }
        print("in launch")
        _account = _createAccount(keystore, password: password)
        // print("current address: \(_account?.getAddress().getHex())")
        return _account
    }

    // TODO: return a file path
    open func launch(_ configuration: EthAccountConfiguration) -> (GethKeyStore?, GethAccount?, String) {
        defaultConfiguration = configuration
        var keystoreFilePath: String
        (_keystore, keystoreFilePath) = _createKeystore(configuration.namespace)
        
        guard let keystore = _keystore else {
            print("Failed to create keystore")
            return (nil, nil, keystoreFilePath)
        }
        if let password = configuration.password {
            _account = _createAccount(keystore, password: password)
            return (_keystore, _account, keystoreFilePath)
        } else {
            print("Account not created as password is not set")
        }
        return (_keystore, nil, keystoreFilePath)
    }
    
    open func createAccount(_ password: String) -> GethAccount? {
        defaultConfiguration.password = password
        if _keystore == nil {
            _keystore = _createKeystore(defaultConfiguration.namespace)
        }
        guard let keystore = _keystore else {
            print("Failed to create keystore")
            return nil
        }
        _account = _createAccount(keystore, password: password)
        return _account
    }

    private func _createKeystore(_ atPath: String?) -> GethKeyStore? {
        let datadir = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0]
        var pathInfix = atPath ?? ""
        if !pathInfix.isEmpty {
            pathInfix += "/"
        }
        let finalPath = datadir + "/" + "\(pathInfix)" + "keystore"
        let keystore = GethNewKeyStore(finalPath, GethLightScryptN, GethLightScryptP)  // GethStandardScryptN, GethStandardScryptP, GethLightScryptN, GethLightScryptP or number
        _gethContext = GethNewContext()
        return keystore
    }
    
    private func _createKeystore(_ atPath: String?) -> (GethKeyStore?, String) {
        let datadir = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0]
        var pathInfix = atPath ?? ""
        if !pathInfix.isEmpty {
            pathInfix += "/"
        }
        let finalPath = datadir + "/" + "\(pathInfix)" + "keystore"
        let keystore = GethNewKeyStore(finalPath, GethLightScryptN, GethLightScryptP)  // GethStandardScryptN, GethStandardScryptP, GethLightScryptN, GethLightScryptP or number
        _gethContext = GethNewContext()
        return (keystore, finalPath)
    }
    
    fileprivate func _createAccount(_ keystore: GethKeyStore, password: String) -> GethAccount? {
        do {
            if let account = getAccount(password) {
                return account
            } else {
                let account = try keystore.newAccount(password)
                return account
            }
        } catch {
            print("Failed to create account!")
            return nil
        }
    }
    
    // TODO:- Update later to handle multiple accounts for wallet
    
    open func getAccount(_ password: String, at: Int = 0) -> GethAccount? {
        if let accounts = _keystore?.getAccounts() {
            do {
                let account: GethAccount? = try accounts.get(at)
                if let account = account {
                    try _keystore?.unlock(account, passphrase: password)
                }
                return account
            } catch {
                return nil
            }
        }
        return nil
    }
    
    open func importPrivateKey(_ jsonKey: String, passphrase: String, newPassphrase: String) -> GethAccount? {
        guard let keystore = keystore else {
            return nil
        }
        do {
            if let jsonKeyData = jsonKey.data(using: .utf8) {
                let newAccount = try keystore.importKey(jsonKeyData, passphrase: passphrase, newPassphrase: newPassphrase)
                _account = newAccount
                defaultConfiguration.password = newPassphrase
                return newAccount
            } else {
                print("Invalid account json file")
                return nil
            }
        } catch {
            print("Import failed try again \(error)")
            return nil
        }
    }
    
    open func updatePassword(_ passphrase: String, newPassphrase: String) -> Bool {
        guard let keystore = keystore, let account = account else {
            return false
        }
        do {
            try keystore.update(account, passphrase: passphrase, newPassphrase: newPassphrase)
            defaultConfiguration.password = newPassphrase
            return true
        } catch {
            print("Failed to update! Try again \(error)")
            return false
        }
    }
    
    open func drain() {
        _keystore = nil
        _account = nil
        _gethContext = nil
    }
    
}

public extension EthAccountCoordinator {
    
    public func sign(address: GethAddress, encodedFunctionData: Data, nonce: Int64, amount: GethBigInt, gasLimit: GethBigInt, gasPrice: GethBigInt, password newPassword: String? = nil) -> GethTransaction? {
        let tmpPassword = newPassword == nil ? defaultConfiguration.password : newPassword
        guard let password = tmpPassword, let keystore = _keystore, let account = _account else {
            // TODO:- We can return Error here/Throw one
            print("Create keystore/account first")
            return nil
        }
        return Sign.sign(keystore: keystore, account: account, address: address, encodedFunctionData: encodedFunctionData, nonce: nonce, amount: amount, gasLimit: gasLimit, gasPrice: gasPrice, password: password)
    }
    
}
