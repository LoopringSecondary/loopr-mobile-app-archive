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
    var newsObject: NewsProtocol!
    var isFirtTimeAppear: Bool = true

    @IBOutlet weak var navigationBar: UINavigationBar!

    @IBOutlet open var card: UIView!
    @IBOutlet weak var webView: WKWebView!
    @IBOutlet weak var progressView: UIProgressView!
    var showProgressView: Bool = true
    var progressKVOhandle: NSKeyValueObservation?

    fileprivate let userCardPresentAnimationController = NewsDetailPresentAnimationController()
    fileprivate let userCardDismissAnimationController = NewsDetailDismissAnimationController()
    
    var enablePullToNextPage: Bool = false
    var isPullToNextPageImageViewAnimating: Bool = false
    var isPullToNextPageImageViewUp: Bool = true
    let pullToNextPageBottomViewHeight: CGFloat = 120
    let pullToNextPageBottomView = UIView()
    let pullToNextPageTitleLabel = UILabel()
    let pullToNextPageImageView = UIImageView()

    var didCloseButtonClosure: (() -> Void)?

    /*
    let animator = ModalPushPopAnimator()
    var edgeView: UIView? {
        get {
            if (_edgeView == nil && isViewLoaded) {
                _edgeView = UIView()
                _edgeView?.translatesAutoresizingMaskIntoConstraints = false
                view.addSubview(_edgeView!)
                _edgeView?.backgroundColor = UIColor(white: 1.0, alpha: 0.005)
                let bindings = ["edgeView": _edgeView!]
                let options = NSLayoutFormatOptions(rawValue: 0)
                let hConstraints = NSLayoutConstraint.constraints(withVisualFormat: "|-0-[edgeView(5)]", options: options, metrics: nil, views: bindings)
                let vConstraints = NSLayoutConstraint.constraints(withVisualFormat: "V:|-0-[edgeView]-0-|", options: options, metrics: nil, views: bindings)
                view?.addConstraints(hConstraints)
                view?.addConstraints(vConstraints)
            }
            return _edgeView
        }
    }
    private var _edgeView: UIView?
    */

    override open func viewDidLoad() {
        super.viewDidLoad()

        if let news = newsObject as? News {
            modalPresentationStyle = .custom
            transitioningDelegate = self
            webView.alpha = 0
            webView.isHidden = true
        } else {
            
        }
        view.frame = UIScreen.main.bounds
        view.theme_backgroundColor = ColorPicker.cardBackgroundColor
        webView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        webView.scrollView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        webView.isOpaque = false
        
        progressView.theme_trackTintColor = ColorPicker.cardHighLightColor
        progressView.theme_backgroundColor = ColorPicker.cardHighLightColor
        progressView.tintColor = UIColor.theme
        progressView.progressTintColor = UIColor.theme
        progressView.setProgress(0, animated: false)
        progressView.alpha = 0.0
        webView.addObserver(self, forKeyPath: "estimatedProgress", options: .new, context: nil) // add observer for key path
        
        setupCard()
        setupNavigationBar()
        
        /*
        let recognizer = UIScreenEdgePanGestureRecognizer(target: self, action: #selector(self.handleGesture))
        recognizer.edges = UIRectEdge.left
        edgeView?.addGestureRecognizer(recognizer)
        */
    }
    
    fileprivate func setupCard() {
        card.layer.cornerRadius = GarlandConfig.shared.cardRadius
        card.theme_backgroundColor = ColorPicker.cardBackgroundColor
    }
    
    fileprivate func setupNavigationBar() {
        let navigationItem = UINavigationItem(title: newsObject.title)
        
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

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        UINavigationBar.appearance().theme_barTintColor = ColorPicker.barTintColor
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        UINavigationBar.appearance().theme_barTintColor = ColorPicker.cardBackgroundColor
        navigationBar.shadowImage = UIImage()
        
        if let news = newsObject as? News {
            webView.loadHTMLString("<body><font size='\(NewsDetailUIStyleConfig.shared.fontSize)'>\(news.content)</font></body>", baseURL: nil)
        } else if let blog = newsObject as? Blog {
            let url = URL(string: blog.url)!
            let request = URLRequest(url: url)
            webView.navigationDelegate = self
            webView.load(request)
        }
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
        dismiss(animated: true, completion: nil)
        didCloseButtonClosure?()
    }
    
    @objc func pressedSafariButton(_ button: UIBarButtonItem) {
        if let url = URL(string: newsObject.url) {
            UIApplication.shared.open(url)
        }
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey: Any]?, context: UnsafeMutableRawPointer?) {
        progressView.progress = Float(webView.estimatedProgress)
    }
    
    /*
    @objc func handleGesture(recognizer: UIScreenEdgePanGestureRecognizer) {
        self.animator.percentageDriven = true
        let percentComplete = recognizer.location(in: view).x / view.bounds.size.width / 2.0
        switch recognizer.state {
        case .began: dismiss(animated: true, completion: nil)
        case .changed: animator.update(percentComplete > 0.99 ? 0.99 : percentComplete)
        case .ended, .cancelled:
            (recognizer.velocity(in: view).x < 0) ? animator.cancel() : animator.finish()
            self.animator.percentageDriven = false
        default: ()
        }
    }
    */
    
    func setupRefreshControlAtBottom() {
        pullToNextPageBottomView.frame = CGRect(x: 0, y: webView.height, width: webView.width, height: pullToNextPageBottomViewHeight)
        webView.addSubview(pullToNextPageBottomView)

        pullToNextPageTitleLabel.frame = CGRect(x: 0, y: 20, width: pullToNextPageBottomView.width, height: 30)
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
            pullToNextPageBottomView.frame = CGRect(x: 0, y: webView.height - delta, width: scrollView.contentSize.width, height: pullToNextPageBottomViewHeight)
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
            if newsObject as? News != nil {
                let news = NewsDataManager.shared.informationItems[currentIndex+1]
                let detailViewController = NewsDetailViewController.init(nibName: "NewsDetailViewController", bundle: nil)
                detailViewController.currentIndex = currentIndex+1
                detailViewController.newsObject = news
                detailViewController.didCloseButtonClosure = {
                    self.dismiss(animated: false, completion: {
                        
                    })
                }
                self.present(detailViewController, animated: true, completion: {
                    
                })
            }
        }
    }

    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        if showProgressView {
            UIApplication.shared.isNetworkActivityIndicatorVisible = true
            
            progressView.alpha = 0.0
            UIView.animate(withDuration: 0.33, delay: 0.0, options: .curveEaseInOut, animations: {
                self.progressView.alpha = 1.0
            })
        }
    }

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        if newsObject as? News != nil {
            applyCss()
        } else {
            self.showWebView()
        }
        
        showProgressView = false
        UIApplication.shared.isNetworkActivityIndicatorVisible = false
        progressView.alpha = 1.0
        UIView.animate(withDuration: 0.33, delay: 0.0, options: .curveEaseInOut, animations: {
            self.progressView.alpha = 0.0
        })
    }
    
    func applyCss() {
        let bodyCssString = "body { white-space: pre-wrap; color: \(NewsDetailUIStyleConfig.shared.textColor); background-color: \(NewsDetailUIStyleConfig.shared.backgroundColor); font-family: \"\(NewsDetailUIStyleConfig.shared.fontFamily)\"; font-size: 100%; padding-left: 40px; padding-right: 40px; }"
        let imageCssString = "img { width: 100%; padding-top: 0px; padding-bottom: 0px; border-radius: \(NewsDetailUIStyleConfig.shared.imageCornerRadius)px;}"
        let cssString = "\(bodyCssString) \(imageCssString)"
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

// MARK: Transition delegate methods
extension NewsDetailViewController: UIViewControllerTransitioningDelegate {
    
    public func animationController(forPresented presented: UIViewController, presenting: UIViewController, source: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        // animator.dismissing = false
        return userCardPresentAnimationController
    }
    
    public func animationController(forDismissed dismissed: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        // animator.dismissing = true
        // return animator
        return userCardDismissAnimationController
        
    }
    
    // TODO: interaction gesture doesn't work in iOS 12. Have to disable it
    // https://stackoverflow.com/questions/26680311/interactive-delegate-methods-never-called
    // Fix it later
    /*
    func interactionControllerForDismissal(using animator: UIViewControllerAnimatedTransitioning) -> UIViewControllerInteractiveTransitioning? {
        return self.animator.percentageDriven ? self.animator : nil
    }
    */
}
