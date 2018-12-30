//
//  NewsDetailViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/29/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import WebKit

class NewsDetailViewController: UIViewController, WKNavigationDelegate {

    var news: News!
    var isFirtTimeAppear: Bool = true

    @IBOutlet weak var navigationBar: UINavigationBar!
    @IBOutlet open var card: UIView!
    @IBOutlet weak var webView: WKWebView!
    
    fileprivate let userCardPresentAnimationController = NewsDetailPresentAnimationController()
    fileprivate let userCardDismissAnimationController = NewsDetailDismissAnimationController()
    
    override open func viewDidLoad() {
        super.viewDidLoad()
        modalPresentationStyle = .custom
        transitioningDelegate = self
        
        view.frame = UIScreen.main.bounds
        view.theme_backgroundColor = ColorPicker.backgroundColor
        setupCard()
        
        // closeButton.addTarget(self, action: #selector(closeButtonAction), for: .touchDown)
    }
    
    fileprivate func setupCard() {
        card.layer.cornerRadius = GarlandConfig.shared.cardRadius
        card.theme_backgroundColor = ColorPicker.backgroundColor
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        guard isFirtTimeAppear else {
            return
        }
        
        isFirtTimeAppear = false
        webView.navigationDelegate = self
        webView.loadHTMLString(news.content, baseURL: nil)
    }
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        let cssString = "body { font-size: 50px; color: #f00 }"
        let jsString = "var style = document.createElement('style'); style.innerHTML = '\(cssString)'; document.head.appendChild(style);"
        webView.evaluateJavaScript(jsString, completionHandler: nil)
    }

}

// MARK: Actions
extension NewsDetailViewController {
    @objc fileprivate func closeButtonAction() {
        dismiss(animated: true, completion: nil)
    }
}

// MARK: Transition delegate methods
extension NewsDetailViewController: UIViewControllerTransitioningDelegate {
    
    public func animationController(forPresented presented: UIViewController, presenting: UIViewController, source: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        return userCardPresentAnimationController
    }
    
    public func animationController(forDismissed dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        return userCardDismissAnimationController
    }
}

