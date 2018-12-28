//
//  MarketNavigationViewController.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/2/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class MarketNavigationViewController: UINavigationController {

    override func viewDidLoad() {
        super.viewDidLoad()

        
        navigationBar.shadowImage = UIImage()

        let viewController = MarketSwipeViewController(nibName: nil, bundle: nil)
        self.setViewControllers([viewController], animated: false)
    }

}
