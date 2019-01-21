//
//  MainTabControllerDropdown.swift
//  loopr-ios
//
//  Created by ruby on 1/20/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation
import UIKit
import MKDropdownMenu

extension MainTabController: MKDropdownMenuDataSource {

    func numberOfComponents(in dropdownMenu: MKDropdownMenu) -> Int {
        return 1
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, numberOfRowsInComponent component: Int) -> Int {
        return 1
    }
}

extension MainTabController: MKDropdownMenuDelegate {
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, rowHeightForComponent component: Int) -> CGFloat {
        return 50
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, viewForRow row: Int, forComponent component: Int, reusing view: UIView?) -> UIView {
        let baseView = UIView(frame: CGRect(x: 0, y: 0, width: 160, height: 50))
        baseView.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        let iconImageView = UIImageView(frame: CGRect(x: 21, y: 12, width: 24, height: 24))
        iconImageView.contentMode = .scaleAspectFit
        baseView.addSubview(iconImageView)
        
        let titleLabel = UILabel(frame: CGRect(x: 55, y: 0, width: 610-55, height: 50))
        titleLabel.font = FontConfigManager.shared.getRegularFont(size: 16)
        titleLabel.theme_textColor = GlobalPicker.textColor
        baseView.addSubview(titleLabel)
        
        var icon: UIImage?
        switch row {
        case 0:
            titleLabel.text = LocalizedString("Scan", comment: "")
            icon = UIImage.init(named: "dropdown-scan")
        case 1:
            titleLabel.text = LocalizedString("Add Token", comment: "")
            icon = UIImage.init(named: "dropdown-add-token")
        case 2:
            titleLabel.text = LocalizedString("Wallet", comment: "")
            icon = UIImage.init(named: "dropdown-wallet")
        default:
            break
        }
        
        iconImageView.image = icon
        
        return baseView
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, didSelectRow row: Int, inComponent component: Int) {
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, backgroundColorForHighlightedRowsInComponent component: Int) -> UIColor? {
        return UIColor.dark4
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, didCloseComponent component: Int) {
        
    }
    
}
