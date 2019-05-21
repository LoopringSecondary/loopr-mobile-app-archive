//
//  Double+Extension.swift
//  loopr-ios
//
//  Created by xiaoruby on 5/11/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

extension Double {

    var currency: String {
        let currencyFormatter = NumberFormatter()
        let locale = SettingDataManager.shared.getCurrentCurrency().locale
        currencyFormatter.locale = Locale(identifier: locale)
        currencyFormatter.usesGroupingSeparator = true
        currencyFormatter.numberStyle = .currency
        return currencyFormatter.string(from: NSNumber(value: self)) ?? "$0.00"
    }
    
    var ints: Int {
        var num = Int(self)
        var count = 0
        while num > 0 {
            num /= 10
            count += 1
        }
        return count
    }
    
    func withCommas(_ minimumFractionDigits: Int = 4) -> String {
        let numberFormatter = NumberFormatter()
        numberFormatter.numberStyle = .decimal
        numberFormatter.roundingMode = .floor
        numberFormatter.minimumFractionDigits = minimumFractionDigits
        numberFormatter.maximumFractionDigits = minimumFractionDigits
        if let formattedNumber = numberFormatter.string(from: NSNumber(value: self)) {
            return formattedNumber
        } else {
            return self.description
        }        
    }

}
