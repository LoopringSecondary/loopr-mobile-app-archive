//
//  AddTokenViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 3/18/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class AddTokenViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UISearchBarDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    
    var searchText = ""
    var isSearching = false
    let searchBar = UISearchBar()
    var searchButton = UIBarButtonItem()
    var filtedTokens: [Token] = []
    var canHideKeyboard = true
    
    var addTokenButton = UIBarButtonItem()
    
    var numberOfTokens: Int = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        setBackButton()
        self.navigationItem.title = LocalizedString("Add Token", comment: "")
        
        view.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        
        let headerView = UIView(frame: CGRect(x: 0, y: 0, width: 320, height: 10))
        headerView.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.tableHeaderView = headerView

        searchButton = UIBarButtonItem(barButtonSystemItem: .search, target: self, action: #selector(self.pressOrderSearchButton(_:)))
        addTokenButton = UIBarButtonItem(barButtonSystemItem: .add, target: self, action: #selector(self.pressAddButton(_:)))
        self.navigationItem.rightBarButtonItems = [addTokenButton, searchButton]

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
        
        numberOfTokens = TokenDataManager.shared.getTokensToAdd().count
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        tableView.reloadData()
        let currentNumberOfTokens = TokenDataManager.shared.getTokensToAdd().count
        if numberOfTokens != currentNumberOfTokens {
            let indexPath = IndexPath(row: currentNumberOfTokens-1, section: 0)
            self.tableView.scrollToRow(at: indexPath, at: .bottom, animated: true)
        }
    }

    @objc func pressAddButton(_ button: UIBarButtonItem) {
        let viewController = AddCustomizedTokenViewController()
        self.navigationController?.pushViewController(viewController, animated: true)
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
    }
    
    @objc func pressSearchCancel(_ button: UIBarButtonItem) {
        print("pressSearchCancel")
        self.navigationItem.rightBarButtonItems = [addTokenButton, searchButton]
        searchBar.resignFirstResponder()
        searchBar.text = nil
        navigationItem.titleView = nil
        self.navigationItem.title = LocalizedString("Tokens", comment: "")
        searchTextDidUpdate(searchText: "")
        setBackButton()
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        print("scrollViewDidScroll")
        // isSearching = false
        if canHideKeyboard {
            searchBar.resignFirstResponder()
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if isSearching {
            return filtedTokens.count == 0 ? 1 : filtedTokens.count
        } else {
            return TokenDataManager.shared.getTokensToAdd().count
        }
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if isSearching && filtedTokens.count == 0 {
            return SearchTokenNoDataTableViewCell.getHeight()
        } else {
            return AddTokenTableViewCell.getHeight()
        }
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if isSearching && filtedTokens.count == 0 {
            var cell = tableView.dequeueReusableCell(withIdentifier: SearchTokenNoDataTableViewCell.getCellIdentifier()) as? SearchTokenNoDataTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("SearchTokenNoDataTableViewCell", owner: self, options: nil)
                cell = nib![0] as? SearchTokenNoDataTableViewCell
                cell?.selectionStyle = .none
            }
            cell?.pressedAddTokenButtonClosure = {
                self.searchBar.resignFirstResponder()
                let viewController = AddCustomizedTokenViewController()
                self.navigationController?.pushViewController(viewController, animated: true)
            }
            return cell!
        } else {
            var cell = tableView.dequeueReusableCell(withIdentifier: AddTokenTableViewCell.getCellIdentifier()) as? AddTokenTableViewCell
            if cell == nil {
                let nib = Bundle.main.loadNibNamed("AddTokenTableViewCell", owner: self, options: nil)
                cell = nib![0] as? AddTokenTableViewCell
                cell?.selectionStyle = .none
            }
            
            let token: Token
            if isSearching {
                token = filtedTokens[indexPath.row]
            } else {
                token = TokenDataManager.shared.getTokensToAdd()[indexPath.row]
            }
            cell?.token = token
            cell?.update()
            return cell!
        }
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func searchTextDidUpdate(searchText: String) {
        self.searchText = searchText.trim()
        if self.searchText != "" {
            isSearching = true
            filterContentForSearchText(self.searchText)
        } else {
            isSearching = false
            tableView.reloadSections(IndexSet(integersIn: 0...0), with: .fade)
        }
    }
    
    // MARK: - SearchBar Delegate
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        print("searchBar textDidChange \(searchText)")
        searchTextDidUpdate(searchText: searchText)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        print("searchBarSearchButtonClicked")
    }
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        print("searchBarTextDidBeginEditing")
        searchBar.becomeFirstResponder()
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        print("searchBarTextDidEndEditing")
    }

    func filterContentForSearchText(_ searchText: String) {
        let newFiltedTokens = TokenDataManager.shared.getTokensToAdd().filter({(token: Token) -> Bool in
            return token.symbol.lowercased().contains(searchText.lowercased())
        })
        // If filteredMarkets is the same for different searchText, no update tableView.
        if filtedTokens == newFiltedTokens && filtedTokens.count > 0 {
            return
        }
        filtedTokens = newFiltedTokens
        
        if tableView.contentOffset.y == 0 {
            tableView.reloadSections(IndexSet(integersIn: 0...0), with: .fade)
        } else {
            canHideKeyboard = false
            _ = Timer.scheduledTimer(withTimeInterval: 2, repeats: false) { _ in
                self.canHideKeyboard = true
            }

            tableView.reloadData()
            // tableView.setContentOffset(.zero, animated: false)
            let topIndex = IndexPath(row: 0, section: 0)
            tableView.scrollToRow(at: topIndex, at: .top, animated: true)
        }
    }

}
