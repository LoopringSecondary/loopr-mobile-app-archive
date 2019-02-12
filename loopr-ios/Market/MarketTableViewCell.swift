//
//  MarketTableViewCell.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/2/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import UIKit

class MarketTableViewCell: UITableViewCell {

    var market: Market?

    @IBOutlet weak var baseView: UIView!
    @IBOutlet weak var favButton: UIButton!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var balanceLabel: UILabel!
    @IBOutlet weak var marketPriceInBitcoinLabel: UILabel!
    @IBOutlet weak var marketPriceInFiatCurrencyLabel: UILabel!
    @IBOutlet weak var percentageChangeLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code

        selectionStyle = .none
        accessoryType = .none

        theme_backgroundColor = ColorPicker.backgroundColor
        baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        
        nameLabel.font = FontConfigManager.shared.getDigitalFont(size: 14)
        nameLabel.theme_textColor = GlobalPicker.textColor
        
        balanceLabel.font = FontConfigManager.shared.getRegularFont(size: 13)
        balanceLabel.theme_textColor = GlobalPicker.textLightColor
        
        marketPriceInBitcoinLabel.font = FontConfigManager.shared.getMediumFont(size: 14)
        marketPriceInBitcoinLabel.theme_textColor = GlobalPicker.textColor
        
        marketPriceInFiatCurrencyLabel.font = FontConfigManager.shared.getRegularFont(size: 13)
        marketPriceInFiatCurrencyLabel.theme_textColor = GlobalPicker.textLightColor
        
        percentageChangeLabel.font = FontConfigManager.shared.getRegularFont(size: 14)
        percentageChangeLabel.textColor = UIColor.white
        percentageChangeLabel.textAlignment = .center
        percentageChangeLabel.cornerRadius = 6
        percentageChangeLabel.clipsToBounds = true
    }
    
    override func setHighlighted(_ highlighted: Bool, animated: Bool) {
        if highlighted {
            baseView.theme_backgroundColor = ColorPicker.cardHighLightColor
        } else {
            baseView.theme_backgroundColor = ColorPicker.cardBackgroundColor
        }
    }
    
    func updateStarButton(market: Market) {
        if market.isFavorite() {
            favButton.image = UIImage(named: "Star" + ColorTheme.getTheme())?.withRenderingMode(.alwaysOriginal)
        } else {
            favButton.image = UIImage(named: "StarOutline")?.withRenderingMode(.alwaysOriginal)
        }
    }

    func update() {
        if let market = market {
            updateStarButton(market: market)
            nameLabel.text = market.description
            nameLabel.setMarket()
            if market.volumeInPast24 > 1 {
                let vol = Darwin.round(market.volumeInPast24)
                balanceLabel.text = "Vol \(vol.withCommas(0))"
            } else {
                balanceLabel.text = "Vol \(market.volumeInPast24.withCommas())"
            }
            
            marketPriceInBitcoinLabel.text = market.balanceWithDecimals
            marketPriceInFiatCurrencyLabel.text = market.display.description
            percentageChangeLabel.text = market.changeInPat24
            percentageChangeLabel.backgroundColor = UIStyleConfig.getChangeColor(change: market.changeInPat24)
        }
    }
    
    @IBAction func pressedFavButton(_ sender: UIButton) {
        guard let market = market else {
            return
        }
        if market.isFavorite() {
            MarketDataManager.shared.removeFavoriteMarket(market: market)
        } else {
            MarketDataManager.shared.setFavoriteMarket(market: market)
        }
        updateStarButton(market: market)
    }
    
    class func getCellIdentifier() -> String {
        return "MarketTableViewCell"
    }
    
    class func getHeight() -> CGFloat {
        return 60
    }
}
