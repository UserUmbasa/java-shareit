package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import java.util.List;

public interface ItemService {
    ItemResponseDto addItem(String userId, ItemRequestDto item);

    ItemResponseDto updateItem(String userId, Long itemId, ItemRequestDto item);

    List<ItemResponseDto> findItemsUser(String userId);

    ItemResponseDto findItemId(String userId, Long itemId);

    List<ItemResponseDto> findSearchItems(String userId, String text);

    void deleteItem(String userId, Long itemId);

}
