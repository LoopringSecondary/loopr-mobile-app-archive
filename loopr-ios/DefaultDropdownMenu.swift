//
//  File.swift
//  loopr-ios
//
//  Created by ruby on 1/26/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation
import MKDropdownMenu

class DefaultDropdownMenu: MKDropdownMenu {

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        print("touchesBegan.....")
    }

    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        print("touches Moved")
    }
}
