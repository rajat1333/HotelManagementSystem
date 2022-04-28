//
//  SearchViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/28/22.
//

import UIKit
import NVActivityIndicatorView
class SearchViewController: UIViewController,UITableViewDelegate,UITableViewDataSource {
    @IBOutlet weak var activityIndicatorView:NVActivityIndicatorView!
    @IBOutlet weak var searchTableView : UITableView!
    @IBOutlet weak var locationTextField:UITextField!
    @IBOutlet weak var checkInDate : UIDatePicker!
    @IBOutlet weak var checkOutDate : UIDatePicker!
    @IBOutlet weak var searchButtonView : UIView!
    weak var searchArray:NSMutableArray!

    override func viewDidLoad() {
        super.viewDidLoad()
        let nextDay = Calendar.current.date(byAdding: .day, value: 1, to: Date())!
        let nextNextDay = Calendar.current.date(byAdding: .day, value: 1, to: nextDay)!
        checkInDate.setDate(nextDay, animated: false)
        checkOutDate.setDate(nextNextDay.addingTimeInterval(1), animated: false)
        checkInDate.minimumDate = Date()
        checkOutDate.minimumDate = nextDay
        locationTextField.text = "San Jose"
        
        searchArray = NSMutableArray()
        
        searchTableView.isHidden=true
        self.view.bringSubviewToFront(searchTableView)
        // Do any additional setup after loading the view.
    }
    @IBAction func searchButtonAction(){
        searchHotels()
    }
    
    func searchHotels(){
        activityIndicatorView.startAnimating()
        let startDate = self.checkInDate.date
        let endDate = self.checkOutDate.date
        let StartDateString = startDate.getFormattedDate(format: "yyyy-MM-dd") // Set output format
        let EndDateString = endDate.getFormattedDate(format: "yyyy-MM-dd") // Set output format
        let url = URL(string: "\(globals.api)getAvailableHotels")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let json: [String: Any] = ["destinationName": "\(String(describing: self.locationTextField.text)))",
                                   "startDate": "\(StartDateString)",
                                   "endDate": "\(EndDateString)"]

        let jsonData = try? JSONSerialization.data(withJSONObject: json)
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
                        DispatchQueue.main.async { () -> Void in
                            self.showToast(message: json["message"] as! String, font: .systemFont(ofSize: 12.0))
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
                
                let searchArray = json
                DispatchQueue.main.async { [self] () -> Void in
                    if searchArray.count>0 {
                        self.searchArray = searchArray as? NSMutableArray
                        searchTableView.isHidden=false
                        searchTableView.reloadData()
                        self.view.bringSubviewToFront(searchTableView)
                        
                    }
                    else{
                        searchTableView.isHidden=true
                        
                    }
                }

            } catch {
                print("error")
            }
            self.activityIndicatorView.stopAnimating()
        }

        task.resume()
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return searchArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
        
        let dataDict = searchArray.object(at: indexPath.row) as! NSDictionary

        cell.hotelName.text = dataDict["name"] as? String
        cell.location.text = dataDict["city"] as? String
        let price = dataDict["basePrice"] as! Int
        cell.price.text = "from $\(String(describing: price))"
        let url = URL(string: dataDict["imageURL"] as! String)!
        cell.hotelImage.af.setImage(withURL: url, cacheKey: "searchTable\(indexPath.row)", placeholderImage: UIImage (named: "tableListImage"), serializer: nil, filter: nil, progress:nil, progressQueue: .global(), imageTransition: .noTransition, runImageTransitionIfCached: false, completion: nil)
        return cell
   
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 170
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "HotelDetailViewController") as! HotelDetailViewController
        
        vc.hotelBasicDetail = searchArray.object(at: indexPath.row) as? NSMutableDictionary
        vc.checkIndate.setDate(self.checkInDate.date, animated: false)
        vc.checkOutDate.setDate(self.checkOutDate.date.addingTimeInterval(1), animated: false)
        navigationController?.pushViewController(vc, animated: true)
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
