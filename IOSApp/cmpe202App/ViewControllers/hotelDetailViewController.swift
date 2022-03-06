//
//  hotelDetailViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/5/22.
//

import UIKit

class hotelDetailViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UITableViewDelegate, UITableViewDataSource  {

    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var hangingNameView: UIView!
    @IBOutlet weak var buttonView: UIView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var gradientImage: UIImageView!
    @IBOutlet weak var imageCollection: UICollectionView!
    @IBOutlet weak var collectionView: UIView!
    @IBOutlet weak var topLabel: UILabel!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.hangingNameView.frame = CGRect(x: globals.X(view: hangingNameView)! , y: globals.BOTTOM(view: collectionView)!-45, width: globals.WIDTH(view: hangingNameView)!, height: globals.HEIGHT(view: hangingNameView)!)
        // Do any additional setup after loading the view.
    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 3
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"ImageCollectionViewCell", for:indexPath as IndexPath) as! HomeCollectionViewCell

        return cell
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
            let cell = tableView.dequeueReusableCell(withIdentifier: "staticCell", for: indexPath)
            return cell
        }
        else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "staticCell", for: indexPath)
            return cell
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 {
            return 40
        }
        else if indexPath.row == 1{
            return 20
        }
        else{
            return 30
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
