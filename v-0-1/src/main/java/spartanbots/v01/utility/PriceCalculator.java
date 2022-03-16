package spartanbots.v01.utility;

import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.Room;

import java.util.List;

public class PriceCalculator {
    public static double getPrice(Room room, List<Amenity> amenityList) {

        double price = room.getPrice();

        if (price < 0) {
            return -1;
        }

        for (Amenity amenity : amenityList) {
            price += amenity.getPrice();
        }

        return price;
    }
}
