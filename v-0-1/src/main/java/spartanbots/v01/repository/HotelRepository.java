package spartanbots.v01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.entity.Users.Customer;

import java.util.List;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, Integer> {

    @Query("{name:'?0'}")
    List<Hotel> findHotelByName(String name);


    //public long count();

}