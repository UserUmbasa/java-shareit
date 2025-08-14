package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingState status;
    Item item;
    User booker;
}
