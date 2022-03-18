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
         collectionView.register(BookingCollectionViewCell.self, forCellWithReuseIdentifier: "bookingCollectionViewCell")
         collectionView.dataSource = self
         collectionView.backgroundColor = .clear
         view.backgroundColor = .systemIndigo
         view.addSubview(collectionView)
     }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 13
        
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        collectionView.dequeueReusableCell(withReuseIdentifier: "bookingCollectionViewCell", for: indexPath)

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
