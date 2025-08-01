package noemibaglieri.repositories;

import noemibaglieri.entities.Booking;
import noemibaglieri.entities.Event;
import noemibaglieri.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    boolean existsByUserAndEvent(User user, Event event);
    List<Booking> findByEvent(Event event);
}

