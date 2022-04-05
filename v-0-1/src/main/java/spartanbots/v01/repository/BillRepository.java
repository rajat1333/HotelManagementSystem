package spartanbots.v01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Bill;

/**
 * @author Rajat Masurkar
 */
@Repository
public interface BillRepository extends MongoRepository<Bill, Integer> {
}
