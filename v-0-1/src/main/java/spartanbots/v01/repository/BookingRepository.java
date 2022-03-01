package spartanbots.v01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spartanbots.v01.entity.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    public boolean existsByPhone(String phone);

    public List<Booking> findByPhone(String phone);

    @Query("select max(b.id) from Booking b")
    public Integer findMaxId();
}
