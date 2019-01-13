//
//  WalletNavigationViewController.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class WalletNavigationViewController: UINavigationController {

    var viewController: WalletViewController = WalletViewController(nibName: nil, bundle: nil)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.shadowImage = UIImage()
        self.setViewControllers([viewController], animated: false)
        view.theme_backgroundColor = ColorPicker.backgroundColor
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
    }

    func processExternalUrl() {
        viewController.processExternalUrl()
    }
}
