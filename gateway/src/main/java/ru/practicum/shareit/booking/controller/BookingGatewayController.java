package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.validation.Marker;


@RestController
@RequestMapping(path = "/bookings")
public class BookingGatewayController {
    private final RestClient restClient;
    private static final String HEADER = "X-Sharer-User-Id";
    private static final String BOOKING_ID = "/{bookingId}";

    public BookingGatewayController(@Qualifier("bookingRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(value = HEADER, required = false) String userId,
                                          @Validated(Marker.OnCreate.class) @RequestBody BookingDto book) {
        return restClient.post().header(HEADER, userId).body(book).retrieve().toEntity(Object.class);
    }

    @CacheEvict(value = {"bookingCache", "bookingOwnerCache"}, allEntries = true)
    @PatchMapping(BOOKING_ID)
    public ResponseEntity<Object> updateBookingApproval(@RequestHeader(value = HEADER, required = false) String userId,
                                                        @PathVariable Long bookingId, @RequestParam boolean approved) {
        return restClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(String.valueOf(bookingId))
                        .queryParam("approved", approved)
                        .build())
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "bookingCache", key = "#userId + '_' + #bookingId")
    @GetMapping(BOOKING_ID)
    public ResponseEntity<Object> findBookingId(@RequestHeader(HEADER) String userId, @PathVariable Long bookingId) {
        return restClient.get()
                .uri(BOOKING_ID, bookingId)
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "bookingCache", key = "#userId + '_' + (#state != null ? #state : 'all')")
    @GetMapping()
    public ResponseEntity<Object> findBookingsAllById(@RequestHeader(HEADER) String userId,
                                                      @RequestParam(name = "state", required = false) String state) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("state", state)
                        .build())
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "bookingOwnerCache", key = "#ownerId + '_' + (#state != null ? #state : 'all')")
    @GetMapping("/owner")
    public ResponseEntity<Object> findBookingsByItemsAllById(@RequestHeader(HEADER) String ownerId,
                                                             @RequestParam(name = "state", required = false) String state) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("owner")
                        .queryParam("state", state)
                        .build())
                .header(HEADER, ownerId)
                .retrieve()
                .toEntity(Object.class);
    }
}
