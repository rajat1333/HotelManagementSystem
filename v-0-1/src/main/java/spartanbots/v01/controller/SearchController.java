package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spartanbots.v01.entity.Search;
import spartanbots.v01.service.SearchService;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public ResponseEntity<Object> search(@RequestBody Search search){
        return searchService.search(search);
    }


}
