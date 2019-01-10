//
//  NewsNavigationViewController_v2.swift
//  loopr-ios
//
//  Created by xiaoruby on 1/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NewsNavigationViewController_v2: UINavigationController {

    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.shadowImage = UIImage()
        let viewController = NewsViewController_v2(nibName: nil, bundle: nil)
        self.setViewControllers([viewController], animated: false)
        view.theme_backgroundColor = ["#fff", "#000"]
        
        let window = UIApplication.shared.keyWindow
        let topPadding = window?.safeAreaInsets.top ?? 0
        
        GarlandConfig.shared.animationDuration = 0.3
        GarlandConfig.shared.cardsSpacing = 8
        GarlandConfig.shared.cardRadius = 6
        GarlandConfig.shared.headerVerticalOffset = 0 // topPadding + 10  // the top padding to the screen
        GarlandConfig.shared.headerSize = CGSize(width: UIScreen.main.bounds.width - 15*2, height: 200)
        GarlandConfig.shared.fakeHeaderSize = CGSize(width: 8*2, height: 200*0.8)
        GarlandConfig.shared.cardsSize = CGSize(width: UIScreen.main.bounds.width - 15*2 - 6, height: 190)
        GarlandConfig.shared.cardShadowOffset = CGSize(width: 0, height: 2)
        GarlandConfig.shared.backgroundHeaderColor = .clear
        // GarlandConfig.shared.fakeHeaderColor = UIColor.theme.lighter(by: 10) ?? UIColor.theme
        GarlandConfig.shared.fakeHeaderColor = UIColor(rgba: "#21203A")
    }

}
