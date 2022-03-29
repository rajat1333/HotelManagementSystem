package spartanbots.v01.entity.amenity_old;

public class JacuzziAccess extends Amenity{

    public JacuzziAccess() {
        description = "Garden jacuzzi access";
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
