//
//  AirdropViewController.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/19.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class AirdropViewController: UIViewController, UIScrollViewDelegate {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var addressTextField: UITextField!
    @IBOutlet weak var addressTipLabel: UILabel!
    @IBOutlet weak var amountTextField: UITextField!
    @IBOutlet weak var amountTipLabel: UILabel!
    @IBOutlet weak var claimButton: GradientButton!
    
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
        
        addressTipLabel.font = FontConfigManager.shared.getCharactorFont(size: 12)
        addressTipLabel.theme_textColor = GlobalPicker.textLightColor
        addressTipLabel.text = LocalizedString("Airdrop address tip", comment: "")
        
        amountTipLabel.font = FontConfigManager.shared.getCharactorFont(size: 12)
        amountTipLabel.theme_textColor = GlobalPicker.textLightColor
        amountTipLabel.text = LocalizedString("Airdrop amount tip", comment: "")
        
        claimButton.title = LocalizedString("Airdrop button", comment: "")
        
        scrollView.delegate = self
        scrollView.delaysContentTouches = false
    }

    @IBAction func pressedClaimButton(_ sender: Any) {
        SendCurrentAppWalletDataManager.shared._getBindAddress();
    }
    
}

/*
 "Airdrop title" = "LRN 空投領取";
 "Airdrop address_tip" = "您的LRN空投領取地址";
 "Airdrop amount_tip" = "您本日的LRN空投領取金額";
 "Airdrop button" = "領取空投";
 "Airdrop success" = "成功領取空投";
 "Airdrop failed" = "領取空投失敗，請稍後重試";
 "Airdrop forward" = "前往NEO瀏覽器查看詳情";
 "Airdrop no_bind" = "您尚未绑定LRN的空投地址，目前无法领取";
 "Airdrop time_invalid" = "距您上次领取，还未到一天，请稍后领取";
 "Airdrop browser" = "NEO Tracker";
 
 */
