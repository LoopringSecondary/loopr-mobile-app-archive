//
//  MarketPlaceOrderTableViewCellRight.swift
//  loopr-ios
//
//  Created by ruby on 2/16/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

extension MarketPlaceOrderTableViewCell {

    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0 {
            return 34
        } else if section == 1 {
            return 10
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 0 {
            let screenWidth = (UIScreen.main.bounds.width - 16*2)*0.5
            let height: CGFloat = 30
            let labelWidth = (screenWidth - 8*2)*0.5
            
            let headerView = UIView(frame: CGRect(x: 0, y: 0, width: screenWidth, height: height))
            headerView.backgroundColor = .clear
            
            let label1 = UILabel(frame: CGRect(x: 8, y: 0, width: labelWidth, height: height))
            label1.theme_textColor = GlobalPicker.textLightColor
            label1.font = FontConfigManager.shared.getMediumFont(size: 12)
            label1.text = "\(LocalizedString("Price", comment: ""))(\(market.tradingPair.tradingB))"
            label1.textAlignment = .left
            headerView.addSubview(label1)
            
            let label4 = UILabel(frame: CGRect(x: label1.frame.maxX, y: 0, width: labelWidth, height: height))
            label4.theme_textColor = GlobalPicker.textLightColor
            label4.font = FontConfigManager.shared.getMediumFont(size: 12)
            label4.text = "\(LocalizedString("Amount", comment: ""))(\(market.tradingPair.tradingA))"
            label4.textAlignment = .right
            headerView.addSubview(label4)
            
            let seperateLine1 = UIView(frame: CGRect(x: 8, y: 0, width: headerView.width - 2*8, height: 0.5))
            seperateLine1.theme_backgroundColor = ColorPicker.cardHighLightColor
            headerView.addSubview(seperateLine1)
            
            let seperateLine2 = UIView(frame: CGRect(x: 8, y: height-0.5, width: headerView.width - 2*8, height: 0.5))
            seperateLine2.theme_backgroundColor = ColorPicker.cardHighLightColor
            headerView.addSubview(seperateLine2)
            
            return headerView
            
        } else if section == 1 {
            let screenWidth = (UIScreen.main.bounds.width - 16*2)*0.5
            let headerView = UIView(frame: CGRect(x: 0, y: 0, width: screenWidth, height: 8))
            let seperateLine = UIView(frame: CGRect(x: 8, y: headerView.height*0.5, width: headerView.width - 2*8, height: 0.5))
            seperateLine.theme_backgroundColor = ColorPicker.cardHighLightColor
            headerView.addSubview(seperateLine)
            return headerView
        } else {
            return nil
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return MarketPlaceOrderbookTableViewCell.getHeight()
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 5
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: MarketPlaceOrderbookTableViewCell.getCellIdentifier()) as? MarketPlaceOrderbookTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("MarketPlaceOrderbookTableViewCell", owner: self, options: nil)
            cell = nib![0] as? MarketPlaceOrderbookTableViewCell
        }
        
        if indexPath.section == 0 {
            let index = sells.count-1-indexPath.row
            if index < sells.count && index >= 0 {
                let depth = sells[sells.count-1-indexPath.row]
                cell?.update(indexPath: indexPath, depth: depth)
            } else {
                cell?.setEmptyUI()
            }
        } else if indexPath.section == 1 {
            if indexPath.row < buys.count {
                let depth = buys[indexPath.row]
                cell?.update(indexPath: indexPath, depth: depth)
            } else {
                cell?.setEmptyUI()
            }
        }
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if indexPath.section == 0 {
            let index = sells.count-1-indexPath.row
            if index < sells.count && index >= 0 {
                let depth = sells[sells.count-1-indexPath.row]
                pressedDepthCell(depth: depth)
            } else {
                
            }
        } else if indexPath.section == 1 {
            if indexPath.row < buys.count {
                let depth = buys[indexPath.row]
                pressedDepthCell(depth: depth)
            } else {
                
            }
        }
    }

}
