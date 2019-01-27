//
//  AssetDetailViewController.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/3/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

protocol AssetViewControllerDelegate: class {
    func scrollViewDidScroll(y: CGFloat)
    func reloadCollectionViewInNewsViewController()
}

class AssetDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    weak var delegate: AssetViewControllerDelegate?

    var isNewsViewControllerScrollEnabled: Bool = false
    var assetBalanceView: AssetBalanceView!
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var receiveButton: GradientButton!
    @IBOutlet weak var sendButton: GradientButton!

    var asset: Asset?
    var type: TxSwipeViewType
    var viewAppear: Bool = false
    var isLaunching: Bool = true
    
    let refreshView = UIView()
    let refreshControl = UIRefreshControl()

    var transactions: [Transaction] = []
    var pageIndex: UInt = 1
    var hasMoreData: Bool = true
    
    // Mask view
    var blurVisualEffectView = UIView(frame: .zero)
    
    // Drag down to close a present view controller.
    var dismissInteractor = MiniToLargeViewInteractive()

    convenience init(type: TxSwipeViewType) {
        self.init(nibName: nil, bundle: nil)
        self.type = type
    }
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        type = .all
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getTransactionsFromRelay()

        setBackButton()

        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.delaysContentTouches = false
        
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: 320, height: 0))
        tableView.tableHeaderView = headerView
        
        view.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.backgroundColor = .clear
        
        // Add Refresh Control to Table View
        tableView.refreshControl = refreshControl
        
        // TODO: how to handle different tokens.
        refreshControl.theme_tintColor = GlobalPicker.textColor
        refreshControl.addTarget(self, action: #selector(refreshData(_:)), for: .valueChanged)
        
        // Add Refresh Control to Table View
        refreshControl.updateUIStyle(withTitle: RefreshControlDataManager.shared.get(type: .walletViewController))
        refreshView.frame = CGRect(x: 0, y: AssetBalanceTableViewCell.getHeight()-20, width: 0, height: 0)
        tableView.addSubview(refreshView)
        refreshView.addSubview(refreshControl)
        refreshControl.addTarget(self, action: #selector(refreshData(_:)), for: .valueChanged)
        
        blurVisualEffectView.backgroundColor = UIColor.black.withAlphaComponent(0.8)
        blurVisualEffectView.alpha = 1
        blurVisualEffectView.frame = UIScreen.main.bounds
        
        // Receive & Send button
        receiveButton.setTitle(LocalizedString("Receive", comment: "") + " " + (asset?.symbol ?? ""), for: .normal)
        sendButton.setTitle(LocalizedString("Send", comment: "") + " " + (asset?.symbol ?? ""), for: .normal)
        
        if ColorTheme.current == .green {
            sendButton.setPrimaryColor()
        } else {
            receiveButton.setPrimaryColor()
        }
        
        // init WalletBalanceView
        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        assetBalanceView = AssetBalanceView(frame: CGRect(x: 0, y: 0, width: screenWidth, height: AssetBalanceTableViewCell.getHeight()))
        view.insertSubview(assetBalanceView, belowSubview: tableView)
        assetBalanceView.update(asset: asset)
        
        self.navigationItem.title = asset?.symbol
        if asset?.symbol == "ETH" || asset?.symbol == "WETH" {
            let convertButon = UIBarButtonItem(title: LocalizedString("Convert", comment: ""), style: UIBarButtonItemStyle.plain, target: self, action: #selector(pressedConvertButton))
            convertButon.setTitleTextAttributes([NSAttributedStringKey.font: FontConfigManager.shared.getCharactorFont(size: 14)], for: .normal)
            self.navigationItem.rightBarButtonItem = convertButon
        }
    }
    
    @objc private func refreshData(_ sender: Any) {
        pageIndex = 1
        hasMoreData = true
        getTransactionsFromRelay()
    }
    
    func getTransactionsFromRelay() {
        if let asset = asset {
            CurrentAppWalletDataManager.shared.getTransactionsFromServer(asset: asset, pageIndex: pageIndex) { (newTransactions, error) in
                guard error == nil else {
                    return
                }
                DispatchQueue.main.async {
                    if self.isLaunching {
                        self.isLaunching = false
                    }
                    if newTransactions.count < 50 {
                        self.hasMoreData = false
                    }
                    if self.pageIndex == 1 {
                        self.transactions = self.sortTransactions(newTransactions)
                    } else {
                        self.transactions += self.sortTransactions(newTransactions)
                    }
                    self.tableView.reloadData()
                    self.refreshControl.endRefreshing()
                }
            }
        }
    }
    
    @objc func pressedConvertButton() {
        let viewController = ConvertETHViewController()
        viewController.asset = CurrentAppWalletDataManager.shared.getAsset(symbol: asset!.symbol)
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    @IBAction func pressedReceiveButton(_ sender: Any) {
        print("pressedReceiveButton")
        let viewController = QRCodeViewController()
        viewController.hidesBottomBarWhenPushed = true
        viewController.address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    @IBAction func pressedSendButton(_ sender: Any) {
        print("pressedSendButton")
        let viewController = SendAssetViewController()
        viewController.asset = self.asset!
        SendCurrentAppWalletDataManager.shared.token = TokenDataManager.shared.getTokenBySymbol(self.asset!.symbol)
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    func sortTransactions(_ transsactions: [Transaction]) -> [Transaction] {
        var result: [Transaction] = []
        switch self.type {
        case .status:
            result = transsactions.sorted { (tx1, tx2) -> Bool in
                return tx1.status.description > tx2.status.description
            }
        case .type:
            result = transsactions.sorted { (tx1, tx2) -> Bool in
                return tx1.type.description < tx2.type.description
            }
        default:
            result = transsactions.sorted { (tx1, tx2) -> Bool in
                return tx1.createTime > tx2.createTime
            }
        }
        return result
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if !isLaunching {
            getTransactionsFromRelay()
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func isTableEmpty() -> Bool {
        return transactions.count == 0 && !isLaunching
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return 1
        } else if section == 1 {
            return isTableEmpty() ? 1 : transactions.count
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0 {
            return AssetBalanceTableViewCell.getHeight()
        } else {
            if isTableEmpty() {
                return OrderNoDataTableViewCell.getHeight() - 120
            } else {
                return AssetTransactionTableViewCell.getHeight()
            }
        }
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            var cell = tableView.dequeueReusableCell(withIdentifier: AssetBalanceTableViewCell.getCellIdentifier()) as? AssetBalanceTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("AssetBalanceTableViewCell", owner: self, options: nil)
                cell = nib![0] as? AssetBalanceTableViewCell
                cell?.delegate = self
            }
            return cell!
        } else {
            if isTableEmpty() {
                var cell = tableView.dequeueReusableCell(withIdentifier: OrderNoDataTableViewCell.getCellIdentifier()) as? OrderNoDataTableViewCell
                if cell == nil {
                    let nib = Bundle.main.loadNibNamed("OrderNoDataTableViewCell", owner: self, options: nil)
                    cell = nib![0] as? OrderNoDataTableViewCell
                }
                cell?.noDataLabel.text = LocalizedString("No-data-asset", comment: "")
                cell?.noDataImageView.image = UIImage(named: "No-data-asset")
                return cell!
            } else {
                var cell = tableView.dequeueReusableCell(withIdentifier: AssetTransactionTableViewCell.getCellIdentifier()) as? AssetTransactionTableViewCell
                if cell == nil {
                    let nib = Bundle.main.loadNibNamed("AssetTransactionTableViewCell", owner: self, options: nil)
                    cell = nib![0] as? AssetTransactionTableViewCell
                }
                cell?.transaction = self.transactions[indexPath.row]
                cell?.update()
                
                // Pagination
                if hasMoreData && indexPath.row == transactions.count - 1 {
                    pageIndex += 1
                    getTransactionsFromRelay()
                }
                
                return cell!
            }
        }
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 0 {
            
        } else {
            guard !isTableEmpty() else { return }
            tableView.deselectRow(at: indexPath, animated: false)
            let transaction = self.transactions[indexPath.row]
            
            let vc = AssetTransactionDetailViewController()
            vc.transaction = transaction
            vc.parentNavController = self.navigationController
            
            // TODO: drag to dismiss is broken.
            // vc.transitioningDelegate = self
            vc.modalPresentationStyle = .overFullScreen
            vc.dismissClosure = {
                UIView.animate(withDuration: 0.1, animations: {
                    self.blurVisualEffectView.alpha = 0.0
                }, completion: {(_) in
                    self.blurVisualEffectView.removeFromSuperview()
                })
            }
            
            dismissInteractor.percentThreshold = 0.2
            dismissInteractor.dismissClosure = {
                
            }
            
            self.present(vc, animated: true) {
                // self.dismissInteractor.attachToViewController(viewController: vc, withView: vc.view, presentViewController: nil, backgroundView: self.blurVisualEffectView)
            }
            
            self.navigationController?.view.addSubview(self.blurVisualEffectView)
            UIView.animate(withDuration: 0.3, animations: {
                self.blurVisualEffectView.alpha = 1.0
            }, completion: {(_) in
                
            })
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        print("scrollView y: \(scrollView.contentOffset.y) with \(isNewsViewControllerScrollEnabled)")
        if isNewsViewControllerScrollEnabled {
            delegate?.scrollViewDidScroll(y: scrollView.contentOffset.y)
        }
        
        if isNewsViewControllerScrollEnabled && scrollView.contentOffset.y < -10 {
            self.navigationItem.title = ""
            self.navigationItem.rightBarButtonItem = nil
        } else {
            self.navigationItem.title = asset?.symbol
            // self.navigationItem.rightBarButtonItem = UIBarButtonItem.init(barButtonSystemItem: .add, target: self, action: #selector(self.pressAddButton(_:)))
            if asset?.symbol == "ETH" || asset?.symbol == "WETH" {
                let convertButon = UIBarButtonItem(title: LocalizedString("Convert", comment: ""), style: UIBarButtonItemStyle.plain, target: self, action: #selector(pressedConvertButton))
                convertButon.setTitleTextAttributes([NSAttributedStringKey.font: FontConfigManager.shared.getCharactorFont(size: 14)], for: .normal)
                self.navigationItem.rightBarButtonItem = convertButon
            }
        }
        
        if scrollView.contentOffset.y >= 0 {
            assetBalanceView.frame = CGRect(x: 0, y: -scrollView.contentOffset.y, width: assetBalanceView.frame.width, height: assetBalanceView.frame.height)
        } else {
            if isNewsViewControllerScrollEnabled {
                assetBalanceView.frame = CGRect(x: 0, y: -scrollView.contentOffset.y, width: assetBalanceView.frame.width, height: assetBalanceView.frame.height)
            } else {
                assetBalanceView.frame = CGRect(x: 0, y: 0, width: assetBalanceView.frame.width, height: assetBalanceView.frame.height)
            }
        }
    }

    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        print("scrollViewDidEndDecelerating")
        print("scrollView y: \(scrollView.contentOffset.y)")
        
        if isNewsViewControllerScrollEnabled {
            assetBalanceView.frame = CGRect(x: 0, y: -scrollView.contentOffset.y, width: assetBalanceView.frame.width, height: assetBalanceView.frame.height)
        }
        
        // Reset the state at the end
        isNewsViewControllerScrollEnabled = false
        self.refreshView.isHidden = false
    }

}

extension AssetDetailViewController: AssetBalanceTableViewCellDelegate {
    
    func touchesBegan() {
        isNewsViewControllerScrollEnabled = true
        self.refreshView.isHidden = true
    }
    
    func touchesEnd() {
        isNewsViewControllerScrollEnabled = false
        self.refreshView.isHidden = false
    }

}

extension AssetDetailViewController: UIViewControllerTransitioningDelegate {
    
    func animationController(forDismissed dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        // TODO: Have to disable this one.
        let animator = MiniToLargeViewAnimator()
        animator.transitionType = .Dismiss
        return animator
    }
    
    func interactionControllerForDismissal(using animator: UIViewControllerAnimatedTransitioning) -> UIViewControllerInteractiveTransitioning? {
        // guard !disableInteractivePlayerTransitioning else { return nil }
        return dismissInteractor
    }
    
}
