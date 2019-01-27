//
//  NoDataCollectionViewCell.swift
//  loopr-ios
//
//  Created by ruby on 1/26/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NoDataCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var noDataImageView: UIImageView!
    @IBOutlet weak var noDataLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()

        theme_backgroundColor = ColorPicker.backgroundColor
        contentView.theme_backgroundColor = ColorPicker.backgroundColor

        self.backgroundColor = .clear
        noDataLabel.font = FontConfigManager.shared.getCharactorFont(size: 14)
        noDataLabel.theme_textColor = GlobalPicker.textLightColor
    }

    class func getCellIdentifier() -> String {
        return "NoDataCollectionViewCell"
    }

}
