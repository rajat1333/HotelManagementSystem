package spartanbots.v01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Room;

@Repository
public interface RoomRepository  extends MongoRepository<Room, Integer> {
}
