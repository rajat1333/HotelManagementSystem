package spartanbots.v01.service;

import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.Booking;
import spartanbots.v01.repository.AmenityRepository;
import spartanbots.v01.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, AmenityRepository amenityRepository) {
        this.bookingRepository = bookingRepository;
        this.amenityRepository = amenityRepository;
    }

    @Transactional
    public String createBooking(Booking booking) {
        try {
            booking.setId(bookingRepository.findAll().size() == 0 ? 0 : bookingRepository.findAll().stream().max(Comparator.comparingInt(Booking::getId)).get().getId() + 1);
            List<Amenity> outputAmenities = autoAmenityMapping(booking.getAmenities());
            booking.setAmenities(outputAmenities);
            bookingRepository.save(booking);
            System.out.println("Booking record created: \n" + booking.toString());
            return "Booking record created successfully.";
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Booking> readBooking() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> searchBooking(int id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public String updateBooking(Booking booking) {

        if(bookingRepository.existsById(booking.getId())){
            try{
                Booking bookingToBeUpdated = bookingRepository.findById(booking.getId()).get();
                if (booking.getName() != null) {
                    bookingToBeUpdated.setName(booking.getName());
                }
                if (booking.getPhone() != null) {
                    bookingToBeUpdated.setPhone(booking.getPhone());
                }
                if (booking.getHotel() != null) {
                    bookingToBeUpdated.setHotel(booking.getHotel());
                }
                List<Amenity> outputAmenities = autoAmenityMapping(booking.getAmenities());
                bookingToBeUpdated.setAmenities(outputAmenities);
                bookingRepository.save(bookingToBeUpdated);
                System.out.println("Booking record updated: \n" + booking.toString());
                return "Booking record updated successfully.";
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return "Booking record does not exists.";
        }
    }

    @Transactional
    public String deleteBooking(Booking booking) {
        if(bookingRepository.existsById(booking.getId())){
            try{
                System.out.println("Booking record deleted: \n" + booking.toString());
                bookingRepository.deleteById(booking.getId());
                return "Booking record deleted successfully.";
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return "Booking record does not exists.";
        }
    }

    private List<Amenity> autoAmenityMapping(List<Amenity> inputAmenities) {
        List<Amenity> outputAmenities = new ArrayList<>();
        for(Amenity originalAmenity : inputAmenities){
            if (amenityRepository.existsById(originalAmenity.getId())) {
                Amenity finalAmenity = amenityRepository.findById(originalAmenity.getId()).get();
                outputAmenities.add(finalAmenity);
            }
        }
        return outputAmenities;
    }
}
