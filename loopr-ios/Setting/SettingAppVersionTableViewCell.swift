//
//  SettingAppVersionTableViewCell.swift
//  loopr-ios
//
//  Created by Ruby on 12/22/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class SettingAppVersionTableViewCell: UITableViewCell {

    @IBOutlet weak var leftLabel: UILabel!
    @IBOutlet weak var updateNotificationLabel: UILabel!
    @IBOutlet weak var rightLabel: UILabel!
    @IBOutlet weak var seperateLineUp: UIView!
    @IBOutlet weak var seperateLineDown: UIView!

    @IBOutlet weak var leftLabelWidthLayoutConstraint: NSLayoutConstraint!
    @IBOutlet weak var updateNotificationWidthLayoutConstraint: NSLayoutConstraint!
    @IBOutlet weak var leadingSeperateLineDown: NSLayoutConstraint!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        seperateLineUp.backgroundColor = UIColor.dark3
        seperateLineDown.backgroundColor = UIColor.dark3
        
        theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        leftLabel.font = FontConfigManager.shared.getRegularFont(size: 14)
        leftLabel.theme_textColor = GlobalPicker.textColor
        
        updateNotificationLabel.font = FontConfigManager.shared.getRegularFont(size: 10)
        updateNotificationLabel.textColor = .white
        updateNotificationLabel.textAlignment = .center
        updateNotificationLabel.backgroundColor = UIColor(named: "Color-red")!
        updateNotificationLabel.cornerRadius = 4
        updateNotificationLabel.clipsToBounds = true

        rightLabel.font = FontConfigManager.shared.getRegularFont(size: 14)
        rightLabel.theme_textColor = GlobalPicker.textLightColor
    }

    override func setHighlighted(_ highlighted: Bool, animated: Bool) {
        // TODO: why updateNotificationLabel background color has to been set again?
        updateNotificationLabel.backgroundColor = UIColor(named: "Color-red")!
        if highlighted {
            theme_backgroundColor = ColorPicker.cardHighLightColor
        } else {
            theme_backgroundColor = ColorPicker.cardBackgroundColor
        }
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        updateNotificationLabel.backgroundColor = UIColor(named: "Color-red")!
    }

    func update(indexPath: IndexPath, isLastCell: Bool) {
        leftLabel.text = LocalizedString("App Version", comment: "")
        leftLabelWidthLayoutConstraint.constant = leftLabel.text!.widthOfString(usingFont: leftLabel.font) + 4

        updateNotificationLabel.text = LocalizedString("update", comment: "")
        updateNotificationWidthLayoutConstraint.constant = updateNotificationLabel.text!.widthOfString(usingFont: updateNotificationLabel.font) + 6

        if indexPath.row == 0 {
            seperateLineUp.isHidden = false
        } else {
            seperateLineUp.isHidden = true
        }
        
        if isLastCell {
            leadingSeperateLineDown.constant = 0
        } else {
            leadingSeperateLineDown.constant = 15
        }
        
        if AppServiceUpdateManager.shared.shouldDisplayUpdateNotificationInSettingViewController() {
            updateNotificationLabel.isHidden = false
            isUserInteractionEnabled = true
        } else {
            updateNotificationLabel.isHidden = true
            isUserInteractionEnabled = false
        }
        
        // TODO: move this part to init method
        rightLabel.text = AppServiceUpdateManager.shared.getAppVersionAndBuildVersion()
    }
    
    class func getCellIdentifier() -> String {
        return "SettingAppVersionTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 51
    }

}
