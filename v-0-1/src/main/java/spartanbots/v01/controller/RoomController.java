package spartanbots.v01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spartanbots.v01.entity.Room;
import spartanbots.v01.service.RoomService;

import java.util.List;
import java.util.Optional;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    public RoomController( RoomService roomService ) { this.roomService = roomService;}

    @RequestMapping(value = "createroom", method = RequestMethod.POST)
    public String createRoom(@RequestBody Room room){
        return roomService.createRoom(room);
    }

    @RequestMapping(value = "readroom", method = RequestMethod.GET)
    private List<Room> readRoom() { return roomService.readRoom();}

    @RequestMapping(value = "searchroom/{id}", method = RequestMethod.GET)
    public Optional<Room> searchRoom(@PathVariable int id) { return roomService.searchRoom(id); }

    @RequestMapping(value = "updateroom", method = RequestMethod.PUT)
    public String updateRoom(@RequestBody Room room){
        return roomService.updateRoom(room);
    }

    @RequestMapping(value = "deleteroom", method = RequestMethod.DELETE)
    public String deleteRoom(@RequestBody Room room){ return roomService.deleteRoom(room); }
}
