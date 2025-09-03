package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingResponseDto addBooking(String userId, BookingRequestDto book);

    BookingResponseDto updateBookingApproval(String userId, Long bookingId, boolean approved);

    BookingResponseDto findBookingId(String userId, Long bookingId);

    List<BookingResponseDto> findBookingAllById(String userId, BookingState state);

    List<BookingResponseDto> findBookingsByItemsOwnerId(String userId, BookingState state);

}
