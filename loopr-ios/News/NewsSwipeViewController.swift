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
    @IBOutlet weak var headerView: UIView!
    
    // To avoid the tab bar
    @IBOutlet weak var navigationBar: UINavigationBar!
    var isCloseButton: Bool = true
    
    var viewControllers: [NewsNavigationViewController] = [NewsNavigationViewController(), NewsNavigationViewController()]
    
    var options = SwipeViewOptions.getDefault()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        headerView.theme_backgroundColor = ColorPicker.backgroundColor
        view.theme_backgroundColor = ColorPicker.backgroundColor

        options.swipeContentScrollView.isScrollEnabled = true
        setupChildViewControllers()
        setupCloseButtton()

        topConstraint = 44
        
        NotificationCenter.default.addObserver(self, selector: #selector(pushedNewsDetailViewControllerReceivedNotification), name: .pushedNewsDetailViewController, object: nil)
    }

    // Not firing if NewsSwipeViewController is addChildViewController
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        // Show the Navigation Bar
        // self.navigationController?.setNavigationBarHidden(true, animated: false)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        // Hide the Navigation Bar
        // self.navigationController?.setNavigationBarHidden(false, animated: false)
    }

    func setupChildViewControllers() {
        let vc0 = viewControllers[0]
        vc0.setCurrentIndex(0)

        let vc1 = viewControllers[1]
        vc1.setCurrentIndex(1)

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
    
    func setupCloseButtton() {
        isCloseButton = true

        let navigationItem = UINavigationItem(title: "")
        let button = UIButton(type: UIButtonType.custom)
        
        button.theme_setImage(GlobalPicker.close, forState: .normal)
        button.theme_setImage(GlobalPicker.closeHighlight, forState: .highlighted)
        
        // Default left padding is 20. It should be 12 in our design.
        button.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: -16, bottom: 0, right: 8)
        button.addTarget(self, action: #selector(closeButtonAction(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        button.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        
        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: button)
        navigationBar.setItems([navigationItem], animated: true)
        
        headerView.theme_backgroundColor = ColorPicker.backgroundColor
        navigationBar.theme_barTintColor = ColorPicker.backgroundColor        
    }
    
    func setupBackButton() {
        isCloseButton = false

        let navigationItem = UINavigationItem(title: "")
        let button = UIButton(type: UIButtonType.custom)
        
        button.theme_setImage(GlobalPicker.back, forState: .normal)
        button.theme_setImage(GlobalPicker.backHighlight, forState: .highlighted)

        // Default left padding is 20. It should be 12 in our design.
        button.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: -16, bottom: 0, right: 8)
        button.addTarget(self, action: #selector(closeButtonAction(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        button.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        
        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: button)
        navigationBar.setItems([navigationItem], animated: true)

        headerView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        navigationBar.theme_barTintColor = ColorPicker.cardBackgroundColor
    }

    @objc func pushedNewsDetailViewControllerReceivedNotification() {
        print("pushedNewsDetailViewControllerReceivedNotification")
        setupBackButton()
    }

    @objc fileprivate func closeButtonAction(_ button: UIBarButtonItem) {
        if isCloseButton {
            delegate?.closeButtonAction()
        } else {
            NotificationCenter.default.post(name: .tiggerPopNewsDetailViewController, object: nil)
            setupCloseButtton()
        }
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
