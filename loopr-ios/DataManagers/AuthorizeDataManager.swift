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
