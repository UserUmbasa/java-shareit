package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepositoryImpl;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepositoryImpl itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public ItemResponseDto addItem(String userId, ItemRequestDto item) {
        log.error("Получен запрос на добавление вещи {} - пользователя {}", item, userId);
        try {
            if (!userService.isUserRegistered(Long.parseLong(userId))) {
                throw new NotFoundException("такого пользователя нет");
            }
            item.setUserId(Long.parseLong(userId));
            return itemMapper.mapToItemResponseDto(itemRepository.saveItem(itemMapper.mapToItem(item)));
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public ItemResponseDto updateItem(String userId, Long itemId, ItemRequestDto item) {
        log.error("Получен запрос на обновление вещи {} пользователя {}", itemId, userId);
        try {
            if (!userService.isUserRegistered(Long.parseLong(userId))) {
                throw new NotFoundException("такого пользователя нет");
            }
            Item result = itemRepository.getItemId(itemId).orElseThrow(() -> new NotFoundException("данные не найдены"));
            if (result.getUserId().equals(Long.parseLong(userId))) {
                if (item.getAvailable() != null) {
                    result.setAvailable(item.getAvailable());
                }
                if (item.getName() != null) {
                    result.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    result.setDescription(item.getDescription());
                }
            }
            return itemMapper.mapToItemResponseDto(result);
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public List<ItemResponseDto> findItemsUser(String userId) {
        log.info("Получен запрос на получение всех вещей пользователя {}", userId);
        try {
            if (!userService.isUserRegistered(Long.parseLong(userId))) {
                throw new NotFoundException("такого пользователя нет");
            }
            return itemRepository.getItemsUser(Long.parseLong(userId)).stream()
                    .map(itemMapper::mapToItemResponseDto).toList();
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public ItemResponseDto findItemId(String userId, Long itemId) {
        log.info("Получен запрос на получение вещи itemId:{} userId:{} ", itemId, userId);
        try {
            // смысл юзера пока не раскрыт (нет никакого ограничения)
            if (!userService.isUserRegistered(Long.parseLong(userId))) {
                throw new NotFoundException("данные не найдены");
            }
            Item result = itemRepository.getItemId(itemId).orElseThrow(() -> new NotFoundException("такой айди нет"));
            return itemMapper.mapToItemResponseDto(result);
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public List<ItemResponseDto> findSearchItems(String userId, String text) {
        log.info("Получен запрос на получение вещей пользователя {} подзапрос: {}", userId, text);
        try {
            List<ItemResponseDto> result = itemRepository.getItemsUser(Long.parseLong(userId)).stream()
                    .filter(item -> Boolean.TRUE.equals(item.getAvailable())).map(itemMapper::mapToItemResponseDto).toList();
            return result.stream().filter(item -> {
                return item.getDescription().toLowerCase().contains(text.toLowerCase()) || item.getName().toLowerCase().contains(text.toLowerCase());
            }).toList();
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public void deleteItem(String userId, Long itemId) {
        // в следующем обновлении - если выкинуть вещи, можно стать вечным
    }
}
