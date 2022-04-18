package spartanbots.v01.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;

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

    @Column(name = "imageURL")
    private String imageURL;

    @Column(name = "maxFloor")
    private Integer maxFloor;

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

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", basePrice=" + basePrice +
                ", imageURL='" + imageURL + '\'' +
                ", maxFloor=" + maxFloor +
                '}';
    }
}