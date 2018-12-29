//
//  AboutSectionInViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/22/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension SettingViewController {

    func aboutSectionNumberOfRows() -> Int {
        return Production.getSocialMedia().count + 1
    }
    
    func aboutSectionForCell(indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            var cell = settingsTableView.dequeueReusableCell(withIdentifier: SettingAppVersionTableViewCell.getCellIdentifier()) as? SettingAppVersionTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("SettingAppVersionTableViewCell", owner: self, options: nil)
                cell = nib![0] as? SettingAppVersionTableViewCell
            }
            return cell!
        case 1..<Production.getSocialMedia().count+1:
            return createDetailTableCell(indexPath: indexPath, title: Production.getSocialMedia()[indexPath.row-1].description)
        default:
            return UITableViewCell(frame: .zero)
        }
    }

    func aboutSectionForCellDidSelected(didSelectRowAt indexPath: IndexPath) {
        switch indexPath.row {
        case 0:
            if AppServiceUpdateManager.shared.shouldDisplayUpdateNotificationInSettingViewController() {
                displayUpdateNotification()
            } else {
                
            }
        case 1..<Production.getSocialMedia().count+1:
            if let url = Production.getSocialMedia()[indexPath.row-1].url {
                UIApplication.shared.open(url)
            }
        default:
            break
        }
    }

    func displayUpdateNotification() {
        let vc = AppReleaseNotePopViewController()
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
