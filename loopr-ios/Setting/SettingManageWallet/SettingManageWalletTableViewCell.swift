//
//  SettingManageWalletTableViewCell.swift
//  loopr-ios
//
//  Created by xiaoruby on 4/6/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

protocol SettingManageWalletTableViewCellDelegate: class {
    func pressedQACodeButtonInWalletBalanceTableViewCell(wallet: AppWallet)
}

class SettingManageWalletTableViewCell: UITableViewCell {

    weak var delegate: SettingManageWalletTableViewCellDelegate?

    var wallet: AppWallet?
    
    @IBOutlet weak var baseView: UIImageView!
    @IBOutlet weak var selectedIconView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var totalBalanceLabel: TickerLabel!
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var qrCodeButton: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        accessoryType = .none
        selectionStyle = .none
        
        theme_backgroundColor = ColorPicker.backgroundColor
        
        baseView.image = UIImage(named: "wallet-background" + ColorTheme.getTheme())
        baseView.contentMode = .scaleToFill
        baseView.clipsToBounds = true
        baseView.cornerRadius = 8

        selectedIconView.image = UIImage(named: "wallet-checked")
        selectedIconView.contentMode = .scaleToFill

        nameLabel.font = FontConfigManager.shared.getRegularFont(size: 16)
        nameLabel.textColor = UIColor.init(white: 1, alpha: 1)
        
        totalBalanceLabel.setFont(FontConfigManager.shared.getMediumFont(size: 32), currencySymbolFont: FontConfigManager.shared.getMediumFont(size: 20))
        totalBalanceLabel.animationDuration = 0.3
        totalBalanceLabel.textAlignment = NSTextAlignment.center
        totalBalanceLabel.textColor = UIColor.init(white: 1, alpha: 0.9)
        totalBalanceLabel.initializeLabel()

        addressLabel.font = FontConfigManager.shared.getMediumFont(size: 13)
        addressLabel.textColor = UIColor.init(rgba: "#ffffffcc")
        addressLabel.lineBreakMode = .byTruncatingMiddle
        
        qrCodeButton.setImage(UIImage(named: "QRCode-white-small"), for: .normal)
        qrCodeButton.addTarget(self, action: #selector(self.pressedQRCodeButton(_:)), for: .touchUpInside)
    }

    func update() {
        if let wallet = wallet {
            nameLabel.text = wallet.name
            totalBalanceLabel.setText(wallet.totalCurrency.currency, animated: false)
            addressLabel.text = wallet.address.getAddressFormat(length: 11)
            if wallet.address == CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address {
                setCurrentWallet()
            } else {
                setNoCurrentWallet()
            }
        }
    }

    func setNoCurrentWallet() {
        selectedIconView.isHidden = true
        baseView.image = UIImage(named: "wallet-background" + ColorTheme.getTheme())
    }

    func setCurrentWallet() {
        selectedIconView.isHidden = false
        baseView.image = UIImage(named: "wallet-background-active" + ColorTheme.getTheme())
    }
    
    @objc func pressedQRCodeButton(_ button: UIButton) {
        print("pressedItem1Button")
        if let wallet = wallet {
            delegate?.pressedQACodeButtonInWalletBalanceTableViewCell(wallet: wallet)
        }
    }

    class func getCellIdentifier() -> String {
        return "SettingManageWalletTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 200+10
    }
}
