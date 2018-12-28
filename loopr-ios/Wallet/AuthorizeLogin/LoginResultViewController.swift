//
//  LoginResultViewController.swift
//  loopr-ios
//
//  Created by kenshin on 2018/6/11.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import UIKit

class LoginResultViewController: UIViewController {
    
    @IBOutlet weak var resultLabel: UILabel!
    @IBOutlet weak var detailLabel: UILabel!
    @IBOutlet weak var doneButton: GradientButton!
    
    var result: Bool = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.hidesBackButton = true
        view.theme_backgroundColor = ColorPicker.backgroundColor
        
        resultLabel.font = FontConfigManager.shared.getMediumFont(size: 20)
        resultLabel.theme_textColor = GlobalPicker.textColor

        detailLabel.font = FontConfigManager.shared.getMediumFont(size: 14)
        detailLabel.theme_textColor = GlobalPicker.textColor
        
        doneButton.title = LocalizedString("Done", comment: "")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if result {
            resultLabel.text = LocalizedString("Authorization Successful", comment: "")
            detailLabel.text = LocalizedString("Authorize_Successfully", comment: "")
        } else {
            resultLabel.text = LocalizedString("Authorization Failed", comment: "")
            detailLabel.text = LocalizedString("Authorize_Failed", comment: "")
        }
    }

    @IBAction func pressedDoneButton(_ sender: UIButton) {
        self.navigationController?.popToRootViewController(animated: true)
    }
}
