//
//  AssetSwipeViewController.swift
//  loopr-ios
//
//  Created by 王忱 on 2018/7/19.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import UIKit

class TradeSwipeViewController: SwipeViewController, QRCodeScanProtocol {

    private var type: TradeSwipeType = .trade
    private var types: [TradeSwipeType] = []

    private var viewControllers: [UIViewController] = []

    var options = SwipeViewOptions.getDefault()

    override func viewDidLoad() {
        super.viewDidLoad()

        view.theme_backgroundColor = ColorPicker.backgroundColor
        navigationItem.title = LocalizedString("P2P Trade", comment: "")
        setBackButton()
        setNavigationBarItem()
        setupChildViewControllers()

        // Get the updated nonce
        CurrentAppWalletDataManager.shared.getCurrentAppWallet()?.getNonceFromEthereum(completionHandler: {})
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if Themes.isDark() {
            options.swipeTabView.itemView.textColor = UIColor(rgba: "#ffffff66")
            options.swipeTabView.itemView.selectedTextColor = UIColor(rgba: "#ffffffcc")
        } else {
            options.swipeTabView.itemView.textColor = UIColor(rgba: "#00000099")
            options.swipeTabView.itemView.selectedTextColor = UIColor(rgba: "#000000cc")
        }
        swipeView.reloadData(options: options, default: swipeView.currentIndex)
    }

    func setNavigationBarItem() {
        let icon = UIImage.init(named: "dropdown-scan")
        let button = UIBarButtonItem(image: icon, style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.pressedScanButton))
        self.navigationItem.rightBarButtonItem = button
    }

    @objc func pressedScanButton() {
        let viewController = ScanQRCodeViewController()
        // TODO: do we need to support these types
        viewController.expectedQRCodeTypes = [.p2pOrder, .address]
        viewController.delegate = self
        viewController.shouldPop = false
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }

    func setupChildViewControllers() {
        types = [.trade, .records]
        viewControllers.insert(TradeViewController(), at: 0)
        viewControllers.insert(P2POrderHistoryViewController(), at: 1)
        for viewController in viewControllers {
            self.addChildViewController(viewController)
        }
        swipeView.reloadData(options: options)
    }

    // MARK: - Delegate
    override func swipeView(_ swipeView: SwipeView, viewWillSetupAt currentIndex: Int) {
    }

    override func swipeView(_ swipeView: SwipeView, viewDidSetupAt currentIndex: Int) {
    }

    override func swipeView(_ swipeView: SwipeView, willChangeIndexFrom fromIndex: Int, to toIndex: Int) {
        type = types[toIndex]
    }

    override func swipeView(_ swipeView: SwipeView, didChangeIndexFrom fromIndex: Int, to toIndex: Int) {
    }

    // MARK: - DataSource
    override func numberOfPages(in swipeView: SwipeView) -> Int {
        return viewControllers.count
    }

    override func swipeView(_ swipeView: SwipeView, titleForPageAt index: Int) -> String {
        return types[index].description
    }

    override func swipeView(_ swipeView: SwipeView, viewControllerForPageAt index: Int) -> UIViewController {
        return viewControllers[index]
    }

    // Copy code from WalletViewController.
    func setResultOfScanningQRCode(valueSent: String, type: QRCodeType) {
        if let data = valueSent.data(using: .utf8) {
            let json = JSON(data)
            switch type {
            case .p2pOrder:
                P2POrderDataManager.instance.handleResult(of: json["value"])
                let vc = TradeConfirmationViewController()
                vc.view.theme_backgroundColor = ColorPicker.backgroundColor
                vc.parentNavController = self.navigationController
                vc.order = P2POrderDataManager.instance.p2pOrders[1]
                self.navigationController?.pushViewController(vc, animated: true)

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

}
