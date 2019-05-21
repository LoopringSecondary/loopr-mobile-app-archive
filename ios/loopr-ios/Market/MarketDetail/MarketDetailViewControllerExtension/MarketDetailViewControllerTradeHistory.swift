//
//  MarketDetailViewControllerTradeHistory.swift
//  loopr-ios
//
//  Created by Ruby on 11/29/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension MarketDetailViewController {
    
    func getTradeHistoryFromRelay() {
        /*
        MarketTradeHistoryDataManager.shared.getTradeHistoryFromServer(market: market.name, completionHandler: { (orderFills, _) in
            self.preivousMarketName = self.market.name
            self.orderFills = orderFills
            DispatchQueue.main.async {
                if self.isTradeHistoryLaunching == true {
                    self.isTradeHistoryLaunching = false
                }
                self.tableView.reloadData()
            }
        })
        */
    }
    
    private func isTradeHistoryEmpty() -> Bool {
        return orderFills.count == 0 && !isTradeHistoryLaunching
    }
    
    func getNumberOfRowsInSectionTradeHistory() -> Int {
        guard !isTradeHistoryEmpty() else {
            return 1
        }
        return orderFills.count
    }
    
    func getHeightForRowAtSectionTradeHistory(indexPath: IndexPath) -> CGFloat {
        if isTradeHistoryEmpty() {
            return OrderNoDataTableViewCell.getHeight() - 200
        } else {
            if indexPath.row == tableView.numberOfRows(inSection: MarketDetailSection.depthAndTradeHistory.rawValue) - 1 {
                return MarketDetailTradeHistoryTableViewCell.getHeight() + 10
            } else {
                return MarketDetailTradeHistoryTableViewCell.getHeight()
            }
        }
    }
    
    func getHeightForHeaderInSectionTradeHistory() -> CGFloat {
        return 30 + 10 + 1
    }
    
    func getHeaderViewInSectionTradeHistory() -> UIView {
        let screenWidth = view.frame.size.width
        let labelWidth = (screenWidth - 15*2 - 5)*0.5
        
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: view.frame.size.width, height: 30 + 10 + 1))
        headerView.theme_backgroundColor = ColorPicker.backgroundColor
        
        let baseViewBuy = UIView(frame: CGRect(x: 15, y: 10, width: screenWidth - 15*2, height: 30))
        baseViewBuy.theme_backgroundColor = ColorPicker.cardBackgroundColor
        baseViewBuy.round(corners: [.topLeft, .topRight], radius: 6)
        headerView.addSubview(baseViewBuy)
        
        let label1 = UILabel(frame: CGRect(x: 10, y: 0, width: labelWidth, height: 30))
        label1.theme_textColor = GlobalPicker.textLightColor
        label1.font = FontConfigManager.shared.getMediumFont(size: 12)
        label1.text = LocalizedString("Price", comment: "")
        label1.textAlignment = .left
        baseViewBuy.addSubview(label1)
        
        let label2 = UILabel(frame: CGRect(x: 10 + 10 + (baseViewBuy.width-30)*0.3, y: 0, width: (baseViewBuy.width-30)*0.22, height: 33))
        label2.theme_textColor = GlobalPicker.textLightColor
        label2.font = FontConfigManager.shared.getMediumFont(size: 12)
        label2.text = LocalizedString("Amount", comment: "")
        label2.textAlignment = .right
        baseViewBuy.addSubview(label2)

        let label4 = UILabel(frame: CGRect(x: 15, y: 0, width: (baseViewBuy.width-30), height: 33))
        label4.theme_textColor = GlobalPicker.textLightColor
        label4.font = FontConfigManager.shared.getMediumFont(size: 12)
        label4.text = LocalizedString("Time", comment: "")
        label4.textAlignment = .right
        baseViewBuy.addSubview(label4)
        
        return headerView
    }

    func getMarketDetailTradeHistoryTableViewCell(cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if isTradeHistoryEmpty() {
            var cell = tableView.dequeueReusableCell(withIdentifier: OrderNoDataTableViewCell.getCellIdentifier()) as? OrderNoDataTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("OrderNoDataTableViewCell", owner: self, options: nil)
                cell = nib![0] as? OrderNoDataTableViewCell
            }
            cell?.noDataLabel.text = LocalizedString("No-data-transaction", comment: "")
            cell?.noDataImageView.image = UIImage(named: "No-data-transaction")
            return cell!
        } else {
            var cell = tableView.dequeueReusableCell(withIdentifier: MarketDetailTradeHistoryTableViewCell.getCellIdentifier()) as? MarketDetailTradeHistoryTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("MarketDetailTradeHistoryTableViewCell", owner: self, options: nil)
                cell = nib![0] as? MarketDetailTradeHistoryTableViewCell
            }
            cell?.orderFill = orderFills[indexPath.row]
            cell?.update()
            
            if indexPath.row == tableView.numberOfRows(inSection: 0) - 1 {
                cell?.baseViewBuy.round(corners: [.bottomLeft, .bottomRight], radius: 6)
            } else {
                cell?.baseViewBuy.round(corners: [], radius: 0)
            }
            return cell!
        }
    }

}
