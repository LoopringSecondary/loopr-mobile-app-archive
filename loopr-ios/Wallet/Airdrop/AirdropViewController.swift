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
    @IBOutlet weak var contentViewHeightLayoutConstraint: NSLayoutConstraint!
    let heightForClaim: CGFloat = 270
    let heightForComingBackTomorrow: CGFloat = 370
    
    @IBOutlet weak var addressTextField: UITextField!
    @IBOutlet weak var addressTipLabel: UILabel!
    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var amountTipLabel: UILabel!
    @IBOutlet weak var nextClaimDateTextField: UITextField!
    @IBOutlet weak var nextClaimDateTipLabel: UILabel!

    @IBOutlet weak var claimButton: GradientButton!
    @IBOutlet weak var forwardButton: GradientButton!
    var isFirtTimeAppear: Bool = true
    
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
        addressTextField.isUserInteractionEnabled = false
        
        addressTipLabel.font = FontConfigManager.shared.getCharactorFont(size: 12)
        addressTipLabel.theme_textColor = GlobalPicker.textLightColor
        addressTipLabel.text = LocalizedString("Airdrop address tip", comment: "")

        amountTextField.font = FontConfigManager.shared.getDigitalFont(size: 14)
        amountTextField.theme_tintColor = GlobalPicker.contrastTextColor
        amountTextField.contentMode = UIViewContentMode.bottom
        amountTextField.setLeftPaddingPoints(8)
        amountTextField.isUserInteractionEnabled = false
        
        amountTipLabel.font = FontConfigManager.shared.getCharactorFont(size: 12)
        amountTipLabel.theme_textColor = GlobalPicker.textLightColor
        amountTipLabel.text = LocalizedString("Airdrop amount tip", comment: "")

        nextClaimDateTextField.font = FontConfigManager.shared.getDigitalFont(size: 14)
        nextClaimDateTextField.theme_tintColor = GlobalPicker.contrastTextColor
        nextClaimDateTextField.contentMode = UIViewContentMode.bottom
        nextClaimDateTextField.setLeftPaddingPoints(8)
        nextClaimDateTextField.isUserInteractionEnabled = false
        
        nextClaimDateTipLabel.font = FontConfigManager.shared.getCharactorFont(size: 12)
        nextClaimDateTipLabel.theme_textColor = GlobalPicker.textLightColor
        nextClaimDateTipLabel.text = LocalizedString("Airdrop next claim date", comment: "")

        nextClaimDateTextField.isHidden = true
        nextClaimDateTipLabel.isHidden = true
        
        claimButton.title = LocalizedString("Airdrop button", comment: "")
        claimButton.isHidden = false
        forwardButton.title = LocalizedString("Airdrop forward", comment: "")

        contentViewHeightLayoutConstraint.constant = heightForClaim
        
        scrollView.delegate = self
        scrollView.delaysContentTouches = false
        
        // No need to scroll the scrollView at this moment.
        scrollView.isScrollEnabled = false
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if isFirtTimeAppear {
            setupContent()
            isFirtTimeAppear = false
        }
    }

    func setupContent() {
        SVProgressHUD.show(LocalizedString("Loading Data", comment: ""), maxTime: 10)
        SVProgressHUD.setMaximumDismissTimeInterval(10)
        SendCurrentAppWalletDataManager.shared._getBindAddress { (result, _) in
            DispatchQueue.main.async {
                if let result = result {
                    self.bindAddress = result
                    self.addressTextField.text = self.bindAddress
                    NeoAPIRequest.neo_getAmount(bindAddress: result, completion: { (result, _) -> Void in
                        DispatchQueue.main.async {
                            if let result = result {
                                let value = Asset.getAmount(fromWeiAmount: result.stack[0].value, of: 8)
                                self.bindAmount = value?.withCommas(4)
                                self.amountTextField.text = self.bindAmount
                                _ = self.validateTime()
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
                    banner.duration = 3.0
                    banner.show()
                    self.claimButton.isHidden = false
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
        if bindAmount == nil || !bindAmount!.isDouble || Double(bindAmount!) == 0 {
            self.amountTextField.text = "0.0000"
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
        let date = UserDefaults.standard.object(forKey: "\(UserDefaultsKeys.airdropDate.rawValue)\(self.bindAddress!)")
        if let date = date as? Date {
            // The date has been added a 24 hours.
            let target = Calendar.current.date(byAdding: .hour, value: 24, to: date)
            if Date() < target! {
                self.claimButton.isHidden = true
                self.forwardButton.isHidden = false
                self.bindAmount = "0"
                result = false
                contentViewHeightLayoutConstraint.constant = heightForComingBackTomorrow
                nextClaimDateTextField.isHidden = false
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                let stringGivenDate = dateFormatter.string(from: target!)
                nextClaimDateTextField.text = stringGivenDate
                nextClaimDateTipLabel.isHidden = false
            } else {
                self.claimButton.isHidden = false
                self.forwardButton.isHidden = true
                contentViewHeightLayoutConstraint.constant = heightForComingBackTomorrow
                nextClaimDateTextField.isHidden = false
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                let stringGivenDate = dateFormatter.string(from: date)
                nextClaimDateTextField.text = stringGivenDate
                nextClaimDateTipLabel.text = LocalizedString("Airdrop last claim date", comment: "")
                nextClaimDateTipLabel.isHidden = false
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
                    UserDefaults.standard.setValue(result, forKey: "\(UserDefaultsKeys.airdropTxID.rawValue)\(self.bindAddress!)")
                    UserDefaults.standard.setValue(Date(), forKey: "\(UserDefaultsKeys.airdropDate.rawValue)\(self.bindAddress!)")
                    let notificationTitle = LocalizedString("Airdrop success", comment: "")
                    let banner = NotificationBanner.generate(title: notificationTitle, style: .success)
                    banner.duration = 3.0
                    banner.show()
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
        if let url = UserDefaults.standard.string(forKey: "\(UserDefaultsKeys.airdropTxID.rawValue)\(self.bindAddress!)") {
            let viewController = DefaultWebViewController()
            viewController.navigationTitle = LocalizedString("Airdrop browser", comment: "")
            viewController.url = URL.init(string: "https://neotracker.io/tx/\(url.drop0x())")
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
}
