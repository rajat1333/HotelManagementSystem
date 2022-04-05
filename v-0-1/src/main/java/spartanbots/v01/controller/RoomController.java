package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spartanbots.v01.entity.Room;
import spartanbots.v01.service.RoomService;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    public RoomController( RoomService roomService ) {
        this.roomService = roomService;
    }

    @RequestMapping(value = "createroom", method = RequestMethod.POST)
    public ResponseEntity<Object> createRoom(@RequestBody Room room){
        return roomService.createRoom(room);
    }

    @RequestMapping(value = "readroom", method = RequestMethod.GET)
    private ResponseEntity<Object> readRoom() {
        return roomService.readRoom();
    }

    @RequestMapping(value = "updateroom", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateRoom(@RequestBody Room room){
        return roomService.updateRoom(room);
    }

    @RequestMapping(value = "deleteroom", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteRoom(@RequestBody Room room){
        return roomService.deleteRoom(room);
    }

    @RequestMapping(value = "searchroom", method = RequestMethod.POST)
    public ResponseEntity<Object> searchRoom(@RequestBody Room room){
        return roomService.searchRoom(room);
    }
}
