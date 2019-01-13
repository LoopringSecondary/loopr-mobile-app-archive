//
//  NewsDetailViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/29/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import WebKit

class NewsDetailViewController: UIViewController, WKNavigationDelegate, UIScrollViewDelegate {

    var currentIndex: Int = 0
    var news: News!
    var isFirtTimeAppear: Bool = true

    // @IBOutlet weak var navigationBar: UINavigationBar!

    @IBOutlet weak var webView: WKWebView!
    // @IBOutlet weak var progressView: UIProgressView!
    var showProgressView: Bool = true
    var progressKVOhandle: NSKeyValueObservation?

    var enablePullToNextPage: Bool = false
    var isPullToNextPageImageViewAnimating: Bool = false
    var isPullToNextPageImageViewUp: Bool = true
    let pullToNextPageBottomViewHeight: CGFloat = 120
    let pullToNextPageBottomView = UIView()
    let pullToNextPageTitleLabel = UILabel()
    let pullToNextPageImageView = UIImageView()

    var didCloseButtonClosure: (() -> Void)?

    override open func viewDidLoad() {
        super.viewDidLoad()

        // webView.alpha = 0
        // webView.isHidden = true
        
        view.theme_backgroundColor = ColorPicker.cardBackgroundColor
        webView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        webView.scrollView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        webView.isOpaque = false
        
        /*
        progressView.theme_trackTintColor = ColorPicker.cardHighLightColor
        progressView.theme_backgroundColor = ColorPicker.cardHighLightColor
        progressView.tintColor = UIColor.theme
        progressView.progressTintColor = UIColor.theme
        progressView.setProgress(0, animated: false)
        progressView.alpha = 0.0
        webView.addObserver(self, forKeyPath: "estimatedProgress", options: .new, context: nil) // add observer for key path
        */
        
        setBackButton()
        setupNavigationBar()
    }

    fileprivate func setupNavigationBar() {
        let title: String = news.category.description
        let navigationItem = UINavigationItem(title: title)
        
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
        
        // navigationBar.pushItem(navigationItem, animated: false)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        UINavigationBar.appearance().theme_barTintColor = ColorPicker.barTintColor
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        UINavigationBar.appearance().theme_barTintColor = ColorPicker.cardBackgroundColor
        // navigationBar.shadowImage = UIImage()
        
        let titleHtml = "<h2 style=\"color:white;\"><font size='\(NewsDetailUIStyleConfig.shared.titleFontSize)'>\(news.title)</font></h2>"
        let subTitleHtml = "<font size='\(NewsDetailUIStyleConfig.shared.subTitleFontSize)'><p>\(news.publishTime)  来源:\(news.source)</p></font>"
        let contentHtml = "<font size='\(NewsDetailUIStyleConfig.shared.fontSize)'>\(news.content)</font>"
        webView.loadHTMLString("<body>\(titleHtml)\(subTitleHtml)<br><br>\(contentHtml)</body>", baseURL: nil)
        setupRefreshControlAtBottom()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        guard isFirtTimeAppear else {
            return
        }
        
        isFirtTimeAppear = false
        webView.navigationDelegate = self
        webView.scrollView.delegate = self
    }
    
    @objc fileprivate func closeButtonAction(_ button: UIBarButtonItem) {
        // didCloseButtonClosure?()
        // dismiss(animated: true, completion: nil)
        if self.navigationController != nil {
            self.navigationController?.popViewController(animated: true)
        } else {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    @objc func pressedSafariButton(_ button: UIBarButtonItem) {
        if let url = URL(string: news.url) {
            UIApplication.shared.open(url)
        }
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey: Any]?, context: UnsafeMutableRawPointer?) {
        // progressView.progress = Float(webView.estimatedProgress)
    }

    func setupRefreshControlAtBottom() {
        pullToNextPageBottomView.frame = CGRect(x: 0, y: webView.height, width: UIScreen.main.bounds.width, height: pullToNextPageBottomViewHeight)
        webView.addSubview(pullToNextPageBottomView)

        pullToNextPageTitleLabel.frame = CGRect(x: 0, y: 20, width: UIScreen.main.bounds.width, height: 30)
        pullToNextPageTitleLabel.theme_textColor = GlobalPicker.textColor
        pullToNextPageTitleLabel.font = FontConfigManager.shared.getRegularFont(size: 14)
        pullToNextPageTitleLabel.textAlignment = .center
        pullToNextPageTitleLabel.text = LocalizedString("Pull to read more", comment: "")
        pullToNextPageBottomView.addSubview(pullToNextPageTitleLabel)
        
        pullToNextPageImageView.frame = CGRect(x: (pullToNextPageTitleLabel.width-30)*0.5, y: pullToNextPageTitleLabel.frame.maxY + 4, width: 30, height: 30)
        pullToNextPageImageView.image = UIImage(named: "News-pull-refresh-bottom")
        pullToNextPageImageView.transform = CGAffineTransform(rotationAngle: CGFloat(-1/180*Double.pi))
        pullToNextPageImageView.contentMode = .scaleAspectFit
        pullToNextPageBottomView.addSubview(pullToNextPageImageView)
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        print("scrollView y: \(scrollView.contentOffset.y)")
        let bottomY = scrollView.contentSize.height - scrollView.bounds.size.height + scrollView.contentInset.bottom
        print("the bottom of scrollView: \(bottomY)")
        if scrollView.contentOffset.y > bottomY {
            let delta = scrollView.contentOffset.y  - bottomY
            pullToNextPageBottomView.isHidden = false
            pullToNextPageBottomView.frame = CGRect(x: 0, y: webView.height - delta, width: UIScreen.main.bounds.width, height: pullToNextPageBottomViewHeight)
            if delta > pullToNextPageBottomView.height {
                pullToNextPageTitleLabel.text = LocalizedString("Release to read more", comment: "")
                if !isPullToNextPageImageViewAnimating && isPullToNextPageImageViewUp {
                    isPullToNextPageImageViewAnimating = true
                    self.pullToNextPageBottomView.layoutIfNeeded()
                    UIView.animate(withDuration: 0.2, delay: 0, options: .curveEaseInOut, animations: {
                        self.pullToNextPageImageView.transform = CGAffineTransform(rotationAngle: CGFloat(-Double.pi))
                        self.pullToNextPageBottomView.layoutIfNeeded()
                    }) { (finished) in
                        print(finished)
                        self.enablePullToNextPage = true
                        self.isPullToNextPageImageViewUp = false
                        self.isPullToNextPageImageViewAnimating = false
                    }
                }
            } else {
                pullToNextPageTitleLabel.text = LocalizedString("Pull to read more", comment: "")
                enablePullToNextPage = false
                if !isPullToNextPageImageViewAnimating && !isPullToNextPageImageViewUp {
                    isPullToNextPageImageViewAnimating = true
                    self.pullToNextPageBottomView.layoutIfNeeded()
                    UIView.animate(withDuration: 0.2, delay: 0, options: .curveEaseInOut, animations: {
                        self.pullToNextPageImageView.transform = CGAffineTransform(rotationAngle: CGFloat(-1/180*Double.pi))
                        self.pullToNextPageBottomView.layoutIfNeeded()
                    }) { (finished) in
                        print(finished)
                        self.enablePullToNextPage = false
                        self.isPullToNextPageImageViewUp = true
                        self.isPullToNextPageImageViewAnimating = false
                    }
                }
            }
        } else {
            pullToNextPageBottomView.isHidden = true
        }
    }
    
    func scrollViewWillBeginDecelerating(_ scrollView: UIScrollView) {
        if enablePullToNextPage {
            let news = NewsDataManager.shared.informationItems[currentIndex+1]
            let detailViewController = NewsDetailViewController.init(nibName: "NewsDetailViewController", bundle: nil)
            detailViewController.currentIndex = currentIndex+1
            detailViewController.news = news
            detailViewController.didCloseButtonClosure = {
                self.didCloseButtonClosure?()
                self.dismiss(animated: false, completion: {
                    
                })
            }
            self.present(detailViewController, animated: true, completion: {
                self.navigationController?.popViewController(animated: false)
            })
        }
    }

    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        if showProgressView {
            UIApplication.shared.isNetworkActivityIndicatorVisible = true
            
            /*
            progressView.alpha = 0.0
            UIView.animate(withDuration: 0.33, delay: 0.0, options: .curveEaseInOut, animations: {
                self.progressView.alpha = 1.0
            })
            */
        }
    }

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        applyCss()

        showProgressView = false
        UIApplication.shared.isNetworkActivityIndicatorVisible = false
        /*
        progressView.alpha = 1.0
        UIView.animate(withDuration: 0.33, delay: 0.0, options: .curveEaseInOut, animations: {
            self.progressView.alpha = 0.0
        })
        */
    }
    
    func applyCss() {
        let bodyCssString = "body { white-space: pre-wrap; color: \(NewsDetailUIStyleConfig.shared.textColor); background-color: \(NewsDetailUIStyleConfig.shared.backgroundColor); font-family: \"\(NewsDetailUIStyleConfig.shared.fontFamily)\"; font-size: 100%; padding-left: 40px; padding-right: 40px; text-decoration: none; }"
        let imageCssString = "img { width: 100%; padding-top: 0px; padding-bottom: 0px; border-radius: \(NewsDetailUIStyleConfig.shared.imageCornerRadius)px;}"
        let aLinkCssString = "a:link { color: \(NewsDetailUIStyleConfig.shared.textColor);}"
        let cssString = "\(bodyCssString) \(imageCssString) \(aLinkCssString)"
        let jsString = "var style = document.createElement('style'); style.innerHTML = '\(cssString)'; document.head.appendChild(style);"
        webView.evaluateJavaScript(jsString) { (_, _) in
            self.showWebView()
        }
    }
    
    func showWebView() {
        self.webView.isHidden = false
        UIView.animate(withDuration: NewsDetailUIStyleConfig.shared.webAlphaAnimationDuration, delay: 0, options: .curveLinear, animations: {
            self.webView.alpha = 1
        }, completion: { (_) in
            
        })
    }

}
