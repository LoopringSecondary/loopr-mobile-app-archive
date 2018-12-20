//
//  ContactTableViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/16/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class ContactTableViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.

        self.setBackButton()
        self.navigationItem.title = LocalizedString("Contacts", comment: "")
        view.theme_backgroundColor = ColorPicker.backgroundColor
        
        tableView.dataSource = self
        tableView.delegate = self
        
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: 200, height: 10))
        paddingView.theme_backgroundColor = ColorPicker.backgroundColor
        tableView.tableHeaderView = paddingView
        tableView.tableFooterView = paddingView
        tableView.separatorStyle = .none
        
        tableView.theme_backgroundColor = ColorPicker.backgroundColor
        
        self.navigationItem.rightBarButtonItem = UIBarButtonItem.init(barButtonSystemItem: .add, target: self, action: #selector(self.pressAddButton(_:)))
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        // Load data
        _ = ContactDataManager.shared.getContactsFromLocal()
        tableView.reloadData()
    }
    
    @objc func pressAddButton(_ button: UIBarButtonItem) {
        let viewController = AddContactViewController()
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return ContactTableViewCell.getHeight()
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return ContactDataManager.shared.contacts.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: ContactTableViewCell.getCellIdentifier()) as? ContactTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("ContactTableViewCell", owner: self, options: nil)
            cell = nib![0] as? ContactTableViewCell
        }
        let contact = ContactDataManager.shared.contacts[indexPath.row]
        cell?.update(contact: contact)
        
        if indexPath.row == tableView.numberOfRows(inSection: 0) - 1 {
            cell?.bottomSeperateLine.isHidden = false
        } else {
            cell?.bottomSeperateLine.isHidden = true
        }
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // TODO: add delegate to update the text field
        self.navigationController?.popViewController(animated: true)
    }
}
