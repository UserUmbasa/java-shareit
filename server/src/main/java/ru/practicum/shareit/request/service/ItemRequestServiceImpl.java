package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseAnswersDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    @Transactional
    public ItemResponseDto addItemRequest(String userId, ItemRequestDto request) {
        try {
            Long id = Long.parseLong(userId);
            if (!userRepository.existsById(id)) {
                throw new NotFoundException("такого пользователя нет");
            }
            ItemRequest itemRequest = itemRequestMapper.mapToItemRequest(request);
            itemRequest.setUserId(id);
            return itemRequestMapper.mapToResponseDto(itemRequestRepository.save(itemRequest));
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public List<ItemResponseAnswersDto> findRequestsByUser(String userId) {
        try {
            Long id = Long.parseLong(userId);
            if (!userRepository.existsById(id)) {
                throw new NotFoundException("такого пользователя нет");
            }
            List<ItemRequest> requests = itemRequestRepository.findAllByUserId(id);
            List<Long> itemIds = requests.stream().map(ItemRequest::getId).toList();
            List<Item> items = itemRepository.findAllByRequestIdIn(itemIds);
            List<ItemResponseAnswersDto> result = new ArrayList<>();
            for (ItemRequest itemRequest : requests) {
                List<Item> itemsRequest = items.stream().filter(item -> item.getRequestId().equals(itemRequest.getId())).toList();
                ItemResponseAnswersDto temp = itemRequestMapper.mapToItemResponseAnswersDto(itemRequest, itemsRequest);
                result.add(temp);
            }
            return result;
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public ItemResponseAnswersDto findRequestId(String userId, Long requestId) {
        try {
            Long id = Long.parseLong(userId);
            if (!userRepository.existsById(id)) {
                throw new NotFoundException("такого пользователя нет");
            }
            ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("такого айди нет"));
            List<Item> items = itemRepository.findAllByRequestIdIn(List.of(requestId));
            return itemRequestMapper.mapToItemResponseAnswersDto(itemRequest, items);
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public List<ItemResponseDto> findRequestsByUsers(String userId) {
        try {
            Long id = Long.parseLong(userId);
            if (!userRepository.existsById(id)) {
                throw new NotFoundException("такого пользователя нет");
            }
            List<ItemRequest> requests = itemRequestRepository.findByUserIdNot(id);
            return requests.stream().map(itemRequestMapper::mapToResponseDto).toList();
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }
}

