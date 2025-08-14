package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.validator.Marker;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto addItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) String ownerId,
                                   @Validated(Marker.OnCreate.class) @RequestBody ItemRequestDto item) {
        log.info("Запрос ({}) на добавление вещи - пользователем {}", item, ownerId);
        return itemService.addItem(ownerId, item);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addItemComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) String userId,
                                  @PathVariable Long itemId, @RequestBody Comment comment) {
        log.info("Запрос ({}) комментария - пользователем {}", comment, userId);
        return itemService.addItemComment(userId, itemId, comment);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) String userId,
                                      @PathVariable Long itemId, @RequestBody ItemRequestDto item) {
        log.error("Запрос ({}) на обновление вещи {} - пользователя {}", item, itemId, userId);
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping
    public List<ItemCommentResponseDto> findItemsUser(@RequestHeader("X-Sharer-User-Id") String userId) {
        log.info("Получен запрос на получение всех вещей пользователя {}", userId);
        return itemService.findItemsUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemCommentResponseDto findItemId(@RequestHeader("X-Sharer-User-Id") String userId,
                                             @PathVariable Long itemId) {
        log.info("Получен запрос на получение вещи itemId:{}", itemId);
        return itemService.findItemId(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> findSearchItems(@RequestHeader("X-Sharer-User-Id") String userId,
                                                 @RequestParam(name = "text", required = false) String text) {
        log.info("Получен запрос на получение вещей пользователя {} подзапрос: {}", userId, text);
        return itemService.findSearchItems(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable Long itemId) {
        log.info("Получен запрос на удаление вещи itemId:{}", itemId);
        itemService.deleteItem(userId, itemId);
    }
}
