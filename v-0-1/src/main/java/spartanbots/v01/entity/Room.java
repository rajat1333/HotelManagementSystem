package spartanbots.v01.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.List;

enum RoomType {
    BASIC,
    STANDARD,
    LUXURY;
}

@Document(collection ="room")
public class Room{

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "hotelId")
    private int hotelId;

    @Column(name = "hotelName")
    private String hotelName;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "roomType")
    private RoomType roomType;

    @Column(name = "price")
    private double price;

    @Column(name = "bookingIds")
    private List<Integer> bookingIds;

    private List<Amenity> bookedAmenities;

    public Room() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() { return hotelName; }

    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Integer> getBookingIds() { return bookingIds; }

    public void setBookingIds(List<Integer> bookingIds) { this.bookingIds = bookingIds; }

    public List<Amenity> getBookedAmenities() {
        return bookedAmenities;
    }

    public void setBookedAmenities(List<Amenity> bookedAmenities) {
        this.bookedAmenities = bookedAmenities;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hotelId=" + hotelId +
                ", hotelName='" + hotelName + '\'' +
                ", floor=" + floor +
                ", roomType=" + roomType +
                ", price=" + price +
                ", bookingIds=" + bookingIds +
                '}';
    }
}