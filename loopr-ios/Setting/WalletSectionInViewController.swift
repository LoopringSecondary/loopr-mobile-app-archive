//
//  WalletSectionInViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/22/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension SettingViewController {
    
    func walletSectionNumberOfRows() -> Int {
        return 3
    }
    
    func walletSectionForCell(indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            return createDetailTableCell(indexPath: indexPath, title: LocalizedString("Manage Wallets", comment: ""))
        case 1:
            return createDetailTableCell(indexPath: indexPath, title: LocalizedString("Manage Contacts", comment: ""))
        case 2:
            let title: String
            if UserDefaults.standard.bool(forKey: UserDefaultsKeys.thirdParty.rawValue) {
                title = LocalizedString("Third", comment: "")
            } else {
                title = LocalizedString("Unthird", comment: "")
            }
            return createDetailTableCell(indexPath: indexPath, title: title)
        default:
            return UITableViewCell(frame: .zero)
        }
    }

    func walletSectionForCellDidSelected(didSelectRowAt indexPath: IndexPath) {
        switch indexPath.row {
        case 0:
            print("Setting wallet")
            let viewController = SettingManageWalletViewController()
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        case 1:
            print("Setting contacts")
            let viewController = ContactTableViewController()
            viewController.isCellSelectEnable = false
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        case 2:
            pressedThirdPartyButton()
        default:
            break
        }
    }

    private func pressedThirdPartyButton() {
        if !UserDefaults.standard.bool(forKey: UserDefaultsKeys.thirdParty.rawValue), let openID = UserDefaults.standard.string(forKey: UserDefaultsKeys.openID.rawValue) {
            let title = LocalizedString("Third party title", comment: "")
            let message = LocalizedString("Third party message", comment: "")
            let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: LocalizedString("Confirm", comment: ""), style: .default, handler: { _ in
                UserDefaults.standard.set(true, forKey: UserDefaultsKeys.thirdParty.rawValue)
                // UserDefaults.standard.set(nil, forKey: UserDefaultsKeys.openID.rawValue)
                
                DispatchQueue.main.async {
                    self.settingsTableView.reloadData()
                }
                AppServiceUserManager.shared.deleteUserConfig(openID: openID)
            }))
            alert.addAction(UIAlertAction(title: LocalizedString("Cancel", comment: ""), style: .cancel, handler: { _ in
            }))
            self.present(alert, animated: true, completion: nil)
        } else {
            let vc = ThirdPartyViewController()
            vc.fromSettingViewController = true
            self.present(vc, animated: true, completion: nil)
        }
    }

}
