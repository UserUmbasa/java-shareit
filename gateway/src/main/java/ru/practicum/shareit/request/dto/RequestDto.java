package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validation.Marker;

@Data
public class RequestDto {
    @NotNull(groups = Marker.OnCreate.class, message = "description не указан")
    private String description;
}
