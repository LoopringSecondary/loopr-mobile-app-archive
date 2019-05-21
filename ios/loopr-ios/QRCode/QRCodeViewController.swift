//
//  QRCodeViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/25/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import Social
import NotificationBannerSwift

class QRCodeViewController: UIViewController {
    
    var navigationTitle = LocalizedString("Receive Code", comment: "")
    
    // Set a default value
    var address: String = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
    
    @IBOutlet weak var receiveQRCodeIconView: UIImageView!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var qrcodeImageView: UIImageView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var addressLabel: UILabel!

    @IBOutlet weak var seperateLine: UIView!
    @IBOutlet weak var copyAddressButton: GradientButton!
    @IBOutlet weak var saveToAlbumButton: UIButton!
    
    @IBOutlet weak var shareContentView: UIView!
    @IBOutlet weak var titleInShare: UILabel!
    @IBOutlet weak var titleImageInShare: UIImageView!
    @IBOutlet weak var qrcodeInShare: UIImageView!
    @IBOutlet weak var addressInShare: UILabel!
    @IBOutlet weak var shareImageView: UIImageView!
    @IBOutlet weak var productLabel: UILabel!
    @IBOutlet weak var urlInShare: UILabel!
    @IBOutlet weak var logoImageView: UIImageView!
    
    var qrcodeImage: UIImage!

    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationItem.title = navigationTitle
        
        view.theme_backgroundColor = ColorPicker.backgroundColor
        contentView.layer.cornerRadius = 8
        contentView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        contentView.applyShadow()
        
        seperateLine.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        receiveQRCodeIconView.image = UIImage(named: "Receive-qrcode-icon" + ColorTheme.getTheme())

        titleLabel.font = FontConfigManager.shared.getRegularFont(size: 16)
        titleLabel.theme_textColor = GlobalPicker.textColor
        titleLabel.text = Production.getProduct()

        addressLabel.theme_textColor = GlobalPicker.textColor
        addressLabel.font = UIFont(name: "Menlo", size: 14)
        
        copyAddressButton.setTitle(LocalizedString("Copy Address", comment: ""), for: .normal)
        
        saveToAlbumButton.setTitle(LocalizedString("Save to Album", comment: ""), for: .normal)
        saveToAlbumButton.theme_setTitleColor(GlobalPicker.textColor, forState: .normal)
        saveToAlbumButton.theme_setTitleColor(GlobalPicker.textDarkColor, forState: .highlighted)
        saveToAlbumButton.titleLabel?.font = FontConfigManager.shared.getMediumFont(size: 14)
        
        setupShareButton()
        setBackButton()
        setupShareView()
        
        addressLabel.text = address
        
        let data = address.data(using: String.Encoding.isoLatin1, allowLossyConversion: false)
        generateQRCode(from: data!)
    }
    
    func setupShareView() {
        shareImageView.image = UIImage(named: "Share-wallet")
        logoImageView.image = UIImage(named: "\(Production.getProduct())_share_logo")
        
        titleInShare.font = FontConfigManager.shared.getCharactorFont(size: 20)
        titleInShare.theme_textColor = GlobalPicker.contrastTextColor
        titleInShare.text = "Loopring"
        
        if SettingDataManager.shared.getCurrentLanguage().name == "zh-Hans" || SettingDataManager.shared.getCurrentLanguage().name  == "zh-Hant" {
            titleInShare.isHidden = true
            titleImageInShare.isHidden = false
        } else {
            titleInShare.isHidden = false
            titleImageInShare.isHidden = true
        }
        
        addressInShare.font = FontConfigManager.shared.getCharactorFont(size: 12)
        addressInShare.theme_textColor = GlobalPicker.contrastTextDarkColor
        addressInShare.text = address
        
        productLabel.font = FontConfigManager.shared.getCharactorFont(size: 14)
        productLabel.theme_textColor = GlobalPicker.contrastTextDarkColor
        productLabel.text = Production.getProduct()
        
        urlInShare.font = FontConfigManager.shared.getCharactorFont(size: 12)
        urlInShare.theme_textColor = GlobalPicker.contrastTextColor
        urlInShare.text = Production.getUrlText()
    }
    
    func setupShareButton() {
        let shareButton = UIButton(type: UIButtonType.custom)
        shareButton.setImage(UIImage(named: "ShareButtonImage"), for: .normal)
        shareButton.setImage(UIImage(named: "ShareButtonImage")?.alpha(0.3), for: .highlighted)
        shareButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 8, bottom: 0, right: -8)
        shareButton.addTarget(self, action: #selector(pressedShareButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        shareButton.frame = CGRect(x: 0, y: 0, width: 23, height: 23)
        let shareBarButton = UIBarButtonItem(customView: shareButton)
        
        self.navigationItem.rightBarButtonItem = shareBarButton
        // Add swipe to go-back feature back which is a system default gesture
        self.navigationController?.interactivePopGestureRecognizer?.delegate = self
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        qrcodeImageView.image = qrcodeImage
        qrcodeInShare.image = qrcodeImage
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        view.bringSubview(toFront: receiveQRCodeIconView)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    func generateQRCode(from data: Data) {
        let ciContext = CIContext()
        if let filter = CIFilter(name: "CIQRCodeGenerator") {
            filter.setValue(data, forKey: "inputMessage")
            let transform = CGAffineTransform(scaleX: 5, y: 5)
            let upScaledImage = filter.outputImage?.transformed(by: transform)
            let cgImage = ciContext.createCGImage(upScaledImage!, from: upScaledImage!.extent)
            qrcodeImage = UIImage(cgImage: cgImage!)
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
    }
    
    @IBAction func pressedShareButton(_ button: UIBarButtonItem) {
        let text = LocalizedString("My wallet address", comment: "")
        let image = UIImage.imageWithView(shareContentView)
        let png = UIImagePNGRepresentation(image)
        let shareAll = [text, png!] as [Any]
        let activityVC = UIActivityViewController(activityItems: shareAll, applicationActivities: nil)
        activityVC.excludedActivityTypes = [.message, .mail]
        activityVC.popoverPresentationController?.sourceView = self.view
        self.present(activityVC, animated: true, completion: nil)
    }

    @IBAction func pressedCopyAddressButton(_ sender: Any) {
        print("pressedCopyAddressButton address: \(address)")
        UIPasteboard.general.string = address
        let banner = NotificationBanner.generate(title: "Address copied to clipboard successfully!", style: .success)
        banner.duration = 1
        banner.show()
    }

    @IBAction func pressedSaveToAlbum(_ sender: Any) {
        let address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
        print("pressedSaveToAlbum address: \(address)")
        let image = UIImage.imageWithView(shareContentView)
        QRCodeSaveToAlbum.shared.save(image: image)
    }
}
