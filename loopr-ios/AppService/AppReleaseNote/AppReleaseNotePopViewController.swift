//
//  AppReleaseNotePopViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/23/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import SwiftyMarkdown

class AppReleaseNotePopViewController: UIViewController {

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

        // Do any additional setup after loading the view.
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
        let md = SwiftyMarkdown(string: AppServiceUpdateManager.shared.latestBuildDescription)
        md.h1.fontName = "Rubik-Medium"
        md.h1.fontSize = 18
        md.h1.color = UIColor.init(rgba: "#32384C")
        md.body.fontName = "Rubik-Regular"
        md.body.fontSize = 14
        md.body.color = UIColor.init(rgba: "#32384C")
        releaseNoteTextView.attributedText = md.attributedString()

        seperateLine2.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        updateButton.title = LocalizedString("Update", comment: "")
        updateButton.addTarget(self, action: #selector(pressedUpdateButton), for: .touchUpInside)
        
        skipButton.title = LocalizedString("Skip_Verification", comment: "")
        skipButton.setBlack()
        skipButton.addTarget(self, action: #selector(pressedSkipButton), for: .touchUpInside)
        
    }

    @objc func pressedUpdateButton(_ sender: Any) {
        updateClosure?()
        self.dismiss(animated: true, completion: {
        })
    }
    
    @objc func pressedSkipButton(_ sender: Any) {
        skipClosure?()
        self.dismiss(animated: true, completion: {
        })
    }

}
