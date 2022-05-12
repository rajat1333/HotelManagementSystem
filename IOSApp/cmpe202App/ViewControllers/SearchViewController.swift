//
//  SearchViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/28/22.
//

import UIKit
import NVActivityIndicatorView
class SearchViewController: UIViewController,UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate {
    @IBOutlet weak var activityIndicatorView:NVActivityIndicatorView!
    @IBOutlet weak var searchTableView : UITableView!
    @IBOutlet weak var locationTextField:UITextField!
    @IBOutlet weak var checkInDate : UIDatePicker!
    @IBOutlet weak var checkOutDate : UIDatePicker!
    @IBOutlet weak var searchButtonView : UIView!
    @IBOutlet weak var searchView : UIView!
    @IBOutlet weak var locView : UIView!
    @IBOutlet weak var dateView : UIView!
    @IBOutlet weak var locationLabel : UILabel!
    @IBOutlet weak var dateLabel : UILabel!



    var searchArray:NSArray!

    override func viewDidLoad() {
        super.viewDidLoad()
        let nextDay = Calendar.current.date(byAdding: .day, value: 1, to: Date())!
        let nextNextDay = Calendar.current.date(byAdding: .day, value: 1, to: nextDay)!
        checkInDate.setDate(nextDay, animated: false)
        checkOutDate.setDate(nextNextDay.addingTimeInterval(1), animated: false)
        checkInDate.minimumDate = Date()
        checkOutDate.minimumDate = nextDay
        locationTextField.text = "San Jose"
        checkInDate.setValue(UIColor.white, forKeyPath: "textColor")
        checkInDate.datePickerMode = .date
        checkOutDate.setValue(UIColor.white, forKeyPath: "textColor")
        checkOutDate.datePickerMode = .date
        
        self.checkInDate.addTarget(self, action: #selector(dateChangedAction), for: .valueChanged)
        self.checkOutDate.addTarget(self, action: #selector(dateChangedAction), for: .valueChanged)
        //checkInDate.addTarget(self, action: #selector(checkInChanged), for: .valueChanged)
        searchArray = NSArray()
        searchTableView.dataSource=self
        searchTableView.delegate=self
        searchTableView.register(UINib(nibName: "HomeTableViewCell", bundle: nil), forCellReuseIdentifier: "HomeTableViewCell")
        searchView.isHidden=true
        self.view.bringSubviewToFront(searchView)
        // Do any additional setup after loading the view.
    }
    @IBAction func searchButtonAction(){
        searchHotels()
    }
    @objc func dateChangedAction(){
        let date = self.checkInDate.date as Date
        self.checkOutDate.minimumDate = Calendar.current.date(byAdding: .day, value: 1, to: date)!
    }
    func searchHotels(){
        let startDate = self.checkInDate.date
        let endDate = self.checkOutDate.date
        let StartDateString = startDate.getFormattedDate(format: "yyyy-MM-dd") // Set output format
        let EndDateString = endDate.getFormattedDate(format: "yyyy-MM-dd") // Set output format
        let url = URL(string: "\(globals.api)getAvailableHotels")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let json: [String: Any] = ["destinationName": "\(String(describing: self.locationTextField.text!.lowercased()))",
                                   "startDate": "\(StartDateString)",
                                   "endDate": "\(EndDateString)"]

        let jsonData = try? JSONSerialization.data(withJSONObject: json)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        request.httpMethod = "POST"
        request.httpBody = jsonData
        self.activityIndicatorView.startAnimating()
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data,
            let response = response as? HTTPURLResponse,
            error == nil else {
                // check for fundamental networking error
                print("error", error ?? "Unknown error")
                self.activityIndicatorView.stopAnimating()
                return
            }

            guard (200 ... 299) ~= response.statusCode else {                    // check for http errors
                print("statusCode should be 2xx, but is \(response.statusCode)")
                print("response = \(response)")
                if response.statusCode == 400 {
                    do {
                        let json = try JSONSerialization.jsonObject(with: data) as! Dictionary<String, AnyObject>
                            print(json)
                        
                        self.activityIndicatorView.stopAnimating()
                        DispatchQueue.main.async { () -> Void in
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
                let json = try JSONSerialization.jsonObject(with: data) as! NSArray
                    print(json)
                DispatchQueue.main.async { [self] () -> Void in
                    self.searchArray = NSArray.init(array: json)
                    if searchArray.count>0 {
                        searchView.isHidden=false
                        self.view.bringSubviewToFront(searchView)
                        locationLabel.text = locationTextField.text
                        let startDate = self.checkInDate.date
                        let endDate = self.checkOutDate.date
                        let StartDateString = startDate.getFormattedDate(format: "MMM dd") // Set output format
                        let EndDateString = endDate.getFormattedDate(format: "MMMM dd")
                        let dateString = "\(StartDateString) - \(EndDateString)"
                        dateLabel.text = dateString
                        self.activityIndicatorView.stopAnimating()
                        searchTableView.reloadData()
                    }
                }
                self.activityIndicatorView.stopAnimating()

            }

            catch {
                print("error")
            }
            self.activityIndicatorView.stopAnimating()
        }

        task.resume()
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(searchArray.count>0){
            return searchArray.count
        }
        else{
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "HomeTableViewCell", for: indexPath) as! HomeTableViewCell
        if(self.searchArray.count>0){
            let dataDict = self.searchArray.object(at: indexPath.row) as! NSDictionary

            cell.hotelName.text = dataDict["name"] as? String
            cell.location.text = dataDict["city"] as? String
            let price = dataDict["basePrice"] as! Float
            cell.price.text = "from $\(String(describing: price))"
            
            if let value = (dataDict["imageURL"] as? String) {
                let url = URL(string: dataDict["imageURL"] as! String)!
                cell.hotelImage.af.setImage(withURL: url, cacheKey: "searchTable\(indexPath.row)", placeholderImage: UIImage (named: "tableListImage"), serializer: nil, filter: nil, progress:nil, progressQueue: .global(), imageTransition: .noTransition, runImageTransitionIfCached: false, completion: nil)
            }
            else{
                cell.hotelImage.image = UIImage (named: "tableListImage")
            }
            
            if(indexPath.row==0 || indexPath.row==1){
                cell.ratingImage.image = UIImage(named: "5Star")
            }
            else{
                cell.ratingImage.image = UIImage(named: "4_5Star")
            }
        }
        
        return cell
   
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 170
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "HotelDetailViewController") as! HotelDetailViewController
        
        vc.hotelBasicDetail = (searchArray.object(at: indexPath.row) as! NSDictionary)
        let cI = self.checkInDate.date
        let cO = self.checkOutDate.date
        vc.checkIn = cI
        vc.checkOut = cO
        navigationController?.pushViewController(vc, animated: true)
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()

        return false
    }
    @IBAction func cancelFilterAction(){
        searchView.isHidden = true
        self.activityIndicatorView.stopAnimating()
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
