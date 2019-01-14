//
//  NewsDetailViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/29/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import WebKit

class NewsDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UIScrollViewDelegate {

    var currentIndex: Int = 0
    var news: News!
    var isFirtTimeAppear: Bool = true

    // @IBOutlet weak var navigationBar: UINavigationBar!

    @IBOutlet weak var tableView: UITableView!
    
    var enablePullToNextPage: Bool = false
    var isPullToNextPageImageViewAnimating: Bool = false
    var isPullToNextPageImageViewUp: Bool = true
    let pullToNextPageBottomViewHeight: CGFloat = 120
    let pullToNextPageBottomView = UIView()
    let pullToNextPageTitleLabel = UILabel()
    let pullToNextPageImageView = UIImageView()

    override open func viewDidLoad() {
        super.viewDidLoad()
        
        NewsDataManager.shared.currentIndex = currentIndex

        view.theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        tableView.delegate = self
        tableView.dataSource = self

        tableView.separatorStyle = .none

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
        
        NotificationCenter.default.addObserver(self, selector: #selector(tiggerPopNewsDetailViewControllerReceivedNotification), name: .tiggerPopNewsDetailViewController, object: nil)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        UINavigationBar.appearance().theme_barTintColor = ColorPicker.barTintColor
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        // navigationBar.shadowImage = UIImage()

        setupRefreshControlAtBottom()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        NotificationCenter.default.post(name: .pushedNewsDetailViewController, object: nil)

        guard isFirtTimeAppear else {
            return
        }
        
        isFirtTimeAppear = false
    }
    
    @objc func tiggerPopNewsDetailViewControllerReceivedNotification() {
        print("tiggerPopNewsDetailViewControllerReceivedNotification")
        self.navigationController?.popViewController(animated: true)
    }



    func setupRefreshControlAtBottom() {
        /*
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
        */
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        print("scrollView y: \(scrollView.contentOffset.y)")
        /*
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
                    }) { (_) in
                        self.enablePullToNextPage = false
                        self.isPullToNextPageImageViewUp = true
                        self.isPullToNextPageImageViewAnimating = false
                    }
                }
            }
        } else {
            pullToNextPageBottomView.isHidden = true
        }
        */
    }
    
    func scrollViewWillBeginDecelerating(_ scrollView: UIScrollView) {
        if enablePullToNextPage {
            let news = NewsDataManager.shared.informationItems[currentIndex+1]
            let detailViewController = NewsDetailViewController.init(nibName: "NewsDetailViewController", bundle: nil)
            detailViewController.currentIndex = currentIndex+1
            detailViewController.news = news
            self.present(detailViewController, animated: true, completion: {
                self.navigationController?.popViewController(animated: false)
            })
        }
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return news.paragraphs.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let newsParagraph = news.paragraphs[indexPath.row]
        if newsParagraph.isString {
            return NewsDetailStringTableViewCell.getHeight(content: newsParagraph.content)
        } else {
            return NewsDetailImageTableViewCell.getHeight(image: newsParagraph.image!)
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let newsParagraph = news.paragraphs[indexPath.row]
        if newsParagraph.isString {
            var cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailStringTableViewCell.getCellIdentifier()) as? NewsDetailStringTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("NewsDetailStringTableViewCell", owner: self, options: nil)
                cell = nib![0] as? NewsDetailStringTableViewCell
            }
            cell?.update(content: newsParagraph.content)
            return cell!
        } else {
            var cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailImageTableViewCell.getCellIdentifier()) as? NewsDetailImageTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("NewsDetailImageTableViewCell", owner: self, options: nil)
                cell = nib![0] as? NewsDetailImageTableViewCell
            }
            cell?.backgroundImageView.image = newsParagraph.image
            return cell!
        }
    }
    
}
