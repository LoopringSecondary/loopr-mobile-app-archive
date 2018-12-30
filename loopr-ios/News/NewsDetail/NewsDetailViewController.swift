//
//  NewsDetailViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/29/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class NewsDetailViewController: UIViewController {

    var news: News!
    
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
