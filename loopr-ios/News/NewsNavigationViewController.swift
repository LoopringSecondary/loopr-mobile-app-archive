//
//  NewsNavigationViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 1/13/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NewsNavigationViewController: UINavigationController {
    
    var viewController: NewsViewController = NewsViewController()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.shadowImage = UIImage()
        self.setViewControllers([viewController], animated: false)
        view.backgroundColor = UIColor.red
        
        // Hide navigation bar
        self.setNavigationBarHidden(true, animated: false)
    }
    
    func setCurrentIndex(_ index: Int) {
        viewController.currentIndex = index
    }

}
