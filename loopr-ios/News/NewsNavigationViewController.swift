//
//  NewsNavigationViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 1/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NewsNavigationViewController: UINavigationController {

    var viewController: NewsSwipeViewController = NewsSwipeViewController(nibName: nil, bundle: nil)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.shadowImage = UIImage()
        self.setViewControllers([viewController], animated: false)
        view.theme_backgroundColor = ColorPicker.backgroundColor
    }

}
