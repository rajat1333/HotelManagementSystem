package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.service.AmenityService;

@RestController
public class AmenityController {

    @Autowired
    private AmenityService amenityService;

    @Autowired
    public AmenityController( AmenityService amenityService ) {
        this.amenityService = amenityService;
    }

    @RequestMapping(value = "createamenity", method = RequestMethod.POST)
    public ResponseEntity<Object> createAmenity(@RequestBody Amenity amenity){
        return amenityService.createAmenity(amenity);
    }

    @RequestMapping(value = "readamenity", method = RequestMethod.GET)
    private ResponseEntity<Object> readAmenity() {
        return amenityService.readAmenity();}

    @RequestMapping(value = "updateamenity", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateAmenity(@RequestBody Amenity amenity){
        return amenityService.updateAmenity(amenity);
    }

    @RequestMapping(value = "deleteamenity", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteAmenity(@RequestBody Amenity amenity){
        return amenityService.deleteAmenity(amenity);
    }

    @RequestMapping(value = "searchamenity", method = RequestMethod.POST)
    public ResponseEntity<Object> searchAmenity(@RequestBody Amenity amenity) {
        return amenityService.searchAmenity(amenity); }
}
