//
//  UserCardViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit

class UserCardViewController: UIViewController {
    
    @IBOutlet var closeButton: UIButton!
    @IBOutlet open var card: UIView!
    @IBOutlet open var background: UIView!
    @IBOutlet open var cardConstraits: [NSLayoutConstraint]!
    
    fileprivate let userCardPresentAnimationController = UserCardPresentAnimationController()
    fileprivate let userCardDismissAnimationController = UserCardDismissAnimationController()
    
    override open func viewDidLoad() {
        super.viewDidLoad()
        modalPresentationStyle = .custom
        transitioningDelegate = self
        
        view.frame = UIScreen.main.bounds
        view.backgroundColor = .gray
        setupCard()
        
        closeButton.addTarget(self, action: #selector(closeButtonAction), for: .touchDown)
    }
    
    fileprivate func setupCard() {
        card.layer.cornerRadius = GarlandConfig.shared.cardRadius
    }
}

// MARK: Actions
extension UserCardViewController {
    @objc fileprivate func closeButtonAction() {
        dismiss(animated: true, completion: nil)
    }
}

// MARK: Transition delegate methods
extension UserCardViewController: UIViewControllerTransitioningDelegate {
    
    public func animationController(forPresented presented: UIViewController, presenting: UIViewController, source: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        return userCardPresentAnimationController
    }
    
    public func animationController(forDismissed dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        return userCardDismissAnimationController
    }
}
