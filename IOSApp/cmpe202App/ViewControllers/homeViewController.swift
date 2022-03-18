//
//  homeViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 3/1/22.
//

import UIKit
class homeViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UICollectionViewDelegate, UICollectionViewDataSource {
    
    
    @IBOutlet weak var homeTableView : UITableView!
    let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if #available(iOS 10.0, *) {
            homeTableView.refreshControl = refreshControl
        } else {
            homeTableView.addSubview(refreshControl)
        }
        
        refreshControl.tintColor = UIColor(red:0.25, green:0.72, blue:0.85, alpha:1.0)
        refreshControl.addTarget(self, action: #selector(refreshHotelsTableView(_:)), for: .valueChanged)
        
        homeTableView.dataSource=self
        homeTableView.delegate=self
        homeTableView.reloadData()
        homeTableView.register(UINib(nibName: "HomeTableViewCell", bundle: nil), forCellReuseIdentifier: "HomeTableViewCell")
    }
    override func viewWillAppear(_ animated: Bool) {
        
    }
       
    @objc private func refreshHotelsTableView(_ sender: Any) {
        // Fetch Weather Data
        fetchHotelData()
    }
    private func fetchHotelData() {
        self.refreshControl.endRefreshing()

    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 3
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"HomeCollectionViewCell", for:indexPath as IndexPath) as! HomeCollectionViewCell
        
        cell.hotelImageView.layer.cornerRadius = 20.0
        cell.gradientImageView.layer.cornerRadius = 20.0


        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeCollectionTableViewCell", for: indexPath)
            return cell
        }
        else if indexPath.row == 1{
            let cell = tableView.dequeueReusableCell(withIdentifier: "staticCell", for: indexPath)
            return cell
        }
        else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath)
            return cell
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 {
            return 420
        }
        else if indexPath.row == 1{
            return 50
        }
        else{
            return 170
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "HotelDetailViewController") as! HotelDetailViewController
            self.present(vc, animated: true, completion: nil)
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "HotelDetailViewController") as! HotelDetailViewController
            self.present(vc, animated: true, completion: nil)
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
