package spartanbots.v01.validator;

import spartanbots.v01.entity.Booking;
import spartanbots.v01.entity.Room;

import java.util.List;

public class BookingValidatorForRooms extends BookingValidator{

    public BookingValidatorForRooms(BookingValidator bookingValidator) {
        super(bookingValidator);
    }

    public ValidatorMessage checkAndSet(Booking inputBooking, Booking outputBooking) {
        List<Room> roomList = inputBooking.getRooms();
        if(roomList!=null && !roomList.isEmpty()){
            for(Room room : roomList){
                if (!roomRepository.existsById(room.getId())){
                    logger.error("Room validation fail because room id " + room.getId() + " cannot be found in the room repository...");
                    validatorMessage.setMessage("Room id " + room.getId() + " cannot be found in the room repository");
                    validatorMessage.setResult(false);
                    return validatorMessage;
                }
            }
            logger.info("Room validation pass...");
            return checkAndSetNext(inputBooking, outputBooking);
        }
        else{
            logger.error("Room validation fail because room list is empty or null...");
            validatorMessage.setMessage("Room list is empty or null");
            validatorMessage.setResult(false);
            return validatorMessage;
        }
    }
}