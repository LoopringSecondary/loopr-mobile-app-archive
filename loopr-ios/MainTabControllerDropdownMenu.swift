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
        let baseView = UIView(frame: CGRect(x: 0, y: 0, width: 110, height: 50))
        baseView.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        let smallFontButton = UIButton(type: UIButtonType.custom)
        smallFontButton.setImage(UIImage(named: "Font-adjust-item-small"), for: .normal)
        smallFontButton.setImage(UIImage(named: "Font-adjust-item-small")?.alpha(0.3), for: .highlighted)
        smallFontButton.addTarget(self, action: #selector(pressedSmallFontButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        smallFontButton.frame = CGRect(x: 0, y: 0, width: 50, height: 50)
        baseView.addSubview(smallFontButton)
        
        let largeFontButton = UIButton(type: UIButtonType.custom)
        largeFontButton.setImage(UIImage(named: "Font-adjust-item-large"), for: .normal)
        largeFontButton.setImage(UIImage(named: "Font-adjust-item-large")?.alpha(0.3), for: .highlighted)
        largeFontButton.addTarget(self, action: #selector(pressedLargeFontButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        largeFontButton.frame = CGRect(x: baseView.width - 50, y: 0, width: 50, height: 50)
        baseView.addSubview(largeFontButton)

        return baseView
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, didSelectRow row: Int, inComponent component: Int) {
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, backgroundColorForHighlightedRowsInComponent component: Int) -> UIColor? {
        return UIColor.dark4
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, didOpenComponent component: Int) {
        isDropdownMenuExpanded = true
    }
    
    func dropdownMenu(_ dropdownMenu: MKDropdownMenu, didCloseComponent component: Int) {
        hideDropdownMenu()
        isDropdownMenuExpanded = false
    }
    
    @objc func pressedSmallFontButton(_ button: UIBarButtonItem) {
        print("pressed pressedSmallFontButton")
        NewsUIStyleConfig.shared.setNewsDetailBodyFont(isSmall: true)
    }
    
    @objc func pressedLargeFontButton(_ button: UIBarButtonItem) {
        print("pressed pressedLargeFontButton")
        NewsUIStyleConfig.shared.setNewsDetailBodyFont(isSmall: false)
    }

}
