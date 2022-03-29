package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.Users.Customer;
import spartanbots.v01.repository.CustomerRepository;
import spartanbots.v01.repository.HotelRepository;

import javax.transaction.Transactional;
import java.util.Comparator;

@Service
public class LoginService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public String login(Customer customer) {
        try {
            if (customerRepository.findByEmail(customer.getEmail()).getPassword()==customer.getPassword())
            {
                System.out.println("Email and password are correct");
                return "Credentials are valid";
            }
            return "Credentials are invalid";
        } catch (Exception e) {
            throw e;
        }
    }

}
