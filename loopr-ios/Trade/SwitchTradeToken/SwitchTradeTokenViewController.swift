//
//  SwitchTradeTokenViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 3/14/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

enum SwitchTradeTokenType {
    case tokenS
    case tokenB
}

class SwitchTradeTokenViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UISearchBarDelegate {

    var needUpdateClosure: (() -> Void)?

    var tokens: [Token] = []
    var filteredTokens: [Token] = []

    var type: SwitchTradeTokenType = .tokenS
    @IBOutlet weak var tableView: UITableView!

    var searchText: String = ""
    var isSearching = false
    let searchBar = UISearchBar()
    var searchButton = UIBarButtonItem()

    override func viewDidLoad() {
        super.viewDidLoad()


        view.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.theme_backgroundColor = ColorPicker.backgroundColor

        setBackButton()
        setupSearchBar()

        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none

        getTokens()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        // self.navigationController?.isNavigationBarHidden = false
    }

    func setupSearchBar() {
        searchButton = UIBarButtonItem(barButtonSystemItem: .search, target: self, action: #selector(self.pressOrderSearchButton(_:)))
        self.navigationItem.rightBarButtonItems = [searchButton]

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

        self.navigationItem.title = LocalizedString("Tokens", comment: "")
    }

    // We have to write code like this to make it not too slow.
    func getTokens() {
        var tokens: [Token] = []
        if self.type == .tokenB {
            tokens = TokenDataManager.shared.getErcTokensExcept(for: [P2POrderDataManager.shared.baseToken.symbol])
        } else {
            tokens = TokenDataManager.shared.getErcTokensExcept(for: [P2POrderDataManager.shared.quoteToken.symbol])
        }

        var dict: [String: Double] = [:]
        let notZeroAssets = CurrentAppWalletDataManager.shared.getAssets(isNotZero: true)
        for notZeroAsset in notZeroAssets {
            dict[notZeroAsset.symbol] = notZeroAsset.balance
        }

        self.tokens = tokens.sorted { (a, b) -> Bool in
            let balanceA = dict[a.symbol] ?? 0.0
            let balanceB = dict[b.symbol] ?? 0.0
            if balanceA != balanceB {
                return balanceA > balanceB
            } else {
                return a.symbol < b.symbol
            }
        }
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
        self.navigationItem.rightBarButtonItems = [searchButton]
        searchBar.resignFirstResponder()
        searchBar.text = nil
        navigationItem.titleView = nil
        self.navigationItem.title = LocalizedString("Tokens", comment: "")
        searchTextDidUpdate(searchText: "")
        setBackButton()
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

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if isSearching {
            return filteredTokens.count
        } else {
            return self.tokens.count
        }
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return SwitchTradeTokenTableViewCell.getHeight()
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: SwitchTradeTokenTableViewCell.getCellIdentifier()) as? SwitchTradeTokenTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("SwitchTradeTokenTableViewCell", owner: self, options: nil)
            cell = nib![0] as? SwitchTradeTokenTableViewCell
        }

        let token: Token
        if isSearching {
            token = filteredTokens[indexPath.row]
        } else {
            token = self.tokens[indexPath.row]
        }
        cell?.token = token
        cell?.update()

        if (type == .tokenS && token.symbol == P2POrderDataManager.shared.baseToken.symbol) || (type == .tokenB && token.symbol == P2POrderDataManager.shared.quoteToken.symbol) {
            cell?.enabledIcon.isHidden = false
        } else {
            cell?.enabledIcon.isHidden = true
        }
        return cell!
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            tableView.deselectRow(at: indexPath, animated: true)
            let token: Token
            if self.isSearching {
                token = self.filteredTokens[indexPath.row]
            } else {
                token = self.tokens[indexPath.row]
            }
            switch self.type {
            case .tokenS:
                P2POrderDataManager.shared.changeTokenS(token)
            case .tokenB:
                P2POrderDataManager.shared.changeTokenB(token)
            }
            self.needUpdateClosure?()
            self.navigationController?.popViewController(animated: true)
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
        filteredTokens = self.tokens.filter({(token: Token) -> Bool in
            if token.symbol.range(of: searchText, options: .caseInsensitive) != nil {
                return true
            } else {
                return false
            }
        })
        tableView.reloadSections(IndexSet(integersIn: 0...0), with: .fade)
    }

}
