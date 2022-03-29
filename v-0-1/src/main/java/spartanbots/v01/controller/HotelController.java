package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.service.HotelService;

import java.util.List;
import java.util.Optional;

@RestController
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    public HotelController( HotelService hotelService ) { this.hotelService = hotelService;}

    @RequestMapping(value = "createhotel", method = RequestMethod.POST)
    public String createHotel(@RequestBody Hotel hotel){
        return hotelService.createHotel(hotel);
    }

    @RequestMapping(value = "readhotel", method = RequestMethod.GET)
    private List<Hotel> readHotel() { return hotelService.readHotel();}

    @RequestMapping(value = "searchhotel/{id}", method = RequestMethod.GET)
    public Optional<Hotel> searchHotel(@PathVariable int id) { return hotelService.searchHotel(id); }

    @RequestMapping(value = "updatehotel", method = RequestMethod.PUT)
    public String updateHotel(@RequestBody Hotel hotel){
        return hotelService.updateHotel(hotel);
    }

    @RequestMapping(value = "deletehotel", method = RequestMethod.DELETE)
    public String deleteHotel(@RequestBody Hotel hotel){
        return hotelService.deleteHotel(hotel);
    }

}
