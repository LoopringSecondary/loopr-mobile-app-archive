//
//  WalletViewControllerExtensionAppService.swift
//  loopr-ios
//
//  Created by xiaoruby on 10/15/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation
import UIKit
import Crashlytics

extension WalletViewController {
    
    func displayUpdateNotification() {
        let vc = AppReleaseNotePopViewController()
        
        vc.popFromSettingViewController = false
        
        vc.modalPresentationStyle = .overFullScreen
        vc.updateClosure = {
            UIView.animate(withDuration: 0.1, animations: {
                self.blurVisualEffectView.alpha = 0.0
            }, completion: { (_) in
                self.blurVisualEffectView.removeFromSuperview()
            })
        }
        
        vc.skipClosure = {
            UIView.animate(withDuration: 0.1, animations: {
                self.blurVisualEffectView.alpha = 0.0
            }, completion: { (_) in
                self.blurVisualEffectView.removeFromSuperview()
            })
        }
        
        self.present(vc, animated: true, completion: nil)
        
        self.navigationController?.view.addSubview(self.blurVisualEffectView)
        UIView.animate(withDuration: 0.3, animations: {
            self.blurVisualEffectView.alpha = 1.0
        }, completion: {(_) in
            
        })
    }

}
