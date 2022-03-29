package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.repository.HotelRepository;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository ){
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public String createHotel(Hotel hotel) {
        try {
            Hotel hotelToBeCreated = new Hotel();
            hotelToBeCreated.setId(hotelRepository.findAll().size() == 0 ? 1 : hotelRepository.findAll().stream().max(Comparator.comparingInt(Hotel::getId)).get().getId() + 1);
            System.out.println("id: \n "+hotelToBeCreated.getId());
            hotelRegularization(hotel, hotelToBeCreated);
            hotelRepository.save(hotelToBeCreated);
            System.out.println("Hotel record created: \n" + hotel.toString());
            return "Hotel record created successfully.";
        } catch (Exception e) {
            throw e;
        }
    }
    public List<Hotel> readHotel() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> searchHotel(int id) { return hotelRepository.findById(id); }

    @Transactional
    public String updateHotel(Hotel hotel) {
        if (hotelRepository.existsById(hotel.getId())) {
            try {
                Hotel hotelToBeUpdated = hotelRepository.findById(hotel.getId()).get();
                hotelRegularization(hotel, hotelToBeUpdated);
                hotelRepository.save(hotelToBeUpdated);
                System.out.println("Hotel record updated: \n" + hotelToBeUpdated.toString());

                return "Hotel record updated successfully.";
            } catch (Exception e) {
                throw e;
            }
        } else {
            return "Hotel record does not exists.";
        }
    }

    @Transactional
    public String deleteHotel(Hotel hotel) {
        if(hotelRepository.existsById(hotel.getId())){
            try {
                System.out.println("Hotel record deleted: \n" + hotel.toString());
                hotelRepository.deleteById(hotel.getId());
                return "Hotel record deleted successfully.";
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return "Hotel record does not exists.";
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
