//
//  GarlandCardPresentAnimationController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit

class NewsDetailPresentAnimationController: NSObject, UIViewControllerAnimatedTransitioning {
    
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return NewsDetailAnimationConfig.shared.presentAnimationDuration
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        guard let fromVC = transitionContext.viewController(forKey: UITransitionContextViewControllerKey.from) as? GarlandViewController,
            let toVC = transitionContext.viewController(forKey: UITransitionContextViewControllerKey.to) as? NewsDetailViewController,
            let _ = toVC.view.snapshotView(afterScreenUpdates: true),
            let fromCell = fromVC.garlandCollection.cellForItem(at: fromVC.selectedCardIndex) as? NewsCollectionCell else {
                
                CustomTransitionAnimator.alphaPresent(using: transitionContext, duration: NewsDetailAnimationConfig.shared.presentAnimationDuration)
                return
        }
        
        let containerView = transitionContext.containerView
        containerView.addSubview(toVC.view)
        toVC.view.alpha = 0.0
        
        let snapshot = toVC.card.snapshotView(afterScreenUpdates: true)
        let convertedCellCoord = fromVC.garlandCollection.convert(fromCell.frame.origin, to: nil)
        let cardConvertedFrame = toVC.view.convert(toVC.card.frame, to: nil)
        snapshot?.frame = CGRect(x: convertedCellCoord.x, y: convertedCellCoord.y, width: GarlandConfig.shared.cardsSize.width, height: GarlandConfig.shared.cardsSize.height)
        containerView.addSubview(snapshot!)
        
        let duration = transitionDuration(using: transitionContext)
        
        toVC.card.alpha = 0
        
        UIView.animateKeyframes(withDuration: duration, delay: 0, options: .calculationModeLinear, animations: {
            UIView.addKeyframe(withRelativeStartTime: 0.0, relativeDuration: 1.0, animations: {
                snapshot?.frame = cardConvertedFrame
            })
            UIView.addKeyframe(withRelativeStartTime: 0.2, relativeDuration: 0.7, animations: {
                toVC.view.alpha = 1.0
            })
        }, completion: { _ in
            snapshot?.removeFromSuperview()
            toVC.card.alpha = 1
            
            UIView.animate(withDuration: 0.5, animations: {
                
            }, completion: { _ in
                transitionContext.completeTransition(!transitionContext.transitionWasCancelled)
            })
        })
    }
}
