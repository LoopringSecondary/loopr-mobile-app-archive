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

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var tableViewBottomLayoutConstraint: NSLayoutConstraint!

    var enablePullToNextPage: Bool = false
    var isPullToNextPageImageViewAnimating: Bool = false
    var isPullToNextPageImageViewUp: Bool = true
    let pullToNextPageBottomViewHeight: CGFloat = 40
    let pullToNextPageBottomView = UIView()
    let pullToNextPageTitleLabel = UILabel()
    let pullToNextPageImageView = UIImageView()

    override open func viewDidLoad() {
        super.viewDidLoad()
        setBackButton()

        NewsDataManager.shared.currentIndex = currentIndex

        view.theme_backgroundColor = ColorPicker.cardBackgroundColor
        tableView.theme_backgroundColor = ColorPicker.cardBackgroundColor

        tableView.delegate = self
        tableView.dataSource = self
        tableView.separatorStyle = .none
        tableView.backgroundColor = .green

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
    }
    
    @objc func tiggerPopNewsDetailViewControllerReceivedNotification() {
        print("tiggerPopNewsDetailViewControllerReceivedNotification")
        self.navigationController?.popViewController(animated: true)
    }

    func setupRefreshControlAtBottom() {
        pullToNextPageBottomView.frame = CGRect(x: 0, y: view.height, width: view.width, height: pullToNextPageBottomViewHeight)
        pullToNextPageBottomView.backgroundColor = .red
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
        
        let bottomY = tableView.contentSize.height - tableView.height
        print("test: \(scrollView.contentInset.bottom)")

        print("the bottom of scrollView: \(bottomY)")
        if scrollView.contentOffset.y > bottomY {
            let delta = scrollView.contentOffset.y  - bottomY
            print("delta: \(delta)")
            pullToNextPageBottomView.isHidden = false
            tableView.bringSubview(toFront: pullToNextPageImageView)
            print("tableView.height - delta: \(view.height - delta)")
            pullToNextPageBottomView.frame = CGRect(x: 0, y: view.height - delta, width: view.width, height: pullToNextPageBottomViewHeight)
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
        return news.paragraphs.count + 2
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 {
            return NewsDetailTitleTableViewCell.getHeight(content: news.title)
        } else if indexPath.row == 1 {
            return NewsDetailSubtitleTableViewCell.getHeight()
        } else {
            let newsParagraph = news.paragraphs[indexPath.row-2]
            if newsParagraph.isString {
                return NewsDetailStringTableViewCell.getHeight(content: newsParagraph.content)
            } else {
                return NewsDetailImageTableViewCell.getHeight(image: newsParagraph.image)
            }
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
                cell?.backgroundImageView.image = newsParagraph.image
                return cell!
            }
        }
    }
    
}
