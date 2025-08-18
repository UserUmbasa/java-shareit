package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.validator.Marker;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto addUser(@Validated(Marker.OnCreate.class) @RequestBody UserRequestDto user) {
        log.info("Запрос ({}): на добавление пользователя", user);
        UserResponseDto result = userService.saveUser(user);
        printServerResponse(result);
        return result;
    }

    @GetMapping("/{userId}")
    public UserResponseDto findUser(@PathVariable Long userId) {
        log.info("Запрос ({}): на поиск пользователя по айди", userId);
        UserResponseDto result = userService.findUser(userId);
        printServerResponse(result);
        return result;
    }

    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(@PathVariable Long userId, @RequestBody UserRequestDto user) {
        log.info("Запрос ({}): на обновление пользователя по айди ({})", user, userId);
        UserResponseDto result = userService.update(userId, user);
        printServerResponse(result);
        return result;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя по айди ({})", userId);
        userService.delete(userId);
    }

    private void printServerResponse(UserResponseDto result) {
        log.info("Ответ ({}): сервера", result);
    }
}
