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

    // Use ViewController to avoid the tab bar
    let newsViewController = NewsSwipeViewController()
    var newsViewControllerHeight: CGFloat = 4 * NewsCollectionCell.flashMinHeight
    var newsViewControllerEnabled: Bool = false

    var bottomButtonView: UIView = UIView()
    var bottomPadding: CGFloat = 0
    
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
        
        newsViewController.delegate = self

        setTabBarItems()
        if FeatureConfigDataManager.shared.getShowTradingFeature() {
            viewControllers = [viewController1, viewController2, viewController4]
        } else {
            viewControllers = [viewController1, viewController4]
        }
        
        newsViewController.willMove(toParentViewController: self)
        view.addSubview(newsViewController.view)
        
        // TODO: self.navigationController? is nil. call addChildViewController(newsViewController) will cause viewWillAppear not firing
        self.navigationController?.addChildViewController(newsViewController)
        // view.bringSubview(toFront: newsViewController.view)
        self.newsViewController.didMove(toParentViewController: self)
        
        // Preload News data
        NewsDataManager.shared.get(category: .flash, pageIndex: 0, completion: { (_, _) in
            DispatchQueue.main.async {
                self.newsViewController.viewControllers[0].viewController.collectionView.reloadData()
            }
        })
        NewsDataManager.shared.get(category: .information, pageIndex: 0, completion: { (_, _) in
            DispatchQueue.main.async {
                self.newsViewController.viewControllers[1].viewController.collectionView.reloadData()
            }
        })

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
        // 44 is the height of navigation bar
        self.newsViewControllerHeight = 4 * (NewsCollectionCell.flashMinHeight + 8) + 44
        if self.newsViewControllerHeight < UIScreen.main.bounds.size.height {
            self.newsViewControllerHeight = 5 * (NewsCollectionCell.flashMinHeight + 8) + 44
        }
        self.newsViewController.view.frame = CGRect(x: 0, y: -self.newsViewControllerHeight, width: self.view.frame.width, height: self.newsViewControllerHeight)
        
        // in News Detail View Controller
        let window = UIApplication.shared.keyWindow
        bottomPadding = (window?.safeAreaInsets.bottom ?? 0)
        
        bottomButtonView.frame = CGRect(x: 0, y: self.view.height + self.bottomPadding, width: UIScreen.main.bounds.width, height: 40 + self.bottomPadding)
        
        self.bottomButtonView.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        let fontAdjustmentButton = UIButton(type: UIButtonType.custom)
        fontAdjustmentButton.setImage(UIImage(named: "Font-adjust-item"), for: .normal)
        fontAdjustmentButton.setImage(UIImage(named: "Font-adjust-item")?.alpha(0.3), for: .highlighted)
        // fontAdjustmentButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 8, bottom: 0, right: -8)
        fontAdjustmentButton.addTarget(self, action: #selector(pressedFontAdjustmentButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        fontAdjustmentButton.frame = CGRect(x: bottomButtonView.width - 40, y: 6, width: 23, height: 23)
        bottomButtonView.addSubview(fontAdjustmentButton)
        
        let safariButton = UIButton(type: UIButtonType.custom)
        safariButton.setImage(UIImage(named: "Safari-item-button")?.alpha(0.6), for: .normal)
        safariButton.setImage(UIImage(named: "Safari-item-button")?.alpha(0.3), for: .highlighted)
        safariButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 8, bottom: 0, right: -8)
        safariButton.addTarget(self, action: #selector(pressedSafariButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        safariButton.frame = CGRect(x: (bottomButtonView.width - 23)*0.5 - 2, y: 7, width: 23, height: 23)
        bottomButtonView.addSubview(safariButton)
        
        let shareButton = UIButton(type: UIButtonType.custom)
        shareButton.setImage(UIImage(named: "News-share-large")?.alpha(0.4), for: .normal)
        shareButton.setImage(UIImage(named: "News-share-large")?.alpha(0.2), for: .highlighted)
        shareButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 8, bottom: 0, right: -8)
        shareButton.addTarget(self, action: #selector(pressedShareButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        shareButton.frame = CGRect(x: 7, y: 7, width: 23, height: 23)
        bottomButtonView.addSubview(shareButton)
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
    
    @objc func languageChangedReceivedNotification() {
        setTabBarItems()
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
        NewsUIStyleConfig.shared.setNewsDetailBodyFont(isSmall: false)
    }
    
    @objc func pressedSafariButton(_ button: UIBarButtonItem) {
        print("pressed pressedSafariButton")
        let news = NewsDataManager.shared.getCurrentInformationItem()
        if let url = URL(string: news.url) {
            UIApplication.shared.open(url)
        }
    }
    
    @objc func pressedShareButton(_ button: UIBarButtonItem) {
        let news = NewsDataManager.shared.getCurrentInformationItem()
        if let url = URL(string: news.url) {
            let shareAll = [news.title, url] as [Any]
            let activityVC = UIActivityViewController(activityItems: shareAll, applicationActivities: nil)
            activityVC.popoverPresentationController?.sourceView = self.view
            self.present(activityVC, animated: true, completion: nil)
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
            newsViewController.view.frame = CGRect(x: 0, y: -self.newsViewControllerHeight-y, width: view.frame.width, height: self.newsViewControllerHeight)
        }

        if y < NewsUIStyleConfig.shared.scrollingDistance && !newsViewControllerEnabled {
            newsViewControllerEnabled = true
            view.bringSubview(toFront: newsViewController.view)

            UIView.animate(withDuration: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationDuration, delay: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationDelay, usingSpringWithDamping: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationSpringWithDamping, initialSpringVelocity: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationInitialSpringVelocity, options: .curveEaseInOut, animations: {
                self.newsViewController.view.frame = CGRect(x: 0, y: UIApplication.shared.keyWindow!.safeAreaInsets.top, width: self.view.frame.width, height: self.newsViewControllerHeight)
                self.viewController1.viewController.assetTableView.frame = CGRect(x: 0, y: self.viewController1.viewController.assetTableView.frame.height, width: self.view.frame.width, height: self.viewController1.viewController.assetTableView.frame.height)
            }) { (_) in
                self.viewController1.viewController.refreshControl.endRefreshing()
                self.newsViewController.view.frame = CGRect(x: 0, y: UIApplication.shared.keyWindow!.safeAreaInsets.top, width: self.view.frame.width, height: UIScreen.main.bounds.height)
            }
        }
    }
    
}

extension MainTabController: NewsSwipeViewControllerDelegate {
    
    func closeButtonAction() {
        newsViewControllerEnabled = false
        self.newsViewController.removeFromParentViewController()
        
        self.viewController1.viewController.walletBalanceView.frame = CGRect(x: 0, y: self.viewController1.viewController.assetTableView.frame.height, width: self.viewController1.viewController.walletBalanceView.frame.width, height: WalletButtonTableViewCell.getHeight())
        
        UIView.animate(withDuration: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationDuration, delay: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationDelay, usingSpringWithDamping: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationSpringWithDamping, initialSpringVelocity: NewsUIStyleConfig.shared.newsViewControllerPresentAnimationInitialSpringVelocity, options: .curveEaseInOut, animations: {
            self.newsViewController.view.frame = CGRect(x: 0, y: -self.newsViewControllerHeight, width: self.view.frame.width, height: self.newsViewControllerHeight)
            self.viewController1.viewController.assetTableView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.viewController1.viewController.assetTableView.frame.height)
            self.viewController1.viewController.walletBalanceView.frame = CGRect(x: 0, y: 0, width: self.viewController1.viewController.walletBalanceView.frame.width, height: WalletButtonTableViewCell.getHeight())
        }) { (_) in
            
        }
    }
    
    func setBottomTabBarHidden(_ newValue: Bool, animated: Bool) {
        self.view.addSubview(self.bottomButtonView)

        if newValue {
            if animated {
                UIView.animate(withDuration: 0.6, delay: 0, options: .curveEaseInOut, animations: {
                    self.bottomButtonView.frame = CGRect(x: 0, y: self.view.height + self.bottomPadding, width: UIScreen.main.bounds.width, height: 40 + self.bottomPadding + 20)
                }) { (_) in
                    
                }
            } else {
                self.bottomButtonView.frame = CGRect(x: 0, y: self.view.height + self.bottomPadding, width: UIScreen.main.bounds.width, height: 40 + self.bottomPadding + 20)
            }
            
        } else {
            if animated {
                UIView.animate(withDuration: 0.6, delay: 0, options: .curveEaseInOut, animations: {
                    self.bottomButtonView.frame = CGRect(x: 0, y: self.view.height - 40 - self.bottomPadding, width: UIScreen.main.bounds.width, height: 40 + self.bottomPadding + 20)
                }) { (_) in
                    
                }
            } else {
                self.bottomButtonView.frame = CGRect(x: 0, y: self.view.height - 40 - self.bottomPadding, width: UIScreen.main.bounds.width, height: 40 + self.bottomPadding + 20)
            }
        }
    }

}
