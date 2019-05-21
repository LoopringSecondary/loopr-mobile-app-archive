//
//  FlutterChannel.swift
//  loopr-ios
//
//  Created by ruby on 3/25/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

// TODO: how to use this enum? We may not need it.
enum FlutterChannel: String {
    case blank = "Blank"
    case cloudBakup = "cloudBakup"
    case qrCodeDisplay = "qrCodeDisplay"
    case orderDetail = "orderDetail"
    case backupMnemonic = "backupMnemonic"
    case mnemonicEnterDerivationPath = "mnemonicEnterDerivationPath"
    case airdrop = "airdrop"
    case setGas = "setGas"
    case transactionDetail = "transactionDetail"
}
