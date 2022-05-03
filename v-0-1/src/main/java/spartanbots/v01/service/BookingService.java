package spartanbots.v01.service;

import org.springframework.http.ResponseEntity;
import spartanbots.v01.entity.*;
import spartanbots.v01.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, HotelRepository hotelRepository, RoomRepository roomRepository, AmenityRepository amenityRepository, BillRepository billRepository) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
        this.billRepository = billRepository;
    }

    @Transactional
    public ResponseEntity<Object> createBooking(Booking booking) {
        try {
            Booking bookingToBeCreated = new Booking();
            bookingToBeCreated.setId(bookingRepository.findAll().size() == 0 ? 1 : bookingRepository.findAll().stream().max(Comparator.comparingInt(Booking::getId)).get().getId() + 1);
            bookingToBeCreated.setAmenities(new ArrayList<Amenity>());
            if (!bookingRegularization(booking, bookingToBeCreated)) {
                return ResponseEntity.badRequest().body(new ErrorMessage("Booking record fail to be created."));
            }
            Bill bill = BillService.generateBillFromBooking(bookingToBeCreated);
            bookingToBeCreated.setBill(bill);
            bookingRepository.save(bookingToBeCreated);
            System.out.println("Booking record created: \n" + bookingToBeCreated.toString());
            return ResponseEntity.ok(bookingToBeCreated);
        } catch (Exception e) {
            throw e;
        }
    }


    public ResponseEntity<Object> readBooking() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @Transactional
    public ResponseEntity<Object> updateBooking(Booking booking) {

        if (bookingRepository.existsById(booking.getId())) {
            try {
                Booking bookingToBeUpdated = bookingRepository.findById(booking.getId()).get();
                if (!bookingRegularization(booking, bookingToBeUpdated)) {
                    return ResponseEntity.badRequest().body(new ErrorMessage("Booking record fail to be updated."));
                }
                bookingRepository.save(bookingToBeUpdated);
                System.out.println("Booking record updated: \n" + booking.toString());
                return ResponseEntity.ok(bookingToBeUpdated);
            } catch (Exception e) {
                throw e;
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Booking record does not exists."));
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteBooking(Booking booking) {
        if (bookingRepository.existsById(booking.getId())) {
            try {
                Booking bookingToBeDeleted = bookingRepository.findById(booking.getId()).get();
                List<Room> bookedRooms = bookingToBeDeleted.getRooms();
                if(bookedRooms!=null && !bookedRooms.isEmpty()){
                    for (Room bookedRoom: bookedRooms
                    ) {
                        Room associatedRoom = roomRepository.findById(bookedRoom.getId()).get();
                        if (associatedRoom.getBookingIds().contains(bookingToBeDeleted.getId())) {
                            associatedRoom.getBookingIds().remove(Integer.valueOf(bookingToBeDeleted.getId()));
                            roomRepository.save(associatedRoom);
                        }
                    }
                }
                //Room associatedRoom = roomRepository.findById(bookingRepository.findById(bookingToBeDeleted.getId()).get().getRoomId()).get();

                //deleting associated bill object along with delete booking
                if(bookingToBeDeleted.getBill()!=null){
                    billRepository.deleteById(bookingToBeDeleted.getBill().getId());
                }
                bookingRepository.deleteById(bookingToBeDeleted.getId());
                System.out.println("Booking record deleted: \n" + bookingToBeDeleted.toString());
                return ResponseEntity.ok(bookingToBeDeleted);
            } catch (Exception e) {
                throw e;
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Booking record does not exists."));
        }
    }

    public ResponseEntity<Object> searchBooking(Booking booking) {
        if (bookingRepository.existsById(booking.getId())) {
            return ResponseEntity.ok(bookingRepository.findById(booking.getId()));
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Booking record does not exists."));
        }
    }

    private Boolean bookingRegularization(Booking inputBooking, Booking outputBooking) {
//        if (inputBooking.getName() != null) {
//            outputBooking.setName(inputBooking.getName());
//        }
//        if (inputBooking.getPhone() != null) {
//            outputBooking.setPhone(inputBooking.getPhone());
//        }
        if (inputBooking.getCustomerEmail() != null) {
            outputBooking.setCustomerEmail(inputBooking.getCustomerEmail());
        }

        if (bookingDateValidation(inputBooking)) {
            outputBooking.setBookFrom(inputBooking.getBookFrom());
            outputBooking.setBookTo(inputBooking.getBookTo());
            outputBooking.setBookTime(new Date());
        } else {
            return false;
        }

        if (hotelRepository.existsById(inputBooking.getHotelId())) {
            outputBooking.setHotelId(inputBooking.getHotelId());
            outputBooking.setHotelName(hotelRepository.findById(outputBooking.getHotelId()).get().getName());
        } else {
            return false;
        }

//        if (roomRepository.existsById(inputBooking.getRoomId())) {
//            outputBooking.setRoomId(inputBooking.getRoomId());
//            outputBooking.setRoomName(roomRepository.findById(outputBooking.getRoomId()).get().getName());
//        }
//        else{
//            return false;
//        }

        if(inputBooking.getAmenities()!=null && !inputBooking.getAmenities().isEmpty()){
            List<Amenity> outputAmenities = autoAmenityMapping(inputBooking.getAmenities());
            outputBooking.setAmenities(outputAmenities);
        }


        List<Room> roomList = inputBooking.getRooms();
        if(roomList==null || roomList.isEmpty()){
            return false;
        }
        ArrayList<Room> bookedRoomList = new ArrayList<>();
        for (Room associatedRoom : roomList
        ) {
            Room room = roomRepository.findById(associatedRoom.getId()).get();
            Room bookedRoom = roomRepository.findById(associatedRoom.getId()).get();
            //here each booked room object will contain dynamic price at which it has been booked.
            //We are storing whole room object to save fetching of room object. This will save api calls
            bookedRoom.setPrice(associatedRoom.getPrice());
            bookedRoomList.add(bookedRoom);
            //adding booking ids in room object
            if(room.getBookingIds()==null)
                room.setBookingIds(new ArrayList<>());
            if (!room.getBookingIds().contains(outputBooking.getId())) {
                room.getBookingIds().add(outputBooking.getId());
                roomRepository.save(room);
            }

        }
        outputBooking.setRooms(bookedRoomList);

//        Room associatedRoom = roomRepository.findById(outputBooking.getRoomId()).get();
//        if(!associatedRoom.getBookingIds().contains(outputBooking.getId())){
//            associatedRoom.getBookingIds().add(outputBooking.getId());
//            roomRepository.save(associatedRoom);
//        }
        return true;
    }

    private List<Amenity> autoAmenityMapping(List<Amenity> inputAmenities) {
        List<Amenity> outputAmenities = new ArrayList<>();
        for (Amenity originalAmenity : inputAmenities) {
            if (amenityRepository.existsById(originalAmenity.getId())) {
                Amenity finalAmenity = amenityRepository.findById(originalAmenity.getId()).get();
                outputAmenities.add(finalAmenity);
            }
        }
        return outputAmenities;
    }

    private Boolean bookingDateValidation(Booking inputBooking) {
        Date currentBookingFrom = inputBooking.getBookFrom();
        Date currentBookingTo = inputBooking.getBookTo();
        if (currentBookingFrom == null || currentBookingTo == null) {
            return false;
        }
        Boolean checkRange = currentBookingFrom.before(currentBookingTo);
        if (checkRange) {
            //there should not be need for this check as we will be showing only available rooms on UI
            List<Room> roomList = inputBooking.getRooms();
            if(roomList==null ||roomList.isEmpty())
                return false;
            for (Room currentRoom : roomList
            ) {
                List<Integer> existedBookingIds = roomRepository.findById(currentRoom.getId()).get().getBookingIds();
                if (existedBookingIds.isEmpty() || existedBookingIds == null) {
//                    return true;
                    continue;
                }
                if (existedBookingIds.contains(inputBooking.getId())) {
                    existedBookingIds.remove(Integer.valueOf(inputBooking.getId()));
                }

                for (Integer existedBookingId : existedBookingIds) {
                    Date existedBookingFrom = bookingRepository.findById(existedBookingId).get().getBookFrom();
                    Date existedBookingTo = bookingRepository.findById(existedBookingId).get().getBookTo();
                    System.out.println(existedBookingFrom + " " + existedBookingTo);
                    System.out.println(currentBookingFrom + " " + currentBookingTo);
                    Boolean before = currentBookingFrom.before(existedBookingFrom);
                    Boolean checkBefore = currentBookingTo.before(existedBookingFrom) || currentBookingTo.equals(existedBookingFrom);
                    Boolean after = currentBookingTo.after(existedBookingTo);
                    Boolean checkAfter = currentBookingFrom.after(existedBookingTo) || currentBookingFrom.equals(existedBookingTo);
                    if (before) {
                        if (!checkBefore) {
                            return false;
                        }
                        //case 1 : current [1, 7] and existed [5, 10]
                        if (after) {
                            return false;
                        }
                        //case 2 : current [1, 12] and existed [5, 10]
                    } else {
                        if (!checkAfter) {
                            return false;
                        }
                        //case 3 : current [7, 12] and existed [5, 10]
                        if (!after) {
                            return false;
                        }
                        //case 4 : current [7, 8] and existed [5, 10]
                    }
                }

            }
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public ResponseEntity<HashMap<String, Object>> getBookingByEmail(Booking booking) {

        List<Booking> bookings= bookingRepository.findByEmail(booking.getCustomerEmail());
        HashMap<String, Object> outputBookings = createOutputBookings(bookings);

        return ResponseEntity.ok(outputBookings);
    }

    private HashMap<String, Object> createOutputBookings(List<Booking> bookings) {
        HashMap<String, Object> outputBookings = new HashMap<>();
        ArrayList bookingArray = new ArrayList();
        for (Booking inputBooking: bookings
             ) {
            HashMap<String, Object> booking = new HashMap<>();
            booking.put("id", inputBooking.getId());
            booking.put("customerEmail", inputBooking.getCustomerEmail());
            booking.put("hotelName", inputBooking.getHotelName());
            booking.put("bookFrom", inputBooking.getBookFrom());
            booking.put("bookTo", inputBooking.getBookTo());
            if(inputBooking.getBill()!=null){
                booking.put("totalPayableAmount", inputBooking.getBill().getTotalPayableAmount());
                booking.put("rewardPointsUsed", inputBooking.getBill().getRewardPointsUsed());
            }
            ArrayList roomArray = new ArrayList();

            List<Room> rooms = inputBooking.getRooms();
            if(rooms!=null && !rooms.isEmpty()){
                for (Room room : rooms
                ) {
                    HashMap<String, Object> roomObj = new HashMap<>();
                    roomObj.put("id", room.getId());
                    roomObj.put("roomType", room.getRoomType());
                    roomObj.put("name", room.getName());
                    roomArray.add(roomObj);
                }
                booking.put("rooms", roomArray);
            }
            bookingArray.add(booking);
        }
        outputBookings.put("bookingArray", bookingArray);
        return outputBookings;
    }
}
