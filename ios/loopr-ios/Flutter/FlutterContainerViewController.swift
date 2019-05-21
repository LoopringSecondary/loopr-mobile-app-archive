//
//  FlutterContainerViewController.swift
//  loopr-ios
//
//  Created by ruby on 3/25/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit
import Flutter
import NotificationBannerSwift

@objc class FlutterQRCodeViewController: UIViewController {

    var flutterChannel: FlutterChannel = .blank
    
    convenience init(flutterChannel: FlutterChannel) {
        self.init(nibName: nil, bundle: nil)
        self.flutterChannel = flutterChannel
    }
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setBackButton()
        view.theme_backgroundColor = ColorPicker.backgroundColor
        
        loadFlutterWidget()
    }

    // How to use `FlutterDartProject`?
    // https://stackoverflow.com/questions/55127464/how-to-use-flutterdartproject
    func loadFlutterWidget() {
        let flutterEngine: FlutterEngine?
        switch flutterChannel {
        case .qrCodeDisplay:
            flutterEngine = FlutterEngine(name: "io.flutter", project: nil)
            flutterEngine?.run(withEntrypoint: "qrCode")
        default:
            print("No flutter view is found")
            return
        }
        
        let flutterViewController = FlutterViewController(engine: flutterEngine, nibName: nil, bundle: nil)!
        
        // https://blog.testfairy.com/flutter-and-native-communication/
        let channel = FlutterMethodChannel(name: FlutterChannel.qrCodeDisplay.rawValue, binaryMessenger: flutterViewController)
        channel.setMethodCallHandler {(methodCall, result) in
            print(methodCall.method)
            
            // Need to move this to other places or rename FlutterContainerViewController
            if methodCall.method == FlutterMethod.qrCodeDisplayCopyAddress.rawValue {
                let address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
                print("pressedCopyAddressButton address: \(address)")
                UIPasteboard.general.string = address
                let banner = NotificationBanner.generate(title: "Address copied to clipboard successfully!", style: .success)
                banner.duration = 1
                banner.show()
            } else if methodCall.method == FlutterMethod.qrCodeDisplaySaveToAlbum.rawValue {
                let address = CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address
                print("pressedSaveToAlbum address: \(address)")
                // let image = UIImage.imageWithView(shareContentView)
                // QRCodeSaveToAlbum.shared.save(image: image)
            } else if methodCall.method == FlutterMethod.qrCodeDisplayGet.rawValue {
                result(CurrentAppWalletDataManager.shared.getCurrentAppWallet()!.address)
            } else {
                result(FlutterMethodNotImplemented)
                return
            }
        }
        
        addChildViewController(flutterViewController)
        flutterViewController.view.frame = self.view.frame
        self.view.addSubview(flutterViewController.view)
        
        flutterViewController.didMove(toParentViewController: self)
    }

}
