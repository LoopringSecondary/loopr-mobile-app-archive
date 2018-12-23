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
        releaseNoteTextView.attributedText = NSAttributedString(string: AppServiceUpdateManager.shared.latestBuildDescription, attributes: attributes)
        releaseNoteTextView.textColor = UIColor.text1
        releaseNoteTextView.font = FontConfigManager.shared.getRegularFont(size: 14)

        seperateLine2.theme_backgroundColor = ColorPicker.cardHighLightColor
        
        updateButton.title = LocalizedString("Update", comment: "")
        updateButton.addTarget(self, action: #selector(pressedUpdateButton), for: .touchUpInside)
        
        skipButton.title = LocalizedString("Skip_Verification", comment: "")
        skipButton.setBlack()
        skipButton.addTarget(self, action: #selector(pressedSkipButton), for: .touchUpInside)
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(backgroundViewTapped))
        tap.numberOfTapsRequired = 1
        view.addGestureRecognizer(tap)
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
    
    @objc func backgroundViewTapped() {
        skipClosure?()
        self.dismiss(animated: true, completion: {
        })
    }

}
