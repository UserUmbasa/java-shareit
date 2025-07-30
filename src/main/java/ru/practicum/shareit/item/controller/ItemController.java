package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto addItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) String userId,
                                  @Validated(Marker.OnCreate.class) @RequestBody ItemRequestDto item) {
        log.info("Запрос ({}) на добавление вещи - пользователем {}", item, userId);
        ItemResponseDto result = itemService.addItem(userId, item);
        printServerResponse(result);
        return result;
    }

    // Редактирование вещи
    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) String userId,
                                     @PathVariable Long itemId,
                                     @RequestBody ItemRequestDto item) {
        log.error("Запрос ({}) на обновление вещи {} - пользователя {}", item, itemId, userId);
        ItemResponseDto result = itemService.updateItem(userId, itemId, item);
        printServerResponse(result);
        return result;
    }

    // Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой из них
    @GetMapping
    public List<ItemResponseDto> findItemsUser(@RequestHeader("X-Sharer-User-Id") String userId) {
        log.info("Получен запрос на получение всех вещей пользователя {}", userId);
        List<ItemResponseDto> result = itemService.findItemsUser(userId);
        printServerResponse(result);
        return result;
    }

    // Просмотр информации о конкретной вещи по её идентификатору
    @GetMapping("/{itemId}")
    public ItemResponseDto findItemId(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable Long itemId) {
        log.info("Получен запрос на получение вещи itemId:{}", itemId);
        ItemResponseDto result = itemService.findItemId(userId, itemId);
        printServerResponse(result);
        return result;
    }

    // Поиск вещи потенциальным арендатором.
    @GetMapping("/search")
    public List<ItemResponseDto> findSearchItems(@RequestHeader("X-Sharer-User-Id") String userId,
                                                @RequestParam(name = "text", required = false) String text) {
        log.info("Получен запрос на получение вещей пользователя {} подзапрос: {}", userId, text);
        List<ItemResponseDto> result = itemService.findSearchItems(userId, text);
        printServerResponse(result);
        return result;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable Long itemId) {
        log.info("Получен запрос на удаление вещи itemId:{}", itemId);
        itemService.deleteItem(userId, itemId);
    }

    private void printServerResponse ( ItemResponseDto result){
        log.info("Ответ ({}): сервера", result);
    }

    private void printServerResponse(List<ItemResponseDto> results) {
        log.info("Ответ сервера ({}): ", results);
    }
}
