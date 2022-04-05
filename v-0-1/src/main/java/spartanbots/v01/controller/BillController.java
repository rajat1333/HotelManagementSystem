package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import spartanbots.v01.service.BillService;
import spartanbots.v01.service.HotelService;

/**
 * @author Rajat Masurkar
 */
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    public BillController( BillService billService ) { this.billService = billService;}


}
