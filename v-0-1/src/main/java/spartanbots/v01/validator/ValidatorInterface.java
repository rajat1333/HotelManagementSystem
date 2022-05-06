package spartanbots.v01.validator;

import org.springframework.beans.factory.annotation.Autowired;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.repository.AmenityRepository;
import spartanbots.v01.repository.BookingRepository;
import spartanbots.v01.repository.HotelRepository;
import spartanbots.v01.repository.RoomRepository;

public interface ValidatorInterface {

    BookingValidator next = null;
    ValidatorMessage validatorMessage = new ValidatorMessage(false, null);

    BookingValidator linkWith(BookingValidator next);

    /**
     * Subclasses will implement this method with concrete checks.
     */
    ValidatorMessage checkAndSet(Booking inputBooking, Booking outputBooking);

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    ValidatorMessage checkAndSetNext(Booking inputBooking, Booking outputBooking);
}
