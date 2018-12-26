//
//  NewsNavigationViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class NewsNavigationViewController: UINavigationController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        let tradeSelectionViewController = NewsViewController()
        navigationBar.shadowImage = UIImage()
        setViewControllers([tradeSelectionViewController], animated: false)
    }

}
