package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.user.validator.Marker;

@Data
public class UserRequestDto {
    @NotBlank(groups = Marker.OnCreate.class, message = "имя не должен содержать пустым или null")
    private String name;

    @Email(groups = Marker.OnCreate.class, message = "Неверный формат email")
    @NotBlank(groups = Marker.OnCreate.class, message = "Email не может быть пустым или null")
    private String email;
}
