package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.validator.Marker;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader(value = "X-Sharer-User-Id", required = false) String userId,
                                         @Validated(Marker.OnCreate.class) @RequestBody BookingRequestDto book) {
        log.info("Запрос ({}) на бронирование - пользователем {}", book, userId);
        BookingResponseDto result = bookingService.addBooking(userId, book);
        printServerResponse(result);
        return result;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBookingApproval(@RequestHeader(value = "X-Sharer-User-Id", required = false) String userId,
                                                    @PathVariable Long bookingId, @RequestParam boolean approved) {
        log.info("Запрос на смену статуса бронирования {} подзапрос: {}", bookingId, approved);
        BookingResponseDto result = bookingService.updateBookingApproval(userId, bookingId, approved);
        printServerResponse(result);
        return result;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findBookingId(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable Long bookingId) {
        log.info("Запрос на получение данных о бронировании:{}", bookingId);
        BookingResponseDto result = bookingService.findBookingId(userId, bookingId);
        printServerResponse(result);
        return result;
    }

    @GetMapping()
    public List<BookingResponseDto> findBookingsAllById(@RequestHeader("X-Sharer-User-Id") String userId,
                                                        @RequestParam(name = "state", required = false) BookingState state) {
        log.info("Запрос на получение списка всех бронирований пользователя:{}", state);
        state = (state == null) ? BookingState.ALL : state;
        List<BookingResponseDto> result = bookingService.findBookingAllById(userId, state);
        printServerResponse(result);
        return result;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findBookingsByItemsAllById(@RequestHeader("X-Sharer-User-Id") String ownerId,
                                                               @RequestParam(name = "state", required = false) BookingState state) {
        log.info("Запрос на получение списка бронирований для всех вещей пользователя:{}", ownerId);
        state = (state == null) ? BookingState.ALL : state;
        List<BookingResponseDto> result = bookingService.findBookingsByItemsOwnerId(ownerId, state);
        printServerResponse(result);
        return result;
    }

    private void printServerResponse(BookingResponseDto result) {
        log.info("Ответ ({}): сервера", result);
    }

    private void printServerResponse(List<BookingResponseDto> results) {
        log.info("Ответ сервера ({}): ", results);
    }
}
