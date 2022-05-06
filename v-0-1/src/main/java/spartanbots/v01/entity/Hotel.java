package spartanbots.v01.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Document(collection ="hotel")
public class Hotel {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "basePrice")
    private float basePrice;

    @Column(name = "maxFloor")
    private Integer maxFloor;

    @Column(name = "amenities")
    private List<Amenity> amenities;

    @Column(name = "imageURL")
    private String imageURL;

    public Hotel() {}

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getBasePrice() { return basePrice; }

    public void setBasePrice(float basePrice) { this.basePrice = basePrice; }

    public String getImageURL() { return imageURL; }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public Integer getMaxFloor() {
        return maxFloor;
    }

    public void setMaxFloor(Integer maxFloor) {
        this.maxFloor = maxFloor;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", basePrice=" + basePrice +
                ", imageURL='" + imageURL + '\'' +
                ", maxFloor=" + maxFloor +
                ", amenities=" + amenities +
                '}';
    }
}