//
//  WalletViewController.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/1/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import NotificationBannerSwift
import MKDropdownMenu
import SVProgressHUD
import Lottie

protocol WalletViewControllerDelegate: class {
    func scrollViewDidScroll(y: CGFloat)
    func reloadCollectionViewInNewsViewController()
}

class WalletViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, QRCodeScanProtocol {

    weak var delegate: WalletViewControllerDelegate?

    @IBOutlet weak var assetTableView: UITableView!
    let refreshView = UIView()
    let refreshControl = UIRefreshControl()

    var isLaunching: Bool = true
    var numberOfRowsInSection1: Int = 0

    var isDropdownMenuExpanded: Bool = false
    let dropdownMenu = MKDropdownMenu(frame: .zero)

    var pasteboardValue: String = ""

    var showTradingFeature: Bool = FeatureConfigDataManager.shared.getShowTradingFeature()

    var blurVisualEffectView = UIView(frame: .zero)

    var isNewsViewControllerScrollEnabled: Bool = false
    var walletBalanceView: WalletBalanceView!

    var timer = Timer()
    var showingNewsIndicator: Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()

        view.theme_backgroundColor = ColorPicker.backgroundColor

        assetTableView.dataSource = self
        assetTableView.delegate = self

        // init WalletBalanceView
        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        walletBalanceView = WalletBalanceView(frame: CGRect(x: 0, y: 0, width: screenWidth, height: WalletButtonTableViewCell.getHeight()))
        view.insertSubview(walletBalanceView, belowSubview: assetTableView)

        let footerView = UIView(frame: CGRect(x: 0, y: 0, width: 200, height: 10))
        footerView.theme_backgroundColor = ColorPicker.backgroundColor
        assetTableView.tableFooterView = footerView
        assetTableView.separatorStyle = .none
        assetTableView.delaysContentTouches = false

        // Avoid dragging a cell to the top makes the tableview shake
        assetTableView.estimatedRowHeight = 0
        assetTableView.estimatedSectionHeaderHeight = 0
        assetTableView.estimatedSectionFooterHeight = 0

        // assetTableView.theme_backgroundColor = ColorPicker.backgroundColor
        assetTableView.backgroundColor = .clear

        self.navigationItem.rightBarButtonItem = UIBarButtonItem.init(barButtonSystemItem: .add, target: self, action: #selector(self.pressAddButton(_:)))
        // self.navigationItem.leftBarButtonItem = UIBarButtonItem.init(barButtonSystemItem: .organize, target: self, action: #selector(self.pressSwitchWallet(_:)))

        dropdownMenu.dataSource = self
        dropdownMenu.delegate = self
        dropdownMenu.disclosureIndicatorImage = nil

        dropdownMenu.dropdownShowsTopRowSeparator = false
        dropdownMenu.dropdownBouncesScroll = false
        dropdownMenu.backgroundDimmingOpacity = 0
        dropdownMenu.dropdownCornerRadius = 6
        dropdownMenu.dropdownRoundedCorners = UIRectCorner.allCorners
        dropdownMenu.dropdownBackgroundColor = UIColor.dark2
        dropdownMenu.rowSeparatorColor = UIColor.dark2
        dropdownMenu.componentSeparatorColor = UIColor.dark2
        dropdownMenu.dropdownShowsTopRowSeparator = false
        dropdownMenu.dropdownShowsBottomRowSeparator = false
        dropdownMenu.dropdownShowsBorder = false

        self.view.addSubview(dropdownMenu)

        // Add Refresh Control to Table View
        refreshControl.updateUIStyle(withTitle: RefreshControlDataManager.shared.get(type: .walletViewController))
        refreshView.frame = CGRect(x: 0, y: WalletButtonTableViewCell.getHeight()+20, width: 0, height: 0)
        assetTableView.addSubview(refreshView)
        refreshView.addSubview(refreshControl)
        refreshControl.addTarget(self, action: #selector(refreshData(_:)), for: .valueChanged)

        blurVisualEffectView.backgroundColor = UIColor.black.withAlphaComponent(0.8)
        blurVisualEffectView.alpha = 1
        blurVisualEffectView.frame = UIScreen.main.bounds

        NotificationCenter.default.addObserver(self, selector: #selector(needRelaunchCurrentAppWalletReceivedNotification), name: .needRelaunchCurrentAppWallet, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(processPasteboard), name: .needCheckStringInPasteboard, object: nil)

        if !SettingDataManager.shared.getNewsIndicatorHasShownBefore() {
            timer = Timer.scheduledTimer(timeInterval: 10, target: self, selector: #selector(self.updateCounting), userInfo: nil, repeats: true)
        }
    }

    @objc func needRelaunchCurrentAppWalletReceivedNotification() {
        self.isLaunching = true
    }

    @objc private func refreshData(_ sender: Any) {
        getDataFromRelay()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        assetTableView.isUserInteractionEnabled = true

        self.navigationController?.setNavigationBarHidden(false, animated: false)
        self.navigationItem.title = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.name ?? LocalizedString("Wallet", comment: "")

        walletBalanceView.setup(animated: false)

        assetTableView.reloadData()
        getDataFromRelay()

        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        dropdownMenu.frame = CGRect(x: screenWidth-160-9, y: 0, width: 160, height: 0)

        let spaceView = UIImageView.init(image: UIImage.init(named: "dropdown-triangle"))
        spaceView.contentMode = .center
        dropdownMenu.spacerView = spaceView
        dropdownMenu.spacerViewOffset = UIOffset.init(horizontal: self.dropdownMenu.bounds.size.width - 95, vertical: 1)

        if NewsDataManager.shared.currentNewsListKey != "ALL_CURRENCY" {
            NewsDataManager.shared.currentNewsListKey = "ALL_CURRENCY"
            delegate?.reloadCollectionViewInNewsViewController()
            NewsDataManager.shared.get(category: .information, pageIndex: 0) { (news, _) in
                DispatchQueue.main.async {
                    self.delegate?.reloadCollectionViewInNewsViewController()
                }
            }
            NewsDataManager.shared.get(category: .flash, pageIndex: 0) { (news, _) in
                DispatchQueue.main.async {
                    self.delegate?.reloadCollectionViewInNewsViewController()
                }
            }
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }

    @objc func processPasteboard() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            // Check if the view is visible
            guard self.isViewLoaded && (self.view.window != nil) else {
                return
            }

            // Avoid show banner in isLaunching state.
            if UIPasteboard.general.hasStrings && !self.isLaunching {
                if let string = UIPasteboard.general.string {
                    if self.pasteboardValue != string && QRCodeMethod.isAddress(content: string) && !AppWalletDataManager.shared.isDuplicatedAddress(address: string) {
                        // Update
                        self.pasteboardValue = string

                        let banner = NotificationBanner.generate(title: "Send tokens to the address in pasteboard?", style: .success, hasLeftImage: false)
                        banner.duration = 4
                        banner.show()
                        banner.onTap = {
                            // Limit to WalletViewController.
                            guard self.isViewLoaded && (self.view.window != nil) else {
                                return
                            }
                            let vc = SendAssetViewController()
                            vc.address = string
                            vc.hidesBottomBarWhenPushed = true
                            self.navigationController?.pushViewController(vc, animated: true)
                        }
                    }
                }
            }
        }
    }

    func setResultOfScanningQRCode(valueSent: String, type: QRCodeType) {
        if let data = valueSent.data(using: .utf8) {
            let json = JSON(data)
            switch type {
            case .p2pOrder:
                /*
                P2POrderDataManager.instance.handleResult(of: json["value"])
                let vc = TradeConfirmationViewController()
                vc.view.theme_backgroundColor = ColorPicker.backgroundColor
                vc.parentNavController = self.navigationController
                vc.order = P2POrderDataManager.instance.p2pOrders[1]
                self.navigationController?.pushViewController(vc, animated: true)
                */
                break

            case .address:
                let vc = SendAssetViewController()
                vc.address = valueSent
                vc.hidesBottomBarWhenPushed = true
                self.navigationController?.pushViewController(vc, animated: true)

            case .keystore, .mnemonic, .privateKey:
                let vc = UnlockWalletSwipeViewController()
                vc.hidesBottomBarWhenPushed = true
                vc.setResultOfScanningQRCode(valueSent: valueSent, type: type)
                self.navigationController?.pushViewController(vc, animated: true)
            default:
                return
            }
        }
    }

    @objc func pressQRCodeButton(_ button: UIBarButtonItem) {
        print("pressQRCodeButton")
        if CurrentAppWalletDataManager.shared.getCurrentAppWallet() != nil {
            let viewController = QRCodeViewController()
            viewController.hidesBottomBarWhenPushed = true
            viewController.address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }

    @objc func pressSwitchWallet(_ button: UIBarButtonItem) {
        let viewController = SettingManageWalletViewController()
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }

    @objc func pressAddButton(_ button: UIBarButtonItem) {
        if !isDropdownMenuExpanded {
            dropdownMenu.openComponent(0, animated: true)
            isDropdownMenuExpanded = true
        }
    }

    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        print("scrollViewWillBeginDragging")
    }

    @objc func updateCounting() {
        NSLog("counting..")

        if !showingNewsIndicator && !SettingDataManager.shared.getNewsIndicatorHasShownBefore() {
            showingNewsIndicator = true

            let labelWidth = LocalizedString("Pull Down for the Latest News", comment: "").widthOfString(usingFont: FontConfigManager.shared.getMediumFont(size: 17))

            let baseView = UIView(frame: CGRect(x: 0, y: 0, width: labelWidth + 28*2, height: 44))
            let label = UILabel(frame: CGRect(x: 28, y: 0, width: labelWidth, height: 44))
            label.text = LocalizedString("Pull Down for the Latest News", comment: "")
            label.font = FontConfigManager.shared.getMediumFont(size: 17)
            label.theme_textColor = GlobalPicker.textColor
            baseView.addSubview(label)

            let animationView = LOTAnimationView(name: "arrow_down")
            animationView.frame = CGRect(x: labelWidth + 28 + 4, y: 13, width: 18, height: 18)
            baseView.addSubview(animationView)

            self.navigationItem.title = ""
            self.navigationItem.titleView = baseView
            animationView.play()
        } else {
            showingNewsIndicator = false
            self.navigationItem.titleView = nil
            self.navigationItem.title = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.name ?? LocalizedString("Wallet", comment: "")
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
            self.navigationItem.title = CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.name ?? LocalizedString("Wallet", comment: "")
            self.navigationItem.rightBarButtonItem = UIBarButtonItem.init(barButtonSystemItem: .add, target: self, action: #selector(self.pressAddButton(_:)))
        }

        if scrollView.contentOffset.y >= 0 {
            walletBalanceView.frame = CGRect(x: 0, y: -scrollView.contentOffset.y, width: walletBalanceView.frame.width, height: walletBalanceView.frame.height)
        } else {
            if isNewsViewControllerScrollEnabled {
                walletBalanceView.frame = CGRect(x: 0, y: -scrollView.contentOffset.y, width: walletBalanceView.frame.width, height: walletBalanceView.frame.height)
            } else {
                walletBalanceView.frame = CGRect(x: 0, y: 0, width: walletBalanceView.frame.width, height: walletBalanceView.frame.height)
            }
        }
    }

    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        print("scrollViewDidEndDecelerating")
        print("scrollView y: \(scrollView.contentOffset.y)")

        if isNewsViewControllerScrollEnabled {
            walletBalanceView.frame = CGRect(x: 0, y: -scrollView.contentOffset.y, width: walletBalanceView.frame.width, height: walletBalanceView.frame.height)
        }

        // Reset the state at the end
        isNewsViewControllerScrollEnabled = false
        self.refreshView.isHidden = false
    }

    func numberOfSections(in tableView: UITableView) -> Int {
        if isLaunching {
            return 2
        }
        return 3
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 1
        case 1:
            return 1
        case 2:
            numberOfRowsInSection1 = CurrentAppWalletDataManager.shared.getAssetsWithHideSmallAssetsOption().count
            return numberOfRowsInSection1
        default:
            return  0
        }
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0 {
            return WalletBalanceTableViewCell.getHeight()
        } else if indexPath.section == 1 {
            return WalletButtonTableViewCell.getHeight()
        } else {
            return AssetTableViewCell.getHeight()
        }
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            var cell = tableView.dequeueReusableCell(withIdentifier: WalletBalanceTableViewCell.getCellIdentifier()) as? WalletBalanceTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("WalletBalanceTableViewCell", owner: self, options: nil)
                cell = nib![0] as? WalletBalanceTableViewCell
                cell?.delegate = self
            }
            walletBalanceView.setup(animated: true)
            if isLaunching {
                walletBalanceView.balanceLabel.setText("", animated: false)
            }
            return cell!
        } else if indexPath.section == 1 {
            var cell = tableView.dequeueReusableCell(withIdentifier: WalletButtonTableViewCell.getCellIdentifier()) as? WalletButtonTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("WalletButtonTableViewCell", owner: self, options: nil)
                cell = nib![0] as? WalletButtonTableViewCell
                cell?.delegate = self
            }
            cell?.setup(showTradingFeature: showTradingFeature, isLaunching: self.isLaunching)
            return cell!
        } else {
            var cell = tableView.dequeueReusableCell(withIdentifier: AssetTableViewCell.getCellIdentifier()) as? AssetTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("AssetTableViewCell", owner: self, options: nil)
                cell = nib![0] as? AssetTableViewCell
            }
            cell?.asset = CurrentAppWalletDataManager.shared.getAssetsWithHideSmallAssetsOption()[indexPath.row]
            cell?.update()
            return cell!
        }
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 0 {

        } else if indexPath.section == 1 {

        } else {
            // Avoid pushing AssetSwipeViewController mutiple times
            assetTableView.isUserInteractionEnabled = false

            tableView.deselectRow(at: indexPath, animated: true)
            let asset = CurrentAppWalletDataManager.shared.getAssetsWithHideSmallAssetsOption()[indexPath.row]
            let viewController = AssetDetailViewController()
            viewController.delegate = self
            viewController.asset = asset
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }

}

extension WalletViewController: WalletBalanceTableViewCellDelegate {

    func touchesBegan() {
        isNewsViewControllerScrollEnabled = true
        self.refreshView.isHidden = true
    }

    func touchesEnd() {
        isNewsViewControllerScrollEnabled = false
        self.refreshView.isHidden = false
    }

    func pressedQRCodeButtonInWalletBalanceTableViewCell() {
        if CurrentAppWalletDataManager.shared.getCurrentAppWallet() != nil {
            let viewController = QRCodeViewController()
            viewController.hidesBottomBarWhenPushed = true
            viewController.address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
            viewController.navigationTitle = LocalizedString("Wallet Address", comment: "")
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }
}

extension WalletViewController: WalletButtonTableViewCellDelegate {

    func navigationToScanViewController() {
        let viewController = ScanQRCodeViewController()
        viewController.expectedQRCodeTypes = [.mnemonic, .keystore, .privateKey, .p2pOrder, .address]
        viewController.delegate = self
        viewController.shouldPop = false
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }

    func navigationToReceiveViewController() {
        if CurrentAppWalletDataManager.shared.getCurrentAppWallet() != nil {
            let viewController = QRCodeViewController()
            viewController.hidesBottomBarWhenPushed = true
            viewController.address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }

    func navigationToSendViewController() {
        let viewController = SendAssetViewController()
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }

    func navigationToTradeViewController() {
        let viewController = AirdropViewController()
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }

    func navigationToContactViewController() {
        let viewController = ContactTableViewController()
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }
}

extension WalletViewController: AssetViewControllerDelegate {

    func scrollViewDidScroll(y: CGFloat) {
        delegate?.scrollViewDidScroll(y: y)
    }

    func reloadCollectionViewInNewsViewController() {
        delegate?.reloadCollectionViewInNewsViewController()
    }

}
