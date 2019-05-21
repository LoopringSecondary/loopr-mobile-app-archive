//
//  OrderTableViewCell.swift
//  loopr-ios
//
//  Created by kenshin on 2018/4/2.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import UIKit

class OrderTableViewCell: UITableViewCell {

    @IBOutlet weak var baseView: UIView!

    @IBOutlet weak var tradingPairLabel: UILabel!
    @IBOutlet weak var orderTypeLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!

    @IBOutlet weak var displayLabel: UILabel!
    @IBOutlet weak var volumeLabel: UILabel!

    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var dateLabel: UILabel!

    @IBOutlet weak var secondLabelXLayoutContraint: NSLayoutConstraint!

    var order: RawOrder?
    var buttonColor: UIColor?
    var pressedCancelButtonClosure: (() -> Void)?

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.selectionStyle = .none
        self.theme_backgroundColor = ColorPicker.backgroundColor
        self.baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        cancelButton.titleLabel?.font = FontConfigManager.shared.getCharactorFont(size: 13)
        buttonColor = UIColor.theme

        priceLabel.font = FontConfigManager.shared.getRegularFont(size: 13)
        priceLabel.theme_textColor = GlobalPicker.textLightColor

        displayLabel.font = FontConfigManager.shared.getDigitalFont(size: 14)
        displayLabel.theme_textColor = GlobalPicker.textColor

        volumeLabel.font = FontConfigManager.shared.getRegularFont(size: 13)
        volumeLabel.theme_textColor = GlobalPicker.textLightColor

        dateLabel.font = FontConfigManager.shared.getRegularFont(size: 13)
        dateLabel.theme_textColor = GlobalPicker.textLightColor

        let label2Width = LocalizedString("Amount/Filled", comment: "").textWidth(font: FontConfigManager.shared.getCharactorFont(size: 13))
        secondLabelXLayoutContraint.constant = (UIScreen.main.bounds.width-15*2)*0.5-label2Width*0.5
    }

    override func setHighlighted(_ highlighted: Bool, animated: Bool) {
        if highlighted {
            baseView.theme_backgroundColor = ColorPicker.cardHighLightColor
        } else {
            baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        }
    }

    func update() {
        guard let order = self.order else { return }

        setupTradingPairlabel(order: order)
        setupPriceLabel(order: order)
        setupOrderTypeLabel(order: order)

        setupVolumeLabel(order: order)

        setupCancelButton(order: order)
        setupOrderDate(order: order)
    }

    func setupCancelButton(order: RawOrder) {
        let (flag, text) = getOrderStatus(order: order)
        cancelButton.isEnabled = flag
        cancelButton.title = text
    }

    func getOrderStatus(order: RawOrder) -> (Bool, String) {
        if order.state.status == .pending_active || order.state.status == .pending {
            let cancelledAll = UserDefaults.standard.bool(forKey: UserDefaultsKeys.cancelledAll.rawValue)
            if cancelledAll || isOrderCancelling(order: order) {
                cancelButton.setTitleColor(.pending, for: .normal)
                return (false, LocalizedString("Canceling", comment: ""))
            } else {
                cancelButton.setTitleColor(buttonColor, for: .normal)
                return (true, LocalizedString("Cancel", comment: ""))
            }
        } else if [OrderStatus.soft_cancelled_by_user, OrderStatus.expired].contains(order.state.status) {
            cancelButton.setTitleColor(.text2, for: .normal)
        } else if order.state.status == .completely_filled {
            cancelButton.setTitleColor(.success, for: .normal)
        }
        return (false, order.state.status.description)
    }

    func isOrderCancelling(order: RawOrder) -> Bool {
        let cancellingOrders = UserDefaults.standard.stringArray(forKey: UserDefaultsKeys.cancellingOrders.rawValue) ?? []
        return cancellingOrders.contains(order.hash)
    }

    func setupTradingPairlabel(order: RawOrder) {
        tradingPairLabel.text = OrderDataManager.shared.market?.name
        tradingPairLabel.font = FontConfigManager.shared.getDigitalFont(size: 14)
        tradingPairLabel.theme_textColor = GlobalPicker.textColor
        tradingPairLabel.setMarket()
    }

    func setupVolumeLabel(order: RawOrder) {
        if order.orderSide == .sell {
            displayLabel.text = order.amountSell.withCommas().trailingZero()
            volumeLabel.text = order.filled
        } else if order.orderSide == .buy {
            displayLabel.text = order.amountBuy.withCommas().trailingZero()
            volumeLabel.text = order.filled
        }
        if order.state.status == .completely_filled {
            volumeLabel.text = "100%"
        }
        if volumeLabel.text == "0%" {
            volumeLabel.text = "0.00%"
        }
    }

    func setupPriceLabel(order: RawOrder) {
        let decimals = MarketDataManager.shared.getDecimals(pair: order.market)
        // TODO: Simplify the followering code.
        if order.orderSide == .buy {
            var value = order.priceBuy.withCommas(decimals)
            if value.count > 9 {
                value = order.priceBuy.withCommas(decimals)
            }
            priceLabel.text = "\(value)"
        } else {
            var value = order.priceSell.withCommas(decimals)
            if value.count > 9 {
                value = order.priceSell.withCommas(decimals)
            }
            priceLabel.text = "\(value)"
        }
    }

    func setupOrderTypeLabel(order: RawOrder) {
        orderTypeLabel.font = FontConfigManager.shared.getBoldFont(size: 10)
        orderTypeLabel.borderWidth = 0.5
        if order.orderSide == .buy {
            orderTypeLabel.text = LocalizedString("B", comment: "")
            orderTypeLabel.backgroundColor = .success
            orderTypeLabel.textColor = .white
        } else if order.orderSide == .sell {
            orderTypeLabel.text = LocalizedString("S", comment: "")
            orderTypeLabel.backgroundColor = .fail
            orderTypeLabel.textColor = .white
        }
        orderTypeLabel.layer.cornerRadius = 2.0
        orderTypeLabel.layer.masksToBounds = true
    }

    func setupOrderDate(order: RawOrder) {
        let since = DateUtil.convertToDate(Int(order.validSince), format: "YYYY-MM-dd HH:mm")
        dateLabel.text = since
    }

    @IBAction func pressedCancelButton(_ sender: Any) {
        if let btnAction = self.pressedCancelButtonClosure {
            btnAction()
        }
    }

    class func getCellIdentifier() -> String {
        return "OrderTableViewCell"
    }

    class func getHeight() -> CGFloat {
        return 69
    }
}
