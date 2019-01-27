//
//  MainTabController.swift
//  loopr-ios
//
//  Created by Matthew Cox on 2/3/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import UserNotifications
import Crashlytics
import Social
// import ESTabBarController_swift

// ESTabBarController
class MainTabController: UITabBarController, UNUserNotificationCenterDelegate {
    
    // We have to use this method due to a UI bug in iOS 12.
    static func instantiate() -> MainTabController {
        return UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MainTabController") as! MainTabController
    }
    
    var viewController1 = WalletNavigationViewController()
    var viewController2: UIViewController!
    var viewController4: UIViewController!

    var isFirtTimeAppear: Bool = true
    
    // Use ViewController to avoid the tab bar
    let newsSwipeViewController = NewsSwipeViewController()
    var newsViewControllerHeight: CGFloat = 4 * NewsCollectionCell.flashMinHeight
    var newsViewControllerEnabled: Bool = false

    var bottomButtonView: UIView = UIView()
    let bottomButtonViewHeight: CGFloat = 49
    var bottomPadding: CGFloat = 0
    
    var isDropdownMenuExpanded: Bool = false
    let dropdownMenu = NewsDetailDropdownMenu(frame: .zero)

    // Mask view
    var blurVisualEffectView = UIView(frame: .zero)

    override func viewDidLoad() {
        super.viewDidLoad()

        let selectedColor = Themes.isDark() ? UIColor.white : UIColor.black
        UITabBarItem.appearance().setTitleTextAttributes([NSAttributedStringKey.foregroundColor: selectedColor], for: .selected)
        self.tabBar.barTintColor = Themes.isDark() ? .dark2 : .white
        
        // Asset view controller
        viewController1.viewController.delegate = self

        // Trade view controller
        viewController2 = TradeSelectionNavigationViewController()

        // Setting view controller
        viewController4 = SettingNavigationViewController()
        
        newsSwipeViewController.newsSwipeViewControllerDelegate = self

        setTabBarItems()
        if FeatureConfigDataManager.shared.getShowTradingFeature() {
            viewControllers = [viewController1, viewController2, viewController4]
        } else {
            viewControllers = [viewController1, viewController4]
        }
        
        newsSwipeViewController.view.frame = CGRect.zero
        newsSwipeViewController.willMove(toParentViewController: self)
        view.addSubview(newsSwipeViewController.view)
        
        // TODO: self.navigationController? is nil. call addChildViewController(newsViewController) will cause viewWillAppear not firing
        self.navigationController?.addChildViewController(newsSwipeViewController)
        // view.bringSubview(toFront: newsViewController.view)
        self.newsSwipeViewController.didMove(toParentViewController: self)

        // Setup notifications
        NotificationCenter.default.addObserver(self, selector: #selector(languageChangedReceivedNotification), name: .languageChanged, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(showTradingFeatureChangedReceivedNotification(notification:)), name: .showTradingFeatureChanged, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(localNotificationReceived), name: .publishLocalNotificationToMainTabController, object: nil)
        
        // News
        NotificationCenter.default.addObserver(self, selector: #selector(hideBottomTabBarDetailViewControllerReceivedNotification), name: .hideBottomTabBarDetailViewController, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(pushedNewsDetailViewControllerReceivedNotification), name: .pushedNewsDetailViewController, object: nil)
    }
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

        if isFirtTimeAppear {
            isFirtTimeAppear = false

            // 8 is the padding between collection cells.
            // 128 is the height of the header view
            // TODO: 20 is the bottom paddding. I think we don't need this. Fix it later
            let window = UIApplication.shared.keyWindow
            let topPadding = window?.safeAreaInsets.top ?? 0
            
            self.newsViewControllerHeight = 3 * (NewsCollectionCell.flashMinHeight + 8) + 128 + topPadding + 20
            if self.newsViewControllerHeight < UIScreen.main.bounds.size.height {
                self.newsViewControllerHeight += (NewsCollectionCell.flashMinHeight + 8)
            }
            self.newsSwipeViewController.view.frame = CGRect(x: 0, y: -self.newsViewControllerHeight, width: self.view.frame.width, height: self.newsViewControllerHeight)
            
            setupTabBarInNewsDetailViewController()
            setupDropdownMenu()
        }
    }

    func setTabBarItems() {
        // ESTabBarController_swift doesn't work in iOS 12.1. However, it worked in in 12.0 and previous versions.
        /*
        viewController1.tabBarItem = ESTabBarItem(TabBarItemBouncesContentView(frame: .zero), title: LocalizedString("Wallet", comment: ""), image: UIImage(named: "Assets"), selectedImage: UIImage(named: "Assets-selected" + ColorTheme.getTheme()))
        viewController2.tabBarItem = ESTabBarItem.init(TabBarItemBouncesContentView(frame: .zero), title: LocalizedString("Trade", comment: ""), image: UIImage(named: "Trade"), selectedImage: UIImage(named: "Trade-selected" + ColorTheme.getTheme()))
        viewController3.tabBarItem = ESTabBarItem(TabBarItemBouncesContentView(frame: .zero), title: LocalizedString("Settings", comment: ""), image: UIImage(named: "Settings"), selectedImage: UIImage(named: "Settings-selected" + ColorTheme.getTheme()))
        */

        viewController1.tabBarItem = UITabBarItem(title: LocalizedString("Wallet", comment: ""), image: UIImage(named: "Assets")?.withRenderingMode(UIImageRenderingMode.alwaysOriginal), selectedImage: UIImage(named: "Assets-selected" + ColorTheme.getTheme())?.withRenderingMode(UIImageRenderingMode.alwaysOriginal))
        viewController2.tabBarItem = UITabBarItem.init(title: LocalizedString("Trade", comment: ""), image: UIImage(named: "Trade")?.withRenderingMode(UIImageRenderingMode.alwaysOriginal), selectedImage: UIImage(named: "Trade-selected" + ColorTheme.getTheme())?.withRenderingMode(UIImageRenderingMode.alwaysOriginal))
        viewController4.tabBarItem = UITabBarItem(title: LocalizedString("Settings", comment: ""), image: UIImage(named: "Settings")?.withRenderingMode(UIImageRenderingMode.alwaysOriginal), selectedImage: UIImage(named: "Settings-selected" + ColorTheme.getTheme())?.withRenderingMode(UIImageRenderingMode.alwaysOriginal))
    }
    
    func setupTabBarInNewsDetailViewController() {
        // in News Detail View Controller
        let window = UIApplication.shared.keyWindow
        bottomPadding = (window?.safeAreaInsets.bottom ?? 0)
        
        bottomButtonView.frame = CGRect(x: 0, y: self.view.height + self.bottomPadding, width: UIScreen.main.bounds.width, height: bottomButtonViewHeight + self.bottomPadding)
        
        self.bottomButtonView.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        let fontAdjustmentButton = UIButton(type: UIButtonType.custom)
        fontAdjustmentButton.setImage(UIImage(named: "Font-adjust-item"), for: .normal)
        fontAdjustmentButton.setImage(UIImage(named: "Font-adjust-item")?.alpha(0.3), for: .highlighted)
        fontAdjustmentButton.addTarget(self, action: #selector(pressedFontAdjustmentButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        fontAdjustmentButton.frame = CGRect(x: bottomButtonView.width - 40, y: (bottomButtonViewHeight-23)*0.5, width: 23, height: 23)
        bottomButtonView.addSubview(fontAdjustmentButton)

        let shareButton = UIButton(type: UIButtonType.custom)
        shareButton.setImage(UIImage(named: "News-share-large")?.alpha(0.4), for: .normal)
        shareButton.setImage(UIImage(named: "News-share-large")?.alpha(0.2), for: .highlighted)
        shareButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 8, bottom: 0, right: -8)
        shareButton.addTarget(self, action: #selector(pressedShareButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        shareButton.frame = CGRect(x: fontAdjustmentButton.frame.minX - 68, y: (bottomButtonViewHeight-23)*0.5, width: 23, height: 23)
        bottomButtonView.addSubview(shareButton)
        
        blurVisualEffectView.backgroundColor = UIColor.black.withAlphaComponent(0.3)
        blurVisualEffectView.alpha = 0
        blurVisualEffectView.frame = UIScreen.main.bounds
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.handleBlurVisualEffectView(_:)))
        tap.delegate = self
        blurVisualEffectView.addGestureRecognizer(tap)
    }
    
    func setupDropdownMenu() {
        self.view.addSubview(dropdownMenu)
        if UIApplication.shared.delegate?.window??.safeAreaInsets.top ?? 0 > 20 {
            dropdownMenu.frame = CGRect(x: UIScreen.main.bounds.width-110 - 4, y: bottomButtonView.frame.minY-55-60-4 - 50, width: 110, height: 50)
        } else {
            dropdownMenu.frame = CGRect(x: UIScreen.main.bounds.width-110 - 4, y: bottomButtonView.frame.minY-49-2 - 50, width: 110, height: 50)
        }
        
        dropdownMenu.delegate = self
        
        dropdownMenu.isHidden = true
        isDropdownMenuExpanded = false
    }

    func showDropdownMenu() {
        dropdownMenu.isHidden = false
        view.bringSubview(toFront: dropdownMenu)
        isDropdownMenuExpanded = true
        
        view.addSubview(blurVisualEffectView)
        view.insertSubview(blurVisualEffectView, aboveSubview: bottomButtonView)
        UIView.animate(withDuration: 0.3, animations: {
            self.blurVisualEffectView.alpha = 1.0
        }, completion: {(_) in
            
        })
    }
    
    func hideDropdownMenu() {
        dropdownMenu.isHidden = true
        isDropdownMenuExpanded = false
        
        UIView.animate(withDuration: 0.1, animations: {
            self.blurVisualEffectView.alpha = 0.0
        }, completion: {(_) in
            self.blurVisualEffectView.removeFromSuperview()
        })
    }
    
    @objc func handleBlurVisualEffectView(_ sender: UITapGestureRecognizer? = nil) {
        hideDropdownMenu()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        print("touchesBegan")
        if isDropdownMenuExpanded {
            hideDropdownMenu()
        }
    }
    
    @objc func languageChangedReceivedNotification() {
        setTabBarItems()
        newsSwipeViewController.viewControllers[0].viewController.collectionView.reloadData()
        newsSwipeViewController.viewControllers[0].viewController.refreshData()
        newsSwipeViewController.viewControllers[1].viewController.collectionView.reloadData()
        newsSwipeViewController.viewControllers[1].viewController.refreshData()
    }
    
    @objc func showTradingFeatureChangedReceivedNotification(notification: NSNotification) {
        if let showTradingFeature: Bool = notification.userInfo?["showTradingFeature"] as? Bool {
            if showTradingFeature {
                viewControllers = [viewController1, viewController2, viewController4]
            } else {
                viewControllers = [viewController1, viewController4]
            }
        }
    }
    
    @objc func pushedNewsDetailViewControllerReceivedNotification() {
        print("pushedNewsDetailViewControllerReceivedNotification")
        // TODO: this may cause some race condition in UI
        setBottomTabBarHidden(false, animated: false)
    }
    
    @objc func hideBottomTabBarDetailViewControllerReceivedNotification() {
        // Only fire in viewWillDisappear in NewsDetailViewController
        setBottomTabBarHidden(true, animated: false)
    }

    @objc func pressedFontAdjustmentButton(_ button: UIBarButtonItem) {
        print("pressed fontAdjustmentButton")
        if !isDropdownMenuExpanded {
            showDropdownMenu()
        } else {
            hideDropdownMenu()
        }
    }
    
    @objc func pressedSafariButton(_ button: UIBarButtonItem) {
        print("pressed pressedSafariButton")
        if let news = NewsDataManager.shared.getCurrentInformationItem() {
            if let url = URL(string: news.url) {
                UIApplication.shared.open(url)
            }
        }
    }
    
    @objc func pressedShareButton(_ button: UIBarButtonItem) {
        if let news = NewsDataManager.shared.getCurrentInformationItem() {
            if let url = URL(string: news.url) {
                let shareAll = [news.title, url] as [Any]
                let activityVC = UIActivityViewController(activityItems: shareAll, applicationActivities: nil)
                activityVC.popoverPresentationController?.sourceView = self.view
                self.present(activityVC, animated: true, completion: nil)
            }
        }
    }

    func processExternalUrl() {
        viewController1.processExternalUrl()
    }
    
}

// Local Notification
extension MainTabController {
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        
        //displaying the ios local notification when app is in foreground
        completionHandler([.alert, .badge, .sound])
        
        Answers.logCustomEvent(withName: "userNotificationCenter v1",
                               customAttributes: [:])
    }
    
    @objc func localNotificationReceived() {
        //creating the notification content
        let content = UNMutableNotificationContent()
        
        //adding title, subtitle, body and badge
        content.title = "Hey"
        content.body = "We are learning about iOS Local Notification"
        content.badge = 1
        
        UNUserNotificationCenter.current().delegate = self
        
        //getting the notification trigger
        //it will be called after 5 seconds
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 1, repeats: false)
        
        //getting the notification request
        let request = UNNotificationRequest(identifier: "leaf.prod.app", content: content, trigger: trigger)
        
        //adding the notification to notification center
        UNUserNotificationCenter.current().add(request) { (error) in
            print(error)
        }

    }
}

extension MainTabController: WalletViewControllerDelegate {

    func scrollViewDidScroll(y: CGFloat) {
        
        if !newsViewControllerEnabled {
            newsSwipeViewController.view.frame = CGRect(x: 0, y: -self.newsViewControllerHeight-y, width: view.frame.width, height: self.newsViewControllerHeight)
        }

        if y < NewsUIStyleConfig.shared.scrollingDistance && !newsViewControllerEnabled {
            // TODO: If it's shown before, skip
            SettingDataManager.shared.setNewsIndicatorHasShownBefore()
            
            newsViewControllerEnabled = true
            view.bringSubview(toFront: newsSwipeViewController.view)
            
            UIView.animate(withDuration: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationDuration, delay: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationDelay, usingSpringWithDamping: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationSpringWithDamping, initialSpringVelocity: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationInitialSpringVelocity, options: .curveEaseInOut, animations: {
                self.newsSwipeViewController.view.frame = CGRect(x: 0, y: UIApplication.shared.keyWindow!.safeAreaInsets.top, width: self.view.frame.width, height: self.newsViewControllerHeight)
                self.viewController1.viewController.assetTableView.frame = CGRect(x: 0, y: self.viewController1.viewController.assetTableView.frame.height, width: self.view.frame.width, height: self.viewController1.viewController.assetTableView.frame.height)
            }) { (_) in
                self.viewController1.viewController.refreshControl.endRefreshing()
                
                // TODO: need to consider the height when hiding the tab bar in NewsDetailViewController
                self.newsSwipeViewController.view.frame = CGRect(x: 0, y: UIApplication.shared.keyWindow!.safeAreaInsets.top, width: self.view.frame.width, height: UIScreen.main.bounds.height + 44)
                
                self.newsSwipeViewController.swipeView.swipeContentScrollView?.isScrollEnabled = true
                self.newsSwipeViewController.viewControllers[0].viewController.rightFakeView.alpha = 1
                self.newsSwipeViewController.viewControllers[1].viewController.leftFakeView.alpha = 1
            }
        }
    }
    
    func reloadCollectionViewInNewsViewController() {
        self.newsSwipeViewController.viewControllers[0].viewController.collectionView.reloadData()
        self.newsSwipeViewController.viewControllers[1].viewController.collectionView.reloadData()
    }
    
}

extension MainTabController: NewsSwipeViewControllerDelegate {
    
    func closeButtonAction() {
        newsViewControllerEnabled = false
        // self.newsViewController.removeFromParentViewController()
        
        self.viewController1.viewController.walletBalanceView.frame = CGRect(x: 0, y: self.viewController1.viewController.assetTableView.frame.height, width: self.viewController1.viewController.walletBalanceView.frame.width, height: WalletButtonTableViewCell.getHeight())
                
        UIView.animate(withDuration: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationDuration, delay: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationDelay, usingSpringWithDamping: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationSpringWithDamping, initialSpringVelocity: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationInitialSpringVelocity, options: .curveEaseInOut, animations: {
            self.newsSwipeViewController.view.frame = CGRect(x: 0, y: -self.newsViewControllerHeight, width: self.view.frame.width, height: self.newsViewControllerHeight)
            // self.newsSwipeViewController.view.frame = CGRect(x: 0, y: -100, width: self.view.frame.width, height: self.newsViewControllerHeight)
            self.viewController1.viewController.assetTableView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.viewController1.viewController.assetTableView.frame.height)
            self.viewController1.viewController.walletBalanceView.frame = CGRect(x: 0, y: 0, width: self.viewController1.viewController.walletBalanceView.frame.width, height: WalletButtonTableViewCell.getHeight())
        }) { (_) in
            self.newsSwipeViewController.swipeView.swipeContentScrollView?.isScrollEnabled = true
            self.newsSwipeViewController.viewControllers[0].viewController.rightFakeView.alpha = 1
            self.newsSwipeViewController.viewControllers[1].viewController.leftFakeView.alpha = 1
        }
    }
    
    func setBottomTabBarHidden(_ newValue: Bool, animated: Bool) {
        self.view.addSubview(self.bottomButtonView)

        if newValue {
            if animated {
                UIView.animate(withDuration: 0.6, delay: 0, options: .curveEaseInOut, animations: {
                    self.bottomButtonView.frame = CGRect(x: 0, y: self.view.height + self.bottomPadding, width: UIScreen.main.bounds.width, height: self.bottomButtonViewHeight + self.bottomPadding + 20)
                }) { (_) in
                    
                }
            } else {
                self.bottomButtonView.frame = CGRect(x: 0, y: self.view.height + self.bottomPadding, width: UIScreen.main.bounds.width, height: self.bottomButtonViewHeight + self.bottomPadding + 20)
            }
            
        } else {
            if animated {
                UIView.animate(withDuration: 0.6, delay: 0, options: .curveEaseInOut, animations: {
                    self.bottomButtonView.frame = CGRect(x: 0, y: self.view.height - self.bottomButtonViewHeight - self.bottomPadding, width: UIScreen.main.bounds.width, height: self.bottomButtonViewHeight + self.bottomPadding + 20)
                }) { (_) in
                    
                }
            } else {
                self.bottomButtonView.frame = CGRect(x: 0, y: self.view.height - self.bottomButtonViewHeight - self.bottomPadding, width: UIScreen.main.bounds.width, height: self.bottomButtonViewHeight + self.bottomPadding + 20)
            }
        }
    }

}

extension MainTabController: NewsDetailDropdownMenuProtocol {
    func dismiss() {
        hideDropdownMenu()
    }
}
