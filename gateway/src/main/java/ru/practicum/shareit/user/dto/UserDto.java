package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.practicum.shareit.validation.Marker;

@Data
public class UserDto {
    @NotBlank(groups = Marker.OnCreate.class, message = "имя не должен содержать пустым или null")
    private String name;

    @Email(groups = Marker.OnCreate.class, message = "Неверный формат email")
    @NotBlank(groups = Marker.OnCreate.class, message = "Email не может быть пустым или null")
    @Email(groups = Marker.OnUpdate.class, message = "Неверный формат email")
    @Pattern(groups = Marker.OnUpdate.class, regexp = "(.+)|null", message = "Неверный формат email")
    private String email;
}
