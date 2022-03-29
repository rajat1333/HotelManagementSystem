package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Users.Customer;
import spartanbots.v01.repository.CustomerRepository;

import javax.transaction.Transactional;

@Service
public class SignUpService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public String signup(Customer customer) {
        try {
            customerRepository.save(customer);
            return "New User has been created";
        } catch (Exception e) {
            throw e;
        }
    }
}
