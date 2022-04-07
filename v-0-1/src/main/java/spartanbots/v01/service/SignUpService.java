package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.ErrorMessage;
import spartanbots.v01.entity.Users.Customer;
import spartanbots.v01.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SignUpService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public ResponseEntity<Object> signup(Customer customer) {
        try {
            //First check if the email is already used by other user
            List<Customer> user= customerRepository.findByEmail(customer.getEmail());

            //If email already registered return error message
            if(user.size()>=1)
            {
                return ResponseEntity.badRequest().body(new ErrorMessage("Email is already registered with us"));
            }
            customerRepository.save(customer);
            List<Customer> currentUser= customerRepository.findByEmail(customer.getEmail());
            return ResponseEntity.ok(currentUser.get(0));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }
}
