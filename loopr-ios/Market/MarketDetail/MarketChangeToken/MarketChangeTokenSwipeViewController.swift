//
//  MarketChangeTokenSwipeViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 8/17/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class MarketChangeTokenSwipeViewController: SwipeViewController, UISearchBarDelegate {

    private var type: MarketSwipeViewType = .favorite
    private var types: [MarketSwipeViewType] = []
    private var viewControllers: [MarketChangeTokenViewController] = []
    
    var options = SwipeViewOptions.getDefault()
    
    var searchText = ""
    var isSearching = false
    let searchBar = UISearchBar()
    var searchButton = UIBarButtonItem()
    
    var didSelectRowClosure: ((Market) -> Void)?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        view.theme_backgroundColor = ColorPicker.backgroundColor
        
        setupSearchBar()
        setupCloseButton()
        self.navigationItem.title = LocalizedString("Markets", comment: "")
        
        setupChildViewControllers()
    }
    
    func setupCloseButton() {
        let backButton = UIButton(frame: .zero)
        
        backButton.setImage(UIImage.init(named: "Close-dark"), for: .normal)
        
        // Default left padding is 20. It should be 12 in our design.
        backButton.imageEdgeInsets = UIEdgeInsets.init(top: 0, left: -16, bottom: 0, right: 8)
        backButton.addTarget(self, action: #selector(pressedCloseButton(_:)), for: UIControlEvents.touchUpInside)
        // The size of the image.
        backButton.frame = CGRect(x: 0, y: 0, width: 40, height: 40)
        let backBarButton = UIBarButtonItem(customView: backButton)
        self.navigationItem.leftBarButtonItem = backBarButton
    }
    
    @objc func pressedCloseButton(_ button: UIBarButtonItem) {
        self.dismiss(animated: true) {
            
        }
    }
    
    func setupChildViewControllers() {
        types = [.favorite, .ETH, .LRC, .TUSD, .USDT]
        
        let vc0 = MarketChangeTokenViewController(type: .favorite)
        vc0.didSelectRowClosure = { (market) -> Void in
            self.didSelectRowClosure?(market)
        }
        vc0.didSelectBlankClosure = {
            self.searchBar.resignFirstResponder()
        }
        let vc1 = MarketChangeTokenViewController(type: .ETH)
        vc1.didSelectRowClosure = { (market) -> Void in
            self.didSelectRowClosure?(market)
        }
        vc1.didSelectBlankClosure = {
            self.searchBar.resignFirstResponder()
        }
        let vc2 = MarketChangeTokenViewController(type: .LRC)
        vc2.didSelectRowClosure = { (market) -> Void in
            self.didSelectRowClosure?(market)
        }
        vc2.didSelectBlankClosure = {
            self.searchBar.resignFirstResponder()
        }
        let vc3 = MarketChangeTokenViewController(type: .TUSD)
        vc3.didSelectRowClosure = { (market) -> Void in
            
        }
        vc3.didSelectBlankClosure = {
            self.searchBar.resignFirstResponder()
        }
        let vc4 = MarketChangeTokenViewController(type: .USDT)
        vc4.didSelectRowClosure = { (market) -> Void in
            
        }
        vc4.didSelectBlankClosure = {
            self.searchBar.resignFirstResponder()
        }
        viewControllers = [vc0, vc1, vc2, vc3, vc4]
        for viewController in viewControllers {
            self.addChildViewController(viewController)
        }
        
        if Themes.isDark() {
            options.swipeTabView.itemView.textColor = UIColor(rgba: "#ffffff66")
            options.swipeTabView.itemView.selectedTextColor = UIColor(rgba: "#ffffffcc")
        } else {
            options.swipeTabView.itemView.textColor = UIColor(rgba: "#00000099")
            options.swipeTabView.itemView.selectedTextColor = UIColor(rgba: "#000000cc")
        }
        swipeView.reloadData(options: options)
        
        if MarketDataManager.shared.getMarketsWithoutReordered(type: .favorite).count == 0 {
            swipeView.jump(to: 1, animated: false)
            viewControllers[1].viewAppear = true
        } else {
            viewControllers[0].viewAppear = true
        }
    }
        
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if isSearching {
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .cancel, target: self, action: #selector(self.pressSearchCancel))
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // viewControllers[self.swipeView.currentIndex].searchTextDidUpdate(searchText: "")
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
    }
    
    func setupSearchBar() {
        searchButton = UIBarButtonItem(barButtonSystemItem: .search, target: self, action: #selector(self.pressOrderSearchButton(_:)))
        
        searchBar.showsCancelButton = false
        searchBar.placeholder = LocalizedString("Search", comment: "")
        searchBar.delegate = self
        searchBar.searchBarStyle = .default
        searchBar.keyboardType = .alphabet
        searchBar.autocapitalizationType = .allCharacters
        searchBar.keyboardAppearance = Themes.isDark() ? .dark : .default
        searchBar.theme_tintColor = GlobalPicker.textColor
        searchBar.textColor = Themes.isDark() ? UIColor.init(rgba: "#ffffffcc") : UIColor.init(rgba: "#000000cc")
        searchBar.setTextFieldColor(color: UIColor.dark3)
        
        self.navigationItem.setRightBarButton(searchButton, animated: true)
    }
    
    @objc func pressOrderSearchButton(_ button: UIBarButtonItem) {
        let cancelBarButton = UIBarButtonItem(barButtonSystemItem: .cancel, target: self, action: #selector(self.pressSearchCancel))
        self.navigationItem.rightBarButtonItems = [cancelBarButton]
        self.navigationItem.leftBarButtonItem = nil
        self.navigationItem.hidesBackButton = true
        
        let searchBarContainer = SearchBarContainerView(customSearchBar: searchBar)
        searchBarContainer.frame = CGRect(x: 0, y: 0, width: view.frame.width, height: 44)
        self.navigationItem.titleView = searchBarContainer
        
        searchBar.becomeFirstResponder()
        viewControllers[self.swipeView.currentIndex].searchTextDidUpdate(searchText: "")
    }
    
    @objc func pressSearchCancel(_ button: UIBarButtonItem) {
        print("pressSearchCancel")
        self.navigationItem.rightBarButtonItems = [searchButton]
        searchBar.resignFirstResponder()
        searchBar.text = nil
        navigationItem.titleView = nil
        self.navigationItem.title = LocalizedString("Markets", comment: "")
        isSearching = false
        viewControllers[self.swipeView.currentIndex].searchTextDidUpdate(searchText: "")
        setBackButton()
    }
    
    // MARK: - Delegate
    override func swipeView(_ swipeView: SwipeView, viewWillSetupAt currentIndex: Int) {
        // print("will setup SwipeView")
    }
    
    override func swipeView(_ swipeView: SwipeView, viewDidSetupAt currentIndex: Int) {
        // print("did setup SwipeView")
    }
    
    override func swipeView(_ swipeView: SwipeView, willChangeIndexFrom fromIndex: Int, to toIndex: Int) {
        // print("will change from item \(fromIndex) to item \(toIndex)")
        var isFiltering: Bool = false
        let searchText = searchBar.text ?? ""
        if searchText.trim() != "" {
            isFiltering = true
        }
        type = types[toIndex]
        let viewController = viewControllers[toIndex]
        viewController.reloadAfterSwipeViewUpdated(isSearching: isFiltering, searchText: searchText)
    }
    
    override func swipeView(_ swipeView: SwipeView, didChangeIndexFrom fromIndex: Int, to toIndex: Int) {
        // print("did change from item \(fromIndex) to section \(toIndex)")
        viewControllers[fromIndex].viewAppear = false
        viewControllers[toIndex].viewAppear = true
    }
    
    // MARK: - DataSource
    override func numberOfPages(in swipeView: SwipeView) -> Int {
        return viewControllers.count
    }
    
    override func swipeView(_ swipeView: SwipeView, titleForPageAt index: Int) -> String {
        return types[index].description
    }
    
    override func swipeView(_ swipeView: SwipeView, viewControllerForPageAt index: Int) -> UIViewController {
        return viewControllers[index]
    }
    
    // MARK: - SearchBar Delegate
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        print("searchBar textDidChange \(searchText) \(self.swipeView.currentIndex)")
        viewControllers[self.swipeView.currentIndex].searchTextDidUpdate(searchText: searchText)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        print("searchBarSearchButtonClicked")
    }
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        print("searchBarTextDidBeginEditing")
        isSearching = true
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .cancel, target: self, action: #selector(self.pressSearchCancel))
        searchBar.becomeFirstResponder()
        
        // No need to reload nor call searchTextDidUpdate
        viewControllers[self.swipeView.currentIndex].isSearching = true
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        print("searchBarTextDidEndEditing")
    }

}
