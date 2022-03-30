package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import spartanbots.v01.entity.ErrorMessage;
import spartanbots.v01.entity.Search;
import spartanbots.v01.repository.HotelRepository;

import javax.transaction.Transactional;

public class SearchService {

    @Autowired
    private HotelRepository hotelRepository;

    @Transactional
    public ResponseEntity<Object> search(Search search) {
        try {

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }



}
