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
                if (!bookingRegularization(booking, bookingToBeUpdated)) {
                    return ResponseEntity.badRequest().body(new ErrorMessage("Booking record fail to be updated."));
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

    private Boolean bookingRegularization(Booking inputBooking, Booking outputBooking) {
//        if (inputBooking.getName() != null) {
//            outputBooking.setName(inputBooking.getName());
//        }
//        if (inputBooking.getPhone() != null) {
//            outputBooking.setPhone(inputBooking.getPhone());
//        }
        if (inputBooking.getCustomerEmail() != null) {
            outputBooking.setCustomerEmail(inputBooking.getCustomerEmail());
            System.out.println("Customer email validation pass...");
        }
        else{
            System.out.println("Customer email validation fail...");
        }

        if (bookingDateRangeValidation(inputBooking)) {
            outputBooking.setBookFrom(inputBooking.getBookFrom());
            outputBooking.setBookTo(inputBooking.getBookTo());
            outputBooking.setBookTime(new Date());
            System.out.println("Booking date validation pass...");
        } else {
            System.out.println("Booking date validation fail...");
            return false;
        }

        if (hotelRepository.existsById(inputBooking.getHotelId())) {
            outputBooking.setHotelId(inputBooking.getHotelId());
            outputBooking.setHotelName(hotelRepository.findById(outputBooking.getHotelId()).get().getName());
            System.out.println("Hotel name validation pass...");
        } else {
            System.out.println("Hotel name validation fail...");
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
            //List<Amenity> outputAmenities = autoAmenityMapping(inputBooking.getAmenities());
            //outputBooking.setAmenities(outputAmenities);
            System.out.println("Amenities validation pass...");
        } else {
            System.out.println("Amenities validation fail...");
        }

        List<Room> roomList = inputBooking.getRooms();
        if(roomList==null || roomList.isEmpty()){
            System.out.println("Room validation fail...");
            return false;
        }else{
            System.out.println("Room validation pass...");
        }

        removeBookingIdFromAssociatedRooms(outputBooking);
        System.out.println("Removed booking id from previous associated rooms(if any)...");
        addBookingIdToAssociatedRooms(inputBooking);
        System.out.println("Added booking id to current associated rooms...");

        ArrayList<Room> bookedRoomList = new ArrayList<>();
        for (Room associatedRoom : roomList) {
            Room bookedRoom = roomRepository.findById(associatedRoom.getId()).get();
            //here each booked room object will contain dynamic price at which it has been booked.
            //We are storing whole room object to save fetching of room object. This will save api calls
            bookedRoom.setPrice(associatedRoom.getPrice());
            bookedRoomList.add(bookedRoom);
        }
        outputBooking.setRooms(bookedRoomList);
        System.out.println("Added price to current associated rooms...");

        return true;
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

    private Boolean bookingDateRangeValidation(Booking inputBooking) {
        Date currentBookingFrom = inputBooking.getBookFrom();
        Date currentBookingTo = inputBooking.getBookTo();
        Date currentBookingTime = new Date();
        if (currentBookingFrom == null || currentBookingTo == null) {
            return false;
        }

        boolean checkRange = currentBookingFrom.before(currentBookingTo) &&
                (currentBookingFrom.after(currentBookingTime) || currentBookingFrom.equals(currentBookingTime));

        //boolean checkRange = currentBookingFrom.before(currentBookingTo);
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
                    boolean before = currentBookingFrom.before(existedBookingFrom);
                    boolean checkBefore = currentBookingTo.before(existedBookingFrom) || currentBookingTo.equals(existedBookingFrom);
                    boolean after = currentBookingTo.after(existedBookingTo);
                    boolean checkAfter = currentBookingFrom.after(existedBookingTo) || currentBookingFrom.equals(existedBookingTo);
                    if (dateRangeValidationCheck(before, checkBefore, after, checkAfter)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }


    }

    static boolean dateRangeValidationCheck(boolean before, boolean checkBefore, boolean after, boolean checkAfter) {
        if (before) {
            if (!checkBefore) {
                return true;
            }
            //case 1 : current [1, 7] and existed [5, 10]
            if (after) {
                return true;
            }
            //case 2 : current [1, 12] and existed [5, 10]
        } else {
            if (!checkAfter) {
                return true;
            }
            //case 3 : current [7, 12] and existed [5, 10]
            if (!after) {
                return true;
            }
            //case 4 : current [7, 8] and existed [5, 10]
        }
        return false;
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
