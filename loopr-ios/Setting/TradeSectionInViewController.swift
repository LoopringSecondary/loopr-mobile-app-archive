//
//  TradeSectionInViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/22/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension SettingViewController {
    
    func tradingSectionForCell(indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            return createDetailTableCell(indexPath: indexPath, title: LocalizedString("Contract Version", comment: ""))
        case 1:
            return createDetailTableCell(indexPath: indexPath, title: LocalizedString("LRC Fee Ratio", comment: ""))
        case 2:
            return createDetailTableCell(indexPath: indexPath, title: LocalizedString("Trade FAQ", comment: ""))
        default:
            return UITableViewCell(frame: .zero)
        }
    }
    
    func tradingSectionForCellDidSelected(didSelectRowAt indexPath: IndexPath) {
        switch indexPath.row {
        case 0:
            print("contract version")
            let viewController = DisplayContractVersionViewController()
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        case 1:
            print("LRC Fee ratio")
            let viewController = SettingLRCFeeRatioViewController()
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        case 2:
            // TODO: Trade FAQ is not ready.
            print("Trade FAQ")
            let viewController = TradeFAQViewController()
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        default:
            break
        }
    }

}
