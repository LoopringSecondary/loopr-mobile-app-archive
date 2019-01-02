//
//  NewsViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit
import Social
import SVProgressHUD

class NewsViewController: GarlandViewController, UICollectionViewDelegateFlowLayout {

    var currentIndex: Int = 0
    let newsParamsList: [NewsParams] = [
        NewsParams(token: "ALL_CURRENCY", category: .flash),
        NewsParams(token: "ALL_CURRENCY", category: .information)
    ]

    private let header: NewsListHeaderView = UIView.loadFromNib(withName: "NewsListHeaderView")!
    
    fileprivate let scrollViewContentOffsetMargin: CGFloat = -150.0
    fileprivate var headerIsSmall: Bool = false

    @IBOutlet weak var fakeTradeButton: UIButton!

    let refreshControl = UIRefreshControl()

    var pageIndex: UInt = 0
    
    var expandedNewsUuid: String?
    var previousExpandedIndexPath: IndexPath?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .clear

        let nib = UINib(nibName: NewsCollectionCell.getCellIdentifier(), bundle: nil)
        garlandCollection.register(nib, forCellWithReuseIdentifier: NewsCollectionCell.getCellIdentifier())
        garlandCollection.delegate = self
        garlandCollection.dataSource = self
        garlandCollection.showsVerticalScrollIndicator = true

        refreshControl.updateUIStyle(withTitle: RefreshControlDataManager.shared.get(type: .newsViewController))
        refreshControl.addTarget(self, action: #selector(refreshData(_:)), for: .valueChanged)
        garlandCollection.refreshControl = refreshControl
        
        nextViewController = { _ in
            let vc = NewsViewController()
            if self.currentIndex == self.newsParamsList.count - 1 {
                vc.currentIndex = 0
            } else {
                vc.currentIndex = self.currentIndex + 1
            }
            return vc
        }
        setupHeader(header)
        header.titleLabel.text = newsParamsList[currentIndex].title
        
        let window = UIApplication.shared.keyWindow
        let bottomPadding = window?.safeAreaInsets.bottom ?? 0
        
        garlandCollection.frame = CGRect(x: 0, y: GarlandConfig.shared.headerVerticalOffset, width: view.bounds.width, height: view.bounds.height - GarlandConfig.shared.headerVerticalOffset - 49 - bottomPadding)
        garlandCollection.theme_backgroundColor = ColorPicker.backgroundColor
        
        if !FeatureConfigDataManager.shared.getShowTradingFeature() {
            fakeTradeButton.isHidden = true
        }

        if newsParamsList[currentIndex].category == .information {
            if NewsDataManager.shared.informationItems.count == 0 {
                SVProgressHUD.show(withStatus: LocalizedString("Loading Data", comment: ""))
                getNewsFromAPIServer()
            }
        } else {
            if NewsDataManager.shared.flashItems.count == 0 {
                SVProgressHUD.show(withStatus: LocalizedString("Loading Data", comment: ""))
                getNewsFromAPIServer()
            }
        }
    }
    
    @objc private func refreshData(_ sender: Any) {
        pageIndex = 0
        getNewsFromAPIServer()
    }
    
    func getNewsFromAPIServer() {
        NewsDataManager.shared.get(category: newsParamsList[currentIndex].category, pageIndex: pageIndex, completion: { (_, _) in
            DispatchQueue.main.async {
                self.garlandCollection.reloadData()
                SVProgressHUD.dismiss()
                self.refreshControl.endRefreshing(refreshControlType: .newsViewController)
            }
        })
    }
    
    @IBAction func clickedFakeButtonWallet(_ sender: Any) {
        NotificationCenter.default.post(name: .switchToWalletViewController, object: nil, userInfo: nil)
    }
    
    @IBAction func clickedFakeButtonTrade(_ sender: Any) {
        NotificationCenter.default.post(name: .switchToTradeViewController, object: nil, userInfo: nil)
    }
    
    @IBAction func clickedFakeButtonNews(_ sender: Any) {
        
    }

    @IBAction func clickedFakeButtonSettings(_ sender: Any) {
        NotificationCenter.default.post(name: .switchToSettingViewController, object: nil, userInfo: nil)
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
        return NewsCollectionCell.getSize(news: news, isExpanded: news.uuid == expandedNewsUuid ?? "")
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
        
        cell.updateUIStyle(news: news, isExpanded: news.uuid == expandedNewsUuid ?? "")
        cell.didClickedCollectionCellClosure = { (news) -> Void in
            let news: News
            if self.newsParamsList[self.currentIndex].category == .information {
                news = NewsDataManager.shared.informationItems[indexPath.row]
                self.selectedCardIndex = indexPath
                let detailViewController = NewsDetailViewController.init(nibName: "NewsDetailViewController", bundle: nil)
                detailViewController.news = news
                self.present(detailViewController, animated: true, completion: {
                    
                })
            } else {
                news = NewsDataManager.shared.flashItems[indexPath.row]
                self.expandedNewsUuid = news.uuid
                if self.previousExpandedIndexPath != nil && self.previousExpandedIndexPath! != indexPath {
                    collectionView.reloadItems(at: [indexPath, self.previousExpandedIndexPath!])
                } else {
                    collectionView.reloadItems(at: [indexPath])
                }
                self.previousExpandedIndexPath = indexPath
            }
        }
        
        cell.didPressedShareButtonClosure = { (news) -> Void in
            if let url = URL(string: news.url) {
                let text = url.absoluteString
                let shareAll = [text] as [Any]
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
        /*
        let news = NewsDataManager.shared.informationItems[indexPath.row]
        if let url = URL(string: news.url) {
            let config = SFSafariViewController.Configuration()
            config.entersReaderIfAvailable = true
            
            let vc = SFSafariViewController(url: url, configuration: config)
            present(vc, animated: true)
        }
        */
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let startOffset = (garlandCollection.contentOffset.y + GarlandConfig.shared.cardsSpacing + GarlandConfig.shared.headerSize.height) / GarlandConfig.shared.headerSize.height
        let maxHeight: CGFloat = 1.0
        let minHeight: CGFloat = 0.9
        let _: CGFloat = 0.0
        
        let divided = startOffset / 3
        _ = startOffset / 1.5
        let height = max(minHeight, min(maxHeight, 1.0 - divided))
        // let alpha = max(minAlpha, min(maxHeight, 1.0 - offsetCounter * 2))
        // let collapsedViewSize = max(0, min(maxHeight, 1.0 - offsetCounter))
        header.frame.size.height = GarlandConfig.shared.headerSize.height * height
    }
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y > scrollViewContentOffsetMargin, !headerIsSmall {
            headerIsSmall = true
            scrollView.setContentOffset(CGPoint(x: scrollView.contentOffset.x, y: 0.0), animated: true)
        } else if scrollView.contentOffset.y < 0.0, headerIsSmall {
            headerIsSmall = false
            scrollView.setContentOffset(CGPoint(x: scrollView.contentOffset.x, y: -164.0), animated: true)
        }
    }
}
