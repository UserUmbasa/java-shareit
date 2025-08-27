package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class ResponsesToQuery {
    private Long itemId;
    private Long ownerId;
    private String name;
}

