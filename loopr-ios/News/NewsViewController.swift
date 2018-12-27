//
//  NewsViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/26/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class NewsViewController: GarlandViewController {

    private let header: HeaderView = UIView.loadFromNib(withName: "HeaderView")!
    
    fileprivate let scrollViewContentOffsetMargin: CGFloat = -150.0
    fileprivate var headerIsSmall: Bool = false

    @IBOutlet weak var fakeTradeButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        let nib = UINib(nibName: "CollectionCell", bundle: nil)
        garlandCollection.register(nib, forCellWithReuseIdentifier: "CollectionCell")
        garlandCollection.delegate = self
        garlandCollection.dataSource = self
        
        nextViewController = { _ in
            return NewsViewController()
        }
        setupHeader(header)
        
        let window = UIApplication.shared.keyWindow
        let bottomPadding = window?.safeAreaInsets.bottom ?? 0
        
        garlandCollection.frame = CGRect(x: 0, y: GarlandConfig.shared.headerVerticalOffset, width: view.bounds.width, height: view.bounds.height - GarlandConfig.shared.headerVerticalOffset - 49 - bottomPadding)
        view.backgroundColor = .clear
        
        if !FeatureConfigDataManager.shared.getShowTradingFeature() {
            fakeTradeButton.isHidden = true
        }
    }
    
    func dismissChildViewControllers() {
        guard let vc = nextViewController?(.left) else { return }
        vc.dismiss(animated: false) {
            
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
        return 30
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "CollectionCell", for: indexPath) as? CollectionCell else { return UICollectionViewCell() }
        
        return cell
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
        let alpha = max(minAlpha, min(maxHeight, 1.0 - offsetCounter * 2))
        let collapsedViewSize = max(0, min(maxHeight, 1.0 - offsetCounter))
        header.collapsedView.transform = CGAffineTransform(scaleX: 1.0, y: collapsedViewSize)
        header.avatar.transform = header.collapsedView.transform
        header.collapsedView.alpha = alpha
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
