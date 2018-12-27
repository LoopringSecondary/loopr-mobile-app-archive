//
//  NewsNavigationViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class NewsNavigationViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        let newsViewController = NewsViewController()
        /*
        navigationBar.shadowImage = UIImage()
        setViewControllers([tradeSelectionViewController], animated: false)
        */

        /*
        newsViewController.view.frame = self.view.bounds;
        newsViewController.willMove(toParentViewController: self)
        self.view.addSubview(newsViewController.view)
        self.addChildViewController(newsViewController)
        newsViewController.didMove(toParentViewController: self)
        */
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        let newsViewController = NewsViewController()
        newsViewController.modalPresentationStyle = .overCurrentContext
        present(newsViewController, animated: false) {
            
        }
    }

}
