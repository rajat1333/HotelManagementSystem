//
//  hotelDetailViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/5/22.
//

import UIKit
import NVActivityIndicatorView

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
    
    @IBOutlet weak var hotelName: UILabel!
    @IBOutlet weak var hotelLocation: UILabel!
    @IBOutlet weak var hotelPrice: UILabel!
    @IBOutlet weak var checkIndate: UIDatePicker!
    @IBOutlet weak var checkOutDate: UIDatePicker!

    var hotelBasicDetail:NSMutableDictionary!
    override func viewDidLoad() {
        super.viewDidLoad()
        checkIndate.minimumDate = Date()
        checkOutDate.minimumDate = Calendar.current.date(byAdding: .day, value: 1, to: Date())!
        self.hangingNameView.frame = CGRect(x: globals.X(view: collectionView)!, y: globals.BOTTOM(view: collectionView)!-45, width: globals.WIDTH(view: collectionView)!, height: globals.HEIGHT(view: hangingNameView)!)
        
        self.hangingShadowView.frame = CGRect(x: globals.X(view: hangingNameView)!+10 , y: globals.BOTTOM(view: collectionView)!-35, width: globals.WIDTH(view: collectionView)!-20, height: globals.HEIGHT(view: hangingNameView)!-20)
        
        self.hangingNameView.layer.masksToBounds=false
        self.hangingNameView.layer.shadowOffset = CGSize(width: 4,
                                          height: 4)
        self.hangingNameView.layer.shadowRadius = 4
        self.hangingNameView.layer.shadowOpacity = 0.5
        
        hotelName.text = hotelBasicDetail["name"] as? String
        hotelLocation.text = hotelBasicDetail["city"] as? String
        hotelPrice.text = hotelBasicDetail["basePrice"] as? String
        
        
        
        
        
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
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"ammenitiesCollectionViewCell", for:indexPath as IndexPath)

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
    
    func getRooms(){
        let url = URL(string: "\(globals.api)getAvailableRooms")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let startDate = self.checkIndate.date
        let endDate = self.checkOutDate.date
        let StartDateString = startDate.getFormattedDate(format: "yyyy-MM-dd") // Set output format
        let EndDateString = endDate.getFormattedDate(format: "yyyy-MM-dd") // Set output format

        let json: [String: Any] = ["hotelId": "\(String(describing: hotelBasicDetail["id"] as? String))",
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
                
                let roomArray = json
                DispatchQueue.main.async { [self] () -> Void in
                    
                }

            } catch {
                print("error")
            }
        }

        task.resume()
    }
    @IBAction func backButtonAction(){
        self.navigationController?.popViewController(animated: true)
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
