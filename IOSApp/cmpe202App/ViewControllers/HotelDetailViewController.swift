//
//  hotelDetailViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/5/22.
//

import UIKit
import NVActivityIndicatorView
import SkeletonView

class HotelDetailViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UITableViewDelegate, UITableViewDataSource,SkeletonTableViewDataSource, UITextFieldDelegate  {

    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var hangingNameView: UIView!
    @IBOutlet weak var buttonView: UIView!
    @IBOutlet weak var refreshButtonView: UIView!

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
    @IBOutlet weak var activityIndicatorView : NVActivityIndicatorView!
    @IBOutlet weak var receiptView: UIView!
    @IBOutlet weak var cardView: UIView!
    @IBOutlet weak var pointsView: UIView!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var taxLabel: UILabel!
    @IBOutlet weak var cardNumber: UITextField!
    @IBOutlet weak var cardName: UITextField!
    @IBOutlet weak var cvv: UITextField!
    @IBOutlet weak var cardMonth: UITextField!
    @IBOutlet weak var cardYear: UITextField!
    @IBOutlet weak var payView: UIView!
    @IBOutlet weak var cancelpaymentView: UIView!
    @IBOutlet weak var rewardPointsLabel: UILabel!
    @IBOutlet weak var checkImage: UIImageView!
    @IBOutlet weak var pointsShadowView: UIView!
    @IBOutlet weak var doneView: UIView!
    @IBOutlet weak var noDataView: UIView!
    @IBOutlet weak var disclaimerLabel: UILabel!



    var useRewards:Bool!
    var checkIn:Date!
    var checkOut:Date!
    var roomData:NSMutableArray!
    var hotelBasicDetail:NSDictionary!
    var amenitiesArray:NSMutableArray!
    var imageArray : NSMutableArray!
    var bookingResponseArray : NSMutableDictionary!

    var dateChanged : Bool!
    override func viewDidLoad() {
        super.viewDidLoad()
        doneView.isHidden=true
        noDataView.isHidden=true
        useRewards = false
        receiptView.isHidden=true
        dateChanged = false
        checkIndate.minimumDate = Date()
        
        imageArray = ["basic","standard","luxury"]
        self.checkIndate.setDate(checkIn, animated: true)
        self.checkOutDate.setDate(checkOut, animated: true)
        self.checkIndate.preferredDatePickerStyle = .compact
        let date = self.checkIndate.date
        checkOutDate.minimumDate = Calendar.current.date(byAdding: .day, value: 1, to: date)!
        checkOutDate.maximumDate = Calendar.current.date(byAdding: .day, value: 7, to: date)!
        self.hangingNameView.frame = CGRect(x: globals.X(view: collectionView)!, y: globals.BOTTOM(view: collectionView)!-40, width: globals.WIDTH(view: collectionView)!, height: globals.HEIGHT(view: hangingNameView)!)
        
        self.hangingShadowView.frame = CGRect(x: globals.X(view: hangingNameView)!+10 , y: globals.BOTTOM(view: collectionView)!-25, width: globals.WIDTH(view: collectionView)!-20, height: globals.HEIGHT(view: hangingNameView)!-20)
        
        self.hangingNameView.layer.masksToBounds=false
        self.hangingNameView.layer.shadowOffset = CGSize(width: 4,
                                          height: 4)
        self.hangingNameView.layer.shadowRadius = 4
        self.hangingNameView.layer.shadowOpacity = 0.5
        self.checkIndate.addTarget(self, action: #selector(dateChangedAction), for: .valueChanged)
        self.checkOutDate.addTarget(self, action: #selector(dateChangedAction), for: .valueChanged)
        topLabel.text = hotelBasicDetail["name"] as? String
        hotelName.text = hotelBasicDetail["name"] as? String
        hotelLocation.text = hotelBasicDetail["city"] as? String
        let price = hotelBasicDetail["basePrice"] as! Float
        hotelPrice.text = "$\(String(describing: price))"
        let gradient = SkeletonGradient(baseColor: UIColor.silver)
        let animation = SkeletonAnimationBuilder().makeSlidingAnimation(withDirection: .leftRight)
        tableView.showAnimatedGradientSkeleton(usingGradient: gradient, animation: animation)
        self.tableView.showSkeleton()
        getRoomsAPI()
        amenitiesArray = NSMutableArray()
        
        if let arr = self.hotelBasicDetail.object(forKey: "amenities") as? NSArray{
            for anItem in arr as! [NSDictionary]{
                let dict = anItem.mutableCopy()
                (dict as AnyObject).setValue(false, forKey: "selected")
                self.amenitiesArray.add(dict)
            }
        }
        
        
        buttonView.isHidden=true
        refreshButtonView.isHidden=true
        
//        // Do any additional setup after loading the view.
    }
    @objc func dateChangedAction(){
        let date = self.checkIndate.date as Date
        checkOutDate.minimumDate = Calendar.current.date(byAdding: .day, value: 1, to: date)!
        checkOutDate.maximumDate = Calendar.current.date(byAdding: .day, value: 7, to: date)!

        dateChanged = true
        buttonView.isHidden=true
        self.noDataView.isHidden = true
        self.tableView.isHidden=true
        refreshButtonView.isHidden=false
        if(roomData != nil){
            roomData.removeAllObjects()
            tableView.reloadData()
        }
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
            let priceStr = roomDict.object(forKey: "price") as! Float
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
    @IBAction func getRoomsAgain(){
        self.tableView.showSkeleton()
        self.tableView.isHidden=false
        getRoomsAPI()
    }
    @IBAction func bookHotel(){
        if(roomData.count > 0){
            let bookingDict = NSMutableDictionary()
            let roomArr = NSMutableArray()
            let amenityArr = NSMutableArray()
            for dict in self.roomData as! [NSDictionary] {
                if((dict.object(forKey: "selected")) != nil && dict.value(forKey: "selected") as! Bool == true){
                    let roomDict = NSMutableDictionary()
                    roomDict.setValue(dict.object(forKey: "id"), forKey: "id" )
                    roomDict.setValue(dict.object(forKey: "price"), forKey: "price")
                    roomArr.add(roomDict)
                }
            }
            if(roomArr.count <= 0){
                self.showToast(message: "No Room Selected", font: .systemFont(ofSize: 12.0))
                
            }
            else{
                for dict in self.amenitiesArray as! [NSDictionary] {
                    if((dict.object(forKey: "selected")) != nil && dict.value(forKey: "selected") as! Bool == true){
                        let amenityDict = NSMutableDictionary()
                        amenityDict.setValue(dict.object(forKey: "id"), forKey: "id" )
                        amenityArr.add(amenityDict)
                    }
                }
                let preferences = UserDefaults.standard
                let key = "user"
                let userDict = preferences.object(forKey: key) as! NSDictionary
                let email = userDict.value(forKey: "email")
                let hotelId = hotelBasicDetail.value(forKey: "id")
                let bookfrom = self.checkIndate.date
                let bookto = self.checkOutDate.date
                
                bookingDict.setValue(roomArr, forKey: "rooms")
                bookingDict.setValue(amenityArr, forKey: "amenities")
                bookingDict.setValue(hotelId, forKey: "hotelId")
                bookingDict.setValue(email, forKey: "customerEmail")
                bookingDict.setValue(globals.dateToString(date:bookfrom), forKey: "bookFrom")
                bookingDict.setValue(globals.dateToString(date:bookto), forKey: "bookTo")
                
                print(bookingDict)
                createBookingAPI(bookingDict: bookingDict)


                
            }
        }
        
    }
   
    @IBAction func payButtonAction(){
        let dict = NSMutableDictionary()
        let billObject = self.bookingResponseArray.object(forKey: "bill") as! NSDictionary
        dict.setValue(billObject.value(forKey:"id"), forKey: "id")
        dict.setValue(self.bookingResponseArray.value(forKey:"id"), forKey: "bookingId")
        
        dict.setValue(billObject.value(forKey:"totalAmount"), forKey: "totalAmount")
        dict.setValue(billObject.value(forKey:"taxAmount"), forKey: "taxAmount")
        dict.setValue(billObject.value(forKey:"paymentMode"), forKey: "paymentMode")
        dict.setValue(billObject.value(forKey:"paymentStatus"), forKey: "paymentStatus")
        if(useRewards){
            dict.setValue(billObject.value(forKey:"amountPayableByRewardPoints"), forKey: "rewardPointsUsed")

        }
        else{
            dict.setValue(0, forKey: "rewardPointsUsed")

        }
        //dict.setValue(billObject.value(forKey:"id"), forKey: "rewardPointsEarned")
        dict.setValue(billObject.value(forKey:"discountAmount"), forKey: "discountAmount")
        dict.setValue(billObject.value(forKey:"totalPayableAmount"), forKey: "totalPayableAmount")
        dict.setValue(billObject.value(forKey:"amountPayableByRewardPoints"), forKey: "amountPayableByRewardPoints")
        
        if(self.cardName.text == "" || self.cardNumber.text == "" || self.cardYear.text == "" || self.cardMonth.text == "" || self.cvv.text == "" ){
            self.showToast(message: "Enter Card Details", font: .systemFont(ofSize: 12.0))
        }
        else{
            makePaymentAPI(dict: dict)
        }
        
        
    }
    
    @IBAction func checkRewardsAction(){
        if(useRewards){
            useRewards = false
            self.checkImage.image = UIImage(named: "emptyRing")
            let billObject = self.bookingResponseArray.object(forKey: "bill") as! NSDictionary
            self.priceLabel.text = "$ \((billObject.value(forKey: "totalPayableAmount") as! Double))"
        }
        else{
            useRewards = true
            self.checkImage.image = UIImage(named: "checkBox")
            let billObject = self.bookingResponseArray.object(forKey: "bill") as! NSDictionary
            let price = (billObject.value(forKey: "totalPayableAmount") as! Double)
            let rewards = (billObject.value(forKey: "amountPayableByRewardPoints") as! Double)
            if(rewards>price){
                self.priceLabel.text = "$ \(Float(price-rewards))"
            }
            self.priceLabel.text = "$ \(Float(price-rewards))"
        }
        
    }
    @IBAction func cancelPaymentAction(){
        
        self.receiptView.isHidden=true
        let bookid = self.bookingResponseArray.object(forKey: "id") as! Int
        let dict = NSMutableDictionary()
        dict.setValue(bookid, forKey: "id")
        cancelBookingAPI(dict: dict)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        // Try to find next responder
        let nextResponder = textField.superview?.viewWithTag(nextTag) as UIResponder?

        if nextResponder != nil {
            // Found next responder, so set it
            nextResponder?.becomeFirstResponder()
        } else {
            // Not found, so remove keyboard
            textField.resignFirstResponder()
        }

        return false
    }
    
    
    /** Model Code **/
    func getRoomsAPI(){
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
                            if((json["message"] as! String) == "No rooms available"){
                                self.noDataView.isHidden=false
                                self.refreshButtonView.isHidden=true
                                self.buttonView.isHidden=true
                            }
                            else{
                                self.showToast(message: json["message"] as! String, font: .systemFont(ofSize: 12.0))
                            }
                            self.tableView.isHidden=true
                            self.tableView.hideSkeleton()

                            
                            
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
                    if(roomArray.count>0){
                        tableView.reloadData()
                        self.tableView.hideSkeleton()
                        buttonView.isHidden=false
                        refreshButtonView.isHidden=true
                    }
                    else{
                        self.tableView.isHidden=true
                        self.noDataView.isHidden=false
                        refreshButtonView.isHidden=true
                        buttonView.isHidden=true
                    }
                    

                }

            } catch {
                print("error")
            }
        }

        task.resume()
    }
    func createBookingAPI(bookingDict: NSMutableDictionary){
        activityIndicatorView.startAnimating()
        let url = URL(string: "\(globals.api)createbooking")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        
        let jsonData = try? JSONSerialization.data(withJSONObject: bookingDict)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        request.httpMethod = "POST"
        request.httpBody = jsonData

        let task = URLSession.shared.dataTask(with: request) { [self] data, response, error in
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
                            self.activityIndicatorView.stopAnimating()
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
                let json = try JSONSerialization.jsonObject(with: data) as! NSDictionary
                print(json)
                self.bookingResponseArray = json.mutableCopy() as! NSMutableDictionary
                DispatchQueue.main.async { () -> Void in
                    self.activityIndicatorView.stopAnimating()
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.4) {
                        self.receiptView.isHidden=false
                        let billObject = self.bookingResponseArray.object(forKey: "bill") as! NSDictionary
                        self.priceLabel.text = "$ \(Float(billObject.value(forKey: "totalPayableAmount") as! Double))"
                        let tax = (billObject.value(forKey: "taxAmount") as! Double)
                        self.taxLabel.text = "Includes Tax $\(Float(tax))"
                        let reward = (billObject.value(forKey: "amountPayableByRewardPoints") as! Double)
                        if(reward == 0.0){
                            self.pointsView.isHidden=true
                            self.pointsShadowView.isHidden=true
                            self.disclaimerLabel.isHidden=true
                        }
                        else{
                            self.disclaimerLabel.isHidden=false
                            self.pointsView.isHidden=false
                            self.pointsShadowView.isHidden=false
                        }
                        self.rewardPointsLabel.text = "Use Reward Points : \(Float(reward))"
                        self.checkImage.image = UIImage(named: "emptyRing")
                        self.useRewards = false
                        //self.navigationController?.popViewController(animated: true)
                    }
                    
                }

            } catch {
                print("error")
            }
            
        }

        task.resume()

    }
    func cancelBookingAPI(dict:NSMutableDictionary){
        activityIndicatorView.startAnimating()
        self.tableView.isHidden=true
        let url = URL(string: "\(globals.api)deletebooking")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        
        let jsonData = try? JSONSerialization.data(withJSONObject: dict)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        request.httpMethod = "DELETE"
        request.httpBody = jsonData

        let task = URLSession.shared.dataTask(with: request) { [self] data, response, error in
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
                        self.activityIndicatorView.stopAnimating()
                        DispatchQueue.main.async { () -> Void in
                            self.activityIndicatorView.stopAnimating()
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
                
                let json = try JSONSerialization.jsonObject(with: data) as! NSDictionary
                print(json)
                
                DispatchQueue.main.async { () -> Void in
                    self.activityIndicatorView.stopAnimating()
                    self.tableView.isHidden=false
                }


            } catch {
                print("error")
            }
            
        }

        task.resume()

    }
    func makePaymentAPI(dict:NSMutableDictionary){
        activityIndicatorView.startAnimating()
        let url = URL(string: "\(globals.api)makePayment")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        
        let jsonData = try? JSONSerialization.data(withJSONObject: dict)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        request.httpMethod = "POST"
        request.httpBody = jsonData

        let task = URLSession.shared.dataTask(with: request) { [self] data, response, error in
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
                            self.activityIndicatorView.stopAnimating()
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
                let json = try JSONSerialization.jsonObject(with: data) as! NSDictionary
                print(json)
                DispatchQueue.main.async { () -> Void in
                    self.activityIndicatorView.stopAnimating()
                    
                    self.doneView.isHidden = false
//                    let imageData = try? Data(contentsOf: Bundle.main.url(forResource: "payment-success", withExtension: "gif")!)
//
//                    let advTimeGif = UIImage.gifImageWithData(imageData!)
                    let jeremyGif = UIImage.gifImageWithName("payment-success")
                    let imgView = UIImageView(image: jeremyGif)
                    self.doneView.addSubview(imgView)
                    imgView.frame = CGRect(x: 107, y: 448-95, width:
                    200, height: 190)
                    imgView.contentMode = .scaleAspectFill
                    
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                        self.showToast(message: "Booking Successful", font: .systemFont(ofSize: 12.0))
                    }
                    DispatchQueue.main.asyncAfter(deadline: .now() + 4.0) {
                        self.navigationController?.popViewController(animated: true)
                    }
                    
                }

            } catch {
                print("error")
            }
            
        }

        task.resume()

    }
}
