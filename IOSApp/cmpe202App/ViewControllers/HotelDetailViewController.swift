//
//  hotelDetailViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/5/22.
//

import UIKit

class HotelDetailViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UITableViewDelegate, UITableViewDataSource  {

    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var hangingNameView: UIView!
    @IBOutlet weak var buttonView: UIView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var gradientImage: UIImageView!
    @IBOutlet weak var imageCollection: UICollectionView!
    @IBOutlet weak var collectionView: UIView!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var hangingShadowView: UIView!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.hangingNameView.frame = CGRect(x: globals.X(view: collectionView)!, y: globals.BOTTOM(view: collectionView)!-45, width: globals.WIDTH(view: collectionView)!, height: globals.HEIGHT(view: hangingNameView)!)
        
        self.hangingShadowView.frame = CGRect(x: globals.X(view: hangingNameView)!+10 , y: globals.BOTTOM(view: collectionView)!-35, width: globals.WIDTH(view: collectionView)!-20, height: globals.HEIGHT(view: hangingNameView)!-20)
        
        self.hangingNameView.layer.masksToBounds=false
        self.hangingNameView.layer.shadowOffset = CGSize(width: 4,
                                          height: 4)
        self.hangingNameView.layer.shadowRadius = 4
        self.hangingNameView.layer.shadowOpacity = 0.5
        
        
//        // Do any additional setup after loading the view.
    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == imageCollection {
            return 3
        }
        else{
            return 12
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        if collectionView == imageCollection {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"ImageCollectionViewCell", for:indexPath as IndexPath) as! HomeCollectionViewCell

            return cell
        }
        else{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"ammenitiesCollectionViewCell", for:indexPath as IndexPath) as! UICollectionViewCell

            return cell
        }
        
    }
    
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 6
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "staticCell", for: indexPath)
            return cell
        }
        else if indexPath.row == 1{
            let cell = tableView.dequeueReusableCell(withIdentifier: "roomCell", for: indexPath)
            return cell
        }
//        else if indexPath.row == 2{
//            let cell = tableView.dequeueReusableCell(withIdentifier: "roomCell", for: indexPath)
//            return cell
//        }
        else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "staticCell", for: indexPath)
            return cell
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 {
            return 80
        }
        else if indexPath.row == 1{
            return 100
        }
        else{
            return 0
        }
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
