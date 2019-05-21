//
//  AppServiceManager.swift
//  loopr-ios
//
//  Created by xiaoruby on 10/14/18.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class AppServiceUpdateManager {
    
    static let shared = AppServiceUpdateManager()

    var latestBuildVersion: String = "0.0.1"
    private var latestBuildReleaseNote_en: String = ""
    private var latestBuildReleaseNote_chs: String = ""
    private var latestBuildReleaseNote_cht: String = ""

    private init() {
        
    }

    // Current build version
    func getBuildVersion() -> String {
        let build = Bundle.main.object(forInfoDictionaryKey: kCFBundleVersionKey as String) as! String
        return build
    }

    func getAppVersionAndBuildVersion() -> String {
        let version = Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as! String
        let build = Bundle.main.object(forInfoDictionaryKey: kCFBundleVersionKey as String) as! String
        return version + " (" + build + ")"
    }
    
    func shouldDisplayUpdateNotificationInSettingViewController() -> Bool {
        let currentBuildVersion = self.getBuildVersion()
        if latestBuildVersion.compare(currentBuildVersion, options: .numeric) == .orderedDescending && latestBuildVersion != currentBuildVersion {
            return true
        } else {
            return false
        }
    }
    
    func getReleaseNote() -> String {
        let currentLanguage = SettingDataManager.shared.getCurrentLanguage().name
        if currentLanguage == "zh-Hans" {
            return latestBuildReleaseNote_chs
        } else if currentLanguage == "zh-Hant" {
            return latestBuildReleaseNote_cht
        } else {
            return latestBuildReleaseNote_en
        }
    }

    func getLatestAppVersion(completion: @escaping (_ shouldDisplayUpdateNotification: Bool) -> Void) {
        guard Production.getCurrent() == .upwallet else {
            return
        }
        
        // Seprate from Request.
        let url = URL(string: "https://www.loopring.mobi/rpc/v1/version/ios/getLatest")
        let task = URLSession.shared.dataTask(with: url! as URL) { data, _, error in
            guard let data = data, error == nil else {
                print("error=\(String(describing: error))")
                return
            }
            let json = JSON(data)
            let latestBuildVersion = json["message"]["version"].string ?? "0.0.1"
            self.latestBuildVersion = latestBuildVersion
            self.latestBuildReleaseNote_en = json["message"]["release_note_en"].string ?? ""
            self.latestBuildReleaseNote_chs = json["message"]["release_note_chs"].string ?? ""
            self.latestBuildReleaseNote_cht = json["message"]["release_note_cht"].string ?? ""

            let currentBuildVersion = self.getBuildVersion()
            let largestSkipBuildVersion = self.getLargestSkipBuildVersion()
            if latestBuildVersion.compare(currentBuildVersion, options: .numeric) == .orderedDescending && latestBuildVersion != currentBuildVersion && latestBuildVersion.compare(largestSkipBuildVersion, options: .numeric) == .orderedDescending && latestBuildVersion != largestSkipBuildVersion {
                print("latestBuildVersion version is newer")
                completion(true)
            } else {
                completion(false)
            }
        }
        task.resume()
    }

    func getLargestSkipBuildVersion() -> String {
        let defaults = UserDefaults.standard
        return defaults.string(forKey: UserDefaultsKeys.largestSkipBuildVersion.rawValue) ?? "0.0.1"
    }

    func setLargestSkipBuildVersion() {
        let defaults = UserDefaults.standard
        defaults.set(latestBuildVersion, forKey: UserDefaultsKeys.largestSkipBuildVersion.rawValue)
    }

}
