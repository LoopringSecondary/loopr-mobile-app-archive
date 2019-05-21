//
//  MarketDataManager.swift
//  loopr-ios
//
//  Created by Xiao Dou Dou on 2/2/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class MarketDataManager {

    static let shared = MarketDataManager()

    // For a specified market
    private var oneHourTrends: [MarketHistoryItem]
    private var twoHoursTrends: [MarketHistoryItem]
    private var fourHoursTrends: [MarketHistoryItem]
    private var oneDayTrends: [MarketHistoryItem]
    private var oneWeekTrends: [MarketHistoryItem]

    private var markets: [Market]

    private var favoriteSequence: [String]

    private init() {
        markets = []

        oneHourTrends = []
        twoHoursTrends = []
        fourHoursTrends = []
        oneDayTrends = []
        oneWeekTrends = []

        let defaults = UserDefaults.standard

        // If users have never set or remove favorite market pair, use the default values "LRC-WETH"
        if defaults.array(forKey: UserDefaultsKeys.favoriteSequence.rawValue) as? [String] == nil {
            favoriteSequence = ["LRC-WETH"]
            UserDefaults.standard.set(favoriteSequence, forKey: UserDefaultsKeys.favoriteSequence.rawValue)
        } else {
            favoriteSequence = defaults.array(forKey: UserDefaultsKeys.favoriteSequence.rawValue) as? [String] ?? []
        }
    }

    func isMarketsEmpty() -> Bool {
        return markets.isEmpty
    }

    func setMarkets(newMarkets: [Market]) {
        let filteredMarkets = newMarkets.filter { (market) -> Bool in
            return market.name != ""
        }
        self.markets = filteredMarkets
    }

    func getDecimals(tokenSymbol: String) -> Int {
        let filteredMarkets = markets.filter { (market) -> Bool in
            return market.name == "\(tokenSymbol)-USDT"
        }
        guard filteredMarkets.count > 0 else {
            return 8
        }

        return filteredMarkets[0].metadata.priceDecimals
    }

    func getDecimals(pair: String) -> Int {
        let filteredMarkets = markets.filter { (market) -> Bool in
            return market.name == pair
        }
        guard filteredMarkets.count > 0 else {
            return 8
        }

        return filteredMarkets[0].metadata.priceDecimals
    }

    func getBalance(of pair: String) -> Double {
        var result: Double = 0
        for market in markets {
            if market.name.lowercased() == pair.lowercased() {
                result = market.ticker.price
                break
            }
        }
        return result
    }

    func getAllTrends(market: String, completionHandler: @escaping (_ error: Error?) -> Void) {
        /*
        let dispatchGroup = DispatchGroup()

        dispatchGroup.enter()
        LoopringAPIRequest.getTrend(market: market, interval: TrendInterval.oneHour.description, completionHandler: { (trends, error) in
            if error != nil {
                self.oneHourTrends = []
            } else {
                self.oneHourTrends = trends
            }
            dispatchGroup.leave()
        })

        dispatchGroup.enter()
        LoopringAPIRequest.getTrend(market: market, interval: TrendInterval.twoHours.description, completionHandler: { (trends, error) in
            if error != nil {
                self.twoHoursTrends = []
            } else {
                self.twoHoursTrends = trends
            }
            dispatchGroup.leave()
        })

        dispatchGroup.enter()
        LoopringAPIRequest.getTrend(market: market, interval: TrendInterval.fourHours.description, completionHandler: { (trends, error) in
            if error != nil {
                self.fourHoursTrends = []
            } else {
                self.fourHoursTrends = trends
            }
            dispatchGroup.leave()
        })

        dispatchGroup.enter()
        LoopringAPIRequest.getTrend(market: market, interval: TrendInterval.oneDay.description, completionHandler: { (trends, error) in
            if error != nil {
                self.oneDayTrends = []
            } else {
                self.oneDayTrends = trends
            }
            dispatchGroup.leave()
        })

        dispatchGroup.enter()
        LoopringAPIRequest.getTrend(market: market, interval: TrendInterval.oneWeek.description, completionHandler: { (trends, error) in
            if error != nil {
                self.oneWeekTrends = []
            } else {
                self.oneWeekTrends = trends
            }
            dispatchGroup.leave()
        })

        dispatchGroup.notify(queue: .main) {
            completionHandler(nil)
        }
        */
    }

    func getTrends(trendRange: MarketRange) -> [MarketHistoryItem] {
        /*
        var trends: [Trend] = []
        switch trendRange.getTrendInterval() {
        case .oneHour:
            trends = oneHourTrends
        case .twoHours:
            trends = twoHoursTrends
        case .fourHours:
            trends = fourHoursTrends
        case .oneDay:
            trends = oneDayTrends
        case .oneWeek:
            trends = oneWeekTrends
        }
        if trends.count >= trendRange.getCount() {
            return Array(trends[0..<trendRange.getCount()].reversed())
        } else {
            return trends.reversed()
        }
        */
        return []
    }

    func getMarket(byTradingPair tradingPair: String) -> Market? {
        for case let market in self.markets where market.name.lowercased() == tradingPair.lowercased() {
            return market
        }
        return nil
    }

    func getMarketsWithoutReordered(type: MarketSwipeViewType = .all) -> [Market] {
        var result: [Market]
        switch type {
        case .favorite:
            result = markets.filter({ (market) -> Bool in
                return favoriteSequence.contains(market.name)
            })
        case .LRC:
            let sortedMarkets = markets.filter({ (market) -> Bool in
                return market.ticker.baseSymbol.uppercased() == "LRC"
            }).sorted { (a, b) -> Bool in
                return a.name < b.name
            }
            result = sortedMarkets
        case .ETH:
            let sortedMarkets = markets.filter({ (market) -> Bool in
                return market.ticker.baseSymbol.uppercased() == "ETH" || market.ticker.baseSymbol.uppercased() == "WETH"
            }).sorted { (a, b) -> Bool in
                return a.name < b.name
            }
            result = sortedMarkets
        case .USDT:
            let sortedMarkets = markets.filter({ (market) -> Bool in
                return market.ticker.baseSymbol.uppercased() == "USDT"
            }).sorted { (a, b) -> Bool in
                return a.name < b.name
            }
            result = sortedMarkets
        case .TUSD:
            let sortedMarkets = markets.filter({ (market) -> Bool in
                return market.ticker.baseSymbol.uppercased() == "TUSD"
            }).sorted { (a, b) -> Bool in
                return a.name < b.name
            }
            result = sortedMarkets
        case .all:
            result = markets
        }

        return result
    }

    func getFavoriteMarketKeys() -> [String] {
        return favoriteSequence
    }

    func setFavoriteMarket(market: Market) {
        favoriteSequence.append(market.name)
        UserDefaults.standard.set(favoriteSequence, forKey: UserDefaultsKeys.favoriteSequence.rawValue)
    }

    func removeFavoriteMarket(market: Market) {
        favoriteSequence = favoriteSequence.filter { $0 != market.name }
        UserDefaults.standard.set(favoriteSequence, forKey: UserDefaultsKeys.favoriteSequence.rawValue)
    }

}