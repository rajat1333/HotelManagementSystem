//
//  bookingsViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/5/22.
//

import UIKit
import CollectionViewPagingLayout
import SwiftUI

class BookingsViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    var collectionView : UICollectionView!
    override func viewDidLoad() {
        super.viewDidLoad()
        setupCollectionView()
     }
         
     private func setupCollectionView() {
         let layout = CollectionViewPagingLayout()
         collectionView = UICollectionView(frame: view.frame, collectionViewLayout: layout)
         collectionView.isPagingEnabled = true
         collectionView.register(BookingCollectionViewCell.self, forCellWithReuseIdentifier: "BookingCollectionViewCell")
         collectionView.dataSource = self
         collectionView.backgroundColor = .clear
         view.backgroundColor = UIColor(red: 86.0/255.0, green: 148.0/255.0, blue: 214.0/255.0, alpha: 1.0)
         view.addSubview(collectionView)
     }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 13
        
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "BookingCollectionViewCell", for: indexPath) as! BookingCollectionViewCell
        
        
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
        attributedString.append(NSAttributedString(string: " Los Angeles"))
        cell.hotelLocation.attributedText=attributedString
        cell.hotelImage.image = UIImage(named: "hotelStatic")
        cell.hotelName.text = "De Sol Spa Hotel"
        cell.bookingDate.text = "Check In : \nMarch 22, 2022"
        cell.qrImage.image = generateQRCode(from: "12345SDFG6789")
        
        
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

//    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
//        let width = ((collectionView.frame.width - 15) / 2) // 15 because of paddings
//        print("cell width : \(width)")
//
//        if indexPath.row % 2 == 1 {
//            return CGSize(width: 190, height: 194)
//        }
//        else{
//            return CGSize(width: 190, height: 214)
//        }
//
//    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
