package spartanbots.v01.validator;

import spartanbots.v01.entity.Booking;
import spartanbots.v01.repository.HotelRepository;

public class BookingValidatorForHotel extends BookingValidator{

    public BookingValidatorForHotel(BookingValidator bookingValidator){
        super(bookingValidator);
    }

    public ValidatorMessage checkAndSet(Booking inputBooking, Booking outputBooking) {
        if (hotelRepository.existsById(inputBooking.getHotelId())) {
            outputBooking.setHotelId(inputBooking.getHotelId());
            outputBooking.setHotelName(hotelRepository.findById(outputBooking.getHotelId()).get().getName());
            logger.info("Hotel id validation pass...");
            return checkAndSetNext(inputBooking, outputBooking);
        }
        else{
            logger.error("Hotel id validation fail because hotel id is invalid, empty or null");
            validatorMessage.setMessage("Hotel id is invalid, empty or null");
            validatorMessage.setResult(false);
            return validatorMessage;
        }
    }
}

