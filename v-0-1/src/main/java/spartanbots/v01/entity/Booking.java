package spartanbots.v01.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;

@Document(collection ="booking")
public class Booking {

    @Id
    private int id;

    private String name;

    private String phone;

    private String hotel;

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

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", hotel='" + hotel + '\'' +
                '}';
    }
}
