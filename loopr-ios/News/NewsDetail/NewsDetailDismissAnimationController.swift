//
//  GarlandCardDismissAnimationController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit

class NewsDetailDismissAnimationController: NSObject, UIViewControllerAnimatedTransitioning {
    
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return GarlandConfig.shared.animationDuration
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        guard let fromVC = transitionContext.viewController(forKey: UITransitionContextViewControllerKey.from) as? NewsDetailViewController,
            let toVC = transitionContext.viewController(forKey: UITransitionContextViewControllerKey.to) as? GarlandViewController,
            let snapshotSubviews = fromVC.card.snapshotView(afterScreenUpdates: true),
            let cell = toVC.garlandCollection.cellForItem(at: toVC.selectedCardIndex) as? NewsCollectionCell else {
                
                CustomTransitionAnimator.alphaDismiss(using: transitionContext, duration: GarlandConfig.shared.animationDuration)
                return
        } 
        
        let containerView = transitionContext.containerView
        containerView.insertSubview(toVC.view, at: 0)
        
        let convertedCellCoord = toVC.garlandCollection.convert(cell.frame.origin, to: nil)
        
        let snapshot = UIView(frame: fromVC.card.frame)
        snapshot.layer.cornerRadius = fromVC.card.layer.cornerRadius
        snapshot.layer.masksToBounds = true
        snapshot.backgroundColor = fromVC.card.backgroundColor
        snapshotSubviews.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        snapshot.addSubview(snapshotSubviews)
        containerView.addSubview(snapshot)
        fromVC.card.alpha = 0
        containerView.backgroundColor = .clear

        let duration = transitionDuration(using: transitionContext)

        UIView.animateKeyframes(withDuration: duration, delay: 0, options: .calculationModeLinear, animations: {

            UIView.addKeyframe(withRelativeStartTime: 0.0, relativeDuration: 0.4, animations: {
                snapshotSubviews.alpha = 0
            })
            
            UIView.addKeyframe(withRelativeStartTime: 0.0, relativeDuration: 1.0, animations: {
                snapshot.frame = CGRect(x: convertedCellCoord.x, y: convertedCellCoord.y, width: cell.frame.width, height: cell.frame.height)
                fromVC.view.alpha = 0.0
            })
        }, completion: { _ in
            snapshot.removeFromSuperview()
            fromVC.view.removeFromSuperview()
            
            cell.subviews.forEach { $0.alpha = 0 }
            UIView.animate(withDuration: 0.4, animations: {
                cell.subviews.forEach { $0.alpha = 1 }
            }, completion: { _ in
                transitionContext.completeTransition(!transitionContext.transitionWasCancelled)
            })
        })
    }
}
