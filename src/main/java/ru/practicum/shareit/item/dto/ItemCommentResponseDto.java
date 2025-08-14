package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemCommentResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    List<Comment> comments;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
}
