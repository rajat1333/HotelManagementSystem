package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.service.AmenityService;

import java.util.List;

@RestController
public class AmenityController {

    @Autowired
    private AmenityService amenityService;

    @Autowired
    public AmenityController( AmenityService amenityService ) { this.amenityService = amenityService;}

    @RequestMapping(value = "createamenity", method = RequestMethod.POST)
    public String createBooking(@RequestBody Amenity amenity){
        return amenityService.createAmenity(amenity);
    }

    @RequestMapping(value = "readamenity", method = RequestMethod.GET)
    private List<Amenity> findAllAmenity() { return amenityService.readAmenity();}

    @RequestMapping(value = "updateamenity", method = RequestMethod.PUT)
    public String updateBooking(@RequestBody Amenity amenity){
        return amenityService.updateAmenity(amenity);
    }

    @RequestMapping(value = "deleteamenity", method = RequestMethod.DELETE)
    public String deleteBooking(@RequestBody Amenity amenity){
        return amenityService.deleteAmenity(amenity);
    }
}
