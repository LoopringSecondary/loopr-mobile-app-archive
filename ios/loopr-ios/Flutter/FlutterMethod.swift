//
//  FlutterMethod.swift
//  loopr-ios
//
//  Created by ruby on 4/2/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

enum FlutterMethod: String {
    
    // Airdrop
    case airdropUpdate = "airdrop.update"
    
    // QRCode
    case qrCodeDisplayGet = "qrCodeDisplay.get"
    case qrCodeDisplayCopyAddress = "qrCodeDisplay.copyAddress"
    case qrCodeDisplaySaveToAlbum = "qrCodeDisplay.saveToAlbum"
    
    // OrderDetail
    case orderDetailGet = "orderDetail.get"
    
    // Backup mnemonic
    case backupMnemonicGet = "backupMnemonic.get"
    case backupMnemonicUpdate = "backupMnemonic.update"
    case backupMnemonicVerify = "backupMnemonic.verify"
    case backupMnemonicSkip = "backupMnemonic.skip"
    
    case mnemonicEnterDerivationPathUpdate = "mnemonicEnterDerivationPath.update"
    case mnemonicEnterDerivationPathNext = "mnemonicEnterDerivationPath.next"

    // Set Gas
    case setGasGet = "setGas.get"
    case setGasUpdate = "setGas.update"

    // Transaction Detail
    case transactionDetailGet = "transactionDetail.get"

}
