package spartanbots.v01.entity.amenity;

public class DailyAllMeals extends Amenity{

    public DailyAllMeals() {
        description = "Daily all meals";
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
