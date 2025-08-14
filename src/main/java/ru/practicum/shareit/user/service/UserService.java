package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto saveUser(UserRequestDto user);

    UserResponseDto findUser(Long id);

    UserResponseDto update(Long id, UserRequestDto user);

    void delete(Long userId);
}
