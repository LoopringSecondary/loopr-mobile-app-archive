//
//  IconView.swift
//  loopr-ios
//
//  Created by xiaoruby on 3/25/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class IconView: UIView {
    
    var symbol: String = ""
    var symbolLabel: UILabel = UILabel()
    var symbolLabelFont: UIFont = FontConfigManager.shared.getBoldFont(size: 11)

    var circleView: UIView = UIView()
    var circleViewColor: UIColor = UIColor.clear
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        addSubview(circleView)
        addSubview(symbolLabel)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        addSubview(circleView)
        addSubview(symbolLabel)
    }
    
    override func draw(_ rect: CGRect) {
        backgroundColor = UIColor.clear
        let circleRadius = min(rect.width, rect.height) * 0.5

        // Drawing code
        circleView.frame = CGRect(origin: CGPoint(x: rect.minX, y: rect.minY), size: CGSize(width: circleRadius * 2, height: circleRadius * 2))
        circleView.backgroundColor = circleViewColor
        circleView.cornerRadius = circleRadius
        circleView.layer.borderColor = UIColor.text1.cgColor
        circleView.layer.borderWidth = 1
        circleView.clipsToBounds = true
        
        showTextInsdieView(circleRadius: circleRadius)
    }
    
    private func showTextInsdieView(circleRadius: CGFloat) {
        symbolLabel.frame = CGRect(x: 0, y: 0, width: circleRadius*2, height: circleRadius*2)
        symbolLabel.textAlignment = .center
        symbolLabel.theme_textColor = GlobalPicker.textColor
        symbolLabel.font = symbolLabelFont
        symbolLabel.text = symbol
    }
    
    public func setSymbol(_ symbol: String) {
        print(symbol)
        symbolLabel.text = symbol
        addSubview(symbolLabel)
    }
}
