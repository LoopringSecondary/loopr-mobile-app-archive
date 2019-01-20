//
//  NewsNavigationViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 1/13/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

protocol NewsNavigationViewControllerDelegate: class {
    func setNavigationBarHidden(_ newValue: Bool, animated: Bool)
    func setNavigationBarTitle(_ newValue: String)
}

class NewsNavigationViewController: UINavigationController {
    
    weak var newsNavigationViewControllerDelegate: NewsNavigationViewControllerDelegate?
    var viewController: NewsViewController = NewsViewController()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationBar.shadowImage = UIImage()
        self.setViewControllers([viewController], animated: false)
        viewController.newsViewControllerDelegate = self
        view.theme_backgroundColor = ColorPicker.backgroundColor

        // Hide navigation bar
        self.setNavigationBarHidden(true, animated: false)
    }
    
    func setCurrentIndex(_ index: Int) {
        viewController.currentIndex = index
    }

}

extension NewsNavigationViewController: NewsViewControllerDelegate {

    func newsViewControllerSetNavigationBarHidden(_ newValue: Bool, animated: Bool) {
        newsNavigationViewControllerDelegate?.setNavigationBarHidden(newValue, animated: animated)
    }

    func setNavigationBarTitle(_ newValue: String) {
        newsNavigationViewControllerDelegate?.setNavigationBarTitle(newValue)
    }

}
