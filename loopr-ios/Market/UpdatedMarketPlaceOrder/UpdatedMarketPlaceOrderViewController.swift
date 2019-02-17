//
//  UpdatedMarketPlaceOrderViewController.swift
//  loopr-ios
//
//  Created by ruby on 2/12/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

class UpdatedMarketPlaceOrderViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    var market: Market!
    
    var initialType: TradeType = .buy
    var initialPrice: String?
    
    private var types: [TradeType] = [.buy, .sell]
    
    // TODO: needs to update buys and sells in Relay 2.0
    var buys: [Depth] = []
    var sells: [Depth] = []
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setBackButton()
        navigationItem.title = PlaceOrderDataManager.shared.market?.description ?? LocalizedString("Trade", comment: "")
        view.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.theme_backgroundColor = ColorPicker.backgroundColor
        
        tableView.dataSource = self
        tableView.delegate = self
        
        tableView.tableFooterView = UIView(frame: .zero)
        tableView.separatorStyle = .none
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return MarketPlaceOrderTableViewCell.getHeight()
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: MarketPlaceOrderTableViewCell.getCellIdentifier()) as? MarketPlaceOrderTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("MarketPlaceOrderTableViewCell", owner: self, options: nil)
            cell = nib![0] as? MarketPlaceOrderTableViewCell
        }
        cell?.market = market
        cell?.type = initialType
        
        cell?.setBuys(buys)
        cell?.setSells(sells)
        
        cell?.update()
        cell?.updateUI()
        return cell!
    }

}
