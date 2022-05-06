package spartanbots.v01.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Document(collection ="booking")
public class Booking {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "customerEmail")
    private String customerEmail;

    @Column(name = "hotelId")
    private int hotelId;

    @Column(name = "hotelName")
    private String hotelName;

    @Column(name = "amenities")
    private List<Amenity> amenities;

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Column(name = "rooms")
    private List<Room> rooms;

    @Column(name = "bookFrom")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date bookFrom;

    @Column(name = "bookTo")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date bookTo;

    @Column(name = "bookTime")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date bookTime;

    @Column(name = "bill")
    private Bill bill;

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public Date getBookFrom() {
        return bookFrom;
    }

    public void setBookFrom(Date bookFrom) {
        this.bookFrom = bookFrom;
    }

    public Date getBookTo() {
        return bookTo;
    }

    public void setBookTo(Date bookTo) {
        this.bookTo = bookTo;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customerEmail='" + customerEmail + '\'' +
                ", hotelId=" + hotelId +
                ", bill=" + bill +
                ", hotelName='" + hotelName + '\'' +
//                ", amenities=" + amenities +
                ", rooms=" + rooms +
                ", bookFrom=" + bookFrom +
                ", bookTo=" + bookTo +
                ", bookTime=" + bookTime +
                '}';
    }
}
