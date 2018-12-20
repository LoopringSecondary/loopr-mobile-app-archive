//
//  ContactTableViewCell.swift
//  loopr-ios
//
//  Created by Ruby on 12/16/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class ContactTableViewCell: UITableViewCell {

    var iconView: IconView = IconView()
    var nameLabel: UILabel = UILabel()
    var addressLabel: UILabel = UILabel()

    @IBOutlet weak var upSeperateLine: UIView!
    @IBOutlet weak var bottomSeperateLine: UIView!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        selectionStyle = .none
        theme_backgroundColor = ColorPicker.backgroundColor
        
        upSeperateLine.theme_backgroundColor = ColorPicker.cardHighLightColor
        bottomSeperateLine.theme_backgroundColor = ColorPicker.cardHighLightColor

        iconView = IconView(frame: CGRect.init(x: 30, y: 16, width: 36, height: 36))
        iconView.symbolLabelFont = FontConfigManager.shared.getRegularFont(size: 20)
        iconView.backgroundColor = UIColor.clear
        addSubview(iconView)
        
        nameLabel.frame = CGRect.init(x: 82, y: 16, width: 200, height: 17)
        nameLabel.font = FontConfigManager.shared.getMediumFont(size: 14)
        nameLabel.theme_textColor = GlobalPicker.textColor
        nameLabel.text = "ETHETHETHETHETHETHETH"  // Prototype the label size. Will be updated very soon.
        nameLabel.sizeToFit()
        nameLabel.text = ""
        addSubview(nameLabel)
        
        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        
        addressLabel.frame = CGRect.init(x: nameLabel.frame.minX, y: 36, width: screenWidth - nameLabel.frame.minX - 30, height: 16)
        addressLabel.font = FontConfigManager.shared.getRegularFont(size: 13)
        addressLabel.theme_textColor = GlobalPicker.textLightColor
        addressLabel.text = "ETHETHETHETHETHETHETH"
        // addressLabel.sizeToFit()
        addressLabel.text = ""
        addressLabel.lineBreakMode = .byTruncatingMiddle
        addSubview(addressLabel)
    }
    
    func update(contact: Contact) {
        iconView.setSymbol(contact.tag)
        iconView.symbol = contact.tag
        iconView.symbolLabel.text = contact.tag
        nameLabel.text = contact.name
        addressLabel.text = contact.address
    }

    class func getCellIdentifier() -> String {
        return "ContactTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 68
    }

}
