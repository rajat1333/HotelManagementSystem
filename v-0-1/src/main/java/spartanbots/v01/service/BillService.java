package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartanbots.v01.entity.*;
import spartanbots.v01.entity.Users.Customer;
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
    private static RoomRepository roomRepository;

    @Autowired
    private static AmenityRepository amenityRepository;

    @Autowired
    private static CustomerRepository customerRepository;

    @Autowired
    public BillService(BillRepository billRepository, BookingRepository bookingRepository, RoomRepository roomRepository, AmenityRepository amenityRepository, CustomerRepository customerRepository) {
        this.billRepository = billRepository;
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
        this.customerRepository = customerRepository;
    }


    @Transactional
    public ResponseEntity<Object> createBill(Booking booking) {
        try {
            Bill bill = generateBillFromBooking(booking);

            System.out.println("Bill record created: \n" + bill.toString());
            return ResponseEntity.ok(bill);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public ResponseEntity<Object> makePayment(Bill bill){
        if(bill.getPaymentStatus().equals("Paid")){
            ResponseEntity.badRequest().body(new ErrorMessage("Payment for this bill has already been made"));
        }
        bill.setPaymentStatus("Paid");
        int rewardPointsUsed = bill.getRewardPointsUsed();

        bill.setTotalPayableAmount(bill.getTotalPayableAmount()-rewardPointsUsed);//updating total payable amount based on reward point used
        bill.setDiscountAmount(bill.getRewardPointsUsed());     //discount is based on reward points


        //updating bill object in booking
        Booking associatedBooking = bookingRepository.findById(bill.getBookingId()).get();

        //updating customer reward points
        int rewardPointsEarned = (int) (bill.getTotalPayableAmount() * 0.05);
        Customer customer = customerRepository.findByEmail(associatedBooking.getCustomerEmail()).get(0);
        customer.setRewardPoints((int) (customer.getRewardPoints()-rewardPointsUsed) + rewardPointsEarned);

        bill.setRewardPointsEarned(rewardPointsEarned);

        associatedBooking.setBill(bill);
        bookingRepository.save(associatedBooking);
        billRepository.save(bill);
        customerRepository.save(customer);

        return  ResponseEntity.ok(bill);
    }


    public static Bill generateBillFromBooking(Booking booking) {
        Bill bill = new Bill();
        bill.setId(billRepository.findAll().size() == 0 ? 1 : billRepository.findAll().stream().max(Comparator.comparingInt(Bill::getId)).get().getId() + 1);
        bill.setBookingId(booking.getId());
        double totalBillAmount = calculateTotalBillAmount(booking);
        bill.setTotalAmount(totalBillAmount);
        bill.setTaxAmount(totalBillAmount * 0.12 ); //Using 12 percent tax
        bill.setTotalPayableAmount(bill.getTotalAmount() + bill.getTaxAmount());
        Customer customer = customerRepository.findByEmail(booking.getCustomerEmail()).get(0);
        int availableRewardPoints = customer.getRewardPoints();
        bill.setAmountPayableByRewardPoints(availableRewardPoints);  //allowing user to pay using available reward points
        bill.setPaymentStatus("Unpaid");
        billRepository.save(bill);
        return bill;
    }

    private static double calculateTotalBillAmount(Booking booking) {
        double totalBillAmount = 0;
        int noOfDays = (int) ((booking.getBookTo().getTime() - booking.getBookFrom().getTime())/(1000*60*60*24));
        System.out.println(" noOfDays is : " + noOfDays);
        List<Room> roomList = booking.getRooms();
        List<Amenity> amenityList = booking.getAmenities();
            for (Amenity amenity: amenityList
                 ) {
                totalBillAmount += (noOfDays * amenityRepository.findById(amenity.getId()).get().getPrice());
            }
        for (Room room : roomList
             ) {
            double roomPrice = room.getPrice();
            totalBillAmount += (noOfDays * roomPrice);
//            List<Amenity> amenityList = room.getBookedAmenities();
//            for (Amenity amenity: amenityList
//                 ) {
//                totalBillAmount += (noOfDays * amenityRepository.findById(amenity.getId()).get().getPrice());
//            }
            System.out.println("totalBillAmount is " + totalBillAmount);
        }
//        double amenitiesCost = getAmenitiesCost(booking);
//        System.out.println(" amenitiesCost is " + amenitiesCost);
//        totalBillAmount += amenitiesCost;
        return totalBillAmount;
    }

//    private double getAmenitiesCost(Booking booking) {
//        double amenityCost = 0;
//        List<Amenity> amenityList = booking.getAmenities();
//        for (Amenity a: amenityList
//             ) {
//            amenityCost += a.getPrice();
//        }
//        return amenityCost;
//    }

}
