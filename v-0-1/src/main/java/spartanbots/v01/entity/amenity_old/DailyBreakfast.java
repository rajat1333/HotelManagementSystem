package spartanbots.v01.entity.amenity_old;

public class DailyBreakfast extends Amenity{

    public DailyBreakfast() {
        description = "Daily continental breakfast";
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
