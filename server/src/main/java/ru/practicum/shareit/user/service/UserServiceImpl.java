package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto saveUser(UserRequestDto user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            User result = userRepository.save(userMapper.mapToUser(user));
            return userMapper.mapToUserResponseDto(result);
        }
        throw new DuplicateException("такой пользователь уже есть");
    }

    @Override
    public UserResponseDto findUser(Long id) {
        User result = userRepository.findById(id).orElseThrow(() -> new NotFoundException("такого пользователя нет"));
        return userMapper.mapToUserResponseDto(result);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto user) {
        User result = userRepository.findById(id).orElseThrow(() -> new NotFoundException("такого пользователя нет"));
        if (user.getEmail() != null) {
            if (userRepository.existsByEmail(user.getEmail())) {
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
    @Transactional
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("такого пользователя нет");
        }
        userRepository.deleteById(userId);
    }
}
