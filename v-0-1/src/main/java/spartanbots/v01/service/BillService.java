package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.Bill;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.entity.Room;
import spartanbots.v01.repository.*;

import java.util.Comparator;
import java.util.List;


/**
 * @author Rajat Masurkar
 */
@Service
public class BillService {
    @Autowired
    private static BillRepository billRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private static RoomRepository roomRepository;

    @Autowired
    private static AmenityRepository amenityRepository;

    @Autowired
    public BillService(BillRepository billRepository, BookingRepository bookingRepository, HotelRepository hotelRepository, RoomRepository roomRepository, AmenityRepository amenityRepository) {
        this.billRepository = billRepository;
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
    }


    @Transactional
    public ResponseEntity<Object> createBill(Booking booking) {
        try {
            Bill bill = getBillFromBooking(booking);

            System.out.println("Bill record created: \n" + bill.toString());
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            throw e;
        }
    }

    public static Bill getBillFromBooking(Booking booking) {
        Bill bill = new Bill();
        bill.setId(billRepository.findAll().size() == 0 ? 1 : billRepository.findAll().stream().max(Comparator.comparingInt(Bill::getId)).get().getId() + 1);
        double totalBillAmount = calculateTotalBillAmount(booking);
        bill.setTotalAmount(totalBillAmount);
        bill.setTaxAmount(totalBillAmount * 0.12 ); //todo add variable for tax percentage
        bill.setTotalPayableAmount(bill.getTotalAmount() + bill.getTaxAmount());
        bill.setPaymentStatus("Unpaid");
        return bill;
    }

    private static double calculateTotalBillAmount(Booking booking) {
        double totalBillAmount = 0;
        int noOfDays = (int) ((booking.getBookTo().getTime() - booking.getBookFrom().getTime())/(1000*60*60*24));
        System.out.println(" noOfDays is : " + noOfDays);
        List<Room> roomList = booking.getRooms();
        for (Room room : roomList
             ) {
            double roomPrice = getRoomPrice(room.getId());
            totalBillAmount += (noOfDays * roomPrice);
            List<Amenity> amenityList = room.getBookedAmenities();
            for (Amenity amenity: amenityList
                 ) {
                totalBillAmount += (noOfDays * amenityRepository.findById(amenity.getId()).get().getPrice());
            }
            System.out.println("totalBillAmount is " + totalBillAmount);
        }
//        double amenitiesCost = getAmenitiesCost(booking);
//        System.out.println(" amenitiesCost is " + amenitiesCost);
//        totalBillAmount += amenitiesCost;
        return totalBillAmount;
    }

    private double getAmenitiesCost(Booking booking) {
        double amenityCost = 0;
        List<Amenity> amenityList = booking.getAmenities();
        for (Amenity a: amenityList
             ) {
            amenityCost += a.getPrice();
        }
        return amenityCost;
    }

    private static double getRoomPrice(int roomId) {
        Room room = roomRepository.findById(roomId).get();
        double roomPrice = room.getPrice();
        System.out.println("Room price for room id " + roomId + " is : " + roomPrice);
        return roomPrice;
    }
}
