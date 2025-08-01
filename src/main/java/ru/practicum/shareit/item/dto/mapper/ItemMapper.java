package ru.practicum.shareit.item.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public abstract class ItemMapper {

    public abstract Item mapToItem(ItemRequestDto requestDto);

    @Mapping(source = "itemId", target = "id")
    public abstract ItemResponseDto mapToItemResponseDto(Item item);
}
