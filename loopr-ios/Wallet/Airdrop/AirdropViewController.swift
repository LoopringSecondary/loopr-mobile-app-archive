//
//  AirdropViewController.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/19.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import Geth
import SVProgressHUD
import NotificationBannerSwift

class AirdropViewController: UIViewController, UIScrollViewDelegate {

    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var addressTextField: UITextField!
    @IBOutlet weak var addressTipLabel: UILabel!
    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var amountTipLabel: UILabel!
    @IBOutlet weak var claimButton: GradientButton!
    @IBOutlet weak var forwardButton: GradientButton!
    
    var bindAddress: String?

    var bindAmount: String?

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        setBackButton()
        self.navigationController?.interactivePopGestureRecognizer?.isEnabled = false
        view.theme_backgroundColor = ColorPicker.backgroundColor
        self.navigationItem.title = LocalizedString("Airdrop title", comment: "")

        contentView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        contentView.cornerRadius = 6
        contentView.applyShadow()
        
        addressTextField.font = FontConfigManager.shared.getDigitalFont(size: 14)
        addressTextField.theme_tintColor = GlobalPicker.contrastTextColor
        addressTextField.contentMode = UIViewContentMode.bottom
        addressTextField.setLeftPaddingPoints(8)
        
        addressTipLabel.font = FontConfigManager.shared.getCharactorFont(size: 12)
        addressTipLabel.theme_textColor = GlobalPicker.textLightColor
        addressTipLabel.text = LocalizedString("Airdrop address tip", comment: "")

        amountTextField.font = FontConfigManager.shared.getDigitalFont(size: 14)
        amountTextField.theme_tintColor = GlobalPicker.contrastTextColor
        amountTextField.contentMode = UIViewContentMode.bottom
        amountTextField.setLeftPaddingPoints(8)
        
        amountTipLabel.font = FontConfigManager.shared.getCharactorFont(size: 12)
        amountTipLabel.theme_textColor = GlobalPicker.textLightColor
        amountTipLabel.text = LocalizedString("Airdrop amount tip", comment: "")

        claimButton.title = LocalizedString("Airdrop button", comment: "")
        forwardButton.title = LocalizedString("Airdrop forward", comment: "")

        scrollView.delegate = self
        scrollView.delaysContentTouches = false
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        setupContent()
    }

    func setupContent() {
        SVProgressHUD.show(withStatus: LocalizedString("Loading Data", comment: ""))
        SendCurrentAppWalletDataManager.shared._getBindAddress { (result, _) in
            DispatchQueue.main.async {
                if let result = result {
                    self.bindAddress = result
                    NeoAPIRequest.neo_getAmount(bindAddress: result, completion: { (result, _) -> Void in
                        DispatchQueue.main.async {
                            if let result = result {
                                let value = Asset.getAmount(fromWeiAmount: result.stack[0].value, of: 8)
                                self.bindAmount = value?.withCommas(4)
                                _ = self.validateTime()
                                _ = self.validateAddress() && self.validateAmount()
                            } else {
                                let notificationTitle = LocalizedString("Airdrop empty", comment: "")
                                let banner = NotificationBanner.generate(title: notificationTitle, style: .danger)
                                banner.duration = 5.0
                                banner.show()
                            }
                            SVProgressHUD.dismiss()
                        }
                    })
                } else {
                    SVProgressHUD.dismiss()
                    let notificationTitle = LocalizedString("Airdrop no bind", comment: "")
                    let banner = NotificationBanner.generate(title: notificationTitle, style: .danger)
                    banner.duration = 5.0
                    banner.show()
                }
            }
        }
    }

    func validate() -> Bool {
        return validateAddress() && validateAmount()
    }

    func validateAddress() -> Bool {
        if bindAddress == nil || bindAddress!.isEmpty {
            self.addressTipLabel.text = LocalizedString("Airdrop no bind", comment: "")
            self.addressTipLabel.textColor = .fail
            self.addressTipLabel.shake()
            return false
        } else {
            self.addressTextField.text = self.bindAddress
            self.addressTipLabel.theme_textColor = GlobalPicker.textLightColor
            return true
        }
    }

    func validateAmount() -> Bool {
        if bindAmount == nil || !bindAmount!.isDouble {
            self.amountTipLabel.text = LocalizedString("Airdrop empty", comment: "")
            self.amountTipLabel.textColor = .fail
            self.amountTipLabel.shake()
            return false
        } else {
            self.amountTextField.text = self.bindAmount
            self.amountTipLabel.theme_textColor = GlobalPicker.textLightColor
            return true
        }
    }
    
    // To prove claiming only once a day
    func validateTime() -> Bool {
        var result = false
        let date = UserDefaults.standard.object(forKey: UserDefaultsKeys.airdropDate.rawValue)
        if let date = date as? Date {
            let target = Calendar.current.date(byAdding: .hour, value: 24, to: date)
            if Date() < target! {
                self.claimButton.isHidden = true
                self.forwardButton.isHidden = false
                result = false
            } else {
                self.claimButton.isHidden = false
                self.forwardButton.isHidden = true
                result = true
            }
        } else {
            result = true
        }
        return result
    }

    @IBAction func pressedClaimButton(_ sender: Any) {
        guard validate() else { return }
        NeoAPIRequest.neo_claimAmount(bindAddress: bindAddress!) { (response, _) in
            DispatchQueue.main.async {
                if let result = response {
                    self.claimButton.isHidden = true
                    self.forwardButton.isHidden = false
                    UserDefaults.standard.setValue(result, forKey: UserDefaultsKeys.airdropUrl.rawValue)
                    UserDefaults.standard.setValue(Date(), forKey: UserDefaultsKeys.airdropDate.rawValue)
                } else {
                    let notificationTitle = LocalizedString("Airdrop failed", comment: "")
                    let banner = NotificationBanner.generate(title: notificationTitle, style: .danger)
                    banner.duration = 3.0
                    banner.show()
                }
            }
        }
    }

    @IBAction func pressedForwardButton(_ sender: Any) {
        if let url = UserDefaults.standard.string(forKey: UserDefaultsKeys.airdropUrl.rawValue) {
            let viewController = DefaultWebViewController()
            viewController.navigationTitle = LocalizedString("Airdrop browser", comment: "")
            viewController.url = URL.init(string: "https://neotracker.io/tx/\(url.drop0x())")
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
}
