//
//  GlobalPicker.swift
//  loopr-ios
//
//  Created by xiaoruby on 2/24/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import SwiftTheme

// TODO: We have to migrate to ColorPicker
enum GlobalPicker {

    // text colors
    private static let textColors = ["#000000cc", "#ffffffcc"]
    static let textColor = ThemeColorPicker.pickerWithColors(textColors)
    
    // cc -> 0.8
    private static let contrastTextColors = ["#ffffffcc", "#000000cc"]
    static let contrastTextColor = ThemeColorPicker.pickerWithColors(contrastTextColors)
    
    // 99 -> 0.6
    static let textLightColor: ThemeColorPicker = ["#00000099", "#ffffff66"]
    static let contrastTextLightColor: ThemeColorPicker = ["#ffffff66", "#00000099"]
    
    static let textDarkColor: ThemeColorPicker = ["#000000ee", "#ffffff66"]
    static let contrastTextDarkColor: ThemeColorPicker = ["#ffffff66", "#000000ee"]
    
    static let contrastTextExtremeLightColor: ThemeColorPicker = ["#ffffff66", "#00000066"]
  
    private static let barTextColors = ["#00000099", "#ffffffcc"]
    static let barTextColor = ThemeColorPicker.pickerWithColors(barTextColors)
    
    // navigation title attributes
    static let titleAttributes = GlobalPicker.textColors.map { hexString -> [NSAttributedStringKey : NSObject] in
        let shadow = NSShadow()
        shadow.shadowOffset = CGSize(width: 0, height: 0)
        return [
            NSAttributedStringKey.foregroundColor: UIColor(rgba: hexString),
            NSAttributedStringKey.font: FontConfigManager.shared.getMediumFont(size: 17),
            NSAttributedStringKey.shadow: shadow
        ]
    }

    static let back: ThemeImagePicker = ThemeImagePicker(images: UIImage(named: "Back-button-light")!, UIImage(named: "Back-button-dark")!)
    static let backHighlight: ThemeImagePicker = ThemeImagePicker(images: UIImage(named: "Back-button-light")!.alpha(0.3), UIImage(named: "Back-button-dark")!.alpha(0.3))
    static let indicator: ThemeImagePicker = ThemeImagePicker(images: UIImage(named: "Indicator-light\(ColorTheme.getTheme())") ?? UIImage(named: "Indicator-light-yellow")!, UIImage(named: "Indicator-dark\(ColorTheme.getTheme())") ?? UIImage(named: "Indicator-dark-yellow")!)
    static let close: ThemeImagePicker = ThemeImagePicker(images: UIImage(named: "Close-light")!, UIImage(named: "Close-dark")!)
    static let closeHighlight: ThemeImagePicker = ThemeImagePicker(images: UIImage(named: "Close-light")!.alpha(0.3), UIImage(named: "Close-dark")!.alpha(0.3))
}
