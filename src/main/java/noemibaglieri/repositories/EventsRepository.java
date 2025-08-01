package noemibaglieri.repositories;

import noemibaglieri.entities.Event;
import noemibaglieri.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganiser(User organiser);
    List<Event> findByAvailableSeatsGreaterThanAndDateAfter(int minSeats, LocalDateTime dateTime);
    boolean existsByTitleAndDateAndOrganiser(String title, LocalDateTime date, User organiser);

}
