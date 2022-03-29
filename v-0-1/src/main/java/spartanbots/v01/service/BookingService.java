package spartanbots.v01.service;

import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.entity.Room;
import spartanbots.v01.repository.AmenityRepository;
import spartanbots.v01.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.repository.HotelRepository;
import spartanbots.v01.repository.RoomRepository;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository  roomRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, HotelRepository hotelRepository, RoomRepository roomRepository, AmenityRepository amenityRepository) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
    }

    @Transactional
    public String createBooking(Booking booking) {
        try {
            Booking bookingToBeCreated = new Booking();
            bookingToBeCreated.setId(bookingRepository.findAll().size() == 0 ? 1 : bookingRepository.findAll().stream().max(Comparator.comparingInt(Booking::getId)).get().getId() + 1);
            bookingToBeCreated.setAmenities(new ArrayList<Amenity>());
            if(!bookingRegularization(booking, bookingToBeCreated)){
                return "Booking record fail to be created.";
            }
            bookingRepository.save(bookingToBeCreated);
            System.out.println("Booking record created: \n" + booking.toString());
            return "Booking record created successfully.";
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Booking> readBooking() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> searchBooking(int id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public String updateBooking(Booking booking) {

        if(bookingRepository.existsById(booking.getId())){
            try {
                Booking bookingToBeUpdated = bookingRepository.findById(booking.getId()).get();
                if(!bookingRegularization(booking, bookingToBeUpdated)){
                    return "Booking record fail to be updated.";
                }
                bookingRepository.save(bookingToBeUpdated);
                System.out.println("Booking record updated: \n" + booking.toString());
                return "Booking record updated successfully.";
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return "Booking record does not exists.";
        }
    }

    @Transactional
    public String deleteBooking(Booking booking) {
        if(bookingRepository.existsById(booking.getId())){
            try {
                //Booking bookingToBeDeleted  = bookingRepository.findById(booking.getId()).get();
                Room associatedRoom = roomRepository.findById(bookingRepository.findById(booking.getId()).get().getRoomId()).get();
                if(associatedRoom.getBookingIds().contains(booking.getId())){
                    associatedRoom.getBookingIds().remove(Integer.valueOf(booking.getId()));
                    roomRepository.save(associatedRoom);
                }
                System.out.println("Booking record deleted: \n" + booking.toString());
                bookingRepository.deleteById(booking.getId());
                return "Booking record deleted successfully.";
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return "Booking record does not exists.";
        }
    }

    private Boolean bookingRegularization(Booking inputBooking, Booking outputBooking) {
        if (inputBooking.getName() != null) {
            outputBooking.setName(inputBooking.getName());
        }
        if (inputBooking.getPhone() != null) {
            outputBooking.setPhone(inputBooking.getPhone());
        }

        if (bookingDateValidation(inputBooking)) {
            outputBooking.setBookFrom(inputBooking.getBookFrom());
            outputBooking.setBookTo(inputBooking.getBookTo());
            outputBooking.setBookTime(new Date());
        }
        else{
            return false;
        }

        if (hotelRepository.existsById(inputBooking.getHotelId())) {
            outputBooking.setHotelId(inputBooking.getHotelId());
            outputBooking.setHotelName(hotelRepository.findById(outputBooking.getHotelId()).get().getName());
        }
        else {
            return false;
        }

        if (roomRepository.existsById(inputBooking.getRoomId())) {
            outputBooking.setRoomId(inputBooking.getRoomId());
            outputBooking.setRoomName(roomRepository.findById(outputBooking.getRoomId()).get().getName());
        }
        else{
            return false;
        }

        List<Amenity> outputAmenities = autoAmenityMapping(inputBooking.getAmenities());
        outputBooking.setAmenities(outputAmenities);

        Room associatedRoom = roomRepository.findById(outputBooking.getRoomId()).get();
        if(!associatedRoom.getBookingIds().contains(outputBooking.getId())){
            associatedRoom.getBookingIds().add(outputBooking.getId());
            roomRepository.save(associatedRoom);
        }

        return true;
    }

    private List<Amenity> autoAmenityMapping(List<Amenity> inputAmenities) {
        List<Amenity> outputAmenities = new ArrayList<>();
        for(Amenity originalAmenity : inputAmenities){
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
        Boolean checkRange = currentBookingFrom.before(currentBookingTo);
        if (checkRange) {
            List<Integer> existedBookingIds = roomRepository.findById(inputBooking.getRoomId()).get().getBookingIds();
            if(existedBookingIds.isEmpty() || existedBookingIds == null){
                return true;
            }
            for (Integer existedBookingId : existedBookingIds) {
                Date existedBookingFrom = bookingRepository.findById(existedBookingId).get().getBookFrom();
                Date existedBookingTo = bookingRepository.findById(existedBookingId).get().getBookTo();
                Boolean before = currentBookingFrom.before(existedBookingFrom);
                Boolean checkBefore = currentBookingTo.before(existedBookingFrom) || currentBookingTo.equals(existedBookingFrom);
                Boolean after = currentBookingTo.after(existedBookingTo);
                Boolean checkAfter = currentBookingFrom.after(existedBookingTo) || currentBookingFrom.equals(existedBookingTo);
                if (before){
                    if(!checkBefore){ return false;}
                    //case 1 : current [1, 7] and existed [5, 10]
                    if(after){return false;}
                    //case 2 : current [1, 12] and existed [5, 10]
                }
                else{
                    if(!checkAfter){ return false;}
                    //case 3 : current [7, 12] and existed [5, 10]
                    if(!after){return false;}
                    //case 4 : current [7, 8] and existed [5, 10]
                }
            }
            return true;
        }else{
            return false;
        }
    }

}
