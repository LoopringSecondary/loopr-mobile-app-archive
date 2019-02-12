//
//  UIRefreshController.swift
//  loopr-ios
//
//  Created by Ruby on 1/1/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

extension UIRefreshControl {
    
    func updateUIStyle(withTitle title: String) {
        self.theme_tintColor = GlobalPicker.textColor
        let lastPullRefreshString: String = title
        let attributedString = NSMutableAttributedString(string: lastPullRefreshString)
        attributedString.addAttribute(NSAttributedStringKey.foregroundColor, value: UIColor(rgba: "#ffffffcc"), range: NSRange(location: 0, length: lastPullRefreshString.count))
        attributedString.addAttribute(NSAttributedStringKey.font, value: FontConfigManager.shared.getRegularFont(size: 12), range: NSRange(location: 0, length: lastPullRefreshString.count))
        self.attributedTitle = attributedString
    }
    
    func endRefreshing(refreshControlType: RefreshControlType) {
        endRefreshing()
        RefreshControlDataManager.shared.set(type: refreshControlType)
        updateUIStyle(withTitle: RefreshControlDataManager.shared.get(type: refreshControlType))
    }
    
}
