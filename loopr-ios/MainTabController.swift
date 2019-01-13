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

    
    let newsViewController = NewsNavigationViewController()
    var newsViewControllerEnabled: Bool = false

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
        
        newsViewController.viewController.delegate = self

        setTabBarItems()
        if FeatureConfigDataManager.shared.getShowTradingFeature() {
            viewControllers = [viewController1, viewController2, viewController4]
        } else {
            viewControllers = [viewController1, viewController4]
        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(languageChangedReceivedNotification), name: .languageChanged, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(showTradingFeatureChangedReceivedNotification(notification:)), name: .showTradingFeatureChanged, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(localNotificationReceived), name: .publishLocalNotificationToMainTabController, object: nil)
        
        addChildViewController(newsViewController)
        view.addSubview(newsViewController.view)
        view.bringSubview(toFront: newsViewController.view)
        
        // Preload News data
        NewsDataManager.shared.get(category: .flash, pageIndex: 0, completion: { (_, _) in
            self.newsViewController.viewController.viewControllers[0].collectionView.reloadData()
        })
        NewsDataManager.shared.get(category: .information, pageIndex: 0, completion: { (_, _) in
            self.newsViewController.viewController.viewControllers[1].collectionView.reloadData()
        })
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
        
        self.newsViewController.view.frame = CGRect(x: 0, y: -self.view.frame.height, width: self.view.frame.width, height: self.view.frame.height)
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
            newsViewController.view.frame = CGRect(x: 0, y: -view.frame.height-y, width: view.frame.width, height: view.frame.height)
        }
        
        if y < -140 && !newsViewControllerEnabled {
            newsViewControllerEnabled = true
            UIView.animate(withDuration: 1, delay: 0, usingSpringWithDamping: 0.75, initialSpringVelocity: 2, options: .curveEaseInOut, animations: {
                self.newsViewController.view.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
                self.viewController1.viewController.assetTableView.frame = CGRect(x: 0, y: self.view.frame.height, width: self.view.frame.width, height: self.view.frame.height)
            }) { (_) in
                
            }
        }
    }
    
}

extension MainTabController: NewsSwipeViewControllerDelegate {
    
    func closeButtonAction() {
        newsViewControllerEnabled = false
        UIView.animate(withDuration: 1, delay: 0, usingSpringWithDamping: 0.75, initialSpringVelocity: 2
            , options: .curveEaseInOut, animations: {
            self.newsViewController.view.frame = CGRect(x: 0, y: -self.view.frame.height, width: self.view.frame.width, height: self.view.frame.height)
            self.viewController1.viewController.assetTableView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
        }) { (_) in
            
        }
    }

}
