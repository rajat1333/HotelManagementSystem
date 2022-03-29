package spartanbots.v01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.Booking;

import java.util.List;

@Repository
public interface AmenityRepository extends MongoRepository<Amenity, Integer> {
    @Query("{_id:'?0'}")
    Amenity findItemById(int id);

}
