package ru.practicum.shareit.request.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseAnswersDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ResponsesToQuery;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public abstract class ItemRequestMapper {
    @Mapping(target = "created", ignore = true)
    public abstract ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto);

    public abstract ItemResponseDto mapToResponseDto(ItemRequest itemRequest);

    public ItemResponseAnswersDto mapToItemResponseAnswersDto(ItemRequest itemRequests, List<Item> items) {
        ItemResponseAnswersDto result = new ItemResponseAnswersDto();
        result.setId(itemRequests.getId());
        result.setDescription(itemRequests.getDescription());
        result.setCreated(itemRequests.getCreated());
        for (Item item : items) {
            ResponsesToQuery responsesToQuery = new ResponsesToQuery();
            responsesToQuery.setItemId(item.getId());
            responsesToQuery.setOwnerId(item.getOwnerId());
            responsesToQuery.setName(item.getName());
            result.getItems().add(responsesToQuery);
        }
        return result;
    }
}

