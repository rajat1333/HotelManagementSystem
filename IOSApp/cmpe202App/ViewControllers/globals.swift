//
//  globals.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 2/28/22.
//

import Foundation
import UIKit

class globals {
    
//    static let api = "https://7da3-2601-646-8200-5d60-00-3a32.ngrok.io/"
    static let api = "http://52.53.170.174:8080/"

    
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
    public static func getDateAndTime(timeZoneIdentifier: String) -> String? {
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd"
        dateFormatter.timeZone = TimeZone(identifier: timeZoneIdentifier)

        return dateFormatter.string(from: Date())
    }
    public static func  stringToDate(str:String) -> Date {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd"
        return dateFormatter.date(from: str)!
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
       shadowLayer.path = UIBezierPath(roundedRect: layer.bounds, cornerRadius: 20).cgPath
       shadowLayer.fillColor = UIColor.white.cgColor
       layer.cornerRadius = 20
       shadowLayer.shadowColor = UIColor.darkGray.cgColor
       shadowLayer.shadowPath = shadowLayer.path
       shadowLayer.shadowOffset = CGSize(width: 0, height: 0)
       shadowLayer.shadowOpacity = 0.7
       shadowLayer.shadowRadius = 4
       //shadowLayer.frame = layer.frame
       layer.insertSublayer(shadowLayer, at:0)
    }
    
        
    
}

