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
        card = UIView(frame: cardFrame)
        card.backgroundColor = .white
        contentView.addSubview(card)
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
