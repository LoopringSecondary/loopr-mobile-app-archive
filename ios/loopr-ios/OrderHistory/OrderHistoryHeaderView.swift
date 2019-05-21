//
//  OrderHistoryHeaderView.swift
//  loopr-ios
//
//  Created by ruby on 2/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

extension UIView {

    class func getOrderHistoryHeaderView() -> UIView {
        let screenWidth = UIScreen.main.bounds.width
        
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: screenWidth, height: 30+0.5))
        headerView.theme_backgroundColor = ColorPicker.backgroundColor
        
        let baseView = UIView(frame: CGRect(x: 15, y: 0, width: screenWidth - 15*2, height: 30))
        baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        baseView.round(corners: [.topLeft, .topRight], radius: 6)
        headerView.addSubview(baseView)
        
        let labelWidth = (screenWidth-15*2)/3
        let paddingX: CGFloat = 10
        
        let label1 = UILabel(frame: CGRect(x: paddingX, y: 0, width: labelWidth, height: 30))
        label1.theme_textColor = GlobalPicker.textLightColor
        label1.font = FontConfigManager.shared.getCharactorFont(size: 13)
        label1.text = LocalizedString("Market/Price", comment: "")
        label1.textAlignment = .left
        baseView.addSubview(label1)
        
        let label2Width = LocalizedString("Amount/Filled", comment: "").textWidth(font: FontConfigManager.shared.getCharactorFont(size: 13))
        let label2 = UILabel(frame: CGRect(x: (UIScreen.main.bounds.width-15*2)*0.5-label2Width*0.5, y: 0, width: labelWidth, height: 30))
        label2.theme_textColor = GlobalPicker.textLightColor
        label2.font = FontConfigManager.shared.getCharactorFont(size: 13)
        label2.text = LocalizedString("Amount/Filled", comment: "")
        label2.textAlignment = .left
        baseView.addSubview(label2)
        
        let label3 = UILabel(frame: CGRect(x: baseView.width - labelWidth - 10, y: 0, width: labelWidth, height: 30))
        label3.theme_textColor = GlobalPicker.textLightColor
        label3.font = FontConfigManager.shared.getCharactorFont(size: 13)
        label3.text = LocalizedString("Status/Date", comment: "")
        label3.textAlignment = .right
        baseView.addSubview(label3)
        
        return headerView
    }
    
}
