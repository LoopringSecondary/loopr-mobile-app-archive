//
//  ContactInSendAssetViewControllerTableViewCell.swift
//  loopr-ios
//
//  Created by Ruby on 12/21/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class ContactInSendAssetViewControllerTableViewCell: UITableViewCell {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var seperateLine: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        selectionStyle = .none
        
        seperateLine.theme_backgroundColor = ColorPicker.cardBackgroundColor
        nameLabel.lineBreakMode = .byTruncatingMiddle
        nameLabel.font = FontConfigManager.shared.getMediumFont(size: 14)
        // nameLabel.theme_textColor = GlobalPicker.textColor
        
        addressLabel.lineBreakMode = .byTruncatingMiddle
        addressLabel.font = FontConfigManager.shared.getMediumFont(size: 14)
        // addressLabel.theme_textColor = GlobalPicker.textLightColor
    }
    
    override func setHighlighted(_ highlighted: Bool, animated: Bool) {
        if highlighted {
            backgroundColor = UIColor.dark5
        } else {
            backgroundColor = .white
        }
    }
    
    func update(contact: Contact) {
        nameLabel.text = contact.name
        addressLabel.text = contact.address
    }

    class func getCellIdentifier() -> String {
        return "ContactInSendAssetViewControllerTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 48
    }
}
