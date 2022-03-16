package spartanbots.v01.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;


@Document(collection ="booking")
public class Booking {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "hotel")
    private String hotel;

    @Column(name = "amenity")
    private List<Amenity> amenities;

    public Booking(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) { this.phone = phone; }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public void setAmenities(List<Amenity> amenities) { this.amenities = amenities;}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() { return phone; }

    public String getHotel() {
        return hotel;
    }

    public List<Amenity> getAmenities() {return amenities;}

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", hotel='" + hotel + '\'' +
                ", amenities=" + amenities +
                '}';
    }
}
