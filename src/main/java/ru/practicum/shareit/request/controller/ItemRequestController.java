package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseAnswersDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.validator.Marker;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemResponseDto addItemRequest(@RequestHeader(value = HEADER, required = false) String userId,
                                          @Validated(Marker.OnCreate.class) @RequestBody ItemRequestDto request) {
        log.info("Пользователь ({}): создать запрос на поиск вещи {}", userId, request);
        return itemRequestService.addItemRequest(userId, request);
    }

    @GetMapping()
    public List<ItemResponseAnswersDto> findRequestsByUser(@RequestHeader(value = HEADER, required = false) String userId) {
        log.info("Пользователь ({}): вернуть все запросы пользователя", userId);
        return itemRequestService.findRequestsByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemResponseDto> findRequestsByUsers(@RequestHeader(value = HEADER, required = false) String userId) {
        log.info("Пользователь ({}): вернуть все запросы пользователей", userId);
        return itemRequestService.findRequestsByUsers(userId);
    }

    @GetMapping("/{requestId}")
    public ItemResponseAnswersDto findRequestId(@RequestHeader(HEADER) String userId,
                                                @PathVariable Long requestId) {
        log.info("Пользователь ({}): вернуть запрос - ({})", requestId, userId);
        return itemRequestService.findRequestId(userId, requestId);
    }
}

