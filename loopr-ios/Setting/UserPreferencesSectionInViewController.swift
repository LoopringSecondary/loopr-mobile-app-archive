//
//  UserPreferencesSectionInViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/22/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension SettingViewController {
    
    func userPreferencesSectionNumberOfRows() -> Int {
        if BiometricType.get() == .none {
            // Touch ID and Face ID are not available
            return 2
        } else {
            return 3
        }
    }
    
    private func touchIDAndFaceIDTableViewCell(indexPath: IndexPath) -> UITableViewCell {
        var cell = settingsTableView.dequeueReusableCell(withIdentifier: SettingTouchIDAndFaceIDTableViewCell.getCellIdentifier()) as? SettingTouchIDAndFaceIDTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("SettingTouchIDAndFaceIDTableViewCell", owner: self, options: nil)
            cell = nib![0] as? SettingTouchIDAndFaceIDTableViewCell
        }
        return cell!
    }

    func userPreferencesSectionForCell(indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            return createDetailTableCell(indexPath: indexPath, title: LocalizedString("Currency", comment: ""))
        case 1:
            return createDetailTableCell(indexPath: indexPath, title: LocalizedString("Language", comment: ""))
        case 2:
            return touchIDAndFaceIDTableViewCell(indexPath: indexPath)
        default:
            return UITableViewCell(frame: .zero)
        }
    }

    func userPreferencesSectionForCellDidSelected(didSelectRowAt indexPath: IndexPath) {
        switch indexPath.row {
        case 0:
            print("Setting currency")
            let viewController = SettingCurrencyViewController()
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        case 1:
            print("Setting language")
            let viewController = SettingLanguageViewController()
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        case 2:
            print("Touch id")
            if !AuthenticationDataManager.shared.devicePasscodeEnabled() {
                let title: String
                if BiometricType.get() == .touchID {
                    title = LocalizedString("Please turn on Touch ID in settings", comment: "")
                } else {
                    title = LocalizedString("Please turn on Face ID in settings", comment: "")
                }
                let alert = UIAlertController(title: title, message: nil, preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: LocalizedString("OK", comment: ""), style: .default, handler: { _ in
                    
                }))
                UIApplication.shared.keyWindow?.rootViewController?.present(alert, animated: true, completion: nil)
            }
        default:
            break
        }
    }

}
