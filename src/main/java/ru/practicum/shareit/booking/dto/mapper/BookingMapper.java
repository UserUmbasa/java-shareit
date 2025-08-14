package ru.practicum.shareit.booking.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public abstract class BookingMapper {

    public abstract BookingResponseDto mapToBookingResponseDto(Booking booking);

    public Booking mapToBooking(BookingRequestDto book, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(book.getStart());
        booking.setEnd(book.getEnd());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingState.WAITING);
        return booking;
    }
}
