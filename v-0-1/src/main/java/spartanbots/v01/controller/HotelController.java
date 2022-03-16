package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.repository.HotelRepository;
import spartanbots.v01.service.BookingService;
import spartanbots.v01.service.HotelService;

import java.util.List;

@RestController
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @RequestMapping(value = "addhotel", method = RequestMethod.POST)
    public String addHotel(@RequestBody Hotel hotel){
        return hotelService.addHotel(hotel);
    }

}
