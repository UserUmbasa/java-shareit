package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.validator.Marker;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto addItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) String userId,
                                  @Validated(Marker.OnCreate.class) @RequestBody ItemRequestDto item) {
        return itemService.addItem(userId, item);
    }

    // Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) String userId,
                                     @PathVariable Long itemId,
                                     @RequestBody ItemRequestDto item) {
        return itemService.updateItem(userId, itemId, item);
    }

    // Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них
    @GetMapping
    public List<ItemResponseDto> findItemsUser(@RequestHeader("X-Sharer-User-Id") String userId) {
        return itemService.findItemsUser(userId);
    }

    // Просмотр информации о конкретной вещи по её идентификатору
    @GetMapping("/{itemId}")
    public ItemResponseDto findItemId(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable Long itemId) {
        return itemService.findItemId(userId, itemId);
    }

    // Поиск вещи потенциальным арендатором.
    @GetMapping("/search")
    public List<ItemResponseDto> findSearchItems(@RequestHeader("X-Sharer-User-Id") String userId,
                                                @RequestParam(name = "text", required = false) String text) {
        return itemService.findSearchItems(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable Long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}
