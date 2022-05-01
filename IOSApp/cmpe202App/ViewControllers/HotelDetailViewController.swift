//
//  hotelDetailViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/5/22.
//

import UIKit
import NVActivityIndicatorView
import SkeletonView

class HotelDetailViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UITableViewDelegate, UITableViewDataSource,SkeletonTableViewDataSource  {

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
    var checkIn:Date!
    var checkOut:Date!
    var roomData:NSMutableArray!
    var hotelBasicDetail:NSDictionary!
    var amenitiesArray:NSMutableArray!
    var imageArray : NSMutableArray!
    override func viewDidLoad() {
        super.viewDidLoad()
        checkIndate.minimumDate = Date()
        checkOutDate.minimumDate = Calendar.current.date(byAdding: .day, value: 1, to: Date())!
        imageArray = ["basic","standard","luxury"]
        self.checkIndate.setDate(checkIn, animated: true)
        self.checkOutDate.setDate(checkOut, animated: true)
        self.checkIndate.preferredDatePickerStyle = .compact
        self.hangingNameView.frame = CGRect(x: globals.X(view: collectionView)!, y: globals.BOTTOM(view: collectionView)!-40, width: globals.WIDTH(view: collectionView)!, height: globals.HEIGHT(view: hangingNameView)!)
        
        self.hangingShadowView.frame = CGRect(x: globals.X(view: hangingNameView)!+10 , y: globals.BOTTOM(view: collectionView)!-25, width: globals.WIDTH(view: collectionView)!-20, height: globals.HEIGHT(view: hangingNameView)!-20)
        
        self.hangingNameView.layer.masksToBounds=false
        self.hangingNameView.layer.shadowOffset = CGSize(width: 4,
                                          height: 4)
        self.hangingNameView.layer.shadowRadius = 4
        self.hangingNameView.layer.shadowOpacity = 0.5
        
        
        topLabel.text = hotelBasicDetail["name"] as? String
        hotelName.text = hotelBasicDetail["name"] as? String
        hotelLocation.text = hotelBasicDetail["city"] as? String
        let price = hotelBasicDetail["basePrice"] as! Int
        hotelPrice.text = "$\(String(describing: price))"
        let gradient = SkeletonGradient(baseColor: UIColor.silver)
        let animation = SkeletonAnimationBuilder().makeSlidingAnimation(withDirection: .leftRight)
        tableView.showAnimatedGradientSkeleton(usingGradient: gradient, animation: animation)
        self.tableView.showSkeleton()
        getRooms()
        amenitiesArray = NSMutableArray()
        
        if let arr = self.hotelBasicDetail.object(forKey: "amenities") as? NSArray{
            for anItem in arr as! [NSDictionary]{
                let dict = anItem.mutableCopy()
                (dict as AnyObject).setValue(false, forKey: "selected")
                self.amenitiesArray.add(dict)
            }
        }
        
        
        
        
//        // Do any additional setup after loading the view.
    }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == imageCollection {
            return 3
        }
        else{
            return 0
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        if collectionView == imageCollection {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"ImageCollectionViewCell", for:indexPath as IndexPath) as! HomeCollectionViewCell
            let imgView = cell.contentView.viewWithTag(2) as? UIImageView
            imgView?.image = UIImage(named: "\(self.imageArray.object(at: indexPath.row))")
            
            return cell
        }
        else{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier:"ammenitiesCollectionViewCell", for:indexPath as IndexPath)

            return cell
        }
        
    }
    
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(self.roomData != nil){
            if(self.amenitiesArray != nil){
                return self.roomData.count + self.amenitiesArray.count + 1
            }
            else{
                return self.roomData.count
            }
            
        }
        else{
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if(indexPath.row < roomData.count){
            let roomDict = roomData.object(at: indexPath.row) as! NSMutableDictionary
            let cell = tableView.dequeueReusableCell(withIdentifier: "roomCell", for: indexPath)
        
            let imgView = cell.contentView.viewWithTag(1) as? UIImageView
            let typeName = cell.contentView.viewWithTag(2) as? UILabel
            let price = cell.contentView.viewWithTag(3) as? UILabel
            
            typeName?.text = (roomDict.object(forKey: "roomType") as! String)
            let priceStr = roomDict.object(forKey: "price") as! Int
            price?.text = "$\(String(describing: priceStr))"
            let selected = roomDict.object(forKey: "selected") as! Bool
            if(selected){
                imgView?.image=UIImage(named: "checkBox")
            }
            else{
                imgView?.image=UIImage(named: "emptyRing")
            }
            let backgroundView = UIView()
            backgroundView.backgroundColor = UIColor.white
            cell.selectedBackgroundView = backgroundView

            return cell
        }
        if indexPath.row == roomData.count {
            let cell = tableView.dequeueReusableCell(withIdentifier: "staticAmenityCell", for: indexPath)
            let backgroundView = UIView()
            backgroundView.backgroundColor = UIColor.white
            cell.selectedBackgroundView = backgroundView
            return cell
        }
        else{
            let roomDict = amenitiesArray.object(at: indexPath.row - self.roomData.count - 1) as! NSMutableDictionary
            let cell = tableView.dequeueReusableCell(withIdentifier: "amenityCell", for: indexPath)
        
            let imgView = cell.contentView.viewWithTag(1) as? UIImageView
            let typeName = cell.contentView.viewWithTag(2) as? UILabel
            let price = cell.contentView.viewWithTag(3) as? UILabel
            
            typeName?.text = (roomDict.object(forKey: "name") as! String)
            let priceStr = roomDict.object(forKey: "price") as! Int
            price?.text = "+ $\(String(describing: priceStr))"
            let selected = roomDict.object(forKey: "selected") as! Bool
            if(selected){
                imgView?.image=UIImage(named: "checkBox")
            }
            else{
                imgView?.image=UIImage(named: "emptyRing")
            }
            let backgroundView = UIView()
            backgroundView.backgroundColor = UIColor.white
            cell.selectedBackgroundView = backgroundView

            return cell
        }
        

    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(self.roomData != nil && indexPath.row < roomData.count  ){
            return 45
        }
        else if(self.roomData != nil && indexPath.row == roomData.count){
            if(self.amenitiesArray != nil && self.amenitiesArray.count>0 ){
                return 40
            }
            else{ return 0}
        }
        else{
            return 45
        }
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if(indexPath.row < roomData.count){
            let dict = roomData.object(at: indexPath.row) as! NSMutableDictionary
            var selected = dict.object(forKey: "selected") as! Bool
            if(selected){
                selected = false
            }
            else{
                selected = true
            }
            dict.setValue(selected, forKey: "selected")
            roomData.replaceObject(at: indexPath.row, with: dict)
        }
        else if (indexPath.row == roomData.count){
            
        }
        else{
            let dict = amenitiesArray.object(at: indexPath.row - self.roomData.count - 1) as! NSMutableDictionary
            var selected = dict.object(forKey: "selected") as! Bool
            if(selected){
                selected = false
            }
            else{
                selected = true
            }
            dict.setValue(selected, forKey: "selected")
            amenitiesArray.replaceObject(at: indexPath.row-self.roomData.count-1, with: dict)
        }
        self.tableView.reloadRows(at:[indexPath], with: .none)
        
    }
    
    func getRooms(){
        let url = URL(string: "\(globals.api)getAvailableRooms")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let startDate = self.checkIndate.date
        let endDate = self.checkOutDate.date
        let StartDateString = startDate.getFormattedDate(format: "yyyy-MM-dd") // Set output format
        let EndDateString = endDate.getFormattedDate(format: "yyyy-MM-dd") // Set output format

        let json: [String: Any] = ["hotelId": "\(hotelBasicDetail["id"]!)",
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
                let roomArray = NSMutableArray()
                for anItem in json as! [NSDictionary]{
                    let dict = anItem.mutableCopy()
                    (dict as AnyObject).setValue(false, forKey: "selected")
                    roomArray.add(dict)
                }
                print(roomArray)
                self.roomData = (roomArray.mutableCopy() as! NSMutableArray)
                DispatchQueue.main.async { [self] () -> Void in
                    tableView.reloadData()
                    self.tableView.hideSkeleton()

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
    func collectionSkeletonView(_ skeletonView: UITableView, cellIdentifierForRowAt indexPath: IndexPath) -> ReusableCellIdentifier {
        return "roomCell"
    }
    func collectionSkeletonView(_ skeletonView: UITableView, numberOfRowsInSection section: Int) -> Int{
        return 4
    }
    
    func collectionSkeletonView(_ skeletonView: UITableView, skeletonCellForRowAt indexPath: IndexPath) -> UITableViewCell? {
        let cell = skeletonView.dequeueReusableCell(withIdentifier: "roomCell", for: indexPath) as? UITableViewCell
        return cell

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
