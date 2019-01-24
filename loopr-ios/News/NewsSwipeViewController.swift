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
    func setBottomTabBarHidden(_ newValue: Bool, animated: Bool)
}

class NewsSwipeViewController: SwipeViewController, UIScrollViewDelegate {

    weak var delegate: NewsSwipeViewControllerDelegate?
    
    // Setup the background color at the status bar
    @IBOutlet weak var headerView: UIView!
    
    // To avoid the tab bar
    @IBOutlet weak var navigationBar: UINavigationBar!
    var isNavigationBarHide: Bool = false
    
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
        NotificationCenter.default.addObserver(self, selector: #selector(willShowNewsViewControllerReceivedNotification), name: .willShowNewsViewController, object: nil)
        
        swipeView.swipeContentScrollView?.delegate = self
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
        vc0.newsNavigationViewControllerDelegate = self
        vc0.setCurrentIndex(0)

        let vc1 = viewControllers[1]
        vc1.newsNavigationViewControllerDelegate = self
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
        
        // button.theme_setImage(GlobalPicker.close, forState: .normal)
        // button.theme_setImage(GlobalPicker.closeHighlight, forState: .highlighted)
        button.setImage(UIImage(named: "News-close-dark"), for: .normal)
        button.setImage(UIImage(named: "News-close-dark")?.alpha(0.6), for: .normal)

        // Default left padding is 20. It should be 12 in our design.
        // button.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 8, bottom: 0, right: -16)
        button.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 6, bottom: 0, right: -14)
        button.addTarget(self, action: #selector(closeButtonAction(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        button.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(customView: button)
        
        // right is nil
        navigationItem.leftBarButtonItem = nil
        
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

        // Move the safari button to bottom tab bar.
        // Safari button
        let safariButton = UIButton(type: UIButtonType.custom)
        safariButton.setImage(UIImage(named: "Safari-item-button"), for: .normal)
        safariButton.setImage(UIImage(named: "Safari-item-button")?.alpha(0.3), for: .highlighted)
        safariButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 8, bottom: 0, right: -8)
        safariButton.addTarget(self, action: #selector(pressedSafariButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        safariButton.frame = CGRect(x: 0, y: 0, width: 23, height: 23)
        let shareBarButton = UIBarButtonItem(customView: safariButton)
        // navigationItem.rightBarButtonItem = shareBarButton
        navigationItem.rightBarButtonItem = nil

        navigationBar.setItems([navigationItem], animated: true)

        headerView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        navigationBar.theme_barTintColor = ColorPicker.cardBackgroundColor
    }
    
    @objc func willShowNewsViewControllerReceivedNotification() {
        setupCloseButtton()
        swipeView.swipeContentScrollView?.isScrollEnabled = true
    }

    @objc func pushedNewsDetailViewControllerReceivedNotification() {
        print("pushedNewsDetailViewControllerReceivedNotification")
        setupBackButton()
        swipeView.swipeContentScrollView?.isScrollEnabled = false
    }

    @objc fileprivate func closeButtonAction(_ button: UIBarButtonItem) {
        if isCloseButton {
            delegate?.closeButtonAction()
        } else {
            NotificationCenter.default.post(name: .tiggerPopNewsDetailViewController, object: nil)
            setupCloseButtton()
        }
    }
    
    @objc func pressedSafariButton(_ button: UIBarButtonItem) {
        let news = NewsDataManager.shared.getCurrentInformationItem()
        if let url = URL(string: news.url) {
            UIApplication.shared.open(url)
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
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        // UIView.animate(withDuration: 0.3) {
            self.viewControllers[0].viewController.rightFakeView.alpha = 0
            self.viewControllers[1].viewController.leftFakeView.alpha = 0
        // }
    }

    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        UIView.animate(withDuration: 0.6) {
            self.viewControllers[0].viewController.rightFakeView.alpha = 1
            self.viewControllers[1].viewController.leftFakeView.alpha = 1
        }
    }

}

extension NewsSwipeViewController: NewsNavigationViewControllerDelegate {

    func setNavigationBarHidden(_ newValue: Bool, animated: Bool) {
        
        // Send data to MainTabController
        delegate?.setBottomTabBarHidden(newValue, animated: animated)
        
        if newValue {
            if !isNavigationBarHide {
                if animated {
                    UIView.animate(withDuration: 0.5, delay: 0, options: .curveEaseInOut, animations: {
                        self.swipeView.frame = CGRect(x: 0, y: self.swipeView.y - 44, width: self.swipeView.width, height: self.swipeView.height)
                    }) { (_) in
                        
                    }
                } else {
                    self.swipeView.frame = CGRect(x: 0, y: self.swipeView.y - 44, width: self.swipeView.width, height: self.swipeView.height)
                }
            }
        } else {
            if isNavigationBarHide {
                if animated {
                    UIView.animate(withDuration: 0.5, delay: 0, options: .curveEaseInOut, animations: {
                        self.swipeView.frame = CGRect(x: 0, y: self.swipeView.y + 44, width: self.swipeView.width, height: self.swipeView.height)
                    }) { (_) in
                        
                    }
                } else {
                    self.swipeView.frame = CGRect(x: 0, y: self.swipeView.y + 44, width: self.swipeView.width, height: self.swipeView.height)
                }
            }
        }
        isNavigationBarHide = newValue
    }

    func setNavigationBarTitle(_ newValue: String) {
        // The implementation is not reliable
        /*
        guard newValue != "" else {
            navigationBar.topItem?.title = ""
            return
        }

        if newValue.count <= 16 {
            navigationBar.topItem?.title = newValue
        } else {
            navigationBar.topItem?.title = newValue.substring(toIndex: 16) + "..."
        }
        */
    }
}
