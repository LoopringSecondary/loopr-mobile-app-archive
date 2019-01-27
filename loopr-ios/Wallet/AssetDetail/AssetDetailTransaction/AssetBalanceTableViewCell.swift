//
//  AssetBalanceTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 1/26/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class AssetBalanceTableViewCell: UITableViewCell {

    override func awakeFromNib() {
        super.awakeFromNib()
        
        selectionStyle = .none
        backgroundColor = .clear
    }
    
    class func getCellIdentifier() -> String {
        return "AssetBalanceTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 134
    }

}
