package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Room;
import spartanbots.v01.repository.HotelRepository;
import spartanbots.v01.repository.RoomRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public String createRoom(Room room) {
        try {
            Room roomToBeCreated = new Room();
            roomToBeCreated.setId(roomRepository.findAll().size() == 0 ? 1 : roomRepository.findAll().stream().max(Comparator.comparingInt(Room::getId)).get().getId() + 1);
            roomToBeCreated.setBookingIds(new ArrayList<Integer>());
            roomRegularization(room, roomToBeCreated);
            roomRepository.save(roomToBeCreated);
            System.out.println("Room record created: \n" + room.toString());
            return "Room record created successfully.";
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Room> readRoom() {
        return roomRepository.findAll();
    }

    public Optional<Room> searchRoom(int id) { return roomRepository.findById(id); }

    @Transactional
    public String updateRoom(Room room) {
        if (roomRepository.existsById(room.getId())) {
            try {
                Room roomToBeUpdated = roomRepository.findById(room.getId()).get();
                roomRegularization(room, roomToBeUpdated);
                roomRepository.save(roomToBeUpdated);
                System.out.println("Room record updated: \n" + roomToBeUpdated.toString());
                return "Room record updated successfully.";
            } catch (Exception e) {
                throw e;
            }
        } else {
            return "Room record does not exists.";
        }
    }

    @Transactional
    public String deleteRoom(Room room) {
        if(roomRepository.existsById(room.getId())){
            try{
                System.out.println("Room record deleted: \n" + room.toString());
                roomRepository.deleteById(room.getId());
                return "Room record deleted successfully.";
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return "Room record does not exists.";
        }
    }

    private void roomRegularization(Room inputRoom, Room outputRoom) {

        if (inputRoom.getName() != null) {
            outputRoom.setName(inputRoom.getName());
        }
        if (hotelRepository.existsById(inputRoom.getHotelId())) {
            outputRoom.setHotelId(inputRoom.getHotelId());
            outputRoom.setHotelName(hotelRepository.findById(outputRoom.getHotelId()).get().getName());
        }
        //int maxFloor = hotelRepository.findById(room.getId()).get().getMaxFloor();
        if (inputRoom.getFloor() != null && inputRoom.getFloor() <= hotelRepository.findById(inputRoom.getId()).get().getMaxFloor()) {
            outputRoom.setFloor(inputRoom.getFloor());
        }
        if (inputRoom.getRoomType() != null) {
            outputRoom.setRoomType(inputRoom.getRoomType());
        }
        if (inputRoom.getPrice() > 0) {
            outputRoom.setPrice(inputRoom.getPrice());
        }
    }
}


