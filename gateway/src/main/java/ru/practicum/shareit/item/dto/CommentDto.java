package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.validation.Marker;

@Data
public class CommentDto {
    @NotBlank(groups = Marker.OnCreate.class, message = "text не должен содержать пустым или null")
    private String text;
}
