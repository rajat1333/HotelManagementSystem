package spartanbots.v01.entity.amenity_old;

public class DailyParking extends Amenity{

    public DailyParking() {
        description = "Daily parking";
        price = 25.0;
    }

    @Override
    public double totalAmenityCost() {
        return price;
    }

    @Override
    public String totalAmenityDescription() {
        return description;
    }
}
