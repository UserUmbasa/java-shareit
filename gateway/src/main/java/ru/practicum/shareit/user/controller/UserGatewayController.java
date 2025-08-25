package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Marker;

@RestController
@RequestMapping("/users")
public class UserGatewayController {
    private final RestClient restClient;

    public UserGatewayController(@Qualifier("userRestClient") RestClient restClient) {
        this.restClient = restClient;
    }


    @PostMapping
    public ResponseEntity<Object> addUser(@Validated(Marker.OnCreate.class) @RequestBody UserDto user) {
        return restClient.post()
                .body(user)
                .retrieve()
                .toEntity(Object.class);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUser(@PathVariable Long userId) {
        return restClient.get()
                .uri("/{userId}", userId) // указываем параметр в URI
                .retrieve()
                .toEntity(Object.class);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @Validated(Marker.OnUpdate.class) @RequestBody UserDto user) {
        return restClient.patch()
                .uri("/{userId}", userId)
                .body(user)
                .retrieve()
                .toEntity(Object.class);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        return restClient.delete()
                .uri("/{userId}", userId)
                .retrieve()
                .toEntity(Object.class);
    }
}
