package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Users.Customer;
import spartanbots.v01.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SignUpService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public ResponseEntity<Customer> signup(Customer customer) {
        try {
            customerRepository.save(customer);
            List<Customer> user= customerRepository.findByEmail(customer.getEmail());
            return ResponseEntity.ok(user.get(0));
        } catch (Exception e) {
            throw e;
        }
    }
}
