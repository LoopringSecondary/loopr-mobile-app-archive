//
//  NewsDetailViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/29/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import WebKit

protocol NewsDetailViewControllerDelegate: class {
    func setNavigationBarHidden(_ newValue: Bool, animated: Bool)
}

class NewsDetailViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UIScrollViewDelegate {

    weak var previousViewController: NewsViewController?
    weak var newsDetailViewControllerDelegate: NewsDetailViewControllerDelegate?
    
    var currentIndex: Int = 0
    var news: News!

    var tableView: UITableView!
    
    var enablePullToPreviousPage: Bool = false
    let refreshControl = UIRefreshControl()
    
    var enablePullToNextPage: Bool = false
    var isPullToNextPageImageViewAnimating: Bool = false
    var isPullToNextPageImageViewUp: Bool = true
    let pullToNextPageBottomViewHeight: CGFloat = 80
    let pullToNextPageBottomView = UIView()
    let pullToNextPageTitleLabel = UILabel()
    let pullToNextPageImageView = UIImageView()
    
    var presentedChildViewControllers: [NewsDetailViewController] = []

    override open func viewDidLoad() {
        super.viewDidLoad()
        setBackButton()

        NewsDataManager.shared.currentIndex = currentIndex

        view.theme_backgroundColor = ColorPicker.cardBackgroundColor

        let window = UIApplication.shared.keyWindow
        let topPadding = (window?.safeAreaInsets.top ?? 0) + 44
        let bottomPadding = (window?.safeAreaInsets.bottom ?? 0)

        tableView = UITableView(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height - topPadding - bottomPadding))
        view.addSubview(tableView)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.separatorStyle = .none
        tableView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        let footerView = UIView(frame: CGRect(x: 0, y: 0, width: 200, height: 0))
        footerView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        tableView.tableFooterView = footerView

        NotificationCenter.default.addObserver(self, selector: #selector(tiggerPopNewsDetailViewControllerReceivedNotification), name: .tiggerPopNewsDetailViewController, object: nil)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        UINavigationBar.appearance().theme_barTintColor = ColorPicker.barTintColor
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        NotificationCenter.default.post(name: .pushedNewsDetailViewController, object: nil)
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        setupRefreshControlAtBottom()
        
        if currentIndex > 0 {
            // refreshControl.updateUIStyle(withTitle: RefreshControlDataManager.shared.get(type: .marketViewController))
            refreshControl.theme_tintColor = GlobalPicker.textColor
            let lastPullRefreshString = LocalizedString("Pull to read previous", comment: "")
            let attributedString = NSMutableAttributedString(string: lastPullRefreshString)
            attributedString.addAttribute(NSAttributedStringKey.foregroundColor, value: UIColor(rgba: "#ffffffcc"), range: NSRange(location: 0, length: lastPullRefreshString.count))
            attributedString.addAttribute(NSAttributedStringKey.font, value: FontConfigManager.shared.getRegularFont(size: 12), range: NSRange(location: 0, length: lastPullRefreshString.count))
            refreshControl.attributedTitle = attributedString
            refreshControl.addTarget(self, action: #selector(refreshData(_:)), for: .valueChanged)
            tableView.refreshControl = refreshControl
        }
    }
    
    @objc func tiggerPopNewsDetailViewControllerReceivedNotification() {
        print("tiggerPopNewsDetailViewControllerReceivedNotification")
        self.navigationController?.popViewController(animated: true)
    }
    
    @objc private func refreshData(_ sender: Any) {
        
    }

    func setupRefreshControlAtBottom() {
        pullToNextPageBottomView.frame = CGRect(x: 0, y: view.height, width: view.width, height: pullToNextPageBottomViewHeight)
        view.addSubview(pullToNextPageBottomView)

        pullToNextPageTitleLabel.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 30)
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

        if scrollView.panGestureRecognizer.translation(in: scrollView).y < 0 {
            newsDetailViewControllerDelegate?.setNavigationBarHidden(true, animated: true)
        } else {
            newsDetailViewControllerDelegate?.setNavigationBarHidden(false, animated: true)
        }
        
        let bottomY = tableView.contentSize.height - tableView.height
        print("test: \(scrollView.contentInset.bottom)")

        print("the bottom of scrollView: \(bottomY)")
        if scrollView.contentOffset.y > bottomY {
            let delta = scrollView.contentOffset.y  - bottomY + 45
            print("delta: \(delta)")
            pullToNextPageBottomView.isHidden = false
            tableView.bringSubview(toFront: pullToNextPageImageView)
            print("tableView.height - delta: \(view.height - delta)")
            pullToNextPageBottomView.frame = CGRect(x: 0, y: view.height - delta, width: view.width, height: pullToNextPageBottomViewHeight)
            if delta > pullToNextPageBottomView.height + 20 {
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
        
        if scrollView.contentOffset.y < -50 && currentIndex > 0 {
            enablePullToPreviousPage = true
        } else {
            enablePullToPreviousPage = false
        }
    }

    func scrollViewWillBeginDecelerating(_ scrollView: UIScrollView) {
        if enablePullToNextPage {
            let news = NewsDataManager.shared.informationItems[currentIndex+1]
            let detailViewController = NewsDetailViewController.init(nibName: "NewsDetailViewController", bundle: nil)
            detailViewController.currentIndex = currentIndex+1
            detailViewController.news = news
            detailViewController.previousViewController = previousViewController
            presentedChildViewControllers.append(detailViewController)

            view.addSubview(detailViewController.view)
            detailViewController.view.frame = CGRect(x: 0, y: view.frame.height, width: view.frame.width, height: view.frame.height)
            
            UIView.animate(withDuration: 0.6, delay: 0, usingSpringWithDamping: 0.78, initialSpringVelocity: 0.3, options: .curveEaseInOut, animations: {
                self.tableView.frame = CGRect(x: 0, y: -self.tableView.frame.height, width: self.tableView.frame.width, height: self.tableView.height)
                detailViewController.view.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
                self.newsDetailViewControllerDelegate?.setNavigationBarHidden(false, animated: false)

            }) { (_) in
                self.addChildViewController(detailViewController)
            }
        } else if enablePullToPreviousPage {
            let news = NewsDataManager.shared.informationItems[currentIndex-1]
            let detailViewController = NewsDetailViewController.init(nibName: "NewsDetailViewController", bundle: nil)
            detailViewController.currentIndex = currentIndex-1
            detailViewController.news = news
            detailViewController.previousViewController = previousViewController
            presentedChildViewControllers.append(detailViewController)

            view.addSubview(detailViewController.view)
            detailViewController.view.frame = CGRect(x: 0, y: -view.frame.height, width: view.frame.width, height: view.frame.height)
            detailViewController.pullToNextPageBottomView.isHidden = true
            
            UIView.animate(withDuration: 0.6, delay: 0, usingSpringWithDamping: 0.78, initialSpringVelocity: 0.3, options: .curveEaseInOut, animations: {
                self.tableView.frame = CGRect(x: 0, y: self.tableView.frame.height, width: self.tableView.frame.width, height: self.tableView.height)
                detailViewController.view.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
                self.newsDetailViewControllerDelegate?.setNavigationBarHidden(false, animated: false)

            }) { (_) in
                self.addChildViewController(detailViewController)
                detailViewController.pullToNextPageBottomView.isHidden = false
                detailViewController.newsDetailViewControllerDelegate = self.previousViewController
            }
        }
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return news.paragraphs.count + 2
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 {
            return NewsDetailTitleTableViewCell.getHeight(content: news.title)
        } else if indexPath.row == 1 {
            return NewsDetailSubtitleTableViewCell.getHeight()
        } else {
            var height: CGFloat = 0
            let newsParagraph = news.paragraphs[indexPath.row-2]
            if newsParagraph.isString {
                height = NewsDetailStringTableViewCell.getHeight(content: newsParagraph.content)
            } else {
                height = NewsDetailImageTableViewCell.getHeight(image: newsParagraph.newsImage?.image)
            }
            if indexPath.row == news.paragraphs.count+2-1 {
                height += 30
            }
            return height
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            var cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailTitleTableViewCell.getCellIdentifier()) as? NewsDetailTitleTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("NewsDetailTitleTableViewCell", owner: self, options: nil)
                cell = nib![0] as? NewsDetailTitleTableViewCell
            }
            cell?.update(content: news.title)
            return cell!
        } else if indexPath.row == 1 {
            var cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailSubtitleTableViewCell.getCellIdentifier()) as? NewsDetailSubtitleTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("NewsDetailSubtitleTableViewCell", owner: self, options: nil)
                cell = nib![0] as? NewsDetailSubtitleTableViewCell
            }
            cell?.update(news: news)
            return cell!
        } else {
            let newsParagraph = news.paragraphs[indexPath.row-2]
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
                cell?.backgroundImageView.image = newsParagraph.newsImage?.image
                return cell!
            }
        }
    }

}
