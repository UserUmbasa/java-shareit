package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validation.Marker;

@Data
public class ItemDto {
    private Long requestId;
    @NotBlank(groups = Marker.OnCreate.class, message = "name не должен содержать пустым или null")
    private String name;
    @NotBlank(groups = Marker.OnCreate.class, message = "description не должен содержать пустым или null")
    private String description;
    @NotNull(groups = Marker.OnCreate.class, message = "available не должен быть null")
    private Boolean available;
}
