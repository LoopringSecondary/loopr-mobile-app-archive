//
//  SettingViewController.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/3/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class SettingViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    private enum Sections: Int {
        case partner = 0
        case wallet = 1
        case userPreferences = 2
        case trade = 3
        case about = 4
    }
    private var sections: [Sections] = []
    
    @IBOutlet weak var settingsTableView: UITableView!

    var blurVisualEffectView = UIView(frame: .zero)
    
    override func viewDidLoad() {
        super.viewDidLoad()

        
        self.navigationItem.title = LocalizedString("Settings", comment: "")
        
        view.theme_backgroundColor = ColorPicker.backgroundColor
        settingsTableView.separatorStyle = .none
        settingsTableView.tableFooterView = UIView(frame: .zero)
        settingsTableView.delaysContentTouches = false
        settingsTableView.theme_backgroundColor = ColorPicker.backgroundColor
        
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: 200, height: 10))
        headerView.theme_backgroundColor = ColorPicker.backgroundColor
        settingsTableView.tableHeaderView = headerView

        let footerView = UIView(frame: CGRect(x: 0, y: 0, width: 200, height: 10))
        footerView.theme_backgroundColor = ColorPicker.backgroundColor
        settingsTableView.tableFooterView = footerView
        
        if Production.isAppStoreVersion() {
            if FeatureConfigDataManager.shared.getShowTradingFeature() {
                sections = [.wallet, .userPreferences, .trade, .about]
            } else {
                sections = [.wallet, .userPreferences, .about]
            }
        } else {
            if FeatureConfigDataManager.shared.getShowTradingFeature() {
                sections = [.partner, .wallet, .userPreferences, .about]
            } else {
                sections = [.partner, .wallet, .userPreferences, .trade, .about]
            }
        }
        
        blurVisualEffectView.backgroundColor = UIColor.black.withAlphaComponent(0.8)
        blurVisualEffectView.alpha = 1
        blurVisualEffectView.frame = UIScreen.main.bounds
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        settingsTableView.reloadData()
        self.navigationItem.title = LocalizedString("Settings", comment: "")
    }

    //Table view configuration
    func numberOfSections(in tableView: UITableView) -> Int {
        return sections.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let section = sections[indexPath.section]
        switch section {
        case .partner:
            return partnerSectionForCell(indexPath: indexPath)
        case .wallet:
            return walletSectionForCell(indexPath: indexPath)
        case .userPreferences:
            return userPreferencesSectionForCell(indexPath: indexPath)
        case .trade:
            return tradeSectionForCell(indexPath: indexPath)
        case .about:
            return aboutSectionForCell(indexPath: indexPath)
        }
    }
    
    // If you configure things like the label text color in the tableView:cellForRowAtIndexPath: method, your changes will be lost at some point before the cell is actually displayed.
    // https://stackoverflow.com/questions/1890265/what-is-uitableviewdelegate-willdisplaycellforrowatindexpath-for?noredirect=1&lq=1
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {

        let section = sections[indexPath.section]
        let isLastCell = indexPath.row == settingsTableView.numberOfRows(inSection: indexPath.section) - 1

        if let cell = cell as? SettingStyleTableViewCell {
            // Common UI setting
            cell.updateUIStyle(indexPath: indexPath, isLastCell: isLastCell)

            // Customized UI setting in each section.
            switch section {
            case .partner:
                partnerSectionForCell(willDisplay: cell)
                
            default:
                return
            }
        }
        
        if let cell = cell as? SettingTouchIDAndFaceIDTableViewCell {
            switch section {
            case .userPreferences:
                cell.updateUIStyle(indexPath: indexPath, isLastCell: isLastCell)
            default:
                return
            }
        }
        
        if let cell = cell as? SettingAppVersionTableViewCell {
            switch section {
            case .about:
                cell.updateUIStyle(indexPath: indexPath, isLastCell: isLastCell)
            default:
                return
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let section = sections[indexPath.section]
        switch section {
        case .partner:
            partnerSectionForCellDidSelected(didSelectRowAt: indexPath)
        case .wallet:
            walletSectionForCellDidSelected(didSelectRowAt: indexPath)
        case .userPreferences:
            userPreferencesSectionForCellDidSelected(didSelectRowAt: indexPath)
        case .trade:
            tradeSectionForCellDidSelected(didSelectRowAt: indexPath)
        case .about:
            aboutSectionForCellDidSelected(didSelectRowAt: indexPath)
        }
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let section = sections[section]
        switch section {
        case .partner:
            return partnerSectionNumberOfRows()
        case .wallet:
            return walletSectionNumberOfRows()
        case .userPreferences:
            return userPreferencesSectionNumberOfRows()
        case .trade:
            return tradeSectionNumberOfRows()
        case .about:
            return aboutSectionNumberOfRows()
        }
    }

    func createDetailTableCell(indexPath: IndexPath, title: String) -> UITableViewCell {
        var cell = settingsTableView.dequeueReusableCell(withIdentifier: SettingStyleTableViewCell.getCellIdentifier()) as? SettingStyleTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("SettingStyleTableViewCell", owner: self, options: nil)
            cell = nib![0] as? SettingStyleTableViewCell
        }
        
        cell?.leftLabel.text = title
        cell?.rightLabel.isHidden = true
        cell?.disclosureIndicator.isHidden = false

        return cell!
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: view.frame.size.width, height: 20))
        headerView.theme_backgroundColor = ColorPicker.backgroundColor
        return headerView
    }

    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        let section = sections[section]
        switch section {
        case .partner:
            return 0
        default:
            return 10
        }
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 51
    }

}
