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
    
    override func viewWillAppear(_ animated: Bool) {
        //animation frames set beginning
        self.mainTextLabel.frame = CGRect(x: -self.mainTextLabel.frame.width , y: self.mainTextLabel.frame.origin.y, width: self.mainTextLabel.frame.width, height: self.mainTextLabel.frame.height)
        self.subTextLabel.frame = CGRect(x: -self.subTextLabel.frame.width , y: self.subTextLabel.frame.origin.y, width: self.subTextLabel.frame.width, height: self.subTextLabel.frame.height)
        self.buttonView.frame = CGRect(x: self.buttonView.frame.origin.x , y: self.gradientView.frame.size.height, width: self.buttonView.frame.width, height: self.buttonView.frame.height)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        UIView.animate(withDuration: 0.5,
                       delay: 0.3,
                       options: [],
                       animations: { [weak self] in
                        //self?.view.layoutIfNeeded()
            self!.mainTextLabel.frame = CGRect(x: 15 , y: self!.mainTextLabel.frame.origin.y, width: self!.mainTextLabel.frame.width, height: self!.mainTextLabel.frame.height)
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
        // Do any additional setup after loading the view.
    }

    @IBAction func letsStartAction(_ sender: Any) {
        
    }
}

