package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseAnswersDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemResponseDto addItemRequest(String userId, ItemRequestDto request);

    List<ItemResponseAnswersDto> findRequestsByUser(String userId);

    ItemResponseAnswersDto findRequestId(String userId, Long requestId);

    List<ItemResponseDto> findRequestsByUsers(String userId);
}
