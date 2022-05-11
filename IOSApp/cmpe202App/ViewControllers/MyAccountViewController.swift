//
//  MyAccountViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/28/22.
//

import UIKit

class MyAccountViewController: UIViewController {
    @IBOutlet weak var logoutView : UIView!
    @IBOutlet weak var emailLabel : UILabel!
    @IBOutlet weak var rewardsLabel : UILabel!
    @IBOutlet weak var addHotelView : UIView!


    override func viewDidLoad() {
        super.viewDidLoad()
        

        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        let preferences = UserDefaults.standard
        let dict = preferences.object(forKey: "user") as! NSDictionary
        emailLabel.text = dict.value(forKey: "email") as! String
        getRewardPointsAPI()
        
        if(dict.value(forKey: "email") as! String == "admin"){
            self.addHotelView.isHidden = false
        }
        else{
            self.addHotelView.isHidden = true

        }

    }
    @IBAction func logoutAction(){
        let preferences = UserDefaults.standard
        preferences.removeObject(forKey: "user")
        preferences.synchronize()

        let loginVC = self.storyboard?.instantiateViewController(withIdentifier: "loginNavigation") as! UINavigationController
        let appDel:AppDelegate = UIApplication.shared.delegate as! AppDelegate
        appDel.window?.rootViewController = loginVC
    }
    func getRewardPointsAPI(){
        let url = URL(string: "\(globals.api)getRewardPointsByEmail")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let preferences = UserDefaults.standard
        let key = "user"
        let userDict = preferences.object(forKey: key) as! NSDictionary
        let email = userDict.value(forKey: "email")
        let jsonDict = NSMutableDictionary()
        jsonDict.setValue("\(String(describing: email!))", forKey: "email")

        let jsonData = try? JSONSerialization.data(withJSONObject: jsonDict)
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
                        DispatchQueue.main.async { [self] () -> Void in
                            self.showToast(message: json["message"] as! String, font: .systemFont(ofSize: 12.0))

                        }

                        } catch {
                            print("error")
                        }
                    
                }
                print(String(data: data, encoding: .utf8))
                return
            }
            do{
                let json = try JSONSerialization.jsonObject(with: data) as! NSDictionary

                DispatchQueue.main.async { [self] () -> Void in
                    self.rewardsLabel.text = "Reward Points : \(json.value(forKey: "rewardPoints")!)"
                }
            }
            catch{
                print("JSON Error : MyBooking API")
            }
            
            
        }

        task.resume()

    }
    @IBAction func addHotelAction(){
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "AddHotelViewController") as! AddHotelViewController
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
