//
//  AddContactViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 8/17/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import Geth
import NotificationBannerSwift
import SVProgressHUD

class AddContactViewController: UIViewController, UITextFieldDelegate, QRCodeScanProtocol {
    
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var addressTextField: UITextField!
    @IBOutlet weak var noteTextField: UITextField!
    @IBOutlet weak var saveButton: GradientButton!
    
    var isCreatingContact = true
    var address: String = ""
    var name: String = ""
    var note: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        setBackButton()
        setNavigationBarItem()
        self.navigationItem.title = LocalizedString("Add Contact", comment: "")
        
        view.theme_backgroundColor = ColorPicker.backgroundColor
        contentView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        contentView.cornerRadius = 6
        contentView.applyShadow()
        
        // Name
        nameTextField.delegate = self
        nameTextField.tag = 0
        nameTextField.keyboardAppearance = Themes.isDark() ? .dark : .default
        nameTextField.font = FontConfigManager.shared.getDigitalFont()
        nameTextField.theme_tintColor = GlobalPicker.contrastTextColor
        nameTextField.placeholder = LocalizedString("Contact Name", comment: "")
        nameTextField.text = name
        nameTextField.contentMode = UIViewContentMode.bottom
        nameTextField.setLeftPaddingPoints(8)
        nameTextField.setRightPaddingPoints(8)
        nameTextField.cornerRadius = 6
        
        // Address
        addressTextField.delegate = self
        addressTextField.tag = 1
        addressTextField.keyboardType = .alphabet
        addressTextField.keyboardAppearance = Themes.isDark() ? .dark : .default
        addressTextField.font = FontConfigManager.shared.getDigitalFont()
        addressTextField.theme_tintColor = GlobalPicker.contrastTextColor
        addressTextField.placeholder = LocalizedString("Contact Address", comment: "")
        addressTextField.text = address
        addressTextField.contentMode = UIViewContentMode.bottom
        addressTextField.setLeftPaddingPoints(8)
        addressTextField.setRightPaddingPoints(8)
        addressTextField.cornerRadius = 6
        
        // Note
        noteTextField.delegate = self
        noteTextField.tag = 2
        noteTextField.keyboardAppearance = Themes.isDark() ? .dark : .default
        noteTextField.font = FontConfigManager.shared.getDigitalFont()
        noteTextField.theme_tintColor = GlobalPicker.contrastTextColor
        noteTextField.placeholder = LocalizedString("Contact Note", comment: "")
        noteTextField.text = note
        noteTextField.contentMode = UIViewContentMode.bottom
        noteTextField.setLeftPaddingPoints(8)
        noteTextField.setRightPaddingPoints(8)
        noteTextField.cornerRadius = 6
        
        // Save button
        saveButton.title = LocalizedString("Save", comment: "")
        
        let scrollViewTap = UITapGestureRecognizer(target: self, action: #selector(scrollViewTapped))
        scrollViewTap.numberOfTapsRequired = 1
        scrollView.addGestureRecognizer(scrollViewTap)
        scrollView.delaysContentTouches = false
    }
    
    func setNavigationBarItem() {
        let icon = UIImage.init(named: "dropdown-scan")
        let button = UIBarButtonItem(image: icon, style: UIBarButtonItemStyle.plain, target: self, action: #selector(self.pressedScanButton))
        self.navigationItem.rightBarButtonItem = button
    }
    
    @objc func pressedScanButton() {
        let viewController = ScanQRCodeViewController()
        // TODO: do we need to support these types
        viewController.expectedQRCodeTypes = [.address]
        viewController.delegate = self
        viewController.shouldPop = false
        viewController.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }

    @objc func scrollViewTapped() {
        print("scrollViewTapped")
        addressTextField.resignFirstResponder()
        nameTextField.resignFirstResponder()
        noteTextField.resignFirstResponder()        
    }
    
    @IBAction func pressedSaveButton(_ sender: Any) {
        if validateName() && validateAddress() {
            let contact = Contact(name: nameTextField.text!.trim(),
                                  address: addressTextField.text!, note: noteTextField.text ?? "")
            if isCreatingContact {
                if !ContactDataManager.shared.contacts.contains(where: { (item) -> Bool in
                    return item.address.uppercased() == contact.address.uppercased()}) {
                    ContactDataManager.shared.addContact(contact)
                    self.navigationController?.popViewController(animated: true)
                } else {
                    let notificationTitle = LocalizedString("Duplicated address", comment: "")
                    let banner = NotificationBanner.generate(title: notificationTitle, style: .danger)
                    banner.duration = 1.5
                    banner.show()
                    return
                }
            } else {
                ContactDataManager.shared.updateContact(contact)
                self.navigationController?.popViewController(animated: true)
            }

        }
    }
    
    func validateAddress() -> Bool {
        if var toAddress = addressTextField.text {
            toAddress = toAddress.trim()
            if !toAddress.isEmpty && toAddress.isHexAddress() {
                var error: NSError?
                if GethNewAddressFromHex(toAddress, &error) != nil {
                    self.address = toAddress
                    return true
                }
            } else {
                let notificationTitle = LocalizedString("Please input a correct address", comment: "")
                let banner = NotificationBanner.generate(title: notificationTitle, style: .danger)
                banner.duration = 1.5
                banner.show()
            }
        }
        return false
    }
    
    func validateName() -> Bool {
        if var name = nameTextField.text {
            name = name.trim()
            if !name.isEmpty {
                self.name = name
                return true
            } else {
                let notificationTitle = LocalizedString("Contact Input", comment: "")
                let banner = NotificationBanner.generate(title: notificationTitle, style: .danger)
                banner.duration = 1.5
                banner.show()
            }
        }
        return false
    }
    
    func setResultOfScanningQRCode(valueSent: String, type: QRCodeType) {
        switch type {
        case .address:
            self.addressTextField.text = valueSent
            self.navigationController?.popViewController(animated: true)
        case .mnemonic, .keystore, .privateKey, .login, .convert,
             .submitOrder, .cancelOrder, .p2pOrder, .approve, .undefined:
            let notificationTitle = LocalizedString("Please enter the token address", comment: "")
            let banner = NotificationBanner.generate(title: notificationTitle, style: .danger)
            banner.duration = 1.5
            banner.show()
        }
    }
}
