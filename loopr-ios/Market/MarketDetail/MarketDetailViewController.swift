//
//  MarketDetailSwipeViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 7/28/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class MarketDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, MarketDetailDepthTableViewCellDelegate {

    var market: Market!
    var marketDetailSwipeViewController = MarketDetailSwipeViewController()
    
    // Depth
    var preivousMarketName: String = ""
    var minSellPrice: Double = 0
    var buys: [Depth] = []
    var sells: [Depth] = []
    var maxAmountInDepthView: Double = 0
    
    // Trade History
    var orderFills: [OrderFill] = []
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var buyButton: GradientButton!
    @IBOutlet weak var sellButton: GradientButton!
    
    let buttonInNavigationBar =  UIButton(frame: .zero)

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        view.theme_backgroundColor = ColorPicker.backgroundColor
        setBackButton()
        setupMarket()
        updateHistoryButton()

        buyButton.setGreen()
        sellButton.setRed()
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.separatorStyle = .none
 
        buttonInNavigationBar.frame = CGRect(x: 0, y: 0, width: 400, height: 40)
        buttonInNavigationBar.titleLabel?.font = FontConfigManager.shared.getDigitalFont(size: 18)
        buttonInNavigationBar.theme_setTitleColor(GlobalPicker.barTextColor, forState: .normal)
        buttonInNavigationBar.setTitleColor(UIColor.init(white: 0.8, alpha: 1), for: .highlighted)
        buttonInNavigationBar.addTarget(self, action: #selector(self.clickNavigationTitleButton(_:)), for: .touchUpInside)
        self.navigationItem.titleView = buttonInNavigationBar
        
        getDepthFromRelay()
        self.buys = MarketDepthDataManager.shared.getBuys()
        self.sells = MarketDepthDataManager.shared.getSells()
        
        if buys.count > 0 && sells.count > 0 {
            self.maxAmountInDepthView = max(buys[buys.count / 2].amountAInDouble, sells[sells.count / 2].amountAInDouble) * 1.5
        } else if buys.count > 0 {
            self.maxAmountInDepthView = buys[buys.count / 2].amountAInDouble * 1.5
        } else if sells.count > 0 {
            self.maxAmountInDepthView = sells[sells.count / 2].amountAInDouble * 1.5
        } else {
            self.maxAmountInDepthView = 0
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        buttonInNavigationBar.setRightImage(imageName: "Caret-down-dark", imagePaddingTop: 0, imagePaddingLeft: -24, titlePaddingRight: 0)
        // TODO: needs to update the icon. It's too big here.
        var padding = "  "
        for _ in 0..<market!.description.count {
            padding += " "
        }
        buttonInNavigationBar.title = padding + market!.description
    }
    
    func setupMarket() {
        PlaceOrderDataManager.shared.new(tokenA: market.tradingPair.tradingA, tokenB: market.tradingPair.tradingB, market: market)
        
        buyButton.setTitle(LocalizedString("Buy", comment: "") + " " + market.tradingPair.tradingA, for: .normal)
        sellButton.setTitle(LocalizedString("Sell", comment: "") + " " + market.tradingPair.tradingA, for: .normal)
        
        marketDetailSwipeViewController.market = market
    }

    func updateHistoryButton() {
        var icon: UIImage?
        if Themes.isDark() {
            icon = UIImage(named: "Order-history-dark")?.withRenderingMode(.alwaysOriginal)
        } else {
            icon = UIImage(named: "Order-history-light")?.withRenderingMode(.alwaysOriginal)
        }
        let starButton = UIBarButtonItem(image: icon, style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.pressHistoryButton(_:)))
        self.navigationItem.rightBarButtonItem = starButton
    }
    
    @objc func pressHistoryButton(_ button: UIBarButtonItem) {
        print("pressStarButton")
        let viewController = UpdatedOrderHistoryViewController()
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    @objc func clickNavigationTitleButton(_ button: UIButton) {
        print("select another wallet.")
        let viewController = MarketChangeTokenSwipeViewController()
        viewController.didSelectRowClosure = { (market) -> Void in
            // Update market
            self.market = market
            self.setupMarket()
        }

        let navController = UINavigationController(rootViewController: viewController)
        self.present(navController, animated: true) {
            
        }
    }

    @IBAction func pressedSellButton(_ sender: Any) {
        print("pressedSellButton")
        let viewController = BuyAndSellSwipeViewController()
        viewController.market = market
        viewController.initialType = .sell
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    @IBAction func pressedBuyButton(_ sender: Any) {
        print("pressedBuyButton")
        let viewController = BuyAndSellSwipeViewController()
        viewController.market = market
        viewController.initialType = .buy
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 1
        case 1:
            return getNumberOfRowsInDepthSection()
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch indexPath.section {
        case 0:
            return MarketDetailSummaryTableViewCell.getHeight()
        case 1:
            return MarketDetailDepthTableViewCell.getHeight()
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            return getMarketDetailSummaryTableViewCell()
        } else if indexPath.section == 1 {
            return getMarketDetailDepthTableViewCell(cellForRowAt: indexPath)
        } else {
            return UITableViewCell()
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 1 {
            return getHeightForHeaderInSection()
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        if section == 1 {
            return getHeaderViewInDepthSection()
        }
        return nil
    }
    
    private func getMarketDetailSummaryTableViewCell() -> MarketDetailSummaryTableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: MarketDetailSummaryTableViewCell.getCellIdentifier()) as? MarketDetailSummaryTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("MarketDetailSummaryTableViewCell", owner: self, options: nil)
            cell = nib![0] as? MarketDetailSummaryTableViewCell
        }
        cell?.setup(market: market)
        return cell!
    }

}
