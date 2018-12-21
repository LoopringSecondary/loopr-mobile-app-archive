//
//  ContactTableViewController.swift
//  loopr-ios
//
//  Created by Ruby on 12/16/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

protocol ContactTableViewControllerDelegate: class {
    func didSelectedContact(contact: Contact)
}

class ContactTableViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    var isCellSelectEnable = true
    weak var delegate: ContactTableViewControllerDelegate?
    
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
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let editAction = UITableViewRowAction(style: .normal, title: LocalizedString("Edit", comment: "")) { (rowAction, indexPath) in
            let contact = ContactDataManager.shared.contacts[indexPath.row]
            let viewController = AddContactViewController()
            viewController.isCreatingContact = false
            viewController.address = contact.address
            viewController.name = contact.name
            viewController.note = contact.note
            self.navigationController?.pushViewController(viewController, animated: true)
        }
        editAction.backgroundColor = UIColor.theme
        
        let deleteAction = UITableViewRowAction(style: .normal, title: LocalizedString("Delete", comment: "")) { (rowAction, indexPath) in
            let contact = ContactDataManager.shared.contacts[indexPath.row]
            ContactDataManager.shared.deleteContact(contact)
            self.tableView.reloadData()
        }
        deleteAction.backgroundColor = UIColor(named: "Color-red")!
        return [editAction, deleteAction]
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            guard self.isCellSelectEnable else {
                return
            }
            let contact = ContactDataManager.shared.contacts[indexPath.row]
            self.delegate?.didSelectedContact(contact: contact)
            self.navigationController?.popViewController(animated: true)
        }
    }

}
