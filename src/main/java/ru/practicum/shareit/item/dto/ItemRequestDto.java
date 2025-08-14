package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.validator.Marker;

@Data
public class ItemRequestDto {
    private Long ownerId;
    @NotBlank(groups = Marker.OnCreate.class, message = "name не должен содержать пустым или null")
    private String name;
    @NotBlank(groups = Marker.OnCreate.class, message = "description не должен содержать пустым или null")
    private String description;
    @NotNull(groups = Marker.OnCreate.class, message = "available не должен быть null")
    private Boolean available;
}
