package spartanbots.v01.entity.amenity;

public class SwimmingPoolAccess extends Amenity{

    public SwimmingPoolAccess() {
        description = "Swimming pool access";
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
