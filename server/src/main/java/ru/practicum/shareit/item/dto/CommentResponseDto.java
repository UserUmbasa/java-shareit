package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private String authorName;
    private LocalDateTime created;
    private ItemResponseDto item;
    private String text;
}
