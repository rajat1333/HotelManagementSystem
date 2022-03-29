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
public class LoginService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public ResponseEntity<Object> login(Customer customer) {
        try {
            List<Customer> user= customerRepository.findByEmail(customer.getEmail());
            if(user.size()<1)
            {
                return ResponseEntity.badRequest().body(new ErrorMessage("Email is not registered with us"));
            }
            else if (user.get(0).getPassword().contentEquals(customer.getPassword()))
            {
                return ResponseEntity.ok(user.get(0));
            }
            else
            {
                return ResponseEntity.badRequest().body(new ErrorMessage("Invalid Credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

}
