package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spartanbots.v01.entity.Users.Customer;
import spartanbots.v01.service.SignUpService;

@RestController
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String addHotel(@RequestBody Customer customer){
        return signUpService.signup(customer);
    }
}
