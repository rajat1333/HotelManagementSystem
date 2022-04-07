package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.repository.*;

/**
 * @author Rajat Masurkar
 */
@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    public BillService(BillRepository billRepository, BookingRepository bookingRepository, HotelRepository hotelRepository, RoomRepository roomRepository, AmenityRepository amenityRepository) {
        this.billRepository = billRepository;
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
    }
}
