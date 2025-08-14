package ru.practicum.shareit.item.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public abstract class ItemMapper {

    public abstract Item mapToItem(ItemRequestDto requestDto);

    public abstract ItemResponseDto mapToItemResponseDto(Item item);

    public abstract ItemCommentResponseDto mapToItemCommentResponseDto(Item item, List<Comment> comments);

}
