//
//  NewsViewController_v2.swift
//  loopr-ios
//
//  Created by xiaoruby on 1/9/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class NewsViewController_v2: SwipeViewController {

    private var viewControllers: [NewsViewController_v3] = []
    
    var options = SwipeViewOptions.getDefault()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.theme_backgroundColor = ColorPicker.backgroundColor

        options.swipeContentScrollView.isScrollEnabled = true
        setupChildViewControllers()
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
        let vc0 = NewsViewController_v3()
        vc0.currentIndex = 0

        let vc1 = NewsViewController_v3()
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
