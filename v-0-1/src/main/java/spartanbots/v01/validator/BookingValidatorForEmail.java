package spartanbots.v01.validator;

import spartanbots.v01.entity.Booking;

public class BookingValidatorForEmail extends BookingValidator{

    public BookingValidatorForEmail(BookingValidator bookingValidator) {
        super(bookingValidator);
    }

    public ValidatorMessage checkAndSet(Booking inputBooking, Booking outputBooking) {
        if (inputBooking.getCustomerEmail() != null) {
            outputBooking.setCustomerEmail(inputBooking.getCustomerEmail());
            logger.info("Customer email validation passed...");
            return checkAndSetNext(inputBooking, outputBooking);
        }
        else{
            logger.error("Customer email validation fail because customer email field is empty or null");
            validatorMessage.setMessage("Customer email field is empty or null");
            validatorMessage.setResult(false);
            return validatorMessage;
        }
    }
}
