//
//  MnemonicBackupModeCollectionViewController.swift
//  loopr-ios
//
//  Created by xiaoruby on 5/30/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

protocol MnemonicBackupModeCollectionViewControllerDelegate: class {
    func collectionViewDidSelectItemAt(indexPath: IndexPath)
}

class MnemonicBackupModeCollectionViewController: UICollectionViewController {
    
    weak var delegate: MnemonicBackupModeCollectionViewControllerDelegate?
    
    let count: Int = 12  // 12 mnemonic words
    var index = 0
    var mnemonics: [String] = []
    var userSelections: [Int] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        let nib = UINib(nibName: MnemonicBackupModeCollectionViewCell.getCellIdentifier(), bundle: nil)
        collectionView?.register(nib, forCellWithReuseIdentifier: MnemonicBackupModeCollectionViewCell.getCellIdentifier())
        
        self.collectionView?.backgroundColor = UIColor.clear
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        userSelections = []
    }

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of items
        if mnemonics.count > 0 {
            return count
        } else {
            return 0
        }
    }
    
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: MnemonicBackupModeCollectionViewCell.getCellIdentifier(), for: indexPath) as! MnemonicBackupModeCollectionViewCell
        cell.mnemonicLabel.text = mnemonics[indexPath.row]
        
        // To avoid duplicated words in mnemonic
        if userSelections.contains(indexPath.row) {
            cell.applyGradient(withColors: UIColor.secondary, gradientOrientation: .horizontal)
        } else {
            for layer in cell.layer.sublayers ?? [] where layer.name == "gradientLayer" {
                layer.removeFromSuperlayer()
            }
            cell.backgroundColor = Themes.isDark() ? UIColor.dark3 : UIColor.white
        }
        return cell
    }
    
    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        delegate?.collectionViewDidSelectItemAt(indexPath: indexPath)
    }

}
