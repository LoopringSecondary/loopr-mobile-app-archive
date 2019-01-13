//
//  UILabelExtension.swift
//  loopr-ios
//
//  Created by kenshin on 2018/6/1.
//  Copyright © 2018年 Loopring. All rights reserved.
//

import Foundation
import UIKit

extension UILabel {

    func setTitleDigitFont() {
        let font = FontConfigManager.shared.getDigitalFont()
        self.theme_textColor = ["#000000cc", "#ffffffcc"]
        self.font = font
    }
    
    func setSubTitleDigitFont() {
        let font = FontConfigManager.shared.getDigitalFont(size: 12)
        self.theme_textColor = ["#00000099", "#ffffff66"]
        self.font = font
    }
    
    func setTitleCharFont() {
        let font = FontConfigManager.shared.getCharactorFont(size: 14)
        self.theme_textColor = ["#000000cc", "#ffffffcc"]
        self.font = font
    }
    
    func setSubTitleCharFont() {
        let font = FontConfigManager.shared.getCharactorFont(size: 13)
        self.theme_textColor = ["#00000099", "#ffffff66"]
        self.font = font
    }
    
    func setMarket() {
        if let text = self.text {
            let range = (text as NSString).range(of: "-\\w*\\d*", options: .regularExpression)
            let attribute = NSMutableAttributedString.init(string: text)
            attribute.addAttributes([
                NSAttributedStringKey.font: FontConfigManager.shared.getRegularFont(size: 13),
                NSAttributedStringKey.foregroundColor: UIColor.text2], range: range)
            self.attributedText = attribute
        }
    }
    
    func calculateMaxLines() -> Int {
        let maxSize = CGSize(width: frame.size.width, height: CGFloat(Float.infinity))
        let charSize = font.lineHeight
        let text = (self.text ?? "") as NSString
        let textSize = text.boundingRect(with: maxSize, options: .usesLineFragmentOrigin, attributes: [.font: font], context: nil)
        let linesRoundedUp = Int(ceil(textSize.height/charSize))
        return linesRoundedUp
    }

}
