package spartanbots.v01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Bill;
import spartanbots.v01.entity.Users.Customer;

import java.util.List;

/**
 * @author Rajat Masurkar
 */
@Repository
public interface BillRepository extends MongoRepository<Bill, Integer> {
    @Query("{id:'?0'}")
    List<Bill> findById(String id);
}
