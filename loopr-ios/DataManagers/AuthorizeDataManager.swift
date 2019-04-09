//
//  AuthorizeDataManager.swift
//  loopr-ios
//
//  Created by kenshin on 2018/6/11.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation

class AuthorizeDataManager {
    
    static let shared = AuthorizeDataManager()

    // submit order authorization
    var submitHash: String!
    var submitOrder: RawOrder!
    var signTransactions: [String: [RawTransaction]]!
    
    // cancel order authorization
    var cancelHash: String!
    
    // string & type from request
    var value: String?
    var type: QRCodeType?

    func _signTimestamp(timestamp: String) -> SignatureData? {
        var data = Data()
        SendCurrentAppWalletDataManager.shared._keystore()
        let timestamp = timestamp.utf8
        data.append(contentsOf: Array(timestamp))
        let (signature, _) = web3swift.sign(message: data)
        return signature
    }
    
    func _authorizeOrder(completion: @escaping (_ result: String?, _ error: Error?) -> Void) {
        let tokens = Array(signTransactions.keys)
        if self.signTransactions.count == 0 {
            completion(nil, nil)
            return
        } else if self.signTransactions.count == 1 {
            if let rawTxs = self.signTransactions[tokens[0]] {
                if rawTxs.count == 1 {
                    SendCurrentAppWalletDataManager.shared.transferOnce(rawTx: rawTxs[0], completion: completion)
                } else if rawTxs.count == 2 {
                    SendCurrentAppWalletDataManager.shared.transferTwice(rawTxs: rawTxs, completion: completion)
                }
            }
        } else if self.signTransactions.count == 2 {
            if let rawTxsA = self.signTransactions[tokens[0]], let rawTxsB = self.signTransactions[tokens[1]] {
                if rawTxsA.count == 1 {
                    SendCurrentAppWalletDataManager.shared.transferOnce(rawTx: rawTxsA[0]) { (txHash, error) in
                        guard error == nil && txHash != nil else { completion(nil, error!); return }
                        if rawTxsB.count == 1 {
                            SendCurrentAppWalletDataManager.shared.transferOnce(rawTx: rawTxsB[0], completion: completion)
                        } else if rawTxsB.count == 2 {
                            SendCurrentAppWalletDataManager.shared.transferTwice(rawTxs: rawTxsB, completion: completion)
                        }
                    }
                } else if rawTxsA.count == 2 {
                    SendCurrentAppWalletDataManager.shared.transferTwice(rawTxs: rawTxsA) { (txHash, error) in
                        guard error == nil && txHash != nil else { completion(nil, error!); return }
                        if rawTxsB.count == 1 {
                            SendCurrentAppWalletDataManager.shared.transferOnce(rawTx: rawTxsB[0], completion: completion)
                        } else if rawTxsB.count == 2 {
                            SendCurrentAppWalletDataManager.shared.transferTwice(rawTxs: rawTxsB, completion: completion)
                        }
                    }
                }
            }
        }
    }
    
    func authorizeCancel(completion: @escaping (_ result: String?, _ error: Error?) -> Void) {
        let error = NSError(domain: "cancel", code: 0, userInfo: ["message": "error"])
        guard let owner = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.address,
            let hash = self.cancelHash, let cancelOrder = self.cancelOrder else { completion(nil, error); return }
        guard owner.lowercased() == cancelOrder.owner.lowercased() else { completion(nil, error); return }
        let timestamp = cancelOrder.timestamp.description
        if let signature = _signTimestamp(timestamp: timestamp) {
            LoopringAPIRequest.cancelOrder(owner: cancelOrder.owner, type: cancelOrder.type, orderHash: cancelOrder.orderHash, cutoff: cancelOrder.cutoff, tokenS: cancelOrder.tokenS, tokenB: cancelOrder.tokenB, signature: signature, timestamp: timestamp) { (_, error) in
                guard error == nil else { completion(nil, error); return }
                LoopringAPIRequest.notifyStatus(hash: hash, status: .accept, completionHandler: completion)
            }
        }
    }
    
    func _authorizeCancel(completion: @escaping (_ result: String?, _ error: Error?) -> Void) {
        if AuthenticationDataManager.shared.getPasscodeSetting() {
            AuthenticationDataManager.shared.authenticate(reason: "Authenticate to cancel") { (error) in
                guard error == nil else { completion(nil, error); return }
                self.authorizeCancel(completion: completion)
            }
        } else {
            self.authorizeCancel(completion: completion)
        }
    }
    
    func authorizeConvert(completion: @escaping (_ result: String?, _ error: Error?) -> Void) {
        let error = NSError(domain: "convert", code: 0, userInfo: ["message": "error"])
        guard let owner = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.address,
            let hash = self.convertHash, let rawTx = self.convertTx else { completion(nil, error); return }
        guard owner.lowercased() == self.convertOwner.lowercased() else { completion(nil, error); return }
        SendCurrentAppWalletDataManager.shared._transfer(rawTx: rawTx) { (_, error) in
            guard error == nil else { completion(nil, error); return }
            
        }
    }
    
    func _authorizeConvert(completion: @escaping (_ result: String?, _ error: Error?) -> Void) {
        if AuthenticationDataManager.shared.getPasscodeSetting() {
            AuthenticationDataManager.shared.authenticate(reason: LocalizedString("Authenticate to convert", comment: "")) { (error) in
                guard error == nil else { completion(nil, error); return }
                self.authorizeConvert(completion: completion)
            }
        } else {
            self.authorizeConvert(completion: completion)
        }
    }
    
    func authorizeApprove(completion: @escaping (_ result: String?, _ error: Error?) -> Void) {
        let error = NSError(domain: "convert", code: 0, userInfo: ["message": "error"])
        guard let owner = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.address,
            let hash = self.approveHash, let rawTxs = self.approveTxs else { completion(nil, error); return }
        guard owner.lowercased() == self.approveOwner.lowercased() else { completion(nil, error); return }
        if rawTxs.count == 1 {
            SendCurrentAppWalletDataManager.shared.transferOnce(rawTx: rawTxs[0]) { (_, error) in
                guard error == nil else { completion(nil, error); return }
                
            }
        } else if rawTxs.count == 2 {
            SendCurrentAppWalletDataManager.shared.transferTwice(rawTxs: rawTxs) { (_, error) in
                guard error == nil else { completion(nil, error); return }
                
            }
        }
    }
    
    func _authorizeApprove(completion: @escaping (_ result: String?, _ error: Error?) -> Void) {
        if AuthenticationDataManager.shared.getPasscodeSetting() {
            AuthenticationDataManager.shared.authenticate(reason: LocalizedString("Authenticate to approve the transaction", comment: "")) { (error) in
                guard error == nil else { completion(nil, error); return }
                self.authorizeApprove(completion: completion)
            }
        } else {
            self.authorizeApprove(completion: completion)
        }
    }
    
    func process(qrContent: String) {
        if QRCodeMethod.isAddress(content: qrContent) {
            self.type = .address
        } else if QRCodeMethod.isMnemonicValid(content: qrContent) {
            self.type = .mnemonic
        } else if QRCodeMethod.isPrivateKey(content: qrContent) {
            self.type = .privateKey
        } else if QRCodeMethod.isP2POrder(content: qrContent) {
            self.type = .p2pOrder
        } else {
            self.type = .undefined
        }
        self.value = qrContent
    }
}
