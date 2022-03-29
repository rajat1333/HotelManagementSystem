package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.service.BookingService;

import java.util.List;
import java.util.Optional;

@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    public BookingController( BookingService bookingService ) { this.bookingService = bookingService;}

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

    @RequestMapping(value = "searchbooking/{id}", method = RequestMethod.POST)
    public Optional<Booking> searchBooking(@PathVariable int id) { return bookingService.searchBooking(id); }

    @RequestMapping(value = "updatebooking", method = RequestMethod.PUT)
    public String updateBooking(@RequestBody Booking booking){
        return bookingService.updateBooking(booking);
    }

    @RequestMapping(value = "deletebooking", method = RequestMethod.DELETE)
    public String deleteBooking(@RequestBody Booking booking){
        return bookingService.deleteBooking(booking);
    }
}
