//
//  WalletNavigationViewController.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class WalletNavigationViewController: UINavigationController {

    var viewController: UIViewController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.shadowImage = UIImage()
        viewController = WalletViewController(nibName: nil, bundle: nil)
        self.setViewControllers([viewController], animated: false)
        view.theme_backgroundColor = ["#fff", "#000"]
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
    }

    func processExternalUrl() {
        if let vc = viewController as? WalletViewController {
            vc.processExternalUrl()
        }
    }
}
