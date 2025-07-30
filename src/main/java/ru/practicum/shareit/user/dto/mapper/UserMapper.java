package ru.practicum.shareit.user.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract User mapToUser(UserRequestDto user);

    public abstract UserResponseDto mapToUserResponseDto(User user);
}
