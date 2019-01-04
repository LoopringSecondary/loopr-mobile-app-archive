//
//  News.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/24.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class Blog: NewsProtocol {
    var title: String
    var url: String
    var imageUrl: String
    var image: UIImage?
    
    init(json: JSON) {
        self.title = json["title"].stringValue
        self.url = json["url"].stringValue
        self.imageUrl = json["imageUrl"].stringValue
        downloadImage(from: self.imageUrl)
    }
    
    func downloadImage(from urlString: String) {
        let url = URL(string: urlString)!
        print("Download Started")
        getData(from: url) { data, response, error in
            guard let data = data, error == nil else { return }
            print(response?.suggestedFilename ?? url.lastPathComponent)
            print("Download Finished")
            if let image = UIImage(data: data) {
                self.image = image
            }
        }
    }
    
    func getData(from url: URL, completion: @escaping (Data?, URLResponse?, Error?) -> ()) {
        URLSession.shared.dataTask(with: url, completionHandler: completion).resume()
    }

}
