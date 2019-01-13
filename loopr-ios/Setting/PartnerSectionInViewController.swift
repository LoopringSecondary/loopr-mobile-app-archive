//
//  PartnerSectionInViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/22/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension SettingViewController {

    func partnerSectionNumberOfRows() -> Int {
        return 1
    }
    
    func partnerSectionForCell(indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            var cell = settingsTableView.dequeueReusableCell(withIdentifier: SettingStyleTableViewCell.getCellIdentifier()) as? SettingStyleTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("SettingStyleTableViewCell", owner: self, options: nil)
                cell = nib![0] as? SettingStyleTableViewCell
            }
            
            cell?.leftLabel.text = LocalizedString("Partner_Slogan", comment: "")

            return cell!
        default:
            return UITableViewCell(frame: .zero)
        }
    }
    
    func partnerSectionForCell(willDisplay cell: SettingStyleTableViewCell) {
        cell.leftLabel.textColor = .success
        cell.leftLabel.font = FontConfigManager.shared.getRegularFont(size: 14)
        
        cell.rightLabel.isHidden = true
        cell.disclosureIndicator.isHidden = false
    }

    func partnerSectionForCellDidSelected(didSelectRowAt indexPath: IndexPath) {
        if indexPath.row == 0 {
            print("Setting partner")
            let viewController = SettingPartnerViewController()
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }

}
