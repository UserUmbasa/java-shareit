package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final UserRepository repository;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto saveUser(UserRequestDto user) {
        if (!repository.existsByEmail(user.getEmail())) {
            User result = repository.save(userMapper.mapToUser(user));
            return userMapper.mapToUserResponseDto(result);
        }
        throw new DuplicateException("такой пользователь уже есть");
    }

    @Override
    public UserResponseDto findUser(Long id) {
        User result = repository.findById(id).orElseThrow(() -> new NotFoundException("такого пользователя нет"));
        return userMapper.mapToUserResponseDto(result);
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto user) {
        User result = repository.findById(id).orElseThrow(() -> new NotFoundException("такого пользователя нет"));
        if (user.getEmail() != null) {
            if (repository.existsByEmail(user.getEmail())) {
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
        if (!repository.existsById(userId)) {
            throw new NotFoundException("такого пользователя нет");
        }
        repository.deleteById(userId);
    }
}
