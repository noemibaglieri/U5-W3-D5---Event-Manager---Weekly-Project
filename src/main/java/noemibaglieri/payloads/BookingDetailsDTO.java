package noemibaglieri.payloads;

import java.time.LocalDateTime;

public record BookingDetailsDTO(
        long bookingId,
        String eventTitle,
        String eventLocation,
        LocalDateTime eventDate
) {}

