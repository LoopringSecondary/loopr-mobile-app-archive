//
//  NewsImage.swift
//  loopr-ios
//
//  Created by xiaoruby on 1/17/19.
//  Copyright © 2019 Loopring. All rights reserved.
//

import Foundation

class NewsImage {
    
    static let folder: String = "NewsParagraph"

    var imageUrl: String
    var localFileName: String
    var image: UIImage?
    var isLoading: Bool = false
    
    init?(imageUrl: String) {
        guard URL(string: imageUrl) != nil else {
            return nil
        }
        self.imageUrl = imageUrl
        self.localFileName = NewsImage.folder + "/" + imageUrl.replacingOccurrences(of: "/", with: "_")
        getImage()
    }
    
    private func getImage() {
        let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        createNewsParagraphFolderIfNotExit()
        
        let filePath = documentsURL.appendingPathComponent("\(localFileName).png").path
        if FileManager.default.fileExists(atPath: filePath) {
            print("Image has been stored locally.")
            self.image = UIImage(contentsOfFile: filePath)
        } else {
            print("No image are found locally.")
            downloadImage { (_) in
                
            }
        }
    }
    
    private func createNewsParagraphFolderIfNotExit() {
        let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        let folderPath = documentsURL.appendingPathComponent(NewsImage.folder)
        if !FileManager.default.fileExists(atPath: folderPath.path) {
            do {
                try FileManager.default.createDirectory(atPath: folderPath.path, withIntermediateDirectories: true, attributes: nil)
            } catch let error as NSError {
                print("Unable to create directory \(error.debugDescription)")
            }
        } else {
            print("NewsParagraph folder exists")
        }
    }
    
    public func downloadImage(completion: @escaping (UIImage?) -> Void) {
        isLoading = true
        let url = URL(string: imageUrl)!
        print("Download Started")
        getData(from: url) { data, response, error in
            self.isLoading = false
            guard let data = data, error == nil else {
                completion(nil)
                return
            }
            print(response?.suggestedFilename ?? url.lastPathComponent)
            print("Download Finished")
            if let image = UIImage(data: data) {
                self.image = image
                self.saveImageDataToFileSystem(image: image, localFileName: self.localFileName)
                completion(image)
            } else {
                completion(nil)
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
