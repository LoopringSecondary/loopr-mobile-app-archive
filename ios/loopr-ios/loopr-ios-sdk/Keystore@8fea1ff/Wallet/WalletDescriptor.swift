// Copyright © 2017-2018 Trust.
//
// This file is part of Trust. The full Trust copyright notice, including
// terms governing use, modification, and redistribution, is contained in the
// file LICENSE at the root of the source code distribution tree.

import Foundation

public struct WalletDescriptor {
    /// Wallet's mnemominc phrase.
    public var mnemonic: String

    /// Ethereum address at index 0.
    public var address: Address

    /// Wallet UUID.
    public var id = UUID()

    /// Creates a new `WalletDescriptor`.
    public init(mnemonic: String, address: Address) {
        self.mnemonic = mnemonic
        self.address = address
    }

    /// Initializes a `WalletDescriptor` from a JSON wallet.
    public init(contentsOf url: URL) throws {
        let data = try Data(contentsOf: url)
        self = try JSONDecoder().decode(WalletDescriptor.self, from: data)
    }
}

extension WalletDescriptor: Codable {
    enum CodingKeys: String, CodingKey {
        case mnemonic
        case address
        case id
    }

    public init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        mnemonic = try values.decode(String.self, forKey: .mnemonic)
        address = Address(data: try values.decodeHexString(forKey: .address))
        id = UUID(uuidString: try values.decode(String.self, forKey: .id)) ?? UUID()
    }

    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(mnemonic, forKey: .mnemonic)
        try container.encode(address.description, forKey: .address)
        try container.encode(id.uuidString, forKey: .id)
    }
}
