package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.ErrorMessage;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.repository.HotelRepository;

import javax.transaction.Transactional;
import java.util.Comparator;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository ){
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public ResponseEntity<Object> createHotel(Hotel hotel) {
        try {
            Hotel hotelToBeCreated = new Hotel();
            hotelToBeCreated.setId(hotelRepository.findAll().size() == 0 ? 1 : hotelRepository.findAll().stream().max(Comparator.comparingInt(Hotel::getId)).get().getId() + 1);
            hotelRegularization(hotel, hotelToBeCreated);
            hotelRepository.save(hotelToBeCreated);
            System.out.println("Hotel record created: \n" + hotelToBeCreated.toString());
            return ResponseEntity.ok(hotelToBeCreated);
        } catch (Exception e) {
            throw e;
        }
    }
    public ResponseEntity<Object> readHotel() {
        return ResponseEntity.ok(hotelRepository.findAll());
    }

    @Transactional
    public ResponseEntity<Object> updateHotel(Hotel hotel) {
        if (hotelRepository.existsById(hotel.getId())) {
            try {
                Hotel hotelToBeUpdated = hotelRepository.findById(hotel.getId()).get();
                hotelRegularization(hotel, hotelToBeUpdated);
                hotelRepository.save(hotelToBeUpdated);
                System.out.println("Hotel record updated: \n" + hotelToBeUpdated.toString());
                return ResponseEntity.ok(hotelToBeUpdated);
            } catch (Exception e) {
                throw e;
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Hotel record does not exists."));
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteHotel(Hotel hotel) {
        if(hotelRepository.existsById(hotel.getId())){
            try {
                Hotel hotelToBeDeleted = hotelRepository.findById(hotel.getId()).get();
                hotelRepository.deleteById(hotelToBeDeleted.getId());
                System.out.println("Hotel record deleted: \n" + hotelToBeDeleted.toString());
                return ResponseEntity.ok(hotelToBeDeleted);
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Hotel record does not exists."));
        }
    }

    public ResponseEntity<Object> searchHotel(Hotel hotel) {
        if(hotelRepository.existsById(hotel.getId())){
            return ResponseEntity.ok(hotelRepository.findById(hotel.getId()));
        }
        else{
            return ResponseEntity.badRequest().body(new ErrorMessage("Hotel record does not exists."));
        }
    }

    @Transactional
    public ResponseEntity<Hotel> addHotel(Hotel hotel) {
        if(hotelRepository.existsById(hotel.getId())){
            try {
                System.out.println("Hotel with given Id already Present: \n" + hotel.toString());
                //hotelRepository.deleteById(hotel.getId());

                //return ResponseEntity.ok(""); //todo : add return format
                return null;
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            try {
                hotelRepository.save(hotel);
                List<Hotel> hotels= hotelRepository.findHotelByName(hotel.getName());
                return ResponseEntity.ok(hotels.get(0));
            } catch (Exception e) {
                throw e;
            }
        }
    }

    private void hotelRegularization(Hotel inputHotel, Hotel outputHotel) {
        if (inputHotel.getName() != null) {
            outputHotel.setName(inputHotel.getName());
        }
        if (inputHotel.getCity() != null) {
            outputHotel.setCity(inputHotel.getCity());
        }
        if (inputHotel.getMaxFloor() != null && inputHotel.getMaxFloor() > 0) {
            outputHotel.setMaxFloor(inputHotel.getMaxFloor());
        }
    }

}
