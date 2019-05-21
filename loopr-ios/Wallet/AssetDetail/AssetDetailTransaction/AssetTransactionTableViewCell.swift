//
//  UpdatedAssetTransactionTableViewCell.swift
//  loopr-ios
//
//  Created by xiaoruby on 6/9/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class AssetTransactionTableViewCell: UITableViewCell {

    var transaction: Transaction?

    var baseView: UIView = UIView()

    var typeImageView: UIImageView = UIImageView()

    var titleLabel: UILabel = UILabel()

    var statusImage: UIImageView = UIImageView()

    var dateLabel: UILabel = UILabel()

    var amountLabel: UILabel = UILabel()

    var displayLabel: UILabel = UILabel()

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code

        selectionStyle = .none
        theme_backgroundColor = ColorPicker.backgroundColor

        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width

        baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        baseView.frame = CGRect.init(x: 15, y: 4, width: screenWidth - 15 * 2, height: AssetTransactionTableViewCell.getHeight() - 8)
        baseView.cornerRadius = 6
        addSubview(baseView)
        // applyShadow must be after addSubview
        baseView.applyShadow(withColor: UIColor.black)

        typeImageView.frame = CGRect.init(x: 20, y: 20, width: 28, height: 28)
        typeImageView.contentMode = .scaleAspectFit
        baseView.addSubview(typeImageView)

        titleLabel.frame = CGRect.init(x: 64, y: 16, width: 200, height: 36)
        titleLabel.font = FontConfigManager.shared.getMediumFont(size: 14)
        titleLabel.theme_textColor = GlobalPicker.textColor
        titleLabel.text = "ETHETHETHETHETHETHETH"
        titleLabel.sizeToFit()
        titleLabel.text = ""
        baseView.addSubview(titleLabel)

        dateLabel.frame = CGRect.init(x: titleLabel.frame.minX, y: 36, width: 200, height: 27)
        dateLabel.font = FontConfigManager.shared.getRegularFont(size: 13)
        dateLabel.theme_textColor = GlobalPicker.textLightColor
        dateLabel.text = "ETHETHETHETHETHETHETH"
        dateLabel.sizeToFit()
        dateLabel.text = ""
        baseView.addSubview(dateLabel)

        amountLabel.frame = CGRect.init(x: baseView.frame.width - 20 - 200, y: titleLabel.frame.minY, width: 200, height: titleLabel.frame.size.height)
        amountLabel.font = FontConfigManager.shared.getMediumFont(size: 14)
        amountLabel.textAlignment = .right
        baseView.addSubview(amountLabel)

        displayLabel.frame = CGRect.init(x: amountLabel.frame.minX, y: dateLabel.frame.minY, width: 200, height: dateLabel.frame.size.height)
        displayLabel.font = FontConfigManager.shared.getRegularFont(size: 13)
        displayLabel.theme_textColor = GlobalPicker.textLightColor
        displayLabel.textAlignment = .right
        baseView.addSubview(displayLabel)

        baseView.addSubview(statusImage)
    }

    override func setHighlighted(_ highlighted: Bool, animated: Bool) {
        if highlighted {
            baseView.theme_backgroundColor = ColorPicker.cardHighLightColor
        } else {
            baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        }
    }

    func update() {
        if let tx = transaction {
            // TODO: disable during Relay 2.0 refactor
            /*
            switch tx.type {
            case .ether_wrap:
                updateWrap()
            case .ether_unwrap:
                updateUnwrap()
            case .token_auth:
                updateApprove()
            case .order_cancel:
                udpateCutoffAndCanceledOrder()
            default:
                updateDefault()
            }
            */
            amountLabel.textColor = UIStyleConfig.getChangeColor(change: amountLabel.text ?? "")
            typeImageView.image = tx.icon
            dateLabel.text = tx.createTime
            updateStatusImage(transaction: tx)
        }
    }

    private func updateWrap() {
        amountLabel.isHidden = false
        displayLabel.isHidden = false
        if let transaction = self.transaction {
            if transaction.symbol.lowercased() == "weth" {
                amountLabel.text = "+\(transaction.value) \(transaction.symbol)"
            } else if transaction.symbol.lowercased() == "eth" {
                amountLabel.text = "-\(transaction.value) \(transaction.symbol)"
            }
            titleLabel.text = LocalizedString("Convert to WETH", comment: "")
            displayLabel.text = transaction.currency
        }
    }

    private func updateUnwrap() {
        amountLabel.isHidden = false
        displayLabel.isHidden = false
        if let transaction = self.transaction {
            if transaction.symbol.lowercased() == "weth" {
                amountLabel.text = "-\(transaction.value) \(transaction.symbol)"
            } else if transaction.symbol.lowercased() == "eth" {
                amountLabel.text = "+\(transaction.value) \(transaction.symbol)"
            }
            titleLabel.text = LocalizedString("Convert to ETH", comment: "")
            displayLabel.text = transaction.currency
        }
    }

    private func updateApprove() {
        amountLabel.isHidden = true
        displayLabel.isHidden = true
        let header = LocalizedString("Enabled", comment: "")
        let footer = LocalizedString("to Trade", comment: "")
        titleLabel.text = LocalizedString("\(header) \(transaction!.symbol) \(footer)", comment: "")
    }

    private func udpateCutoffAndCanceledOrder() {
        amountLabel.isHidden = true
        displayLabel.isHidden = true
        titleLabel.text = LocalizedString("Cancel Order", comment: "")
    }

    private func updateReceived() {
        amountLabel.isHidden = false
        displayLabel.isHidden = false
        titleLabel.text = transaction!.type.description + " " + transaction!.symbol
        amountLabel.text = "+\(transaction!.value) \(transaction?.symbol ?? " ")"
        displayLabel.text = transaction!.currency
    }

    private func updateSent() {
        amountLabel.isHidden = false
        displayLabel.isHidden = false
        titleLabel.text = transaction!.type.description + " " + transaction!.symbol
        amountLabel.text = "-\(transaction!.value) \(transaction?.symbol ?? " ")"
        displayLabel.text = transaction!.currency
    }

    private func updateDefault() {
        if let tx = self.transaction {
            titleLabel.text = tx.type.description + " " + tx.symbol
            displayLabel.text = tx.currency
            amountLabel.isHidden = false
            displayLabel.isHidden = false

            if tx.type == .trade_buy || tx.type == .token_in || tx.type == .eth_in {
                amountLabel.text = "+\(tx.value) \(tx.symbol)"
            } else if tx.type == .trade_sell || tx.type == .token_out || tx.type == .eth_out {
                amountLabel.text = "-\(tx.value) \(tx.symbol)"
            } else {
                amountLabel.isHidden = true
                displayLabel.isHidden = true
            }
        }
    }

    func updateStatusImage(transaction: Transaction) {
        let x = titleLabel.intrinsicContentSize.width + titleLabel.frame.minX + 15
        statusImage.frame = CGRect(origin: CGPoint(x: 0, y: 0), size: CGSize(width: 15, height: 15))
        statusImage.center = CGPoint(x: x, y: titleLabel.frame.midY)

        switch transaction.status {
        case .success:
            self.statusImage.image = UIImage(named: "Checked")
        case .pending:
            self.statusImage.image = UIImage(named: "Clock")
        case .failed:
            self.statusImage.image = UIImage(named: "Warn")
        default:
            self.statusImage.image = UIImage(named: "Checked")
        }
    }

    class func getCellIdentifier() -> String {
        return "AssetTransactionTableViewCell"
    }

    class func getHeight() -> CGFloat {
        return 76
    }
}