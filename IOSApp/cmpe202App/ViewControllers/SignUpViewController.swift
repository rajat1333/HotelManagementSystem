//
//  SignUpViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/17/22.
//

import UIKit
import NVActivityIndicatorView
class SignUpViewController: UIViewController, UITextFieldDelegate {
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    @IBOutlet weak var signUpButton: UIButton!
    @IBOutlet weak var activityIndicatorView:NVActivityIndicatorView!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    @IBAction func signUpAction(){
        if((self.emailTextField.text!.isEmpty) || (self.passwordTextField.text!.isEmpty) || (self.confirmPasswordTextField.text!.isEmpty)){
            self.showToast(message: "Fields cannot be empty", font: .systemFont(ofSize: 12.0))
            
        }
        else if(self.passwordTextField.text! != self.confirmPasswordTextField.text!){
            self.showToast(message: "Passwords do not match", font: .systemFont(ofSize: 12.0))
        }
        else{
            signUp()
        }
    }
    
    func signUp(){
        activityIndicatorView.startAnimating()
        let url = URL(string: "\(globals.api)signup")!
        var request = URLRequest(url: url,timeoutInterval: Double.infinity)
        let json: [String: Any] = ["email": "\(self.emailTextField.text!)",
                                   "password": "\(self.passwordTextField.text!)"]

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
            DispatchQueue.main.async { () -> Void in
                self.showToast(message: "Account Created", font: .systemFont(ofSize: 12.0))
                self.activityIndicatorView.stopAnimating()
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                    self.dismiss(animated: true, completion: nil)
                }
                
            }
        }

        task.resume()

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
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
