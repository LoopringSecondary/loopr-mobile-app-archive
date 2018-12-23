//
//  AboutSectionInViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/22/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension SettingViewController {

    func aboutSectionForCell(indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.row {
        case 0:
            var cell = settingsTableView.dequeueReusableCell(withIdentifier: SettingStyleTableViewCell.getCellIdentifier()) as? SettingStyleTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("SettingStyleTableViewCell", owner: self, options: nil)
                cell = nib![0] as? SettingStyleTableViewCell
            }
            cell?.selectionStyle = .none
            cell?.leftLabel.text = title
            cell?.rightLabel.isHidden = false
            cell?.disclosureIndicator.isHidden = true
            
            cell?.update(indexPath: indexPath, isLastCell: false)
            
            cell?.leftLabel.text = LocalizedString("App Version", comment: "")
            cell?.rightLabel.text = AppServiceUpdateManager.shared.getAppVersionAndBuildVersion()
            return cell!
        case 1..<Production.getSocialMedia().count+1:
            return createDetailTableCell(indexPath: indexPath, title: Production.getSocialMedia()[indexPath.row-1].description)
        default:
            return UITableViewCell(frame: .zero)
        }
    }

    func aboutSectionForCellDidSelected(didSelectRowAt indexPath: IndexPath) {
        switch indexPath.row {
        case 1..<Production.getSocialMedia().count+1:
            if let url = Production.getSocialMedia()[indexPath.row-1].url {
                UIApplication.shared.open(url)
            }
        default:
            break
        }
    }

}
