//
//  NewsViewController_v2.swift
//  loopr-ios
//
//  Created by xiaoruby on 1/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

protocol NewsSwipeViewControllerDelegate: class {
    func closeButtonAction()
}

class NewsSwipeViewController: SwipeViewController {

    weak var delegate: NewsSwipeViewControllerDelegate?

    var viewControllers: [NewsViewController] = []
    
    var options = SwipeViewOptions.getDefault()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.theme_backgroundColor = ColorPicker.backgroundColor

        options.swipeContentScrollView.isScrollEnabled = true
        setupChildViewControllers()
        
        // Back button
        let backButton = UIButton(type: UIButtonType.custom)
        
        backButton.theme_setImage(GlobalPicker.close, forState: .normal)
        backButton.theme_setImage(GlobalPicker.closeHighlight, forState: .highlighted)
        
        // Default left padding is 20. It should be 12 in our design.
        backButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: -16, bottom: 0, right: 8)
        backButton.addTarget(self, action: #selector(closeButtonAction(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        backButton.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        
        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: backButton)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        // Show the Navigation Bar
        self.navigationController?.setNavigationBarHidden(true, animated: false)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(true)
        // Hide the Navigation Bar
        // self.navigationController?.setNavigationBarHidden(false, animated: false)
    }

    func setupChildViewControllers() {
        let vc0 = NewsViewController()
        vc0.currentIndex = 0

        let vc1 = NewsViewController()
        vc1.currentIndex = 1

        viewControllers = [vc0, vc1]
        for viewController in viewControllers {
            self.addChildViewController(viewController)
        }
        
        if Themes.isDark() {
            options.swipeTabView.itemView.textColor = UIColor(rgba: "#ffffff66")
            options.swipeTabView.itemView.selectedTextColor = UIColor(rgba: "#ffffffcc")
        } else {
            options.swipeTabView.itemView.textColor = UIColor(rgba: "#00000099")
            options.swipeTabView.itemView.selectedTextColor = UIColor(rgba: "#000000cc")
        }
        options.swipeContentScrollView.isScrollEnabled = true
        options.swipeTabView.height = 0
        options.swipeTabView.underlineView.height = 0
        swipeView.reloadData(options: options)
    }
    
    @objc fileprivate func closeButtonAction(_ button: UIBarButtonItem) {
        delegate?.closeButtonAction()
    }
    
    // MARK: - DataSource
    override func numberOfPages(in swipeView: SwipeView) -> Int {
        return viewControllers.count
    }
    
    override func swipeView(_ swipeView: SwipeView, titleForPageAt index: Int) -> String {
        return ""
    }
    
    override func swipeView(_ swipeView: SwipeView, viewControllerForPageAt index: Int) -> UIViewController {
        return viewControllers[index]
    }
}
