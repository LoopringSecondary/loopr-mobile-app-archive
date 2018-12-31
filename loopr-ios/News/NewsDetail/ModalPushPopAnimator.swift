//
//  SCModalPushPopAnimator.swift
//  loopr-ios
//
//  Created by Ruby on 12/31/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit

class ModalPushPopAnimator: UIPercentDrivenInteractiveTransition, UIViewControllerAnimatedTransitioning {
    
    var dismissing = false
    var percentageDriven: Bool = false
    
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return 0.75
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        let fromViewController = transitionContext.viewController(forKey: UITransitionContextViewControllerKey.from)!
        let toViewController = transitionContext.viewController(forKey: UITransitionContextViewControllerKey.to)!
        
        let topView = dismissing ? fromViewController.view : toViewController.view
        let bottomViewController = dismissing ? toViewController : fromViewController
        var bottomView = bottomViewController.view
        let offset = bottomView!.bounds.size.width
        if let navVC = bottomViewController as? UINavigationController {
            bottomView = navVC.topViewController?.view
        }
        
        transitionContext.containerView.insertSubview(toViewController.view, aboveSubview: fromViewController.view)
        if dismissing { transitionContext.containerView.insertSubview(toViewController.view, belowSubview: fromViewController.view) }
        
        topView!.frame = fromViewController.view.frame
        topView!.transform = dismissing ? CGAffineTransform.identity : CGAffineTransform(translationX: offset, y: 0)
        
        let shadowView = UIImageView(image: UIImage(named: "shadow"))
        shadowView.contentMode = UIViewContentMode.scaleAspectFill
        shadowView.layer.anchorPoint = CGPoint(x: 0, y: 0.5)
        shadowView.frame = bottomView!.bounds
        bottomView!.addSubview(shadowView)
        shadowView.transform = dismissing ? CGAffineTransform(scaleX: 0.01, y: 1) : CGAffineTransform.identity
        shadowView.alpha = self.dismissing ? 1.0 : 0.0
        
        UIView.animate(withDuration: transitionDuration(using: transitionContext), delay: 0, usingSpringWithDamping: 0.9, initialSpringVelocity: 1.0, options: ModalPushPopAnimator.animOpts(), animations: { () -> Void in
            topView!.transform = self.dismissing ? CGAffineTransform(translationX: offset, y: 0) : CGAffineTransform.identity
            shadowView.transform = self.dismissing ? CGAffineTransform.identity : CGAffineTransform(scaleX: 0.01, y: 1)
            shadowView.alpha = self.dismissing ? 0.0 : 1.0
        }) { ( finished ) -> Void in
            topView!.transform = CGAffineTransform.identity
            shadowView.removeFromSuperview()
            transitionContext.completeTransition(!transitionContext.transitionWasCancelled)
        }
    }
    
    class func animOpts() -> UIViewAnimationOptions {
        return UIViewAnimationOptions.allowAnimatedContent.union(UIViewAnimationOptions.beginFromCurrentState).union(UIViewAnimationOptions.layoutSubviews)
    }
}
