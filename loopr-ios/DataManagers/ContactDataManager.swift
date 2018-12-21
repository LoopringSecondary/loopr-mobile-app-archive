//
//  ContactDataManager.swift
//  loopr-ios
//
//  Created by Ruby on 12/16/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class ContactDataManager {
    
    static let shared = ContactDataManager()
    
    var contacts: [Contact]

    private init() {
        contacts = []
    }
    
    // TODO: merge updateContact and addContact
    func updateContact(_ updatedContact: Contact) {
        let results = contacts.filter { $0.address.uppercased() == updatedContact.address.uppercased() }
        contacts.removeAll { (contact) -> Bool in
            return results.contains(contact)
        }
        contacts.append(updatedContact)

        let encodedData = NSKeyedArchiver.archivedData(withRootObject: contacts)
        UserDefaults.standard.set(encodedData, forKey: UserDefaultsKeys.userContacts.rawValue)
        
        // Post data to server.
        AppServiceUserManager.shared.updateUserConfigWithUserDefaults()
    }
    
    func addContact(_ newContact: Contact) {
        contacts.append(newContact)
        let encodedData = NSKeyedArchiver.archivedData(withRootObject: contacts)
        UserDefaults.standard.set(encodedData, forKey: UserDefaultsKeys.userContacts.rawValue)
        UserDefaults.standard.synchronize()
        
        // Post data to server.
        AppServiceUserManager.shared.updateUserConfigWithUserDefaults()
    }
    
    func deleteContact(_ dealatedContact: Contact) {
        let results = contacts.filter { $0.address.uppercased() == dealatedContact.address.uppercased() }
        contacts.removeAll { (contact) -> Bool in
            return results.contains(contact)
        }

        let encodedData = NSKeyedArchiver.archivedData(withRootObject: contacts)
        UserDefaults.standard.set(encodedData, forKey: UserDefaultsKeys.userContacts.rawValue)
        UserDefaults.standard.synchronize()

        // Post data to server.
        AppServiceUserManager.shared.updateUserConfigWithUserDefaults()
    }
    
    func setContacts(_ newContacts: [Contact]) {
        let encodedData = NSKeyedArchiver.archivedData(withRootObject: newContacts)
        UserDefaults.standard.set(encodedData, forKey: UserDefaultsKeys.userContacts.rawValue)
        contacts = newContacts
    }

    func getContactsFromLocal() -> [Contact] {
        let defaults = UserDefaults.standard
        if let decodedData = defaults.data(forKey: UserDefaultsKeys.userContacts.rawValue) {
            if let array = NSKeyedUnarchiver.unarchiveObject(with: decodedData) as? [Contact] {
                contacts = array
                return contacts
            }
        }
        contacts = []
        return contacts
    }
    
    // TODO
    func getContactsFromServer() -> [Contact] {
        return []
    }

    func toJson() -> JSON {
        let contacts = getContactsFromLocal()
        var contactsJson: [JSON] = []
        for contact in contacts {
            let contactJson = contact.toJson()
            contactsJson.append(contactJson)
        }
        return JSON(contactsJson)
    }
}
