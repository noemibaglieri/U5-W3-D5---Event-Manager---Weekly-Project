package noemibaglieri.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NewEventDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String location,
        @NotNull @Future LocalDateTime date,
        @Min(1) int availableSeats
) {}
