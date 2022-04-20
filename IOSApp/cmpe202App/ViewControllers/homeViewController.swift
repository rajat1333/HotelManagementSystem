//
//  homeViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 3/1/22.
//

import UIKit
import NVActivityIndicatorView
import Alamofire
import AlamofireImage
class homeViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UICollectionViewDelegate, UICollectionViewDataSource {
    
    @IBOutlet weak var activityIndicatorView:NVActivityIndicatorView!
    @IBOutlet weak var homeTableView : UITableView!
    var collectionArray : NSMutableArray! = nil
    var tableArray : NSMutableArray! = nil
    let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if #available(iOS 10.0, *) {
            homeTableView.refreshControl = refreshControl
        } else {
            homeTableView.addSubview(refreshControl)
        }
        tableArray = NSMutableArray()
        collectionArray = NSMutableArray()
        refreshControl.tintColor = UIColor(red:0.25, green:0.72, blue:0.85, alpha:1.0)
        refreshControl.addTarget(self, action: #selector(refreshHotelsTableView(_:)), for: .valueChanged)
        
        homeTableView.dataSource=self
        homeTableView.delegate=self
        homeTableView.reloadData()
        homeTableView.register(UINib(nibName: "HomeTableViewCell", bundle: nil), forCellReuseIdentifier: "HomeTableViewCell")
    }
    override func viewWillAppear(_ animated: Bool) {
        fetchHotelData()
    }
       
    @objc private func refreshHotelsTableView(_ sender: Any) {
        fetchHotelData()
    }
    private func fetchHotelData() {
        getHotelApi()
        self.refreshControl.endRefreshing()

    }
    func getHotelApi(){
        activityIndicatorView.startAnimating()
        let url = URL(string: "\(globals.api)readhotel")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        request.httpMethod = "GET"
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
            do {
                let json = try JSONSerialization.jsonObject(with: data) as! [Any]
                    print(json)
                
                let dataArray = json
                DispatchQueue.main.async { [self] () -> Void in
                    if(dataArray.count>3){}
                    let top3 = Array(dataArray.prefix(3))
                    collectionArray = NSMutableArray.init(array: top3)
                    tableArray = NSMutableArray.init(array: Array(dataArray[3 ..< dataArray.count]))
                    self.homeTableView.reloadData()
                    self.activityIndicatorView.stopAnimating()
                }

            } catch {
                print("error")
            }
        }

        task.resume()

    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return collectionArray.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"HomeCollectionViewCell", for:indexPath as IndexPath) as! HomeCollectionViewCell
        
        let dataDict = collectionArray.object(at: indexPath.row) as! NSDictionary

        cell.nameLabel.text = dataDict["name"] as? String
        cell.locationLabel.text = dataDict["city"] as? String
        let url = URL(string: dataDict["imageURL"] as! String)!
        
        cell.hotelImageView.af.setImage(withURL: url, cacheKey: "mainCollection\(indexPath.row)", placeholderImage: UIImage (named: "hotelStatic"), serializer: nil, filter: nil, progress: nil, progressQueue: .global(), imageTransition: .noTransition, runImageTransitionIfCached: false, completion: nil)
        cell.hotelImageView.layer.cornerRadius = 20.0
        cell.gradientImageView.layer.cornerRadius = 20.0
        

        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableArray.count + 2
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeCollectionTableViewCell", for: indexPath) as! HomeCollectionTableViewCell
            cell.collectionView.dataSource = self
            cell.collectionView.delegate = self
            cell.collectionView.reloadData()
            return cell
        }
        else if indexPath.row == 1{
            let cell = tableView.dequeueReusableCell(withIdentifier: "staticCell", for: indexPath)
            return cell
        }
        else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
            
            let dataDict = tableArray.object(at: indexPath.row - 2) as! NSDictionary

            cell.hotelName.text = dataDict["name"] as? String
            cell.location.text = dataDict["city"] as? String
            let price = dataDict["basePrice"] as! Int
            cell.price.text = "from $\(String(describing: price))"
            let url = URL(string: dataDict["imageURL"] as! String)!
            cell.hotelImage.af.setImage(withURL: url, cacheKey: "mainTable\(indexPath.row)", placeholderImage: UIImage (named: "tableListImage"), serializer: nil, filter: nil, progress:nil, progressQueue: .global(), imageTransition: .noTransition, runImageTransitionIfCached: false, completion: nil)
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
