//
//  AddHotelViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 5/10/22.
//

import UIKit
import NVActivityIndicatorView

class AddHotelViewController: UIViewController, UITableViewDelegate,UITableViewDataSource, UITextFieldDelegate {
    
    @IBOutlet weak var indicator:NVActivityIndicatorView!

    @IBOutlet weak var hotelName:UITextField!
    @IBOutlet weak var hotelLocation:UITextField!
    @IBOutlet weak var buttonView:UIView!
    @IBOutlet weak var basicImg:UIImageView!
    @IBOutlet weak var standardImg:UIImageView!
    @IBOutlet weak var luxuryImg:UIImageView!
    @IBOutlet weak var basicPrice:UITextField!
    @IBOutlet weak var standardPrice:UITextField!
    @IBOutlet weak var luxuryPrice:UITextField!
    @IBOutlet weak var RoomButtonView:UIView!
    @IBOutlet weak var RoomView:UIView!
    @IBOutlet weak var tableView:UITableView!
    var amenityData:NSMutableArray!
    var hotelId : Int!
    var basic : Bool!
    var standard : Bool!
    var luxury : Bool!
    var total : Int!
    var done : Int!
    var dispatchGroup : DispatchGroup! = nil
    var dispatchQueue : DispatchQueue! = nil
    var dispatchSemaphore : DispatchSemaphore! = nil
    override func viewDidLoad() {
        super.viewDidLoad()
        self.RoomView.isHidden=true
        basic = false
        standard = false
        luxury = false
        getAmenitiesAPI()
        tableView.delegate=self
        tableView.dataSource=self
        self.amenityData=NSMutableArray()
        total=0
        done=0
        // Do any additional setup after loading the view.
    }
    func getAmenitiesAPI(){
        self.indicator.startAnimating()
        let url = URL(string: "\(globals.api)readamenity")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        request.httpMethod = "GET"
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data,
            let response = response as? HTTPURLResponse,
            error == nil else {
                // check for fundamental networking error
               

                print("error", error ?? "Unknown error")
                self.indicator.stopAnimating()

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
                            self.indicator.stopAnimating()

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
                self.amenityData = (roomArray.mutableCopy() as! NSMutableArray)
                DispatchQueue.main.async { [self] () -> Void in
                    if(roomArray.count>0){
                        self.tableView.reloadData()
                        self.indicator.stopAnimating()

                    }
                    else{
                        self.indicator.stopAnimating()
                    }
                    

                }

            } catch {
                print("error")
            }
        }

        task.resume()
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if self.amenityData != nil {
            return self.amenityData.count
        }
        else{
            return 0;
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "addamenitycell", for: indexPath) as! UITableViewCell
        
        let img = cell.viewWithTag(1) as! UIImageView?
        let lbl = cell.viewWithTag(2) as! UILabel?
        
        let tempDict = self.amenityData.object(at: indexPath.row) as! NSMutableDictionary
        let name = tempDict.value(forKey: "name") as! String
        let selected = tempDict.value(forKey: "selected") as! Bool
        
        lbl?.text = name
        if(selected){
            img?.image=UIImage(named: "checkBox")
        }
        else{
            img?.image=UIImage(named: "emptyRing")
        }
        
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let dict = self.amenityData.object(at: indexPath.row) as! NSMutableDictionary
        var selected = dict.object(forKey: "selected") as! Bool
        if(selected){
            selected = false
        }
        else{
            selected = true
        }
        dict.setValue(selected, forKey: "selected")
        self.amenityData.replaceObject(at: indexPath.row, with: dict)
        self.tableView.reloadRows(at:[indexPath], with: .none)

    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 73
    }
    @IBAction func createHotelAction(){
        if(self.hotelName.text == ""){
            self.showToast(message: "Add Hotel Name", font: .systemFont(ofSize: 12.0))
        }
        else if(self.hotelLocation.text == ""){
            self.showToast(message: "Add Hotel Location", font: .systemFont(ofSize: 12.0))
        }
        else{
            createHotelAPI()
        }
        
    }
    func createHotelAPI(){
        self.indicator.startAnimating()

        let url = URL(string: "\(globals.api)createhotel")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let amenityArr = NSMutableArray()
        for dict in self.amenityData as! [NSDictionary] {
            if((dict.object(forKey: "selected")) != nil && dict.value(forKey: "selected") as! Bool == true){
                let AmenityDict = NSMutableDictionary()
                AmenityDict.setValue(dict.object(forKey: "id"), forKey: "id" )
                amenityArr.add(AmenityDict)
            }
        }
        let dataDict = NSMutableDictionary()
        dataDict.setValue(amenityArr, forKey: "amenities")
        dataDict.setValue(self.hotelName.text!, forKey: "name")
        dataDict.setValue(self.hotelLocation.text!, forKey: "city")
        dataDict.setValue(3, forKey: "maxFloor")
        

        let jsonData = try? JSONSerialization.data(withJSONObject: dataDict)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        request.httpMethod = "POST"
        request.httpBody = jsonData

        let task = URLSession.shared.dataTask(with: request) { [self] data, response, error in
            guard let data = data,
            let response = response as? HTTPURLResponse,
            error == nil else {
                // check for fundamental networking error
               
                self.indicator.stopAnimating()
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
                            self.indicator.stopAnimating()

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
                
                DispatchQueue.main.async { [self] () -> Void in
                    self.showToast(message: "Created Hotel", font: .systemFont(ofSize: 12.0))
                    self.indicator.stopAnimating()
                    self.hotelId = json.value(forKey: "id") as! Int
                    self.RoomView.isHidden=false
                    
                }

            } catch {
                print("error")
            }
            
        }

        task.resume()

    }

    
    @IBAction func basicButtonAction(){
        if(basic){
            basic = false
            basicImg.image = UIImage(named: "emptyRing")
        }
        else{
            basic = true
            basicImg.image = UIImage(named: "checkBox")

        }
    }
    @IBAction func standardButtonAction(){
        if(standard){
            standard = false
            standardImg.image = UIImage(named: "emptyRing")
        }
        else{
            standard = true
            standardImg.image = UIImage(named: "checkBox")

        }
    }
    @IBAction func luxuryButtonAction(){
        if(luxury){
            luxury = false
            luxuryImg.image = UIImage(named: "emptyRing")
        }
        else{
            luxury = true
            luxuryImg.image = UIImage(named: "checkBox")

        }
    }
    @IBAction func backButtonAction(){
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func addRoomAction(){
        if(basic && basicPrice.text==""){
            self.showToast(message: "Add Price for Basic", font: .systemFont(ofSize: 12))
        }
        else if(standard && standardPrice.text==""){
            self.showToast(message: "Add Price for Standard", font: .systemFont(ofSize: 12))

        }
        else if(luxury && luxuryPrice.text==""){
            self.showToast(message: "Add Price for Luxury", font: .systemFont(ofSize: 12))

        }
        else{
            let roomArr = NSMutableArray()
            if(basic){
                let dataDict = NSMutableDictionary()
                dataDict.setValue("101", forKey: "name")
                dataDict.setValue(1, forKey: "floor")
                dataDict.setValue(self.hotelId, forKey: "hotelId")
                dataDict.setValue("BASIC", forKey: "roomType")
                dataDict.setValue(self.basicPrice.text, forKey: "price")
                
                roomArr.add(dataDict)
            }
            if(standard){
                let dataDict = NSMutableDictionary()
                dataDict.setValue("201", forKey: "name")
                dataDict.setValue(1, forKey: "floor")
                dataDict.setValue(self.hotelId, forKey: "hotelId")
                dataDict.setValue("STANDARD", forKey: "roomType")
                dataDict.setValue(self.standardPrice.text, forKey: "price")
                
                roomArr.add(dataDict)
            }
            if(luxury){
                let dataDict = NSMutableDictionary()
                dataDict.setValue("301", forKey: "name")
                dataDict.setValue(1, forKey: "floor")
                dataDict.setValue(self.hotelId, forKey: "hotelId")
                dataDict.setValue("LUXURY", forKey: "roomType")
                dataDict.setValue(self.luxuryPrice.text, forKey: "price")
                
                roomArr.add(dataDict)
            }
            total = roomArr.count
            dispatchGroup = DispatchGroup()
            dispatchQueue = DispatchQueue(label: "any-label-name")
            dispatchSemaphore = DispatchSemaphore(value: 0)
            self.indicator.startAnimating()

            dispatchQueue.async {
                for dict in roomArr as! [NSMutableDictionary]{
                    self.dispatchGroup.enter()
                    self.createRoomAPI(dict: dict)
                    self.dispatchSemaphore.wait()
                }
            }
            dispatchGroup.notify(queue: dispatchQueue) {
                DispatchQueue.main.async {
                    self.indicator.stopAnimating()
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                            self.navigationController?.popViewController(animated: true)
                    }
                }
            }
        }
    }
    func createRoomAPI(dict:NSMutableDictionary){
        self.indicator.startAnimating()
        let url = URL(string: "\(globals.api)createroom")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let jsonData = try? JSONSerialization.data(withJSONObject:dict)
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        request.httpMethod = "POST"
        request.httpBody = jsonData

        let task = URLSession.shared.dataTask(with: request) { [self] data, response, error in
            guard let data = data,
            let response = response as? HTTPURLResponse,
            error == nil else {
                // check for fundamental networking error
               

                print("error", error ?? "Unknown error")
                self.indicator.stopAnimating()

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
                            self.indicator.stopAnimating()

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
                self.showToast(message: "Added Room" , font: .systemFont(ofSize: 12.0))
                self.dispatchSemaphore.signal()
                self.dispatchGroup.leave()
//                DispatchQueue.main.async { () -> Void in
//
//                }

            } catch {
                print("error")
            }
            
        }

        task.resume()

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
