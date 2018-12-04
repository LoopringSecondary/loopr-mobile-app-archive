//
//  SettingPasscodeTableViewCell.swift
//  loopr-ios
//
//  Created by xiaoruby on 5/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class SettingPasscodeTableViewCell: UITableViewCell {

    @IBOutlet weak var passcodeLabel: UILabel!
    @IBOutlet weak var passcodeSwitch: UISwitchCustom!
    @IBOutlet weak var seperateLineUp: UIView!
    @IBOutlet weak var seperateLineDown: UIView!
    @IBOutlet weak var trailingSeperateLineDown: NSLayoutConstraint!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        seperateLineUp.backgroundColor = UIColor.dark3
        seperateLineDown.backgroundColor = UIColor.dark3
        
        passcodeLabel.font = FontConfigManager.shared.getRegularFont(size: 14)
        passcodeLabel.theme_textColor = GlobalPicker.textColor
        backgroundColor = Themes.isDark() ? UIColor.dark2 : UIColor.white
        
        passcodeLabel.text = BiometricType.get().description
        
        passcodeSwitch.transform = CGAffineTransform(scaleX: 0.77, y: 0.77)
        passcodeSwitch.setOn(AuthenticationDataManager.shared.getPasscodeSetting(), animated: false)
        passcodeSwitch.onTintColor = UIColor.theme
        passcodeSwitch.OffTint = UIColor.dark4
        
        if AuthenticationDataManager.shared.devicePasscodeEnabled() {
            passcodeSwitch.isHidden = false
            selectionStyle = .none
        } else {
            passcodeSwitch.isHidden = true
        }
    }
    
    @IBAction func togglePasscodeSwitch(_ sender: Any) {
        print("togglePasscodeSwitch")
        AuthenticationDataManager.shared.setPasscodeSetting(passcodeSwitch.isOn)
    }

    class func getCellIdentifier() -> String {
        return "SettingThemeModeTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 45
    }

}
