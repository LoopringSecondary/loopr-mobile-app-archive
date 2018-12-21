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

    let keys = ["#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "V", "X", "Y", "Z"]
    var dict = [String: [Contact]]()
    
    var isCellSelectEnable = true
    weak var delegate: ContactTableViewControllerDelegate?
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.tableView.sectionIndexColor = UIColor.text1
        self.tableView.reloadData()
        
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
        dict.removeAll()
        for contact in ContactDataManager.shared.getContactsFromLocal() {
            if let tmp = dict[contact.tag] {
                dict[contact.tag]!.append(contact)
            } else {
                dict[contact.tag] = [contact]
            }
        }
        tableView.reloadData()
    }
    
    @objc func pressAddButton(_ button: UIBarButtonItem) {
        let viewController = AddContactViewController()
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return keys.count
    }
    
    func sectionIndexTitles(for tableView: UITableView) -> [String]? {
        return keys
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return ContactTableViewCell.getHeight()
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        print(dict[keys[section]])
        return dict[keys[section]]?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: ContactTableViewCell.getCellIdentifier()) as? ContactTableViewCell
        if cell == nil {
            let nib = Bundle.main.loadNibNamed("ContactTableViewCell", owner: self, options: nil)
            cell = nib![0] as? ContactTableViewCell
        }
        let contact = dict[keys[indexPath.section]]![indexPath.row]
        cell?.update(contact: contact, isCellSelectEnable: isCellSelectEnable)
        
        if indexPath.row == tableView.numberOfRows(inSection: 0) - 1 {
            cell?.bottomSeperateLine.isHidden = false
        } else {
            cell?.bottomSeperateLine.isHidden = true
        }
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let editAction = UITableViewRowAction(style: .normal, title: LocalizedString("Edit", comment: "")) { (rowAction, indexPath) in
            let contact = self.dict[self.keys[indexPath.section]]![indexPath.row]
            let viewController = AddContactViewController()
            viewController.isCreatingContact = false
            viewController.address = contact.address
            viewController.name = contact.name
            viewController.note = contact.note
            self.navigationController?.pushViewController(viewController, animated: true)
        }
        editAction.backgroundColor = UIColor.theme
        
        let deleteAction = UITableViewRowAction(style: .normal, title: LocalizedString("Delete", comment: "")) { (rowAction, indexPath) in
            let contact = self.dict[self.keys[indexPath.section]]![indexPath.row]
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
            let contact = self.dict[self.keys[indexPath.section]]![indexPath.row]
            self.delegate?.didSelectedContact(contact: contact)
            self.navigationController?.popViewController(animated: true)
        }
    }

}
