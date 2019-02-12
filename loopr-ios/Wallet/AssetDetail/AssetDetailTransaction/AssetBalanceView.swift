//
//  AssetBalanceView.swift
//  loopr-ios
//
//  Created by ruby on 1/26/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class AssetBalanceView: UIView {

    var baseView: UIImageView = UIImageView(frame: .zero)
    let balanceLabel: UILabel = UILabel(frame: .zero)
    let currencyLabel: UILabel = UILabel(frame: .zero)
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        theme_backgroundColor = ColorPicker.backgroundColor
        
        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        
        baseView.frame = CGRect(x: 15, y: 10, width: screenWidth - 30, height: 120)
        baseView.image = UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())
        baseView.contentMode = .scaleToFill
        addSubview(baseView)
        
        balanceLabel.frame = CGRect(x: 10, y: 40, width: screenWidth - 20, height: 36)
        balanceLabel.font = FontConfigManager.shared.getMediumFont(size: 32)
        balanceLabel.textColor = UIColor.white
        balanceLabel.textAlignment = .center
        
        addSubview(balanceLabel)
        
        currencyLabel.frame = CGRect(x: 10, y: balanceLabel.frame.maxY, width: screenWidth - 20, height: 30)
        currencyLabel.font = FontConfigManager.shared.getRegularFont(size: 20)
        currencyLabel.textColor = UIColor.init(rgba: "#ffffffcc")
        currencyLabel.textAlignment = .center
        
        addSubview(currencyLabel)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    func update(asset: Asset?) {
        balanceLabel.text = asset?.display
        currencyLabel.text = asset?.currency
    }
}
