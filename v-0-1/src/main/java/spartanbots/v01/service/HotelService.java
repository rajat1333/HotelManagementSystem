package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.ErrorMessage;
import spartanbots.v01.entity.Hotel;
import spartanbots.v01.entity.Room;
import spartanbots.v01.repository.AmenityRepository;
import spartanbots.v01.repository.HotelRepository;
import spartanbots.v01.repository.RoomRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository,AmenityRepository amenityRepository,RoomRepository roomRepository){
        this.hotelRepository = hotelRepository;
        this.amenityRepository=amenityRepository;
        this.roomRepository=roomRepository;
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
        List<Hotel> hotelList=hotelRepository.findAll();
        if(!hotelList.isEmpty())
        {
            for (Hotel hotel:hotelList) {
                List<Room> roomList = roomRepository.findRoomByHotelId(hotel.getId());
                float minBasePrice = 0;
                for (Room room : roomList) {
                    if (minBasePrice == 0) {
                        minBasePrice = (float) room.getPrice();
                    } else if (minBasePrice > (float) room.getPrice()) {
                        minBasePrice = (float) room.getPrice();
                    }
                }hotel.setBasePrice(minBasePrice);
            }
            return ResponseEntity.ok(hotelList);
        }
        else
        {
            return ResponseEntity.badRequest().body(new ErrorMessage("No hotels found"));
        }
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

    @Transactional
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
        if (inputHotel.getBasePrice() > 0) {
            outputHotel.setBasePrice(inputHotel.getBasePrice());
        }
        if (inputHotel.getImageURL() != null) {
            outputHotel.setImageURL(inputHotel.getImageURL());
        }
        if (inputHotel.getMaxFloor() != null && inputHotel.getMaxFloor() > 0) {
            outputHotel.setMaxFloor(inputHotel.getMaxFloor());
        }
        if (inputHotel.getAmenities() != null) {
            //List<Amenity> outputAmenities = autoAmenityMapping(inputHotel.getAmenities());
            outputHotel.setAmenities(autoAmenityMapping(inputHotel.getAmenities()));
        }
    }

    public ResponseEntity<Object> getHotelDetails(Hotel hotel) {
        if(hotelRepository.existsById(hotel.getId())){
            return ResponseEntity.ok(hotelRepository.findById(hotel.getId()));
        }
        else{
            return ResponseEntity.badRequest().body(new ErrorMessage("Hotel record does not exists."));
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
