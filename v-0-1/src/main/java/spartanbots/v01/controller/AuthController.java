package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.service.AmenityService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@RequestBody Amenity amenity){
        return authService.login(amenity);
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String createBooking(@RequestBody Amenity amenity){
        return authService.signup(amenity);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public String createBooking(@RequestBody Amenity amenity){
        return authService.logout(amenity);
    }


}
