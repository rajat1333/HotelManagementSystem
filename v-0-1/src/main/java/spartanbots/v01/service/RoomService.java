package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.ErrorMessage;
import spartanbots.v01.entity.Room;
import spartanbots.v01.repository.HotelRepository;
import spartanbots.v01.repository.RoomRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;


@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public ResponseEntity<Object> createRoom(Room room) {
        try {
            Room roomToBeCreated = new Room();
            roomToBeCreated.setId(roomRepository.findAll().size() == 0 ? 1 : roomRepository.findAll().stream().max(Comparator.comparingInt(Room::getId)).get().getId() + 1);
            roomToBeCreated.setBookingIds(new ArrayList<Integer>());
            roomRegularization(room, roomToBeCreated);
            roomRepository.save(roomToBeCreated);
            System.out.println("Room record created: \n" + roomToBeCreated.toString());
            return ResponseEntity.ok(roomToBeCreated);
        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity<Object> readRoom() { return ResponseEntity.ok(roomRepository.findAll()); }

    @Transactional
    public ResponseEntity<Object> updateRoom(Room room) {
        if (roomRepository.existsById(room.getId())) {
            try {
                Room roomToBeUpdated = roomRepository.findById(room.getId()).get();
                roomRegularization(room, roomToBeUpdated);
                roomRepository.save(roomToBeUpdated);
                System.out.println("Room record updated: \n" + roomToBeUpdated.toString());
                return ResponseEntity.ok(roomToBeUpdated);
            } catch (Exception e) {
                throw e;
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Room record does not exists."));
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteRoom(Room room) {
        if(roomRepository.existsById(room.getId())){
            try{
                Room roomToBeDeleted = roomRepository.findById(room.getId()).get();
                roomRepository.deleteById(roomToBeDeleted.getId());
                System.out.println("Room record deleted: \n" + roomToBeDeleted.toString());
                return ResponseEntity.ok(roomToBeDeleted);
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Room record does not exists."));
        }
    }

    public ResponseEntity<Object> searchRoom(Room room) {
        if(roomRepository.existsById(room.getId())){
            return ResponseEntity.ok(roomRepository.findById(room.getId()));
        }
        else{
            return ResponseEntity.badRequest().body(new ErrorMessage("Room record does not exists."));
        }
    }

    public ResponseEntity<Object> roomAvailability(Room room) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Room not available"));
    }

    private void roomRegularization(Room inputRoom, Room outputRoom) {

        if (inputRoom.getName() != null) {
            outputRoom.setName(inputRoom.getName());
        }
        //String hotelName = hotelRepository.findById(inputRoom.getHotelId()).get().getName()
        if (hotelRepository.existsById(inputRoom.getHotelId())) {
            outputRoom.setHotelId(inputRoom.getHotelId());
            outputRoom.setHotelName(hotelRepository.findById(inputRoom.getHotelId()).get().getName());
        }
        //int hotelMaxFloor = hotelRepository.findById(inputRoom.getHotelId()).get().getMaxFloor();
        if (inputRoom.getFloor() != null && inputRoom.getFloor() <= hotelRepository.findById(inputRoom.getHotelId()).get().getMaxFloor()) {
            outputRoom.setFloor(inputRoom.getFloor());
        }
        if (inputRoom.getRoomType() != null) {
            outputRoom.setRoomType(inputRoom.getRoomType());
        }
        if (inputRoom.getPrice() > 0) {
            outputRoom.setPrice(inputRoom.getPrice());
            //float hotelBasePrice = hotelRepository.findById(outputRoom.getHotelId()).get().getBasePrice()
            if (inputRoom.getPrice() < hotelRepository.findById(outputRoom.getHotelId()).get().getBasePrice()) {
                hotelRepository.findById(outputRoom.getHotelId()).get().setBasePrice(inputRoom.getPrice());
            }
        }
    }
}


