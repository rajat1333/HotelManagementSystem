package spartanbots.v01.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import spartanbots.v01.entity.*;
import spartanbots.v01.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.validator.*;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookingService {

    private static final Logger logger = LogManager.getLogger(BookingService.class);

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

    @Transactional
    public ResponseEntity<Object> createBooking(Booking booking) {
/*
        if(booking.getroomId().size() > 1){
            return createMuliptelBooking();
        }
 */
        try {
            Booking bookingToBeCreated = new Booking();
            bookingToBeCreated.setId(bookingRepository.findAll().size() == 0 ? 1 : bookingRepository.findAll().stream().max(Comparator.comparingInt(Booking::getId)).get().getId() + 1);
            booking.setId(bookingToBeCreated.getId());
            bookingToBeCreated.setAmenities(new ArrayList<Amenity>());

            ValidatorMessage bookingValidatorMessage = newBookingRegularization(booking, bookingToBeCreated);
            boolean passedAllBookingCheck = bookingValidatorMessage.getResult();
            if (!passedAllBookingCheck){
                logger.error("Booking record fail to be created due to: " + bookingValidatorMessage.getMessage());
                return ResponseEntity.badRequest().body(new ErrorMessage("Booking record fail to be created due to: " +
                        bookingValidatorMessage.getMessage()));
            }
            /*
            if (!bookingRegularization(booking, bookingToBeCreated)) {
                logger.error("Booking record fail to be created.");
                return ResponseEntity.badRequest().body(new ErrorMessage("Booking record fail to be created."));
            }
             */
            Bill bill = BillService.generateBillFromBooking(bookingToBeCreated);
            bookingToBeCreated.setBill(bill);
            bookingRepository.save(bookingToBeCreated);
            logger.info("Booking record created: " + bookingToBeCreated.toString());
            return ResponseEntity.ok(bookingToBeCreated);
        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity<Object> readBooking() {
        logger.info("Booking record read.");
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @Transactional
    public ResponseEntity<Object> updateBooking(Booking booking) {
        if (bookingRepository.existsById(booking.getId())) {
            try {
                Booking bookingToBeUpdated = bookingRepository.findById(booking.getId()).get();
                ValidatorMessage bookingValidatorMessage = newBookingRegularization(booking, bookingToBeUpdated);
                boolean passedAllBookingCheck = bookingValidatorMessage.getResult();
                if (!passedAllBookingCheck){
                    logger.error("Booking record fail to be updated due to: " + bookingValidatorMessage.getMessage());
                    return ResponseEntity.badRequest().body(new ErrorMessage("Booking record fail to be updated due to: " +
                            bookingValidatorMessage.getMessage()));
                }
                Bill bill = BillService.modifyBillFromBooking(bookingToBeUpdated);
                bookingToBeUpdated.setBill(bill);
                bookingRepository.save(bookingToBeUpdated);
                logger.info("Booking record updated: " + booking.toString());
                return ResponseEntity.ok(bookingToBeUpdated);
            } catch (Exception e) {
                throw e;
            }
        } else {
            logger.error("Booking record does not exists.");
            return ResponseEntity.badRequest().body(new ErrorMessage("Booking record does not exists."));
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteBooking(Booking booking) {
        if(bookingRepository.existsById(booking.getId())){
            try {
                Booking bookingToBeDeleted  = bookingRepository.findById(booking.getId()).get();
                removeBookingIdFromAssociatedRooms(bookingToBeDeleted);
                //deleting associated bill object along with delete booking                
                if(bookingToBeDeleted.getBill()!=null){
                    billRepository.deleteById(bookingToBeDeleted.getBill().getId());
                }
                bookingRepository.deleteById(bookingToBeDeleted.getId());
                logger.info("Booking record deleted: " + bookingToBeDeleted.toString());
                return ResponseEntity.ok(bookingToBeDeleted);
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            logger.error("Booking record does not exists.");
            return ResponseEntity.badRequest().body(new ErrorMessage("Booking record does not exists."));
        }
    }

    public ResponseEntity<Object> searchBooking(Booking booking) {
        if (bookingRepository.existsById(booking.getId())) {
            logger.info("Booking record searched.");
            return ResponseEntity.ok(bookingRepository.findById(booking.getId()));
        } else {
            logger.error("Booking record does not exists.");
            return ResponseEntity.badRequest().body(new ErrorMessage("Booking record does not exists."));
        }
    }

    private ValidatorMessage newBookingRegularization(Booking inputBooking, Booking outputBooking){
        BookingValidator bookingValidator =
                new BookingValidator(bookingRepository, hotelRepository, roomRepository, amenityRepository);
        bookingValidator.linkWith(new BookingValidatorForEmail(bookingValidator))
                .linkWith(new BookingValidatorForHotel(bookingValidator))
                .linkWith(new BookingValidatorForRooms(bookingValidator))
                .linkWith(new BookingValidatorForDate(bookingValidator));
        ValidatorMessage validatorMessage = bookingValidator.checkAndSet(inputBooking, outputBooking);
        //validatorMessage.getResult() returns true or false depends on the chained responsibilities check above
        boolean passedAllCheck = validatorMessage.getResult();
        if(passedAllCheck){
            logger.info(validatorMessage.getMessage());
            removeBookingIdFromAssociatedRooms(outputBooking);
            addBookingIdToAssociatedRooms(inputBooking);
            setPriceToAssociatedRooms(inputBooking, outputBooking);
            setAmenityByRepositoryMapping(inputBooking, outputBooking);
        }
        return validatorMessage;
    }

    private void setAmenityByRepositoryMapping(Booking inputBooking, Booking outputBooking){
        List<Amenity> inputAmenities = inputBooking.getAmenities();
        List<Amenity> outputAmenities = new ArrayList<>();
        for (Amenity originalAmenity : inputAmenities) {
            if (amenityRepository.existsById(originalAmenity.getId())) {
                Amenity finalAmenity = amenityRepository.findById(originalAmenity.getId()).get();
                outputAmenities.add(finalAmenity);
            }
        }
        outputBooking.setAmenities(outputAmenities);
    }

    private void removeBookingIdFromAssociatedRooms(Booking booking){
        List<Room> RoomsToBeDetached = booking.getRooms();
        //from booking's associated room list, go through each room to remove the booking id.
        if(RoomsToBeDetached != null) {
            for (Room RoomToBeDetached : RoomsToBeDetached) {
                Room associatedRoom = roomRepository.findById(RoomToBeDetached.getId()).get();
                if (associatedRoom.getBookingIds().contains(booking.getId())) {
                    associatedRoom.getBookingIds().remove(Integer.valueOf(booking.getId()));
                    roomRepository.save(associatedRoom);
                }
            }
        }
        logger.info("Removed booking id from previous associated rooms(if any)...");
    }

    private void addBookingIdToAssociatedRooms(Booking booking){
        List<Room> RoomsToBeAttached = booking.getRooms();
        //from booking's associated room list, go through each room to add the booking id.
        for(Room RoomToBeAttached : RoomsToBeAttached){
            Room associatedRoom = roomRepository.findById(RoomToBeAttached.getId()).get();
            if(!associatedRoom.getBookingIds().contains(booking.getId())){
                associatedRoom.getBookingIds().add(booking.getId());
                roomRepository.save(associatedRoom);
            }
        }
        logger.info("Added booking id to current associated rooms...");
    }

    private void setPriceToAssociatedRooms(Booking inputBooking, Booking outputBooking){
        List<Room> roomList = inputBooking.getRooms();
        ArrayList<Room> bookedRoomList = new ArrayList<>();
        for (Room associatedRoom : roomList) {
            Room bookedRoom = roomRepository.findById(associatedRoom.getId()).get();
            //here each booked room object will contain dynamic price at which it has been booked.
            //We are storing whole room object to save fetching of room object. This will save api calls
            bookedRoom.setPrice(associatedRoom.getPrice());
            bookedRoomList.add(bookedRoom);
        }
        outputBooking.setRooms(bookedRoomList);
        logger.info("Set prices to current associated rooms...");
    }

    @Transactional
    public ResponseEntity<Object> deleteExpiredBookingIdsFromAllRoom(){
        List<Room> allRooms = roomRepository.findAll();
        List<Integer> deletedBookingIds = new ArrayList<>();
        Date currentTime = new Date();
        for(Room eachRoom : allRooms){
            List<Integer> associatedBookingIdList = eachRoom.getBookingIds();
            for(Integer associatedBookingId : associatedBookingIdList){
                Date bookingTo = bookingRepository.findById(associatedBookingId).get().getBookTo();
                if(bookingTo.before(currentTime)){
                    eachRoom.getBookingIds().remove(associatedBookingId);
                    deletedBookingIds.add(associatedBookingId);
                }
            }
            roomRepository.save(eachRoom);
        }
        return ResponseEntity.ok(deletedBookingIds);
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            booking.put("bookFrom", dateFormat.format(inputBooking.getBookFrom()));
            booking.put("bookTo", dateFormat.format(inputBooking.getBookTo()));
            if(inputBooking.getAmenities()!=null && !inputBooking.getAmenities().isEmpty()){
                booking.put("amenities",inputBooking.getAmenities());
            }
            booking.put("hotelName", inputBooking.getHotelName());
            Hotel hotel = hotelRepository.findById(inputBooking.getHotelId()).get();
            if(hotel!=null){
                booking.put("city", hotel.getCity());
            }
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
