package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.entity.Search;
import spartanbots.v01.service.BillService;
import spartanbots.v01.service.HotelService;

/**
 * @author Rajat Masurkar
 */
@RestController
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    public BillController( BillService billService ) { this.billService = billService;}

    @RequestMapping(value = "createBill", method = RequestMethod.POST)
    public ResponseEntity<Object> createBill(@RequestBody Booking booking){
        return billService.createBill(booking);
    }

}
