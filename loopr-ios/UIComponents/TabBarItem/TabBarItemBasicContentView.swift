//
//  TabBarItemBasicContentView.swift
//  loopr-ios
//
//  Created by xiaoruby on 6/13/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import ESTabBarController_swift

class TabBarItemBasicContentView: ESTabBarItemContentView {
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        renderingMode = .alwaysOriginal
        imageView.contentMode = .center
        
        // textColor = UIColor.black
        highlightTextColor = Themes.isDark() ? UIColor.white : UIColor.black
        // iconColor = UIColor.black
        highlightIconColor = Themes.isDark() ? UIColor.white : UIColor.black
    }
    
    public required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

}
