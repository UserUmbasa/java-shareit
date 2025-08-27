package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validation.Marker;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    @NotNull(groups = Marker.OnCreate.class, message = "айди не указан")
    private Long itemId;
    @NotNull(groups = Marker.OnCreate.class, message = "дата не указана")
    private LocalDateTime start;
    @NotNull(groups = Marker.OnCreate.class, message = "дата не указана")
    private LocalDateTime end;
}
