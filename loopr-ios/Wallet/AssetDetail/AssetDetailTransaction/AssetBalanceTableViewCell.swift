//
//  AssetBalanceTableViewCell.swift
//  loopr-ios
//
//  Created by ruby on 1/26/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import UIKit

protocol AssetBalanceTableViewCellDelegate: class {
    func touchesBegan()
    func touchesEnd()
}

class AssetBalanceTableViewCell: UITableViewCell {
    
    weak var delegate: AssetBalanceTableViewCellDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        selectionStyle = .none
        backgroundColor = .clear
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        print("touchesBegan")
        delegate?.touchesBegan()
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesEnded(touches, with: event)
        print("touchesEnded")
        delegate?.touchesEnd()
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesEnded(touches, with: event)
        print("touchesMoved")
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesCancelled(touches, with: event)
        print("touchesCancelled")
    }
    
    class func getCellIdentifier() -> String {
        return "AssetBalanceTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 134
    }

}
