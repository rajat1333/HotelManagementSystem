//
//  globals.swift
//  cmpe202App
//
//  Created by Sahib Bhatia on 2/28/22.
//

import Foundation
import UIKit

class globals {
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
}
