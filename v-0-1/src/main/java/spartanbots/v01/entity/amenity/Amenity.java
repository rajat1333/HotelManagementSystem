package spartanbots.v01.entity.amenity;

public abstract class Amenity {
    String description;
    Double price;

    public String getDescription() {
        return description;
    }
    public Double getPrice() {
        return price;
    }
    public abstract double totalAmenityCost();
    public abstract String totalAmenityDescription();
}
