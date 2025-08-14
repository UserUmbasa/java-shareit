package ru.practicum.shareit.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingRequestDto {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
