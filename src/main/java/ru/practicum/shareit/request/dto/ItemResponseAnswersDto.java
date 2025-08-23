package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemResponseAnswersDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime created;
    List<ResponsesToQuery> items = new ArrayList<>();
}

