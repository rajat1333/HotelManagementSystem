package spartanbots.v01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Users.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    @Query("{email:'?0'}")
    Customer findByEmail(String email);

}
