package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.Amenity;
import spartanbots.v01.repository.AmenityRepository;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AmenityService {

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    public AmenityService(AmenityRepository amenityRepository ){
        this.amenityRepository = amenityRepository;
    }

    @Transactional
    public String createAmenity(Amenity amenity) {
        try {
            Amenity amenityToBeCreated = new Amenity();
            amenityToBeCreated.setId(amenityRepository.findAll().size() == 0 ? 1 : amenityRepository.findAll().stream().max(Comparator.comparingInt(Amenity::getId)).get().getId() + 1);
            AmenityRegularization(amenity, amenityToBeCreated);
            amenityRepository.save(amenityToBeCreated);
            System.out.println("Amenity record created: \n" + amenity.toString());
            return "Amenity record created successfully.";
        } catch (Exception e) {
            throw e;
        }
    }
    public List<Amenity> readAmenity() {
        return amenityRepository.findAll();
    }

    public Optional<Amenity> searchAmenity(int id) { return amenityRepository.findById(id); }

    @Transactional
    public String updateAmenity(Amenity amenity) {
        if (amenityRepository.existsById(amenity.getId())) {
            try {
                Amenity amenityToBeUpdated = amenityRepository.findById(amenity.getId()).get();
                AmenityRegularization(amenity, amenityToBeUpdated);
                amenityRepository.save(amenityToBeUpdated);
                System.out.println("Amenity record updated: \n" + amenityToBeUpdated.toString());
                return "Amenity record updated successfully.";
            } catch (Exception e) {
                throw e;
            }
        } else {
            return "Amenity record does not exists.";
        }
    }

    @Transactional
    public String deleteAmenity(Amenity amenity) {
        if(amenityRepository.existsById(amenity.getId())){
            try {
                System.out.println("Amenity record deleted: \n" + amenity.toString());
                amenityRepository.deleteById(amenity.getId());
                return "Amenity record deleted successfully.";
            } catch (Exception e) {
                throw e;
            }
        }
        else {
            return "Amenity record does not exists.";
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
