//
//  BookingCollectionViewCell.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 4/5/22.
//

import UIKit
import CollectionViewPagingLayout
class BookingCollectionViewCell: UICollectionViewCell {
    var card: UIView!
    var hotelName: UILabel!
    var hotelLocation: UILabel!
    var price: UILabel!
    var nights: UILabel!
    var date: UILabel!
    var daysLeft: UILabel!
    var hotelImage: UIImageView!
    var sideView: UIView!
    var bookingDate: UILabel!
    var qrImage: UIImageView!
    
    var sideCard1: UIView!
    var sideCard2: UIView!
    var sideCard3: UIView!

    var cancelLabel: UILabel!
    var cancelBtn: UIButton!



        
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }
    
    func setup() {
        // Adjust the card view frame
        // you can use Auto-layout too
        
        let cardFrame = CGRect(
            x: 65,
            y: 130,
            width: frame.width - 130,
            height: frame.height - 260
        )
        let sideFrame = CGRect(
            x: 0,
            y: 0,
            width: 0,
            height: 0
        )
        card = UIView(frame: cardFrame)
        card.backgroundColor = .white
        card.layer.cornerRadius=20
        card.layer.masksToBounds=true
        
        sideView = UIView(frame: sideFrame)
        sideView.backgroundColor = UIColor.systemIndigo//UIColor(red: 250/255.0, green: 128/255.0, blue: 114/255.0, alpha: 1.00)
        sideView.frame=CGRect(x: globals.WIDTH(view: card)!-65, y: 0, width: 65, height: globals.HEIGHT(view: card)!)
        
        
        hotelLocation = UILabel(frame: CGRect(x: 8, y: globals.HEIGHT(view: card)!-80, width: globals.WIDTH(view: card)!-16-65, height: 60))
        hotelLocation.textAlignment = .center
        hotelLocation.font = UIFont(name: "HelveticaNeue-Medium", size: 18)
        
        hotelName = UILabel(frame: CGRect(x: 10, y: 20, width: globals.WIDTH(view: card)!-20-65, height: 70))
        hotelName.numberOfLines = 4
        hotelName.font = UIFont(name: "HelveticaNeue-Bold", size: 29)
        hotelName.textColor = UIColor.darkGray
        
        hotelImage = UIImageView(frame: CGRect(x: globals.WIDTH(view: card)!-65-150, y: globals.BOTTOM(view: hotelName)!+25, width: 150, height: 90))
        
        let path = UIBezierPath(roundedRect:hotelImage.bounds, byRoundingCorners:[.topLeft, .bottomLeft], cornerRadii: CGSize(width: 20, height: 20))
        let maskLayer = CAShapeLayer()
        maskLayer.path = path.cgPath
        hotelImage.layer.mask = maskLayer
        
        
        bookingDate = UILabel(frame: CGRect(x: 10, y: globals.BOTTOM(view: hotelImage)!+40, width: globals.WIDTH(view: card)!-20-65, height: 40))
        bookingDate.numberOfLines = 0
        bookingDate.font = UIFont(name: "HelveticaNeue", size: 16)
        bookingDate.textColor = UIColor.lightGray
        bookingDate.textAlignment = .center
        
        qrImage = UIImageView(frame: CGRect(x: ((globals.WIDTH(view: card)!-65)/2)-45, y: globals.BOTTOM(view: bookingDate)!+50, width: 90, height: 90))
        
        sideCard1 = UIView(frame: sideFrame)
        sideCard1.backgroundColor = UIColor(red: 211/255.0, green: 211/255.0, blue: 211/255.0, alpha: 0.1)
        sideCard1.frame=CGRect(x: 0, y: 0, width: 65, height: globals.HEIGHT(view: sideView)!/3)
        
        
        
        sideCard2 = UIView(frame: sideFrame)
        sideCard2.backgroundColor = UIColor(red: 211/255.0, green: 211/255.0, blue: 211/255.0, alpha: 0.1)
        sideCard2.frame=CGRect(x: 0, y: globals.BOTTOM(view: sideCard1)!+1, width: 65, height: globals.HEIGHT(view: sideView)!/3)
        
        
        
        sideCard3 = UIView(frame: sideFrame)
        sideCard3.backgroundColor = UIColor(red: 250/255.0, green: 128/255.0, blue: 114/255.0, alpha: 1.00)
        sideCard3.frame=CGRect(x: 0, y: globals.BOTTOM(view: sideCard2)!-8, width: 65, height: globals.HEIGHT(view: sideView)!/3+8)
        
        sideView.layer.masksToBounds=true
        
        let sidePath = UIBezierPath(roundedRect:sideCard3.bounds, byRoundingCorners:[.topLeft, .bottomRight], cornerRadii: CGSize(width: 20, height: 20))
        let sideMaskLayer = CAShapeLayer()
        sideMaskLayer.path = sidePath.cgPath
        sideCard3.layer.mask = sideMaskLayer
        
        sideView.addSubview(sideCard1)
        sideView.addSubview(sideCard2)
        sideView.addSubview(sideCard3)
        
        price = UILabel(frame: CGRect(x:2 , y: globals.HEIGHT(view: sideCard1)!/2-13, width: globals.WIDTH(view: sideCard1)!-4, height: 26))
        price.textAlignment = .center
        price.font = UIFont(name: "HelveticaNeue", size: 18)
        price.textColor = UIColor.white
        
        
        nights = UILabel(frame: CGRect(x:2 , y: globals.HEIGHT(view: sideCard2)!/2-35-8, width: globals.WIDTH(view: sideCard1)!-4, height: 70))
        nights.textAlignment = .center
        nights.numberOfLines=3
        nights.font = UIFont(name: "HelveticaNeue", size: 17)
        nights.textColor = UIColor.white
        
        
        daysLeft = UILabel(frame: CGRect(x:2 , y: globals.HEIGHT(view: sideCard3)!/2-50, width: globals.WIDTH(view: sideCard1)!-4, height: 100))
        daysLeft.textAlignment = .center
        daysLeft.numberOfLines=4
        daysLeft.font = UIFont(name: "HelveticaNeue", size: 17)
        daysLeft.textColor = UIColor.white

        cancelLabel = UILabel(frame: CGRect(x:2 , y: globals.HEIGHT(view: sideCard3)!/2-50, width: globals.WIDTH(view: sideCard1)!-4, height: 100))
        cancelLabel.textAlignment = .center
        cancelLabel.numberOfLines=6
        cancelLabel.font = UIFont(name: "HelveticaNeue", size: 15)
        cancelLabel.textColor = UIColor.white
        cancelLabel.text = "C\nA\nC\nC\nE\nL"
        
        
        cancelBtn = UIButton(frame: CGRect(x:2 , y: globals.HEIGHT(view: sideCard3)!/2-50, width: globals.WIDTH(view: sideCard1)!-4, height: 100))
        
        contentView.addSubview(card)
        card.addSubview(hotelLocation)
        card.addSubview(sideView)
        card.addSubview(hotelName)
        card.addSubview(hotelImage)
        card.addSubview(bookingDate)
        card.addSubview(qrImage)
        sideCard1.addSubview(price)
        sideCard2.addSubview(daysLeft)
        sideCard3.addSubview(cancelLabel)
        sideCard3.addSubview(cancelBtn)
        sideCard3.bringSubviewToFront(cancelBtn)




        
    }
}

extension BookingCollectionViewCell: ScaleTransformView {
    var transitionType: ScaleTransformViewOptions{
        .layout(.linear)}
    
    
}

//extension BookingCollectionViewCell: TransformableView {
//    func transform(progress: CGFloat) {
//        let alpha = 1 - abs(progress)
//        contentView.alpha = alpha
//    }
//}
