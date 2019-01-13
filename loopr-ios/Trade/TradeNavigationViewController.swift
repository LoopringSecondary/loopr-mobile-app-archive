//
//  TradeViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/9/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class TradeNavigationViewController: UINavigationController {

    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.shadowImage = UIImage()
        let viewController = TradeViewController()
        self.setViewControllers([viewController], animated: false)
    }

}
