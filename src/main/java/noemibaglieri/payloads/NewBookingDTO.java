package noemibaglieri.payloads;

import jakarta.validation.constraints.NotNull;

public record NewBookingDTO(
        @NotNull(message = "Event ID is required")
        Long eventId
) {}

