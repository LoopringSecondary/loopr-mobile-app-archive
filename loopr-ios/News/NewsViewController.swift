//
//  NewsViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class NewsViewController: GarlandViewController, UICollectionViewDelegateFlowLayout {

    private let header: NewsListHeaderView = UIView.loadFromNib(withName: "NewsListHeaderView")!
    
    fileprivate let scrollViewContentOffsetMargin: CGFloat = -150.0
    fileprivate var headerIsSmall: Bool = false

    @IBOutlet weak var fakeTradeButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .clear

        let nib = UINib(nibName: NewsCollectionCell.getCellIdentifier(), bundle: nil)
        garlandCollection.register(nib, forCellWithReuseIdentifier: NewsCollectionCell.getCellIdentifier())
        garlandCollection.delegate = self
        garlandCollection.dataSource = self
        
        nextViewController = { _ in
            return NewsViewController()
        }
        setupHeader(header)
        
        let window = UIApplication.shared.keyWindow
        let bottomPadding = window?.safeAreaInsets.bottom ?? 0
        
        garlandCollection.frame = CGRect(x: 0, y: GarlandConfig.shared.headerVerticalOffset, width: view.bounds.width, height: view.bounds.height - GarlandConfig.shared.headerVerticalOffset - 49 - bottomPadding)
        garlandCollection.theme_backgroundColor = ColorPicker.backgroundColor
        
        if !FeatureConfigDataManager.shared.getShowTradingFeature() {
            fakeTradeButton.isHidden = true
        }
        
        NewsDataManager.shared.getInformation { (_, _) in
            DispatchQueue.main.async {
                self.garlandCollection.reloadData()
            }
        }
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
        return NewsDataManager.shared.informationItems.count
    }
    
    func collectionView(_ collectionView: UICollectionView,
                        layout collectionViewLayout: UICollectionViewLayout,
                        sizeForItemAt indexPath: IndexPath) -> CGSize {
        // revoke GarlandConfig.shared.cardsSize
        return NewsCollectionCell.getSize()
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: NewsCollectionCell.getCellIdentifier(), for: indexPath) as? NewsCollectionCell else { return UICollectionViewCell() }
        let news = NewsDataManager.shared.informationItems[indexPath.row]
        cell.updateUIStyle(news: news)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {

    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        self.selectedCardIndex = indexPath
        let cardController = UserCardViewController.init(nibName: "UserCardViewController", bundle: nil)
        present(cardController, animated: true, completion: nil)
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let startOffset = (garlandCollection.contentOffset.y + GarlandConfig.shared.cardsSpacing + GarlandConfig.shared.headerSize.height) / GarlandConfig.shared.headerSize.height
        let maxHeight: CGFloat = 1.0
        let minHeight: CGFloat = 0.7
        let minAlpha: CGFloat = 0.0
        
        let divided = startOffset / 3
        let offsetCounter = startOffset / 1.5
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
