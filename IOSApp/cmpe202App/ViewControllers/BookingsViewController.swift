//
//  bookingsViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/5/22.
//

import UIKit
import CollectionViewPagingLayout
import SwiftUI
import NVActivityIndicatorView

class BookingsViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    var collectionView : UICollectionView!
    @IBOutlet weak var activityIndicatorView: NVActivityIndicatorView!
     var myBookingsArray:NSArray!
    override func viewDidLoad() {
        super.viewDidLoad()
        setupCollectionView()
        
     }
    override func viewWillAppear(_ animated: Bool) {
        getBookingsAPI()
        collectionView.reloadData()
    }
    override func viewDidAppear(_ animated: Bool) {
        

    }
     private func setupCollectionView() {
         let layout = CollectionViewPagingLayout()
         collectionView = UICollectionView(frame: view.frame, collectionViewLayout: layout)
         collectionView.isPagingEnabled = true
         collectionView.dataSource = self
         collectionView.delegate = self
         collectionView.register(BookingCollectionViewCell.self, forCellWithReuseIdentifier: "BookingCollectionViewCell")
         collectionView.backgroundColor = .clear
         view.backgroundColor = UIColor(red: 86.0/255.0, green: 148.0/255.0, blue: 214.0/255.0, alpha: 1.0)
         view.addSubview(collectionView)
         
     }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if(self.myBookingsArray != nil){
            return self.myBookingsArray.count
        }
        else{
            return 0;
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "BookingCollectionViewCell", for: indexPath) as! BookingCollectionViewCell
        
        if(self.myBookingsArray != nil){
            let dataDict = self.myBookingsArray.object(at: indexPath.row) as! NSDictionary
            
            let titleFont = UIFont(name: "Helvetica Neue", size: 30)

             
            let attributes = [
                NSAttributedString.Key.font : titleFont,
                NSAttributedString.Key.foregroundColor : UIColor(.black)]

            let attributedString = NSMutableAttributedString(string: "",attributes: attributes as [NSAttributedString.Key : Any])
            let pinImage = NSTextAttachment()
            pinImage.image = UIImage(named: "geotag")
            let iconsSize = CGRect(x: 0, y: -5, width: 30, height: 30)
            pinImage.bounds = iconsSize
            attributedString.append(NSAttributedString(attachment: pinImage))
            attributedString.append(NSAttributedString(string: " \(String(describing: dataDict.value(forKey: "city")!))"))
            cell.hotelLocation.attributedText=attributedString
            cell.hotelImage.image = UIImage(named: "hotelStatic")
            cell.hotelName.text = "\(String(describing: dataDict.value(forKey: "hotelName")!))"
            cell.bookingDate.text = "Check In\nMay 22, 2022"
            cell.qrImage.image = generateQRCode(from: "\(String(describing: dataDict.value(forKey: "id")!))")
            let price = dataDict.value(forKey: "totalPayableAmount") as! Float
            let rewards = dataDict.value(forKey: "rewardPointsUsed") as! Float

            cell.price.text = "$\(Int(price-rewards))"
            cell.nights.text = "3\nNight\nStay"
            cell.daysLeft.text = "46\nDays\nto\nGo"
        }
        
        
        
        return cell

    }
    func generateQRCode(from string: String) -> UIImage? {
        let data = string.data(using: String.Encoding.ascii)

        if let filter = CIFilter(name: "CIQRCodeGenerator") {
            filter.setValue(data, forKey: "inputMessage")
            let transform = CGAffineTransform(scaleX: 3, y: 3)

            if let output = filter.outputImage?.transformed(by: transform) {
                return UIImage(ciImage: output)
            }
        }

        return nil
    }

    func getBookingsAPI(){
        activityIndicatorView.startAnimating()
        let url = URL(string: "\(globals.api)getBookingByEmail")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let preferences = UserDefaults.standard
        let key = "user"
        let userDict = preferences.object(forKey: key) as! NSDictionary
        let email = userDict.value(forKey: "email")
        let jsonDict = NSMutableDictionary()
        jsonDict.setValue("\(String(describing: email!))", forKey: "customerEmail")

        let jsonData = try? JSONSerialization.data(withJSONObject: jsonDict)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        request.httpMethod = "POST"
        request.httpBody = jsonData

        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data,
            let response = response as? HTTPURLResponse,
            error == nil else {
                // check for fundamental networking error
                print("error", error ?? "Unknown error")
                return
            }

            guard (200 ... 299) ~= response.statusCode else {                    // check for http errors
                print("statusCode should be 2xx, but is \(response.statusCode)")
                print("response = \(response)")
                if response.statusCode == 400 {
                    do {
                        let json = try JSONSerialization.jsonObject(with: data) as! Dictionary<String, AnyObject>
                            print(json)
                        DispatchQueue.main.async { [self] () -> Void in
                            self.showToast(message: json["message"] as! String, font: .systemFont(ofSize: 12.0))
                            self.activityIndicatorView.stopAnimating()

                        }

                        } catch {
                            print("error")
                        }
                    
                }
                print(String(data: data, encoding: .utf8))
                return
            }
            do{
                let json = try JSONSerialization.jsonObject(with: data) as! NSDictionary
                let arr = json.object(forKey: "bookingArray") as! NSArray
                DispatchQueue.main.async { [self] () -> Void in
                    self.myBookingsArray = NSArray.init(array: arr)
                    self.collectionView.reloadData()
                    self.activityIndicatorView.stopAnimating()
                    
                    

                }
            }
            catch{
                print("JSON Error : MyBooking API")
            }
            
            
        }

        task.resume()

    }
}
