//
//  TradeSelectionNavigationViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 8/8/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class TradeSelectionNavigationViewController: UINavigationController {

    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.shadowImage = UIImage()
        let tradeSelectionViewController = TradeSelectionViewController()
        setViewControllers([tradeSelectionViewController], animated: false)
    }

}
