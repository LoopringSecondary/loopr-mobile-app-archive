//
//  NewsViewController_v3.swift
//  loopr-ios
//
//  Created by Ruby on 1/11/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit
import Social
import SVProgressHUD

protocol NewsViewControllerDelegate: class {
    func newsViewControllerSetNavigationBarHidden(_ newValue: Bool, animated: Bool)
    func setNavigationBarTitle(_ newValue: String)
}

class NewsViewController: UIViewController, UICollectionViewDelegateFlowLayout {

    weak var newsViewControllerDelegate: NewsViewControllerDelegate?
    
    @IBOutlet weak var headerView: UIImageView!
    @IBOutlet weak var categoryLabel: UILabel!
    @IBOutlet weak var leftFakeView: UIView!
    @IBOutlet weak var rightFakeView: UIView!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var bottomSeperateLine: UIView!
    
    var currentIndex: Int = 0
    var newsParamsList: [NewsParams] = [
        NewsParams(token: "ALL_CURRENCY", category: .flash),
        NewsParams(token: "ALL_CURRENCY", category: .information)
    ]
    
    let refreshControl = UIRefreshControl()
    
    var pageIndex: UInt = 0
    
    static var expandedNewsUuids: Set<String> = []
    var expandedIndexPathes: Set<IndexPath> = []

    override func viewDidLoad() {
        super.viewDidLoad()

        view.theme_backgroundColor = ColorPicker.backgroundColor
        collectionView.theme_backgroundColor = ColorPicker.backgroundColor
        
        headerView.cornerRadius = 6
        headerView.image = UIImage(named: "wallet-selected-background" + ColorTheme.getTheme())
        headerView.contentMode = .scaleToFill
        
        categoryLabel.font = FontConfigManager.shared.getNewsTitleFont() // FontConfigManager.shared.getMediumFont(size: 20)
        categoryLabel.textColor = .white
        categoryLabel.text = newsParamsList[currentIndex].title
        
        leftFakeView.round(corners: [.topRight, .bottomRight], radius: 2)
        leftFakeView.backgroundColor = UIColor(rgba: "#fdbc4c")
        
        rightFakeView.round(corners: [.topLeft, .bottomLeft], radius: 2)
        rightFakeView.backgroundColor = UIColor(rgba: "#eec71c")
        
        if currentIndex == 0 {
            leftFakeView.isHidden = true
            rightFakeView.isHidden = false
        } else {
            leftFakeView.isHidden = false
            rightFakeView.isHidden = true
        }
        
        let nib = UINib(nibName: NewsCollectionCell.getCellIdentifier(), bundle: nil)
        collectionView.register(nib, forCellWithReuseIdentifier: NewsCollectionCell.getCellIdentifier())
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.showsVerticalScrollIndicator = true
        collectionView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)

        refreshControl.updateUIStyle(withTitle: RefreshControlDataManager.shared.get(type: .newsViewController))
        refreshControl.addTarget(self, action: #selector(refreshData), for: .valueChanged)
        collectionView.refreshControl = refreshControl

        bottomSeperateLine.theme_backgroundColor = ColorPicker.cardBackgroundColor
        bottomSeperateLine.round(corners: [.topLeft, .topRight, .bottomRight, .bottomLeft], radius: 2)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        collectionView.reloadData()
        // This line of code works
        // self.navigationController?.setNavigationBarHidden(true, animated: false)
        NotificationCenter.default.post(name: .willShowNewsViewController, object: nil)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        // self.navigationController?.setNavigationBarHidden(false, animated: false)
    }

    @objc func refreshData() {
        pageIndex = 0
        getNewsFromAPIServer()
        
        // TODO: need to simply this part
        newsParamsList = [
            NewsParams(token: "ALL_CURRENCY", category: .flash),
            NewsParams(token: "ALL_CURRENCY", category: .information)
        ]
        categoryLabel.text = newsParamsList[currentIndex].title
    }
    
    func getNewsFromAPIServer() {
        NewsDataManager.shared.get(category: newsParamsList[currentIndex].category, pageIndex: pageIndex, completion: { (_, _) in
            DispatchQueue.main.async {
                self.collectionView.reloadData()
                SVProgressHUD.dismiss()
                self.refreshControl.endRefreshing(refreshControlType: .newsViewController)
            }
        })
    }

}

extension NewsViewController: UICollectionViewDataSource, UICollectionViewDelegate {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if newsParamsList[currentIndex].category == .information {
            return NewsDataManager.shared.informationItems.count
        } else {
            return NewsDataManager.shared.flashItems.count
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        // revoke GarlandConfig.shared.cardsSize
        let news: News
        if newsParamsList[currentIndex].category == .information {
            news = NewsDataManager.shared.informationItems[indexPath.row]
        } else {
            news = NewsDataManager.shared.flashItems[indexPath.row]
        }
        return NewsCollectionCell.getSize(news: news, isExpanded: NewsViewController.expandedNewsUuids.contains(news.uuid))
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 8
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: NewsCollectionCell.getCellIdentifier(), for: indexPath) as? NewsCollectionCell else { return UICollectionViewCell() }
        
        let isLastCell = indexPath.row == collectionView.numberOfItems(inSection: 0) - 1
        
        let news: News
        if newsParamsList[currentIndex].category == .information {
            news = NewsDataManager.shared.informationItems[indexPath.row]
            if NewsDataManager.shared.informationHasMoreData && isLastCell {
                pageIndex += 1
                getNewsFromAPIServer()
            }
        } else {
            news = NewsDataManager.shared.flashItems[indexPath.row]
            if NewsDataManager.shared.flashHasMoreData && isLastCell {
                pageIndex += 1
                getNewsFromAPIServer()
            }
        }
        
        cell.updateUIStyle(news: news, isExpanded: NewsViewController.expandedNewsUuids.contains(news.uuid))
        cell.didClickedCollectionCellClosure = { (news) -> Void in
            let news: News
            if self.newsParamsList[self.currentIndex].category == .information {
                news = NewsDataManager.shared.informationItems[indexPath.row]
                let detailViewController = NewsDetailViewController()
                detailViewController.currentIndex = indexPath.row
                detailViewController.news = news
                detailViewController.hidesBottomBarWhenPushed = true
                detailViewController.newsDetailViewControllerDelegate = self
                detailViewController.previousViewController = self
                self.navigationController?.pushViewController(detailViewController, animated: true)
                
            } else {
                news = NewsDataManager.shared.flashItems[indexPath.row]
                if NewsViewController.expandedNewsUuids.contains(news.uuid) {
                    NewsViewController.expandedNewsUuids.remove(news.uuid)
                    self.expandedIndexPathes.remove(indexPath)
                } else {
                    NewsViewController.expandedNewsUuids.insert(news.uuid)
                    self.expandedIndexPathes.insert(indexPath)
                }
                collectionView.reloadItems(at: [indexPath])
            }
        }
        
        cell.didPressedShareButtonClosure = { (news) -> Void in
            if let url = URL(string: news.url) {
                // let image = UIImage(named: "Order-qrcode-icon-green")!
                let shareAll = [news.title, url] as [Any]
                let activityVC = UIActivityViewController(activityItems: shareAll, applicationActivities: nil)
                activityVC.popoverPresentationController?.sourceView = self.view
                self.present(activityVC, animated: true, completion: nil)
            }
        }
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {

    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        
    }
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
    }
}

extension NewsViewController: NewsDetailViewControllerDelegate {

    func setNavigationBarHidden(_ newValue: Bool, animated: Bool) {
        newsViewControllerDelegate?.newsViewControllerSetNavigationBarHidden(newValue, animated: animated)
    }

    func setNavigationBarTitle(_ newValue: String) {
        newsViewControllerDelegate?.setNavigationBarTitle(newValue)
    }

}
