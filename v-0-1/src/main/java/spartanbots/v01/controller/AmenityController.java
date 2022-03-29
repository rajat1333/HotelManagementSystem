package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.service.AmenityService;

import java.util.List;
import java.util.Optional;

@RestController
public class AmenityController {

    @Autowired
    private AmenityService amenityService;

    @Autowired
    public AmenityController( AmenityService amenityService ) { this.amenityService = amenityService;}

    @RequestMapping(value = "createamenity", method = RequestMethod.POST)
    public String createAmenity(@RequestBody Amenity amenity){
        return amenityService.createAmenity(amenity);
    }

    @RequestMapping(value = "readamenity", method = RequestMethod.GET)
    private List<Amenity> readAmenity() { return amenityService.readAmenity();}

    @RequestMapping(value = "searchamenity/{id}", method = RequestMethod.GET)
    public Optional<Amenity> searchAmenity(@PathVariable int id) { return amenityService.searchAmenity(id); }

    @RequestMapping(value = "updateamenity", method = RequestMethod.PUT)
    public String updateAmenity(@RequestBody Amenity amenity){
        return amenityService.updateAmenity(amenity);
    }

    @RequestMapping(value = "deleteamenity", method = RequestMethod.DELETE)
    public String deleteAmenity(@RequestBody Amenity amenity){
        return amenityService.deleteAmenity(amenity);
    }
}
