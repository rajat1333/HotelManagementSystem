package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.repository.BookingRepository;
import spartanbots.v01.repository.HotelRepository;

import javax.transaction.Transactional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Transactional
    public String addHotel(Hotel hotel){
        try {
            hotelRepository.save(hotel);
                return "Booking record created successfully.";
            }
        catch (Exception e){
            throw e;
        }
    }

}
