//
//  TimeAgoSince.swift
//  loopr-ios
//
//  Created by ruby on 1/25/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

public func timeAgoSince(_ date: Date) -> String {
    
    let calendar = Calendar.current
    let now = Date()
    let unitFlags: NSCalendar.Unit = [.second, .minute, .hour, .day, .weekOfYear, .month, .year]
    let components = (calendar as NSCalendar).components(unitFlags, from: date, to: now, options: [])
    
    if let year = components.year, year >= 2 {
        return "\(year) \(LocalizedString("years", comment: ""))"
    }
    
    if let year = components.year, year >= 1 {
        return LocalizedString("Last year", comment: "")
    }
    
    if let month = components.month, month >= 2 {
        return "\(month) \(LocalizedString("months ago", comment: ""))"
    }
    
    if let month = components.month, month >= 1 {
        return LocalizedString("Last month", comment: "")
    }
    
    if let week = components.weekOfYear, week >= 2 {
        return "\(week) \(LocalizedString("weeks ago", comment: ""))"
    }
    
    if let week = components.weekOfYear, week >= 1 {
        return LocalizedString("Last week", comment: "")
    }
    
    if let day = components.day, day >= 2 {
        return "\(day) \(LocalizedString("days ago", comment: ""))"
    }
    
    if let day = components.day, day >= 1 {
        return LocalizedString("Yesterday", comment: "")
    }
    
    if let hour = components.hour, hour >= 2 {
        return "\(hour) \(LocalizedString("hours ago", comment: ""))"
    }
    
    if let hour = components.hour, hour >= 1 {
        return LocalizedString("An hour ago", comment: "")
    }
    
    if let minute = components.minute, minute >= 2 {
        return "\(minute) \(LocalizedString("minutes ago", comment: ""))"
    }
    
    if let minute = components.minute, minute >= 1 {
        return LocalizedString("A minute ago", comment: "")
    }
    
    if let second = components.second, second >= 3 {
        return "\(second) \(LocalizedString("seconds ago", comment: ""))"
    }
    
    return LocalizedString("Just now", comment: "")
    
}
