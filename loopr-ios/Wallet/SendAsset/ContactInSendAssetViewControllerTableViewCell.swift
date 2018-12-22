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
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
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
