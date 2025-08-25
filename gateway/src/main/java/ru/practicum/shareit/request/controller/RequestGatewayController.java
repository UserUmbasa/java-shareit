package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.validation.Marker;


@RestController
@RequestMapping(path = "/requests")
public class RequestGatewayController {
    private final RestClient restClient;
    private static final String HEADER = "X-Sharer-User-Id";

    public RequestGatewayController(@Qualifier("requestRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(value = HEADER, required = false) String userId,
                                             @Validated(Marker.OnCreate.class) @RequestBody RequestDto request) {
        return restClient.post()
                .header(HEADER, userId)
                .body(request)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "requestCache", key = "#userId")
    @GetMapping()
    public ResponseEntity<Object> findRequestsByUser(@RequestHeader(value = HEADER, required = false) String userId) {
        return restClient.get()
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "requestAllCache", key = "#userId")
    @GetMapping("/all")
    public ResponseEntity<Object> findRequestsByUsers(@RequestHeader(value = HEADER, required = false) String userId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("all")
                        .build())
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "requestCache", key = "#userId + '_' + #requestId")
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestId(@RequestHeader(HEADER) String userId, @PathVariable Long requestId) {
        return restClient.get()
                .uri("/{requestId}", requestId)
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }
}
