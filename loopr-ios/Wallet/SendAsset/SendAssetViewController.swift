//
//  SendAssetViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 3/17/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import Geth
import NotificationBannerSwift
import SVProgressHUD

class SendAssetViewController: UIViewController, UITextFieldDelegate, UIScrollViewDelegate, NumericKeyboardDelegate, NumericKeyboardProtocol, QRCodeScanProtocol {

    var asset: Asset!
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var scrollViewButtonLayoutConstraint: NSLayoutConstraint!
    @IBOutlet weak var sendButtonBackgroundView: UIView!
    @IBOutlet weak var sendButton: UIButton!
    @IBOutlet weak var sendButtonLayoutContraint: NSLayoutConstraint!

    // Token
    var tokenIconImageView = UIImageView()
    var tokenTotalAmountLabel = UILabel()

    // Address
    var addressTextField: UITextField = UITextField()
    var scanButton: UIButton = UIButton()
    var addressUnderLine: UIView = UIView()
    var addressInfoLabel: UILabel = UILabel()

    // Amount
    var amountTextField: UITextField = UITextField()
    var tokenSymbolLabel: UILabel = UILabel()
    var amountUnderline: UIView = UIView()
    var amountTradeImage: UIImageView = UIImageView()
    var amountInfoLabel: UILabel = UILabel()
    var maxButton: UIButton = UIButton()
    
    // Transaction Fee
    var transactionFeeLabel = UILabel()
    var transactionFeeAmountLabel = UILabel()
    
    // Advanced
    var advancedButton: UIButton = UIButton()
    var showAdvanced: Bool = false
    var transactionSpeedSlider = UISlider()
    var transactionAmountMinLabel = UILabel()
    var transactionAmountMaxLabel = UILabel()
    var transactionAmountCurrentLabel = UILabel()
    var transactionAmountHelpButton = UIButton()
    
    // Keyboard
    var isKeyboardShow: Bool = false
    var keyboardView: DefaultNumericKeyboard!
    var activeTextFieldTag = -1
    
    // To measure the performance. Will be removed in the future
    var start = Date()
    var end = Date()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        setBackButton()

        sendButton.setTitleColor(.gray, for: .disabled)
        sendButton.title = NSLocalizedString("Send", comment: "")
        sendButton.setupRoundBlack()
        updateButton(isValid: false)
        
        scrollViewButtonLayoutConstraint.constant = 77
        sendButtonLayoutContraint.constant = 15

        // Setup UI in the scroll view
        let screensize: CGRect = UIScreen.main.bounds
        let screenWidth = screensize.width
        // let screenHeight = screensize.height
        
        let originY: CGFloat = 50
        let padding: CGFloat = 15
        
        // First row: token
        tokenIconImageView.frame = CGRect(center: CGPoint(x: screenWidth*0.5, y: originY), size: CGSize(width: 54, height: 54))
        tokenIconImageView.image = UIImage(named: "ETH")
        scrollView.addSubview(tokenIconImageView)
        
        tokenTotalAmountLabel.frame = CGRect(center: CGPoint(x: screenWidth*0.5, y: tokenIconImageView.frame.maxY + 30), size: CGSize(width: screenWidth - 2*padding, height: 21))
        tokenTotalAmountLabel.textAlignment = .center
        tokenTotalAmountLabel.font = FontConfigManager.shared.getLabelFont()
        scrollView.addSubview(tokenTotalAmountLabel)
        
        // Second row: address

        addressTextField.delegate = self
        addressTextField.tag = 0
        addressTextField.keyboardType = .alphabet
        addressTextField.font = FontConfigManager.shared.getLabelFont()
        addressInfoLabel.theme_tintColor = GlobalPicker.textColor
        addressTextField.placeholder = NSLocalizedString("Enter the address", comment: "")
        addressTextField.contentMode = UIViewContentMode.bottom
        addressTextField.frame = CGRect(x: padding, y: tokenTotalAmountLabel.frame.maxY + padding*3, width: screenWidth-padding*2-40, height: 40)
        scrollView.addSubview(addressTextField)
        
        scanButton.image = UIImage(named: "Scan")
        scanButton.frame = CGRect(x: screenWidth-padding-30, y: addressTextField.frame.origin.y, width: 40, height: 40)
        scanButton.addTarget(self, action: #selector(pressedScanButton(_:)), for: .touchUpInside)
        scrollView.addSubview(scanButton)
        
        addressUnderLine.frame = CGRect(x: padding, y: addressTextField.frame.maxY, width: screenWidth - padding * 2, height: 1)
        addressUnderLine.backgroundColor = UIColor.black
        scrollView.addSubview(addressUnderLine)

        addressInfoLabel.frame = CGRect(x: padding, y: addressUnderLine.frame.maxY, width: screenWidth - padding * 2, height: 40)
        addressInfoLabel.font = UIFont.init(name: FontConfigManager.shared.getLight(), size: 14)
        addressInfoLabel.text = NSLocalizedString("Please confirm the address before sending.", comment: "")
        scrollView.addSubview(addressInfoLabel)
        
        // Third row: Amount
        
        amountTextField.delegate = self
        amountTextField.inputView = UIView()
        amountTextField.tag = 1
        amountTextField.font = FontConfigManager.shared.getLabelFont()
        amountTextField.theme_tintColor = GlobalPicker.textColor
        amountTextField.placeholder = NSLocalizedString("Enter the amount you want to trade", comment: "")
        amountTextField.contentMode = UIViewContentMode.bottom
        amountTextField.frame = CGRect(x: padding, y: addressInfoLabel.frame.maxY + padding*1.5, width: screenWidth-padding*2-80, height: 40)
        scrollView.addSubview(amountTextField)

        tokenSymbolLabel.font = FontConfigManager.shared.getLabelFont()
        tokenSymbolLabel.textAlignment = .right
        tokenSymbolLabel.frame = CGRect(x: screenWidth-padding-80, y: amountTextField.frame.origin.y, width: 80, height: 40)
        scrollView.addSubview(tokenSymbolLabel)
        
        amountUnderline.frame = CGRect(x: padding, y: amountTextField.frame.maxY, width: screenWidth - padding * 2, height: 1)
        amountUnderline.backgroundColor = UIColor.black
        scrollView.addSubview(amountUnderline)
        
        amountTradeImage.image = UIImage(named: "Convert")
        amountTradeImage.frame = CGRect(x: padding, y: amountUnderline.frame.maxY + 13, width: 15, height: 15)
        scrollView.addSubview(amountTradeImage)
        
        amountInfoLabel.frame = CGRect(x: padding*2 + 10, y: amountUnderline.frame.maxY, width: screenWidth - padding * 2, height: 40)
        amountInfoLabel.font = UIFont.init(name: FontConfigManager.shared.getLight(), size: 14)
        amountInfoLabel.text = 0.0.currency
        scrollView.addSubview(amountInfoLabel)
        
        maxButton.title = NSLocalizedString("Max", comment: "")
        maxButton.theme_setTitleColor(["#0094FF", "#000"], forState: .normal)
        maxButton.setTitleColor(UIColor.init(rgba: "#cce9ff"), for: .highlighted)
        maxButton.titleLabel?.font = FontConfigManager.shared.getLabelFont()
        maxButton.contentHorizontalAlignment = .right
        maxButton.frame = CGRect(x: screenWidth-80-padding, y: amountUnderline.frame.maxY, width: 80, height: 40)
        maxButton.addTarget(self, action: #selector(self.pressedMaxButton(_:)), for: UIControlEvents.touchUpInside)
        scrollView.addSubview(maxButton)
        
        transactionFeeLabel.frame = CGRect(x: padding, y: maxButton.frame.maxY + padding*2, width: 160, height: 40)
        transactionFeeLabel.font = FontConfigManager.shared.getLabelFont()
        transactionFeeLabel.text = NSLocalizedString("Transaction Fee", comment: "")
        scrollView.addSubview(transactionFeeLabel)
        
        transactionFeeAmountLabel.frame = CGRect(x: screenWidth-300-padding, y: maxButton.frame.maxY + padding*2, width: 300, height: 40)
        transactionFeeAmountLabel.font = FontConfigManager.shared.getLabelFont()
        transactionFeeAmountLabel.textAlignment = .right
        transactionFeeAmountLabel.text = "1.232 LRC"
        scrollView.addSubview(transactionFeeAmountLabel)

        // Fouth row: Advanced
        advancedButton.frame = CGRect(x: padding, y: transactionFeeAmountLabel.frame.maxY + padding, width: 100, height: 40)
        advancedButton.setTitleColor(UIColor.black, for: .normal)
        advancedButton.setTitleColor(UIColor.black.withAlphaComponent(0.3), for: .highlighted)
        advancedButton.titleLabel?.font = FontConfigManager.shared.getLabelFont()
        advancedButton.title = NSLocalizedString("Advanced", comment: "")
        advancedButton.setRightImage(imageName: "Arrow-button-right-light", imagePaddingTop: 0, imagePaddingLeft: 10, titlePaddingRight: 11)
        advancedButton.addTarget(self, action: #selector(pressedAdvancedButton(_:)), for: .touchUpInside)
        scrollView.addSubview(advancedButton)
        
        transactionSpeedSlider.alpha = 0
        transactionSpeedSlider.frame = CGRect(x: padding, y: advancedButton.frame.maxY + padding*0.5, width: screenWidth-2*padding, height: 20)
        
        // TODO: Set value
        transactionSpeedSlider.minimumValue = 0
        transactionSpeedSlider.maximumValue = 100
        transactionSpeedSlider.value = 0
        
        transactionSpeedSlider.isContinuous = true
        transactionSpeedSlider.tintColor = UIColor.black
        transactionSpeedSlider.addTarget(self, action: #selector(sliderValueDidChange(_:)), for: .valueChanged)
        scrollView.addSubview(transactionSpeedSlider)

        transactionAmountMinLabel.alpha = 0
        transactionAmountMinLabel.frame = CGRect(x: padding, y: transactionSpeedSlider.frame.maxY + 10, width: (screenWidth-2*padding)/8, height: 30)
        transactionAmountMinLabel.font = FontConfigManager.shared.getLabelFont()
        transactionAmountMinLabel.text = "slow"
        scrollView.addSubview(transactionAmountMinLabel)
        
        transactionAmountCurrentLabel.alpha = 0
        transactionAmountCurrentLabel.textAlignment = .center
        transactionAmountCurrentLabel.frame = CGRect(x: transactionAmountMinLabel.frame.maxX, y: transactionAmountMinLabel.frame.minY, width: (screenWidth-2*padding)*3/4, height: 30)
        transactionAmountCurrentLabel.font = FontConfigManager.shared.getLabelFont()
        transactionAmountCurrentLabel.text = "gas price: 5000000 gwei"
        
        scrollView.addSubview(transactionAmountCurrentLabel)
        
        transactionAmountHelpButton.alpha = 0
        let pad = (transactionAmountCurrentLabel.frame.width - transactionAmountCurrentLabel.intrinsicContentSize.width) / 2
        transactionAmountHelpButton.frame = CGRect(x: transactionAmountCurrentLabel.frame.maxX - pad, y: transactionAmountCurrentLabel.frame.minY, width: 30, height: 30)
        transactionAmountHelpButton.image = UIImage(named: "HelpIcon")
        transactionAmountHelpButton.addTarget(self, action: #selector(pressedHelpButton), for: .touchUpInside)
        scrollView.addSubview(transactionAmountHelpButton)
        
        transactionAmountMaxLabel.alpha = 0
        transactionAmountMaxLabel.textAlignment = .right
        transactionAmountMaxLabel.frame = CGRect(x: transactionAmountCurrentLabel.frame.maxX, y: transactionAmountMinLabel.frame.minY, width: (screenWidth-2*padding)/8, height: 30)
        transactionAmountMaxLabel.font = FontConfigManager.shared.getLabelFont()
        transactionAmountMaxLabel.text = "fast"
        scrollView.addSubview(transactionAmountMaxLabel)
        
        scrollView.delegate = self
        scrollView.contentSize = CGSize(width: screenWidth, height: transactionAmountMinLabel.frame.maxY + 30)

        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: .UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillDisappear), name: .UIKeyboardWillHide, object: nil)

        let scrollViewTap = UITapGestureRecognizer(target: self, action: #selector(scrollViewTapped))
        scrollViewTap.numberOfTapsRequired = 1
        scrollView.addGestureRecognizer(scrollViewTap)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        // TODO: Update the transaction fee is needed. in SendCurrentAppWalletDataManager
        tokenSymbolLabel.text = asset.symbol
        tokenTotalAmountLabel.text = "\(asset.balance) \(asset.symbol) Available"
        
        SendCurrentAppWalletDataManager.shared.getNonceFromServer()
    }
    
    @objc func pressedHelpButton(_ sender: Any) {
        let title = NSLocalizedString("What is gas?", comment: "")
        let message = NSLocalizedString("Gas is...", comment: "") // TODO
        let alertController = UIAlertController(title: title,
            message: message,
            preferredStyle: .alert)
        let cancelAction = UIAlertAction(title: NSLocalizedString("Confirm", comment: ""), style: .cancel, handler: { _ in
        })
        alertController.addAction(cancelAction)
        self.present(alertController, animated: true, completion: nil)
    }
    
    @objc func scrollViewTapped() {
        print("scrollViewTapped")
        amountTextField.resignFirstResponder()
        self.view.endEditing(true)
        hideKeyboard()
    }
    
    func updateLabel(label: UILabel, text: String, textColor: UIColor) {
        label.textColor = textColor
        label.text = text
    }
    
    func updateButton(isValid: Bool) {
        sendButton.isEnabled = isValid
    }
    
    func validateAddress() -> Bool {
        if let toAddress = addressTextField.text {
            if !toAddress.isEmpty {
                if toAddress.isHexAddress() {
                    var error: NSError? = nil
                    if GethNewAddressFromHex(toAddress, &error) != nil {
                        updateLabel(label: addressInfoLabel, text: NSLocalizedString("Please confirm the address before sending.", comment: ""), textColor: .black)
                        return true
                    }
                }
                updateLabel(label: addressInfoLabel, text: NSLocalizedString("Please input a correct address.", comment: ""), textColor: .red)
            } else {
                updateLabel(label: addressInfoLabel, text: NSLocalizedString("Please confirm the address before sending.", comment: ""), textColor: .black)
            }
        }
        return false
    }
    
    func validateAmount() -> Bool {
        if let amountString = amountTextField.text {
            if !amountString.isEmpty, let amount = Double(amountString) {
                if asset.balance >= amount {
                    if let token = TokenDataManager.shared.getTokenBySymbol(asset!.symbol) {
                        if GethBigInt.generateBigInt(valueInEther: amount, symbol: token.symbol) != nil {
                            if let price = PriceQuoteDataManager.shared.getPriceBySymbol(of: asset.symbol) {
                                let display = (amount * price).currency
                                updateLabel(label: amountInfoLabel, text: display, textColor: .black)
                                return true
                            }
                        }
                    }
                } else {
                    updateLabel(label: amountInfoLabel, text: "Maximum: \(asset.balance) \(asset.symbol)", textColor: .red)
                }
            } else {
                updateLabel(label: amountInfoLabel, text: 0.0.currency, textColor: .black)
            }
        }
        return false
    }

    func validation() {
        var isValid = false
        if validateAddress() && validateAmount() {
            isValid = true
        }
        updateButton(isValid: isValid)
    }
    
    @objc func pressedAdvancedButton(_ sender: Any) {
        print("pressedAdvancedButton")
        showAdvanced = !showAdvanced
        if showAdvanced {
            UIView.animate(withDuration: 0.5, animations: {
                self.transactionSpeedSlider.alpha = 1
                self.transactionAmountMinLabel.alpha = 1
                self.transactionAmountCurrentLabel.alpha = 1
                self.transactionAmountMaxLabel.alpha = 1
                self.transactionAmountHelpButton.alpha = 1
            })
            // TODO: The position of the align icon is related to the size. So we use several hardcoded value here.
            self.advancedButton.setRightImage(imageName: "Arrow-button-down-light", imagePaddingTop: 0, imagePaddingLeft: 10, titlePaddingRight: 2)
        } else {
            UIView.animate(withDuration: 0.5, animations: {
                self.transactionSpeedSlider.alpha = 0
                self.transactionAmountMinLabel.alpha = 0
                self.transactionAmountCurrentLabel.alpha = 0
                self.transactionAmountMaxLabel.alpha = 0
                self.transactionAmountHelpButton.alpha = 0
            })
            self.advancedButton.setRightImage(imageName: "Arrow-button-right-light", imagePaddingTop: 0, imagePaddingLeft: 10, titlePaddingRight: 11)
        }
    }

    @IBAction func pressedSendButton(_ sender: Any) {
        print("start sending")
        // Show activity indicator
        start = Date()
        SVProgressHUD.show(withStatus: "Processing the transaction ...")

        let toAddress = addressTextField.text!
        // let gethAmount = GethBigInt.bigInt(amountTextField.text!)!
        if let token = TokenDataManager.shared.getTokenBySymbol(asset!.symbol) {
            let gethAmount = GethBigInt.generateBigInt(valueInEther: Double(amountTextField.text!)!, symbol: token.symbol)!
            var error: NSError? = nil
            let toAddress = GethNewAddressFromHex(toAddress, &error)!
            if token.symbol.uppercased() == "ETH" {
                SendCurrentAppWalletDataManager.shared._transferETH(amount: gethAmount, gasPrice: gasPrice, toAddress: toAddress, completion: completion)
            } else {
                // ETH doesn't have a protocol_value
                if !token.protocol_value.isHexAddress() {
                    print("token protocol \(token.protocol_value) is invalid")
                    return
                }
                let contractAddress = GethNewAddressFromHex(token.protocol_value, &error)!
                SendCurrentAppWalletDataManager.shared._transferToken(contractAddress: contractAddress, toAddress: toAddress, tokenAmount: gethAmount, gasPrice: gasPrice, completion: completion)
            }
        }
    }
    
    func setResultOfScanningQRCode(valueSent: String, type: QRCodeType) {
        print("value from scanning: \(valueSent)")
        addressTextField.text = valueSent
    }
    
    @objc func pressedScanButton(_ sender: Any) {
        let viewController = ScanQRCodeViewController()
        viewController.delegate = self
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    @objc func pressedMaxButton(_ sender: Any) {
        print("pressedMaxButton")
        amountTextField.text = asset.balance.description
        updateLabel(label: amountInfoLabel, text: asset.display, textColor: .black)
    }

    // To avoid gesture conflicts in swiping to back and UISlider
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        if touch.view != nil && touch.view!.isKind(of: UISlider.self) {
            return false
        }
        return true
    }
    
    @objc func sliderValueDidChange(_ sender: UISlider!) {
        print("Slider value changed \(sender.value)")
        let step: Float = 1
        let roundedStepValue = round(sender.value / step) * step
        
        transactionAmountCurrentLabel.text = "gas price: \(roundedStepValue) gwei"
        
        // Update gas price.
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        var result = false
        if textField.tag == 0 {
            hideKeyboard()
            result = true
        } else if textField.tag == 1 {
            showKeyboard(textField: amountTextField)
        }
        return result
    }
    
    func getActiveTextField() -> UITextField? {
        return amountTextField
    }
    
    @objc func keyboardWillShow(_ notification: Notification) {
        guard let keyboardFrame = notification.userInfo?[UIKeyboardFrameEndUserInfoKey] as? NSValue else {
            return
        }
        let keyboardHeight = keyboardFrame.cgRectValue.height
        if #available(iOS 11.0, *) {
            // Get the the distance from the bottom safe area edge to the bottom of the screen
            let window = UIApplication.shared.keyWindow
            let bottomPadding = window?.safeAreaInsets.bottom
            scrollViewButtonLayoutConstraint.constant = keyboardHeight + 77
            sendButtonLayoutContraint.constant = keyboardHeight + 15 - bottomPadding!
        } else {
            sendButtonLayoutContraint.constant = keyboardHeight
        }
    }
    
    @objc func keyboardWillDisappear(notification: NSNotification?) {
        print("keyboardWillDisappear")
        scrollViewButtonLayoutConstraint.constant = 77
        sendButtonLayoutContraint.constant = 15
        if validateAddress() {
            validation()
        }
    }

    func showKeyboard(textField: UITextField) {
        if !isKeyboardShow {
            let width = self.view.frame.width
            let height = self.sendButtonBackgroundView.frame.origin.y
            let keyboardHeight: CGFloat = 220
            scrollViewButtonLayoutConstraint.constant = keyboardHeight
            keyboardView = DefaultNumericKeyboard(frame: CGRect(x: 0, y: height, width: width, height: keyboardHeight))
            keyboardView.delegate = self
            view.addSubview(keyboardView)
            view.bringSubview(toFront: sendButtonBackgroundView)
            view.bringSubview(toFront: sendButton)
            let destinateY = height - keyboardHeight
            
            // TODO: improve the animation.
            UIView.animate(withDuration: 0.5, delay: 0, options: .curveEaseInOut, animations: {
                self.keyboardView.frame = CGRect(x: 0, y: destinateY, width: width, height: keyboardHeight)
            }, completion: { _ in
                self.isKeyboardShow = true
            })
        }
    }
    
    func hideKeyboard() {
        if isKeyboardShow {
            let width = self.view.frame.width
            let height = self.sendButtonBackgroundView.frame.origin.y
            let keyboardHeight: CGFloat = 220
            let destinateY = height
            self.scrollViewButtonLayoutConstraint.constant = 0
            UIView.animate(withDuration: 0.3, delay: 0, options: .curveEaseInOut, animations: {
                // animation for layout constraint change.
                self.view.layoutIfNeeded()
                self.keyboardView.frame = CGRect(x: 0, y: destinateY, width: width, height: keyboardHeight)
            }, completion: { finished in
                self.isKeyboardShow = false
                if finished {
                }
            })
            if validateAmount() {
                validation()
            }
        } else {
            self.scrollView.setContentOffset(CGPoint.zero, animated: true)
        }
    }
    
    func numericKeyboard(_ numericKeyboard: NumericKeyboard, itemTapped item: NumericKeyboardItem, atPosition position: Position) {
        print("pressed keyboard: (\(position.row), \(position.column))")
        let activeTextField: UITextField? = getActiveTextField()
        guard activeTextField != nil else {
            return
        }
        var currentText = activeTextField!.text ?? ""
        switch (position.row, position.column) {
        case (3, 0):
            activeTextField!.text = currentText + "."
        case (3, 1):
            activeTextField!.text = currentText + "0"
        case (3, 2):
            if currentText.count > 0 {
                currentText = String(currentText.dropLast())
            }
            activeTextField!.text = currentText
        default:
            let itemValue = position.row * 3 + position.column + 1
            activeTextField!.text = currentText + String(itemValue)
        }
    }
    
    func numericKeyboard(_ numericKeyboard: NumericKeyboard, itemLongPressed item: NumericKeyboardItem, atPosition position: Position) {
        print("Long pressed keyboard: (\(position.row), \(position.column))")
        
        let activeTextField = getActiveTextField()
        guard activeTextField != nil else {
            return
        }
        
        var currentText = activeTextField!.text ?? ""
        
        if (position.row, position.column) == (3, 2) {
            if currentText.count > 0 {
                currentText = String(currentText.dropLast())
            }
            activeTextField!.text = currentText
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        print("scrollViewDidScroll \(scrollView.contentOffset.y)")
        if tokenTotalAmountLabel.frame.maxY < scrollView.contentOffset.y {
            self.navigationItem.title = asset!.symbol
        } else {
            self.navigationItem.title = ""
        }
    }

}

extension SendAssetViewController {

    var gasPrice: GethBigInt {
        // TODO: get value from transactionSpeedSlider
        return GethBigInt(5000000000)
    }

    func completion(_ txHash: String?, _ error: Error?) {
        // Close activity indicator
        SVProgressHUD.dismiss()
        end = Date()
        let timeInterval1: Double = end.timeIntervalSince(start)
        print("Time to completion in _transfer: \(timeInterval1) seconds")
        
        guard error == nil && txHash != nil else {
            // Show toast
            DispatchQueue.main.async {
                print("SendAssetViewController \(error.debugDescription)")
                let banner = NotificationBanner.generate(title: String(describing: error), style: .danger)
                banner.duration = 5
                banner.show()
            }
            return
        }
        print("Result of transfer is \(txHash!)")
        // Show toast
        DispatchQueue.main.async {
            let banner = NotificationBanner.generate(title: "Success. Result of transfer is \(txHash!)", style: .success)
            banner.duration = 5
            banner.show()
        }
    }

}
