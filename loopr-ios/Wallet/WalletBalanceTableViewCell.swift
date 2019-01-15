//
//  WalletBalanceTableViewCell.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/19/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

protocol WalletBalanceTableViewCellDelegate: class {
    func pressedQACodeButtonInWalletBalanceTableViewCell()
    func touchesBegan()
    func touchesEnd()
}

class WalletBalanceTableViewCell: UITableViewCell {

    weak var delegate: WalletBalanceTableViewCellDelegate?

    var updateBalanceLabelTimer: Timer?
    var baseView: UIImageView = UIImageView()
    let balanceLabel: TickerLabel = TickerLabel()
    let addressLabel: UILabel = UILabel()
    let qrCodeButton: UIButton = UIButton()

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        selectionStyle = .none
        // theme_backgroundColor = ColorPicker.backgroundColor
        backgroundColor = .clear

        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        
        baseView.isHidden = true
        balanceLabel.isHidden = true
        addressLabel.isHidden = true
        qrCodeButton.isHidden = true

        baseView.frame = CGRect(x: 15, y: 10, width: screenWidth - 15*2, height: 120)
        baseView.image = UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())
        baseView.contentMode = .scaleToFill
        addSubview(baseView)
        
        balanceLabel.frame = CGRect(x: 10, y: 40, width: screenWidth - 20, height: 36)
        balanceLabel.setFont(FontConfigManager.shared.getMediumFont(size: 32), currencySymbolFont: FontConfigManager.shared.getMediumFont(size: 20))
        balanceLabel.animationDuration = 0.3
        balanceLabel.textAlignment = NSTextAlignment.center
        balanceLabel.initializeLabel()

        let balance = CurrentAppWalletDataManager.shared.getTotalAssetCurrencyFormmat()
        balanceLabel.setText("\(balance)", animated: false)
        addSubview(balanceLabel)

        addressLabel.frame = CGRect(x: screenWidth*0.25, y: balanceLabel.frame.maxY, width: screenWidth*0.5, height: 30)
        addressLabel.font = FontConfigManager.shared.getMediumFont(size: 13)
        addressLabel.textColor = UIColor.init(rgba: "#ffffffcc")
        addressLabel.textAlignment = .center
        addressLabel.numberOfLines = 1
        addressLabel.lineBreakMode = .byTruncatingMiddle
        addSubview(addressLabel)
        
        qrCodeButton.frame = CGRect(x: addressLabel.frame.maxX - 4, y: addressLabel.frame.minY + (addressLabel.frame.height-30)*0.5, width: 30, height: 30)
        qrCodeButton.setImage(UIImage(named: "QRCode-white-small"), for: .normal)
        qrCodeButton.addTarget(self, action: #selector(self.pressedQRCodeButton(_:)), for: .touchUpInside)
        addSubview(qrCodeButton)
        
        update()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        print("touchesBegan")
        delegate?.touchesBegan()
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesEnded(touches, with: event)
        print("touchesEnded")
        delegate?.touchesEnd()
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesEnded(touches, with: event)
        print("touchesMoved")
        // delegate?.touchesEnd()
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesCancelled(touches, with: event)
        print("touchesCancelled")
        // delegate?.touchesEnd()
    }

    private func update() {
        balanceLabel.textColor = UIColor.white
    }
    
    func setup(animated: Bool) {
        addressLabel.text = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.address ?? ""
        let balance = CurrentAppWalletDataManager.shared.getTotalAssetCurrencyFormmat()
        balanceLabel.setText(balance, animated: animated)
        balanceLabel.layoutCharacterLabels()
    }
    
    @objc func updateBalance() {
        balanceLabel.textColor = UIColor.white
        var balance = CurrentAppWalletDataManager.shared.getTotalAssetCurrencyFormmat()
        balance.insert(" ", at: balance.index(after: balance.startIndex))
        balanceLabel.setText(balance, animated: true)
        layoutIfNeeded()
    }

    @objc func pressedQRCodeButton(_ button: UIButton) {
        print("pressedItem1Button")
        delegate?.pressedQACodeButtonInWalletBalanceTableViewCell()
    }

    class func getCellIdentifier() -> String {
        return "WalletBalanceTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 134
    }
}
