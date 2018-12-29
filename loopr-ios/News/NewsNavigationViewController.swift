//
//  NewsNavigationViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class NewsNavigationViewController: UIViewController {

    static var tabBarFakeImage: UIImage?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        GarlandConfig.shared.animationDuration = 0.4
        GarlandConfig.shared.cardsSpacing = 8
        GarlandConfig.shared.headerVerticalOffset = 30
        GarlandConfig.shared.headerSize = CGSize(width: UIScreen.main.bounds.width - 15*2, height: 120.0)
        GarlandConfig.shared.fakeHeaderSize = CGSize(width: 8*2, height: 100)
        GarlandConfig.shared.cardsSize = CGSize(width: UIScreen.main.bounds.width - 15*2, height: 120.0)
        GarlandConfig.shared.backgroundHeaderColor = .clear
        GarlandConfig.shared.fakeHeaderColor = UIColor.theme.lighter(by: 10) ?? UIColor.theme
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        // TODO: Still too late to call
        let newsViewController = NewsViewController()
        newsViewController.modalPresentationStyle = .overCurrentContext
        present(newsViewController, animated: false) {
            
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        takeSnapshot()
    }

    func takeSnapshot() {
        if NewsNavigationViewController.tabBarFakeImage == nil {
            let window = UIApplication.shared.keyWindow!
            UIGraphicsBeginImageContextWithOptions(CGSize(width: window.bounds.width, height: window.bounds.height), false, 0)
            window.layer.render(in: UIGraphicsGetCurrentContext()!)
            NewsNavigationViewController.tabBarFakeImage = UIGraphicsGetImageFromCurrentImageContext()
            UIGraphicsEndImageContext()
        }
    }
}
