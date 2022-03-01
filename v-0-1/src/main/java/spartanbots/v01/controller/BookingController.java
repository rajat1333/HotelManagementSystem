package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.service.BookingService;

import java.util.List;

@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @RequestMapping(value = "info" , method = RequestMethod.GET)
    public String info(){
        return "The booking application is up...";
    }

    @RequestMapping(value = "createbooking", method = RequestMethod.POST)
    public String createBooking(@RequestBody Booking booking){
        return bookingService.createBooking(booking);
    }

    @RequestMapping(value = "readbooking", method = RequestMethod.GET)
    public List<Booking> readBooking() { return bookingService.readBooking(); }

    @RequestMapping(value = "updatebooking", method = RequestMethod.PUT)
    public String updateBooking(@RequestBody Booking booking){
        return bookingService.updateBooking(booking);
    }

    @RequestMapping(value = "deletebooking", method = RequestMethod.DELETE)
    public String deleteBooking(@RequestBody Booking booking){
        return bookingService.deleteBooking(booking);
    }
}
