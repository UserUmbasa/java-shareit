package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepositoryImpl;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepositoryImpl userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserResponseDto addUser(UserRequestDto user) {
        if (isUserRegisteredEmail(user.getEmail())) {
            return userMapper.mapToUserResponseDto(userRepository.saveUser(userMapper.mapToUser(user)));
        }
        throw new DuplicateException("такой пользователь уже есть");
    }

    @Override
    public UserResponseDto findUser(Long id) {
        User result = userRepository.findUserById(id).orElseThrow(() -> new NotFoundException("такого пользователя нет"));
        return userMapper.mapToUserResponseDto(result);
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto user) {
        User result = userRepository.findUserById(id)
                .orElseThrow(() -> new NotFoundException("такого пользователя нет"));
        if (user.getEmail() != null) {
            if (!isUserRegisteredEmail(user.getEmail())) {
                throw new DuplicateException("дубликат почты");
            }
            result.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            result.setName(user.getName());
        }
        return userMapper.mapToUserResponseDto(result);
    }

    @Override
    public void delete(Long userId) {
        if (isUserRegistered(userId)) {
            userRepository.delete(userId);
            return;
        }
        throw new NotFoundException("такого пользователя нет");
    }

    public boolean isUserRegistered(Long userId) {
        return userRepository.findUserById(userId).isPresent();
    }

    public boolean isUserRegisteredEmail(String email) {
        return userRepository.findUserByEmail(email).isEmpty();
    }
}
