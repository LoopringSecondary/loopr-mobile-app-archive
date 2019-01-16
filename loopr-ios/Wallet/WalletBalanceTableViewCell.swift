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

    private let balanceLabel: TickerLabel = TickerLabel()
    private let addressLabel: UILabel = UILabel()
    private let qrCodeButton: UIButton = UIButton()

    override func awakeFromNib() {
        super.awakeFromNib()
        
        selectionStyle = .none
        backgroundColor = .clear

        // hide all subviews except qrCodeButton
        balanceLabel.isHidden = true
        addressLabel.isHidden = true
        qrCodeButton.isHidden = false

        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width

        balanceLabel.frame = CGRect(x: 10, y: 40, width: screenWidth - 20, height: 36)

        addressLabel.frame = CGRect(x: screenWidth*0.25, y: balanceLabel.frame.maxY, width: screenWidth*0.5, height: 30)
        addressLabel.font = FontConfigManager.shared.getMediumFont(size: 13)
        addressLabel.textColor = UIColor.init(rgba: "#ffffffcc")
        addressLabel.textAlignment = .center
        addressLabel.numberOfLines = 1
        addressLabel.lineBreakMode = .byTruncatingMiddle
        addSubview(addressLabel)
        
        qrCodeButton.frame = CGRect(x: addressLabel.frame.maxX - 4 - 5, y: addressLabel.frame.minY + (addressLabel.frame.height-30)*0.5 - 5, width: 40, height: 40)
        qrCodeButton.addTarget(self, action: #selector(self.pressedQRCodeButton(_:)), for: .touchUpInside)
        addSubview(qrCodeButton)
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
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesCancelled(touches, with: event)
        print("touchesCancelled")
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
