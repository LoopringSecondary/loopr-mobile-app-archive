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

        NewsDetailUIStyleConfig.shared.fontFammily = FontConfigManager.shared.getCharactorFont().familyName

        modalPresentationStyle = .custom
        transitioningDelegate = self
        
        view.frame = UIScreen.main.bounds
        view.theme_backgroundColor = ColorPicker.backgroundColor
        webView.theme_backgroundColor = ColorPicker.backgroundColor
        webView.scrollView.theme_backgroundColor = ColorPicker.backgroundColor
        webView.isOpaque = false
        webView.alpha = 0
        webView.isHidden = true
        
        setupCard()
        setupNavigationBar()
    }
    
    fileprivate func setupCard() {
        card.layer.cornerRadius = GarlandConfig.shared.cardRadius
        card.theme_backgroundColor = ColorPicker.backgroundColor
    }
    
    fileprivate func setupNavigationBar() {
        let navigationItem = UINavigationItem(title: news.title)
        
        // Back button
        let backButton = UIButton(type: UIButtonType.custom)
        
        backButton.theme_setImage(GlobalPicker.close, forState: .normal)
        backButton.theme_setImage(GlobalPicker.closeHighlight, forState: .highlighted)
        
        // Default left padding is 20. It should be 12 in our design.
        backButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: -16, bottom: 0, right: 8)
        backButton.addTarget(self, action: #selector(closeButtonAction(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        backButton.frame = CGRect(x: 0, y: 0, width: 40, height: 40)

        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: backButton)
        navigationItem.hidesBackButton = true
        
        // Safari button
        let safariButton = UIButton(type: UIButtonType.custom)
        safariButton.setImage(UIImage(named: "Safari-item-button"), for: .normal)
        safariButton.setImage(UIImage(named: "Safari-item-button")?.alpha(0.3), for: .highlighted)
        safariButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: 8, bottom: 0, right: -8)
        safariButton.addTarget(self, action: #selector(pressedSafariButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        safariButton.frame = CGRect(x: 0, y: 0, width: 23, height: 23)
        let shareBarButton = UIBarButtonItem(customView: safariButton)
        navigationItem.rightBarButtonItem = shareBarButton
        
        navigationBar.pushItem(navigationItem, animated: false)
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
        webView.loadHTMLString("<body><font size='\(NewsDetailUIStyleConfig.shared.fontSize)'>\(news.content)</font></body>", baseURL: nil)
    }
    
    @objc fileprivate func closeButtonAction(_ button: UIBarButtonItem) {
        dismiss(animated: true, completion: nil)
    }
    
    @objc func pressedSafariButton(_ button: UIBarButtonItem) {
        if let url = URL(string: news.url) {
            UIApplication.shared.open(url)
        }
    }

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        let cssString = "body { white-space: pre-wrap; color: \(NewsDetailUIStyleConfig.shared.textColor); background-color: \(NewsDetailUIStyleConfig.shared.backgroundColor); font-family: \"\(NewsDetailUIStyleConfig.shared.fontFammily)\"; font-size: 100%; padding-left: 40px; padding-right: 40px; } img { width: 100%; padding-top: 0px; padding-bottom: 0px; }"
        let jsString = "var style = document.createElement('style'); style.innerHTML = '\(cssString)'; document.head.appendChild(style);"
        webView.evaluateJavaScript(jsString) { (_, _) in
            self.webView.isHidden = false
            UIView.animate(withDuration: NewsDetailUIStyleConfig.shared.webAlphaAnimationDuration, delay: 0, options: .curveLinear, animations: {
                self.webView.alpha = 1
            }, completion: { (_) in
                
            })
        }
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
