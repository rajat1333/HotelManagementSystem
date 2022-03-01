package spartanbots.v01.service;

import spartanbots.v01.entity.Booking;
import spartanbots.v01.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public String createBooking(Booking booking){
        try {
            if (!bookingRepository.existsByPhone(booking.getPhone())){
                booking.setId(null == bookingRepository.findMaxId()? 0 : bookingRepository.findMaxId() + 1);
                bookingRepository.save(booking);
                System.out.println("Booking record created: \n" + booking.toString());
                return "Booking record created successfully.";
            }else {
                return "Booking record already exists.";
            }
        }catch (Exception e){
            throw e;
        }
    }

    public List<Booking> readBooking(){
        return bookingRepository.findAll();
    }

    @Transactional
    public String updateBooking(Booking booking){
        if (bookingRepository.existsByPhone(booking.getPhone())){
            try {
                List<Booking> bookings = bookingRepository.findByPhone(booking.getPhone());
                bookings.stream().forEach(b -> {
                    Booking bookingToBeUpdated = bookingRepository.findById(b.getId()).get();
                    if (booking.getName() != null) { bookingToBeUpdated.setName(booking.getName()); }
                    if (booking.getPhone() != null) { bookingToBeUpdated.setPhone(booking.getPhone()); }
                    if (booking.getHotel() != null) { bookingToBeUpdated.setHotel(booking.getHotel()); }
                    bookingRepository.save(bookingToBeUpdated);
                    System.out.println("Booking record updated: \n" + bookingToBeUpdated.toString());
                });
                return "Booking record updated successfully.";
            }catch (Exception e){
                throw e;
            }
        }else {
            return "Booking record does not exists.";
        }
    }

    @Transactional
    public String deleteBooking(Booking booking){
        if (bookingRepository.existsByPhone(booking.getPhone())){
            try {
                List<Booking> bookings = bookingRepository.findByPhone(booking.getPhone());
                bookings.stream().forEach(b -> {
                    System.out.println("Booking record deleted: \n" + b.toString());
                    bookingRepository.delete(b);
                });
                return "Booking record deleted successfully.";
            }catch (Exception e){
                throw e;
            }

        }else {
            return "Booking record does not exists.";
        }
    }
}
