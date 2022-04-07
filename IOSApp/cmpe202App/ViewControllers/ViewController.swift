//
//  ViewController.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 2/26/22.
//

import UIKit
class ViewController: UIViewController {
    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var splashImageView: UIImageView!
    @IBOutlet weak var buttonView: UIView!
    @IBOutlet weak var gradientView: UIView!
    @IBOutlet weak var buttonLabel: UILabel!
    @IBOutlet weak var mainTextLabel: UILabel!
    @IBOutlet weak var subTextLabel: UILabel!
    @IBOutlet weak var button: UIButton!
    
    @IBOutlet weak var buildingView: UIView!
    @IBOutlet weak var tajImageView: UIImageView!
    @IBOutlet weak var libertyImageView: UIImageView!
    @IBOutlet weak var christImageView: UIImageView!
    @IBOutlet weak var leaningImageView: UIImageView!
    @IBOutlet weak var benImageView: UIImageView!
    @IBOutlet weak var tokyoImageView: UIImageView!
    @IBOutlet weak var pyramidImageView: UIImageView!
    @IBOutlet weak var burjImageView: UIImageView!
    @IBOutlet weak var eiffleImageView: UIImageView!
    
    @IBOutlet weak var loginView: UIView!
    @IBOutlet weak var welcomeLabel: UILabel!
    @IBOutlet weak var loginLabel: UILabel!
    @IBOutlet weak var emailTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var loginButtonView: UIView!
    @IBOutlet weak var forgotButtonView: UIView!
    @IBOutlet weak var createButtonView: UIView!
    @IBOutlet weak var guestButtonView: UIView!
    
    override func viewWillAppear(_ animated: Bool) {
        //animation frames set beginning
        self.mainTextLabel.frame = CGRect(x: -self.mainTextLabel.frame.width , y: globals.Y(view: self.mainTextLabel)!, width: globals.WIDTH(view: self.mainTextLabel)!, height: globals.HEIGHT(view: self.mainTextLabel)!)
        
        self.subTextLabel.frame = CGRect(x: -self.subTextLabel.frame.width , y: self.subTextLabel.frame.origin.y, width: self.subTextLabel.frame.width, height: self.subTextLabel.frame.height)
        
        self.buttonView.frame = CGRect(x: self.buttonView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.buttonView.frame.width, height: self.buttonView.frame.height)
        
        self.welcomeLabel.frame = CGRect(x: -self.welcomeLabel.frame.width , y: self.welcomeLabel.frame.origin.y, width: self.welcomeLabel.frame.width, height: self.welcomeLabel.frame.height)
        
        self.loginLabel.frame = CGRect(x: -self.loginLabel.frame.width , y: self.loginLabel.frame.origin.y, width: self.loginLabel.frame.width, height: self.loginLabel.frame.height)
        
        self.emailTextField.frame = CGRect(x: self.gradientView.frame.width+10 , y: self.emailTextField.frame.origin.y, width: self.emailTextField.frame.width, height: self.emailTextField.frame.height)
        
        self.passwordTextField.frame = CGRect(x: self.gradientView.frame.width+10 , y: self.passwordTextField.frame.origin.y, width: self.passwordTextField.frame.width, height: self.passwordTextField.frame.height)
        
        self.loginButtonView.frame = CGRect(x: self.loginButtonView.frame.origin.x , y: self.gradientView.frame.height, width: self.loginButtonView.frame.width, height: self.loginButtonView.frame.height)
        
        self.forgotButtonView.frame = CGRect(x: self.forgotButtonView.frame.origin.x , y: self.gradientView.frame.height, width: self.forgotButtonView.frame.width, height: self.forgotButtonView.frame.height)
        
        self.createButtonView.frame = CGRect(x: self.createButtonView.frame.origin.x , y: self.gradientView.frame.height, width: self.createButtonView.frame.width, height: self.createButtonView.frame.height)
        
        self.guestButtonView.frame = CGRect(x: self.guestButtonView.frame.origin.x , y: self.gradientView.frame.height, width: self.guestButtonView.frame.width, height: self.guestButtonView.frame.height)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        UIView.animate(withDuration: 0.5,
                       delay: 0.3,
                       options: [],
                       animations: { [weak self] in
                        //self?.view.layoutIfNeeded()
            self!.mainTextLabel.frame = CGRect(x: 15 , y: globals.Y(view:self!.mainTextLabel)!, width: self!.mainTextLabel.frame.width, height: self!.mainTextLabel.frame.height)
          }, completion: nil)
        UIView.animate(withDuration: 0.5,
                       delay: 0.3,
                       options: [],
                       animations: { [weak self] in
                        //self?.view.layoutIfNeeded()
            self!.subTextLabel.frame = CGRect(x: 15, y: self!.subTextLabel.frame.origin.y, width: self!.subTextLabel.frame.width, height: self!.subTextLabel.frame.height)
          }, completion: nil)
        UIView.animate(withDuration: 0.5,
                       delay: 0.3,
                       options: [],
                       animations: { [weak self] in
                        //self?.view.layoutIfNeeded()
            self!.buttonView.frame = CGRect(x: self!.buttonView.frame.origin.x , y: 700, width: self!.buttonView.frame.width, height: self!.buttonView.frame.height)
          }, completion: nil)

    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        buildingView.isHidden=true
        loginView.isHidden=true
        self.tajImageView.frame = CGRect(x: self.tajImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.tajImageView.frame.width, height: self.tajImageView.frame.height)
        
        self.christImageView.frame = CGRect(x: self.christImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.christImageView.frame.width, height: self.christImageView.frame.height)
        
        self.leaningImageView.frame = CGRect(x: self.leaningImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.leaningImageView.frame.width, height: self.leaningImageView.frame.height)
        
        self.pyramidImageView.frame = CGRect(x: self.pyramidImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.pyramidImageView.frame.width, height: self.pyramidImageView.frame.height)
        
        self.eiffleImageView.frame = CGRect(x: self.eiffleImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.eiffleImageView.frame.width, height: self.eiffleImageView.frame.height)
        
        self.libertyImageView.frame = CGRect(x: self.libertyImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.libertyImageView.frame.width, height: self.libertyImageView.frame.height)
        
        self.burjImageView.frame = CGRect(x: self.burjImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.burjImageView.frame.width, height: self.burjImageView.frame.height)
        
        self.benImageView.frame = CGRect(x: self.benImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.benImageView.frame.width, height: self.benImageView.frame.height)
        
        self.tokyoImageView.frame = CGRect(x: self.tokyoImageView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.tokyoImageView.frame.width, height: self.tokyoImageView.frame.height)
    }

    @IBAction func letsStartAction(_ sender: Any) {
        UIView.animate(withDuration: 0.5,
                       delay: 0.3,
                       options: [],
                       animations: { [weak self] in
                        //self?.view.layoutIfNeeded()
            self!.mainTextLabel.frame = CGRect(x: 15 , y: -100, width: self!.mainTextLabel.frame.width, height: self!.mainTextLabel.frame.height)
            self!.subTextLabel.frame = CGRect(x: 15, y: -100, width: self!.subTextLabel.frame.width, height: self!.subTextLabel.frame.height)
            self!.buttonView.frame = CGRect(x: self!.buttonView.frame.origin.x+500 , y: 700, width: self!.buttonView.frame.width, height: self!.buttonView.frame.height)
        }, completion: {_ in self.beginLoginAnimation()})
        
    }
    
    func beginLoginAnimation(){
        self.splashImageView.image = UIImage(named:"sky")
        
        self.buildingView.isHidden=false
        self.loginView.isHidden=false
        
        UIView.animate(withDuration: 0.1,
                       delay: 0.1,
                       options: [],
                       animations: { [weak self] in
            self!.tajImageView.frame = CGRect(x: self!.tajImageView.frame.origin.x , y: 674, width: self!.tajImageView.frame.width, height: self!.tajImageView.frame.height)
            
            self!.welcomeLabel.frame = CGRect(x: 15, y: self!.welcomeLabel.frame.origin.y, width: self!.welcomeLabel.frame.width, height: self!.welcomeLabel.frame.height)
            
        }, completion: {_ in UIView.animate(withDuration: 0.1,
                                        delay: 0.0,
                                        options: [],
                                        animations: { [weak self] in
            self!.pyramidImageView.frame = CGRect(x: self!.pyramidImageView.frame.origin.x , y: 695, width: self!.pyramidImageView.frame.width, height: self!.pyramidImageView.frame.height)
            
            self!.loginLabel.frame = CGRect(x: 15 , y: self!.loginLabel.frame.origin.y, width: self!.loginLabel.frame.width, height: self!.loginLabel.frame.height)
            
                             
        }, completion: {_ in UIView.animate(withDuration: 0.1,
                                       delay: 0.0,
                                       options: [],
                                       animations: { [weak self] in
            self!.tokyoImageView.frame = CGRect(x: self!.tokyoImageView.frame.origin.x , y: 699, width: self!.tokyoImageView.frame.width, height: self!.tokyoImageView.frame.height)
            
            self!.emailTextField.frame = CGRect(x: 40 , y: self!.emailTextField.frame.origin.y, width: self!.emailTextField.frame.width, height: self!.emailTextField.frame.height)
                        
          }, completion: {_ in UIView.animate(withDuration: 0.1,
                                             delay: 0.1,
                                             options: [],
                                             animations: { [weak self] in
              self!.christImageView.frame = CGRect(x: self!.christImageView.frame.origin.x , y: 655, width: self!.christImageView.frame.width, height: self!.christImageView.frame.height)
              
              self!.passwordTextField.frame = CGRect(x: 40 , y: self!.passwordTextField.frame.origin.y, width: self!.passwordTextField.frame.width, height: self!.passwordTextField.frame.height)
                                  
          }, completion: {_ in UIView.animate(withDuration: 0.1,
                                                delay: 0.1,
                                                options: [],
                                                animations: { [weak self] in
                self!.libertyImageView.frame = CGRect(x: self!.libertyImageView.frame.origin.x , y: 567, width: self!.libertyImageView.frame.width, height: self!.libertyImageView.frame.height)
                
                self!.loginButtonView.frame = CGRect(x: self!.loginButtonView.frame.origin.x , y: globals.BOTTOM(view: self!.passwordTextField)! + 25, width: self!.loginButtonView.frame.width, height: self!.loginButtonView.frame.height)
                                     
           }, completion: {_ in UIView.animate(withDuration: 0.1,
                                               delay: 0.1,
                                               options: [],
                                               animations: { [weak self] in
               self!.benImageView.frame = CGRect(x: self!.benImageView.frame.origin.x , y: 621, width: self!.benImageView.frame.width, height: self!.benImageView.frame.height)
               
              
               
                                    
          }, completion: {_ in UIView.animate(withDuration: 0.1,
                                              delay: 0.1,
                                              options: [],
                                              animations: { [weak self] in
              self!.leaningImageView.frame = CGRect(x: self!.leaningImageView.frame.origin.x , y: 625, width: self!.leaningImageView.frame.width, height: self!.leaningImageView.frame.height)
              
              self!.forgotButtonView.frame = CGRect(x: self!.forgotButtonView.frame.origin.x , y: globals.BOTTOM(view: self!.loginButtonView)! + 35, width: self!.forgotButtonView.frame.width, height: self!.forgotButtonView.frame.height)
                                   
         }, completion: {_ in UIView.animate(withDuration: 0.1,
                                             delay: 0.1,
                                             options: [],
                                             animations: { [weak self] in
            
             self!.eiffleImageView.frame = CGRect(x: self!.eiffleImageView.frame.origin.x , y: 598, width: self!.eiffleImageView.frame.width, height: self!.eiffleImageView.frame.height)
             
             self!.createButtonView.frame = CGRect(x: self!.createButtonView.frame.origin.x , y: globals.BOTTOM(view: self!.forgotButtonView)! + 5, width: self!.createButtonView.frame.width, height: self!.createButtonView.frame.height)
                                  
        }, completion: {_ in UIView.animate(withDuration: 0.1,
                                            delay: 0.1,
                                            options: [],
                                            animations: { [weak self] in
            
            self!.burjImageView.frame = CGRect(x: self!.burjImageView.frame.origin.x , y: 665, width: self!.burjImageView.frame.width, height: self!.burjImageView.frame.height)
            
            self!.guestButtonView.frame = CGRect(x: self!.guestButtonView.frame.origin.x , y: globals.BOTTOM(view: self!.createButtonView)! + 20, width: self!.guestButtonView.frame.width, height: self!.guestButtonView.frame.height)
                                 
                               }, completion: nil)}
        )}
        )}
        )}
        )}
        )}
        )}
        )}
        )
    }
    @IBAction func loginAction(_ sender: Any) {
        
    }
    @IBAction func signUpAction(_ sender: Any) {
        
    }
    @IBAction func forgotAction(_ sender: Any) {
        
    }
    @IBAction func guestAction(_ sender: Any) {
//        if let viewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "SHCircleBarController") as? SHCircleBarController {
                //viewController.newsObj = newsObj
            //self.navigationController?.pushViewController(viewController, animated: true)
        //}
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let mainTabBarController = storyboard.instantiateViewController(identifier: "SHCircleBarController")
        mainTabBarController.modalPresentationStyle = .fullScreen
        mainTabBarController.modalTransitionStyle = .partialCurl
        
        navigationController?.pushViewController(mainTabBarController, animated: true)
    }
}







