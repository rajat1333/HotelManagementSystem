package spartanbots.v01.entity.amenity_old;

public class FitnessRoomAccess extends Amenity{

    public FitnessRoomAccess() {
        description = "Fitness room access";
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
