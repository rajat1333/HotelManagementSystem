package spartanbots.v01.validator;

import spartanbots.v01.entity.Booking;
import spartanbots.v01.entity.Room;

import java.util.Date;
import java.util.List;

public class BookingValidatorForDate extends BookingValidator{

    public BookingValidatorForDate(BookingValidator bookingValidator) {
        super(bookingValidator);
    }

    public ValidatorMessage checkAndSet(Booking inputBooking, Booking outputBooking) {
        if (bookingDateRangeValidation(inputBooking)) {
            outputBooking.setBookFrom(inputBooking.getBookFrom());
            outputBooking.setBookTo(inputBooking.getBookTo());
            outputBooking.setBookTime(new Date());
            logger.info("Booking date range validation pass...");
            return checkAndSetNext(inputBooking, outputBooking);
        }
        else{
            logger.error("Booking date range validation fail because booking date range is invalid");
            validatorMessage.setMessage("Booking date range is invalid");
            validatorMessage.setResult(false);
            return validatorMessage;
        }
    }

    private Boolean bookingDateRangeValidation(Booking inputBooking) {
        Date currentBookingFrom = inputBooking.getBookFrom();
        Date currentBookingTo = inputBooking.getBookTo();
        Date currentBookingTime = new Date();
        if (currentBookingFrom == null || currentBookingTo == null) {
            logger.error("Date bookingTo or bookingFrom is null");
            return false;
        }

        boolean checkRange = currentBookingFrom.before(currentBookingTo) &&
                (currentBookingFrom.after(currentBookingTime) || currentBookingFrom.equals(currentBookingTime));

        //boolean checkRange = currentBookingFrom.before(currentBookingTo);
        if (checkRange) {
            //there should not be need for this check as we will be showing only available rooms on UI
            List<Room> roomList = inputBooking.getRooms();
//            if(roomList==null ||roomList.isEmpty())
//                return false;
            for (Room currentRoom : roomList) {
                List<Integer> existedBookingIds = roomRepository.findById(currentRoom.getId()).get().getBookingIds();
                if (existedBookingIds.isEmpty() || existedBookingIds == null) {
                    continue;
                }
                if (existedBookingIds.contains(inputBooking.getId())) {
                    existedBookingIds.remove(Integer.valueOf(inputBooking.getId()));
                }
                if(currentDateViolateWithExistedDate(inputBooking, existedBookingIds)){
                    return false;
                }
            }
            return true;
        } else {
            logger.error("Date bookingTo is before bookingFrom, which is invalid");
            return false;
        }
    }

    private boolean currentDateViolateWithExistedDate(Booking inputBooking, List<Integer> existedBookingIds){
        Date currentBookingFrom = inputBooking.getBookFrom();
        Date currentBookingTo = inputBooking.getBookTo();
        for (Integer existedBookingId : existedBookingIds) {
            Date existedBookingFrom = bookingRepository.findById(existedBookingId).get().getBookFrom();
            Date existedBookingTo = bookingRepository.findById(existedBookingId).get().getBookTo();
            boolean before = currentBookingFrom.before(existedBookingFrom);
            boolean checkBefore = currentBookingTo.before(existedBookingFrom) || currentBookingTo.equals(existedBookingFrom);
            boolean after = currentBookingTo.after(existedBookingTo);
            boolean checkAfter = currentBookingFrom.after(existedBookingTo) || currentBookingFrom.equals(existedBookingTo);
            if (before) {
                if (!checkBefore) {
                    //case 1 : current [1, 7] and existed [5, 10]
                    logDateViolationInfo(inputBooking.getId(), currentBookingFrom, currentBookingTo,
                            existedBookingId, existedBookingFrom, existedBookingTo);
                    return true;
                }
                if (after) {
                    //case 2 : current [1, 12] and existed [5, 10]
                    logDateViolationInfo(inputBooking.getId(), currentBookingFrom, currentBookingTo,
                            existedBookingId, existedBookingFrom, existedBookingTo);
                    return true;
                }
            } else {
                if (!checkAfter) {
                    //case 3 : current [7, 12] and existed [5, 10]
                    logDateViolationInfo(inputBooking.getId(), currentBookingFrom, currentBookingTo,
                            existedBookingId, existedBookingFrom, existedBookingTo);
                    return true;
                }
                if (!after) {
                    //case 4 : current [7, 8] and existed [5, 10]
                    logDateViolationInfo(inputBooking.getId(), currentBookingFrom, currentBookingTo,
                            existedBookingId, existedBookingFrom, existedBookingTo);
                    return true;
                }
            }
        }
        return false;
    }

    private void logDateViolationInfo(int currentBookingId, Date currentBookingFrom, Date currentBookingTo, int existedBookingId, Date existedBookingFrom, Date existedBookingTo){
        logger.info("Current booking id " + currentBookingId +
                " has date range: " + currentBookingFrom + " ~ " + currentBookingTo);
        logger.info("Existing booking id " + existedBookingId +
                " has date range: " + existedBookingFrom + " ~ " + existedBookingTo);
        logger.error("The current booking date violates the existed booking date");
    }

}