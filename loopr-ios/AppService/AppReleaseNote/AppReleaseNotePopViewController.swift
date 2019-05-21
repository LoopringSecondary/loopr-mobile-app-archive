//
//  AppReleaseNotePopViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/23/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import SwiftyMarkdown
// import CDMarkdownKit
import Crashlytics

class AppReleaseNotePopViewController: UIViewController {

    var popFromSettingViewController: Bool = true
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var containerViewLayoutConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var appReleaseNoteTitleLabel: UILabel!
    @IBOutlet weak var seperateLine1: UIView!
    @IBOutlet weak var releaseNoteTextView: UITextView!
    @IBOutlet weak var seperateLine2: UIView!
    @IBOutlet weak var skipButton: GradientButton!
    @IBOutlet weak var updateButton: GradientButton!
    
    var skipClosure: (() -> Void)?
    var updateClosure: (() -> Void)?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.modalPresentationStyle = .custom
        view.backgroundColor = .clear
        containerView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        containerView.layer.cornerRadius = 6
        containerView.clipsToBounds = true
        
        appReleaseNoteTitleLabel.theme_textColor = GlobalPicker.textColor
        appReleaseNoteTitleLabel.font = FontConfigManager.shared.getMediumFont(size: 17)
        appReleaseNoteTitleLabel.textAlignment = .center
        appReleaseNoteTitleLabel.text = LocalizedString("App Version", comment: "") + " " + AppServiceUpdateManager.shared.latestBuildVersion
        
        seperateLine1.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        releaseNoteTextView.backgroundColor = .clear
        
        /*
        let md = SwiftyMarkdown(string: AppServiceUpdateManager.shared.latestBuildDescription)
        md.body.fontName = "Rubik-Regular"
        md.body.fontSize = 17
        md.body.color = UIColor.text1
        releaseNoteTextView.attributedText = md.attributedString()
        */
        
        /*
        let markdownParser = CDMarkdownParser()
        releaseNoteTextView.attributedText = markdownParser.parse(AppServiceUpdateManager.shared.latestBuildDescription)
        */

        let style = NSMutableParagraphStyle()
        style.lineSpacing = 20
        let attributes = [NSAttributedStringKey.paragraphStyle: style]
        releaseNoteTextView.attributedText = NSAttributedString(string: AppServiceUpdateManager.shared.getReleaseNote(), attributes: attributes)
        releaseNoteTextView.textColor = UIColor.text1
        releaseNoteTextView.font = FontConfigManager.shared.getRegularFont(size: 14)

        seperateLine2.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        updateButton.title = LocalizedString("Update", comment: "")
        updateButton.addTarget(self, action: #selector(pressedUpdateButton), for: .touchUpInside)

        if popFromSettingViewController {
            skipButton.title = LocalizedString("Back", comment: "")
        } else {
            skipButton.title = LocalizedString("Skip_Verification", comment: "")
        }
        skipButton.setGradient(colors: [UIColor.dark4, UIColor.dark4], hightlightedColors: [UIColor.dark3, UIColor.dark3], gradientOrientation: .bottomLeftTopRight)
        skipButton.addTarget(self, action: #selector(pressedSkipButton), for: .touchUpInside)
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(backgroundViewTapped))
        tap.numberOfTapsRequired = 1
        view.addGestureRecognizer(tap)
    }

    @objc func pressedUpdateButton(_ sender: Any) {
        updateClosure?()
        self.dismiss(animated: true, completion: {
        })
        
        // Navigate to website
        if Production.getCurrent() == .upwallet {
            if Production.isAppStoreVersion() {
                UIApplication.shared.open(NSURL(string:"https://itunes.apple.com/app/1441613740?mt=8")! as URL, options: [:], completionHandler: { (_) in
                    
                })
            } else {
                UIApplication.shared.open(NSURL(string:"itms-services://?action=download-manifest&url=https://loopr.io/ios/manifest.plist")! as URL, options: [:], completionHandler: { (_) in
                    
                })
            }
        } else if let url = URL(string: Production.getUrlText()) {
            UIApplication.shared.open(url)
        }
        
        AppServiceUpdateManager.shared.setLargestSkipBuildVersion()
        Answers.logCustomEvent(withName: "App Update Notification v1",
                               customAttributes: [
                                "update": "true"])
    }
    
    @objc func pressedSkipButton(_ sender: Any) {
        skipClosure?()
        self.dismiss(animated: true, completion: {
        })
        if popFromSettingViewController {
            
        } else {
            AppServiceUpdateManager.shared.setLargestSkipBuildVersion()
            Answers.logCustomEvent(withName: "App Update Notification v1",
                                   customAttributes: [
                                    "update": "false"])
        }
    }
    
    @objc func backgroundViewTapped() {
        skipClosure?()
        self.dismiss(animated: true, completion: {
        })
    }

}
