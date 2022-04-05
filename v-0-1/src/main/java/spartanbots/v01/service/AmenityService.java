package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.entity.ErrorMessage;
import spartanbots.v01.repository.AmenityRepository;

import javax.transaction.Transactional;
import java.util.Comparator;

@Service
public class AmenityService {

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    public AmenityService(AmenityRepository amenityRepository ){
        this.amenityRepository = amenityRepository;
    }

    @Transactional
    public ResponseEntity<Object> createAmenity(Amenity amenity) {
        try {
            Amenity amenityToBeCreated = new Amenity();
            amenityToBeCreated.setId(amenityRepository.findAll().size() == 0 ? 1 : amenityRepository.findAll().stream().max(Comparator.comparingInt(Amenity::getId)).get().getId() + 1);
            AmenityRegularization(amenity, amenityToBeCreated);
            amenityRepository.save(amenityToBeCreated);
            System.out.println("Amenity record created: \n" + amenityToBeCreated.toString());
            return ResponseEntity.ok(amenityToBeCreated);
        } catch (Exception e) {
            throw e;
        }
    }
    //List<Amenity>;
    public ResponseEntity<Object> readAmenity() {
        return ResponseEntity.ok(amenityRepository.findAll());
    }

    @Transactional
    public ResponseEntity<Object> updateAmenity(Amenity amenity) {
        if (amenityRepository.existsById(amenity.getId())) {
            try {
                Amenity amenityToBeUpdated = amenityRepository.findById(amenity.getId()).get();
                AmenityRegularization(amenity, amenityToBeUpdated);
                amenityRepository.save(amenityToBeUpdated);
                System.out.println("Amenity record updated: \n" + amenityToBeUpdated.toString());
                return ResponseEntity.ok(amenityToBeUpdated);
            } catch (Exception e) {
                throw e;
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Amenity record does not exists."));
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteAmenity(Amenity amenity) {
        if(amenityRepository.existsById(amenity.getId())){
            try {
                Amenity amenityToBeDeleted = amenityRepository.findById(amenity.getId()).get();
                amenityRepository.deleteById(amenityToBeDeleted.getId());
                System.out.println("Amenity record deleted: \n" + amenityToBeDeleted.toString());
                return ResponseEntity.ok(amenityToBeDeleted);
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return ResponseEntity.badRequest().body(new ErrorMessage("Amenity record does not exists."));
        }
    }

    //Optional<Amenity>
    public ResponseEntity<Object> searchAmenity(Amenity amenity) {
        if(amenityRepository.existsById(amenity.getId())){
            return ResponseEntity.ok(amenityRepository.findById(amenity.getId()));
        }
        else{
            return ResponseEntity.badRequest().body(new ErrorMessage("Amenity record does not exists."));
        }
    }

    private void AmenityRegularization(Amenity inputAmenity, Amenity outputAmenity) {
        if (inputAmenity.getName() != null) {
            outputAmenity.setName(inputAmenity.getName());
        }
        if (inputAmenity.getPrice() > 0) {
            outputAmenity.setPrice(inputAmenity.getPrice());
        }
    }
}
