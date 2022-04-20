package spartanbots.v01.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.entity.Room;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, Integer> {


    @Query("{'hotelId':?0,'roomId':?1}")
    List<Booking> findBookingByHotelIdRoomId(int hotelId,int roomId);


}
