package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemCommentResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentResponseDto> comments;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
}
