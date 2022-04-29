//
//  MyAccountViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/28/22.
//

import UIKit

class MyAccountViewController: UIViewController {
    @IBOutlet weak var logoutView : UIView!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    @IBAction func logoutAction(){
        let preferences = UserDefaults.standard
        preferences.removeObject(forKey: "user")
        preferences.synchronize()

        let loginVC = self.storyboard?.instantiateViewController(withIdentifier: "ViewController") as! ViewController
        let appDel:AppDelegate = UIApplication.shared.delegate as! AppDelegate
        appDel.window?.rootViewController = loginVC
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
