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

    @IBOutlet weak var tableView: UITableView!
    
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

        tableView.delegate = self
        tableView.dataSource = self
        tableView.separatorStyle = .none
        tableView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        let footerView = UIView(frame: CGRect(x: 0, y: 0, width: 200, height: 40))
        footerView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        tableView.tableFooterView = footerView

        NotificationCenter.default.addObserver(self, selector: #selector(tiggerPopNewsDetailViewControllerReceivedNotification), name: .tiggerPopNewsDetailViewController, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(adjustFontInNewsDetailViewControllerReceivedNotification), name: .adjustFontInNewsDetailViewController, object: nil)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        UINavigationBar.appearance().theme_barTintColor = ColorPicker.barTintColor
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        NotificationCenter.default.post(name: .hideBottomTabBarDetailViewController, object: nil)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        NotificationCenter.default.post(name: .pushedNewsDetailViewController, object: nil)
        self.newsDetailViewControllerDelegate?.setNavigationBarHidden(false, animated: false)
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
    
    @objc func adjustFontInNewsDetailViewControllerReceivedNotification() {
        print("adjustFontInNewsDetailViewControllerReceivedNotification")
        self.tableView.reloadData()
    }
    
    @objc private func refreshData(_ sender: Any) {
        
    }

    func setupRefreshControlAtBottom() {
        pullToNextPageBottomView.frame = CGRect(x: 0, y: tableView.frame.maxY, width: view.width, height: pullToNextPageBottomViewHeight)
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
        
        guard currentIndex != NewsDataManager.shared.informationItems.count - 1 else {
            pullToNextPageBottomView.isHidden = true
            return
        }
        
        let bottomY = tableView.contentSize.height - tableView.height

        print("the bottom of scrollView: \(bottomY)")
        if scrollView.contentOffset.y >= bottomY {
            let delta = scrollView.contentOffset.y  - bottomY + 45
            print("delta: \(delta)")
            pullToNextPageBottomView.isHidden = false
            tableView.bringSubview(toFront: pullToNextPageImageView)
            print("tableView.height - delta: \(view.height - delta)")
            pullToNextPageBottomView.frame = CGRect(x: 0, y: tableView.frame.maxY - delta, width: view.width, height: pullToNextPageBottomViewHeight)
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
            detailViewController.newsDetailViewControllerDelegate = previousViewController
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
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 1 {
            return 30
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        if section == 1 {
            return 40
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.width, height: 30))
        return headerView
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let footerView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.width, height: 40))
        return footerView
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 {
            return news.paragraphs.count + 2
        } else if section == 1 {
            if currentIndex != NewsDataManager.shared.informationItems.count - 1 {
                return 1
            }
        }
        return 0
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0 {
            if indexPath.row == 0 {
                return NewsDetailTitleTableViewCell.getHeight(content: news.title)
            } else if indexPath.row == 1 {
                return NewsDetailSubtitleTableViewCell.getHeight()
            } else if indexPath.row-2 < news.paragraphs.count {
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
        } else if indexPath.section == 1 {
            let nextNews = NewsDataManager.shared.informationItems[currentIndex+1]
            return NewsDetailTitleTableViewCell.getHeight(content: nextNews.title)
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            if indexPath.row == 0 {
                var cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailTitleTableViewCell.getCellIdentifier()) as? NewsDetailTitleTableViewCell
                if cell == nil {
                    let nib = Bundle.main.loadNibNamed("NewsDetailTitleTableViewCell", owner: self, options: nil)
                    cell = nib![0] as? NewsDetailTitleTableViewCell
                }
                cell?.isGradientLayerHidden = true
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
            } else if indexPath.row-2 < news.paragraphs.count {
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
                    if newsParagraph.newsImage!.isLoading == true {
                        newsParagraph.newsImage?.downloadImage { (image) in
                            DispatchQueue.main.async {
                                cell?.backgroundImageView.image = image
                                self.tableView.reloadRows(at: [indexPath], with: .automatic)
                            }
                        }
                    }
                    
                    return cell!
                }
            }
        } else if indexPath.section == 1 {
            let nextNews = NewsDataManager.shared.informationItems[currentIndex+1]
            var cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailTitleTableViewCell.getCellIdentifier()) as? NewsDetailTitleTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("NewsDetailTitleTableViewCell", owner: self, options: nil)
                cell = nib![0] as? NewsDetailTitleTableViewCell
            }
            cell?.isGradientLayerHidden = false
            cell?.update(content: nextNews.title)
            return cell!
        }
        return UITableViewCell()
    }

}
