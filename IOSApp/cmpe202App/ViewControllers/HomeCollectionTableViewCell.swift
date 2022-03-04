//
//  HomeCollectionTableViewCell.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 3/2/22.
//

import UIKit

class HomeCollectionTableViewCell: UITableViewCell {
    @IBOutlet weak var collectionView: UICollectionView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
