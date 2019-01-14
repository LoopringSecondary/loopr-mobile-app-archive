//
//  News.swift
//  loopr-ios
//
//  Created by kenshin on 2018/12/24.
//  Copyright © 2018 Loopring. All rights reserved.
//

import Foundation

class Blog {
    var title: String
    var url: String
    var imageUrl: String
    var localFileName: String
    var image: UIImage?
    
    init(json: JSON) {
        self.title = json["title"].stringValue
        self.url = json["url"].stringValue
        self.imageUrl = json["imageUrl"].stringValue
        self.localFileName = imageUrl.replacingOccurrences(of: "/", with: "")
        getImage()
    }
    
    private func getImage() {
        let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        let filePath = documentsURL.appendingPathComponent("\(localFileName).png").path
        if FileManager.default.fileExists(atPath: filePath) {
            print("Image has been stored locally.")
            self.image = UIImage(contentsOfFile: filePath)
        } else {
            print("No image are found locally.")
            downloadImage()
        }
    }

    private func downloadImage() {
        let url = URL(string: imageUrl)!
        print("Download Started")
        getData(from: url) { data, response, error in
            guard let data = data, error == nil else { return }
            print(response?.suggestedFilename ?? url.lastPathComponent)
            print("Download Finished")
            if let image = UIImage(data: data) {
                self.image = image
                self.saveImageDataToFileSystem(image: image, localFileName: self.localFileName)
            }
        }
    }
    
    private func getData(from url: URL, completion: @escaping (Data?, URLResponse?, Error?) -> ()) {
        URLSession.shared.dataTask(with: url, completionHandler: completion).resume()
    }

    private func saveImageDataToFileSystem(image: UIImage, localFileName: String) {
        do {
            let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
            let fileURL = documentsURL.appendingPathComponent("\(localFileName).png")
            print(fileURL.absoluteString)
            if let pngImageData = UIImagePNGRepresentation(image) {
                try pngImageData.write(to: fileURL, options: .atomic)
                print("Writing image succeeded")
            }
        } catch let error {
            print("Writing image failed: " + error.localizedDescription)
        }
    }
}
