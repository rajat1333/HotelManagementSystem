//
//  globals.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 2/28/22.
//

import Foundation
import UIKit

class globals {
    
    static let api = "https://2002-2601-646-8200-5d60-00-11ae.ngrok.io/"
    
    public static func X(view: UIView) -> CGFloat? {
        return view.frame.origin.x
    }
    public static func Y(view: UIView) -> CGFloat? {
        return view.frame.origin.y
    }
    public static func RIGHT(view: UIView) -> CGFloat? {
        return view.frame.origin.x+view.frame.size.width
    }
    public static func BOTTOM(view: UIView) -> CGFloat? {
        return view.frame.origin.y+view.frame.size.height
    }
    public static func WIDTH(view: UIView) -> CGFloat? {
        return view.frame.size.width
    }
    public static func HEIGHT(view: UIView) -> CGFloat? {
        return view.frame.size.height
    }
    public static func  dateToString(date:Date) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd"
        return dateFormatter.string(from: date)
    }
}
class shadowView: UIView {
     
   override init(frame: CGRect) {
       super.init(frame: frame)
       setRadiusAndShadow()
   }
     
   required init?(coder: NSCoder) {
       super.init(coder: coder)
       setRadiusAndShadow()
   }
     
   func setRadiusAndShadow() {
       let shadowLayer = CAShapeLayer()
       shadowLayer.path = UIBezierPath(roundedRect: bounds, cornerRadius: 20).cgPath
       shadowLayer.fillColor = UIColor.white.cgColor
       layer.cornerRadius = 20
       shadowLayer.shadowColor = UIColor.darkGray.cgColor
       shadowLayer.shadowPath = shadowLayer.path
       shadowLayer.shadowOffset = CGSize(width: 2.0, height: 2.0)
       shadowLayer.shadowOpacity = 0.7
       shadowLayer.shadowRadius = 4

       layer.insertSublayer(shadowLayer, at: 0)
    }
    
        
    
}

