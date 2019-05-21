//
//  TradeCompleteViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 3/17/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class TradeCompleteViewController: UIViewController {

    @IBOutlet weak var resultHeaderImage: UIImageView!
    @IBOutlet weak var exchangedInfoLabel: UILabel!
    @IBOutlet weak var detailsButton: GradientButton!
    @IBOutlet weak var doneButton: UIButton!
    @IBOutlet weak var scrollView: UIScrollView!

    // Need TokenA
    var needATipLabel: UILabel = UILabel(frame: .zero)
    var needAInfoLabel: UILabel = UILabel(frame: .zero)
    var needAUnderline: UIView = UIView(frame: .zero)

    // Need TokenB
    var needBTipLabel: UILabel = UILabel(frame: .zero)
    var needBInfoLabel: UILabel = UILabel(frame: .zero)
    var needBUnderline: UIView = UIView(frame: .zero)

    var order: RawOrder?
    var errorTipInfo: [String] = []
    var verifyInfo: [String: Double]?

    override func viewDidLoad() {
        super.viewDidLoad()

        view.theme_backgroundColor = ColorPicker.backgroundColor

        setBackToTopViewContollersButton()
        setupErrorInfo()
        setupLabels()
        setupRows()
        setupButtons()
    }

    func setupRows() {
        guard !isBalanceEnough() else { return }
        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        let padding: CGFloat = 24

        // 1st row: need A token
        needATipLabel.setTitleCharFont()
        needATipLabel.text = LocalizedString("You Need More", comment: "")
        needATipLabel.frame = CGRect(x: padding, y: padding, width: 150, height: 40)
        scrollView.addSubview(needATipLabel)
        needAInfoLabel.setTitleDigitFont()
        needAInfoLabel.textColor = .fail
        needAInfoLabel.text = errorTipInfo[0]
        needAInfoLabel.textAlignment = .right
        needAInfoLabel.frame = CGRect(x: padding + 150, y: needATipLabel.frame.origin.y, width: screenWidth - padding * 2 - 150, height: 40)
        scrollView.addSubview(needAInfoLabel)

        guard errorTipInfo.count == 2 else { return }

        needAUnderline.frame = CGRect(x: padding, y: needATipLabel.frame.maxY, width: screenWidth - padding * 2, height: 1)
        needAUnderline.theme_backgroundColor = ColorPicker.cardBackgroundColor
        scrollView.addSubview(needAUnderline)

        // 2nd row: need B token
        needBTipLabel.setTitleCharFont()
        needBTipLabel.text = LocalizedString("You Need More", comment: "")
        needBTipLabel.frame = CGRect(x: padding, y: needATipLabel.frame.maxY + padding, width: 150, height: 40)
        scrollView.addSubview(needBTipLabel)
        needBInfoLabel.setTitleDigitFont()
        needBInfoLabel.textColor = .fail
        needBInfoLabel.text = errorTipInfo[1]
        needBInfoLabel.textAlignment = .right
        needBInfoLabel.frame = CGRect(x: padding + 150, y: needBTipLabel.frame.origin.y, width: screenWidth - padding * 2 - 150, height: 40)
        scrollView.addSubview(needBInfoLabel)

        needBUnderline.frame = CGRect(x: padding, y: needBTipLabel.frame.maxY, width: screenWidth - padding * 2, height: 1)
        needBUnderline.theme_backgroundColor = ColorPicker.cardBackgroundColor
        scrollView.addSubview(needBUnderline)
    }

    func setupLabels() {
        exchangedInfoLabel.setTitleCharFont()
        if isBalanceEnough() {
            resultHeaderImage.image = #imageLiteral(resourceName: "Result-header-success")
            exchangedInfoLabel.text = LocalizedString("Congratulations! Your order has been submitted!", comment: "")
        } else {
            resultHeaderImage.image = UIImage.init(named: "Result-header-fail")
            exchangedInfoLabel.text = LocalizedString("Your order has not been submitted! Please make sure you have enough balance to complete the trade.", comment: "")
        }
    }

    func isBalanceEnough() -> Bool {
        return errorTipInfo.count == 0
    }

    func setupButtons() {
        detailsButton.title = LocalizedString("Check Details", comment: "")
        detailsButton.setPrimaryColor()
        if isBalanceEnough() {
            detailsButton.isHidden = false
        } else {
            detailsButton.isHidden = true
        }
        doneButton.title = LocalizedString("Done", comment: "")
        doneButton.addTarget(self, action: #selector(pressedDoneButton(_:)), for: UIControlEvents.touchUpInside)
        doneButton.titleLabel?.setTitleCharFont()
    }

    func setupErrorInfo() {
        if let info = self.verifyInfo {
            for item in info {
                if item.key.starts(with: "MINUS_") {
                    let key = item.key.components(separatedBy: "_")[1]
                    self.errorTipInfo.append("\(item.value.withCommas()) \(key)")
                }
            }
        }
    }

    @IBAction func pressedDetailsButton(_ sender: UIButton) {
        if let order = self.order {
            let viewController = OrderDetailViewController()
            viewController.order = order
            viewController.hidesBottomBarWhenPushed = true
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    }

    @objc func pressedDoneButton(_ sender: Any) {
        for controller in self.navigationController!.viewControllers as Array {
            if controller.isKind(of: TradeViewController.self) || controller.isKind(of: WalletViewController.self) || controller.isKind(of: TradeSelectionViewController.self) {
                self.navigationController!.popToViewController(controller, animated: true)
                break
            }
        }
    }

    // No swipe back
    func setBackToTopViewContollersButton() {
        let backButton = UIButton(type: UIButtonType.custom)

        backButton.theme_setImage(GlobalPicker.back, forState: .normal)
        backButton.theme_setImage(GlobalPicker.backHighlight, forState: .highlighted)

        // Default left padding is 20. It should be 12 in our design.
        backButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: -16, bottom: 0, right: 8)
        backButton.addTarget(self, action: #selector(pressedDoneButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        backButton.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        let backBarButton = UIBarButtonItem(customView: backButton)
        self.navigationItem.leftBarButtonItem = backBarButton
    }
}
