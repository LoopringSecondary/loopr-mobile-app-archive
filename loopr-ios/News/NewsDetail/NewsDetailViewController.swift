//
//  NewsDetailViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/29/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import SafariServices

class NewsDetailViewController: UIViewController {

    var news: News!
    var isFirtTimeAppear: Bool = true

    @IBOutlet open var card: UIView!
    
    fileprivate let userCardPresentAnimationController = NewsDetailPresentAnimationController()
    fileprivate let userCardDismissAnimationController = NewsDetailDismissAnimationController()
    
    override open func viewDidLoad() {
        super.viewDidLoad()
        modalPresentationStyle = .custom
        transitioningDelegate = self
        
        view.frame = UIScreen.main.bounds
        view.theme_backgroundColor = ColorPicker.backgroundColor
        setupCard()
        
        // closeButton.addTarget(self, action: #selector(closeButtonAction), for: .touchDown)
    }
    
    fileprivate func setupCard() {
        card.layer.cornerRadius = GarlandConfig.shared.cardRadius
        card.theme_backgroundColor = ColorPicker.backgroundColor
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        guard isFirtTimeAppear else {
            return
        }
        
        isFirtTimeAppear = false
        if let url = URL(string: self.news.url) {
            let config = SFSafariViewController.Configuration()
            config.entersReaderIfAvailable = true
            config.barCollapsingEnabled = true
            
            let vc = SFSafariViewController(url: url, configuration: config)
            vc.view.theme_backgroundColor = ColorPicker.backgroundColor
            // navigationController is nil
            self.navigationController?.navigationBar.isTranslucent = false
            vc.preferredBarTintColor = UIColor(rgba: "#16162A")
            vc.preferredControlTintColor = UIColor.white
            
            vc.view.backgroundColor = UIColor(rgba: "#16162A")
            
            vc.delegate = self

            self.present(vc, animated: false, completion: {

            })
        }
    }
}

// MARK: Actions
extension NewsDetailViewController {
    @objc fileprivate func closeButtonAction() {
        dismiss(animated: true, completion: nil)
    }
}

// MARK: Transition delegate methods
extension NewsDetailViewController: UIViewControllerTransitioningDelegate {
    
    public func animationController(forPresented presented: UIViewController, presenting: UIViewController, source: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        return userCardPresentAnimationController
    }
    
    public func animationController(forDismissed dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        return userCardDismissAnimationController
    }
}

extension NewsDetailViewController: SFSafariViewControllerDelegate {

    func safariViewControllerDidFinish(_ controller: SFSafariViewController) {
        self.dismiss(animated: true) {
            
        }
    }
    
}
