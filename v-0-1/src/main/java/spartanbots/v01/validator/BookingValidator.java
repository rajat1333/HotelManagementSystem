package spartanbots.v01.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.repository.*;

@Service
public class BookingValidator {
    protected static final Logger logger = LogManager.getLogger(BookingValidator.class);

    @Autowired
    protected BookingRepository bookingRepository;
    @Autowired
    protected HotelRepository hotelRepository;
    @Autowired
    protected RoomRepository roomRepository;
    @Autowired
    protected AmenityRepository amenityRepository;

    protected BookingValidator next;
    protected ValidatorMessage validatorMessage = new ValidatorMessage(false, null);

    @Autowired
    public BookingValidator(BookingRepository bookingRepository, HotelRepository hotelRepository, RoomRepository roomRepository, AmenityRepository amenityRepository) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
    }

    public BookingValidator(BookingValidator bookingValidator) {
        this.setBookingRepository(bookingValidator.getBookingRepository());
        this.setHotelRepository(bookingValidator.getHotelRepository());
        this.setRoomRepository(bookingValidator.getRoomRepository());
        this.setAmenityRepository(bookingValidator.getAmenityRepository());
    }

    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }

    public void setBookingRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public HotelRepository getHotelRepository() {
        return hotelRepository;
    }

    public void setHotelRepository(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public AmenityRepository getAmenityRepository() {
        return amenityRepository;
    }

    public void setAmenityRepository(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public BookingValidator getNext() {
        return next;
    }

    public void setNext(BookingValidator next) {
        this.next = next;
    }

    public ValidatorMessage getValidatorMessage() {
        return validatorMessage;
    }

    public void setValidatorMessage(ValidatorMessage validatorMessage) {
        this.validatorMessage = validatorMessage;
    }

    /**
     * Builds chains of booking validator objects.
     */
    public BookingValidator linkWith(BookingValidator next) {
        this.next = next;
        return next;
    }

    /**
     * Subclasses will implement this method with concrete checks.
     */
    public ValidatorMessage checkAndSet(Booking inputBooking, Booking outputBooking) {
        return checkAndSetNext(inputBooking, outputBooking);
    };

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    protected ValidatorMessage checkAndSetNext(Booking inputBooking, Booking outputBooking) {
        if (next == null) {
            validatorMessage.setMessage("Passed all booking validations.");
            validatorMessage.setResult(true);
            return validatorMessage;
        }
        return next.checkAndSet(inputBooking, outputBooking);
    }
}
