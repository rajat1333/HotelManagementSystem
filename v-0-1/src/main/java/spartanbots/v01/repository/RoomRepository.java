package spartanbots.v01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository  extends MongoRepository<Room, Integer> {


    @Query("{'hotelId': ?0}")
    List<Room> findRoomByHotelId(int hotelId);
}
