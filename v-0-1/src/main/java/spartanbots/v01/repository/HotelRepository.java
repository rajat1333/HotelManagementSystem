package spartanbots.v01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Hotel;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, Integer> {

    //@Query("{name:'?0'}")
    //Hotel findItemByName(String name);

    //public long count();

}