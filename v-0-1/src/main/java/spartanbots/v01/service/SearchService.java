package spartanbots.v01.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spartanbots.v01.entity.*;
import spartanbots.v01.repository.BookingRepository;
import spartanbots.v01.entity.ErrorMessage;
import spartanbots.v01.entity.Search;
import spartanbots.v01.repository.HotelRepository;
import spartanbots.v01.repository.RoomRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    public SearchService(HotelRepository hotelRepository ,BookingRepository bookingRepository,RoomRepository roomRepository){
        this.hotelRepository = hotelRepository;
        this.bookingRepository=bookingRepository;
        this.roomRepository=roomRepository;
    }

    public ResponseEntity<Object> getAvailableHotels(Search search) {
        if(hotelRepository.findByCityRegexMatch(search.getDestinationName()).size()>0)
        {
            List<Hotel> hotelList=hotelRepository.findByCityRegexMatch(search.getDestinationName());
            List<Integer> hotelIdToRemove= new ArrayList<>();
            for (Hotel hotel:hotelList) {
                boolean removeHotelfromResult=true;
                List<Room> roomList=roomRepository.findRoomByHotelId(hotel.getId());
                    for (Room room:roomList) {
                    if(isRoomAvailable(search,room))
                    {
                        removeHotelfromResult=false;
                    }
                }
                if (removeHotelfromResult==true)
                {
                      hotelIdToRemove.add(hotel.getId());
                }
            }
            for(int i=0;i<hotelIdToRemove.size();i++)
            {
                final int j=i;
                hotelList.removeIf(h -> h.getId() == hotelIdToRemove.get(j));
            }
            return ResponseEntity.ok(hotelList);
        }
        else
        {
            return ResponseEntity.badRequest().body(new ErrorMessage("Sorry, we don't have any hotels available for your search"));
        }
    }

    public ResponseEntity<Object> getAvailableRooms(Search search) {
        if(!roomRepository.findRoomByHotelId(search.getHotelId()).isEmpty())
        {
            List<Room> roomList=roomRepository.findRoomByHotelId(search.getHotelId());
            List<Integer> roomIdToRemove= new ArrayList<>();
            boolean removeRoomfromResult=true;
            for (Room room:roomList) {
                if(!isRoomAvailable(search,room))
                {
                    roomIdToRemove.add(room.getId());
                }
            }
            for(int i=0;i<roomIdToRemove.size();i++)
            {
                final int j=i;
                roomList.removeIf(h -> h.getId() == roomIdToRemove.get(j));
            }
            if(roomList.isEmpty())
            {
                return ResponseEntity.badRequest().body(new ErrorMessage("No rooms available"));
            }
            else
            {
                return ResponseEntity.ok(roomList);
            }
        }
        else
        {
            return ResponseEntity.badRequest().body(new ErrorMessage("Hotel Record not found"));
        }
    }



    private Boolean isRoomAvailable(Search search, Room room) {
        Date currentBookingFrom = search.getStartDate();
        Date currentBookingTo = search.getEndDate();
        if (currentBookingFrom == null || currentBookingTo == null) {
            return false;
        }
        Boolean checkRange = currentBookingFrom.before(currentBookingTo);
        if (checkRange) {
            List<Integer> existedBookingIds = roomRepository.findById(room.getId()).get().getBookingIds();
            if (existedBookingIds.isEmpty() || existedBookingIds == null) {
                return true;
            }
            for (Integer existedBookingId : existedBookingIds) {
                Date existedBookingFrom = bookingRepository.findById(existedBookingId).get().getBookFrom();
                Date existedBookingTo = bookingRepository.findById(existedBookingId).get().getBookTo();
                Boolean before = currentBookingFrom.before(existedBookingFrom);
                Boolean checkBefore = currentBookingTo.before(existedBookingFrom) || currentBookingTo.equals(existedBookingFrom);
                Boolean after = currentBookingTo.after(existedBookingTo);
                Boolean checkAfter = currentBookingFrom.after(existedBookingTo) || currentBookingFrom.equals(existedBookingTo);
                if (before) {
                    if (!checkBefore) {
                        return false;
                    }
                    //case 1 : current [1, 7] and existed [5, 10]
                    if (after) {
                        return false;
                    }
                    //case 2 : current [1, 12] and existed [5, 10]
                } else {
                    if (!checkAfter) {
                        return false;
                    }
                    //case 3 : current [7, 12] and existed [5, 10]
                    if (!after) {
                        return false;
                    }
                    //case 4 : current [7, 8] and existed [5, 10]
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
