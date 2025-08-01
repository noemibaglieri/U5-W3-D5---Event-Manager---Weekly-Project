package noemibaglieri.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name = "organiser_id", nullable = false)
    private User organiser;

    public Event(String title, String description, String location, LocalDateTime date, int availableSeats, User organiser) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.availableSeats = availableSeats;
        this.organiser = organiser;
    }
}
