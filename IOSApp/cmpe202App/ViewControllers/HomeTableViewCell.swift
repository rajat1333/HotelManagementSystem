//
//  HomeTableViewCell.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 3/2/22.
//

import UIKit
import NVActivityIndicatorView

class HomeTableViewCell: UITableViewCell {
    @IBOutlet weak var hotelName : UILabel!
    @IBOutlet weak var location : UILabel!
    @IBOutlet weak var hotelImage : UIImageView!
    @IBOutlet weak var price : UILabel!


    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
