package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl {
    private final List<Item> items = new ArrayList<Item>();
    private static Long idItem = 0L;

    public Item saveItem(Item item) {
        item.setItemId(idItem);
        items.add(item);
        idItem++;
        return item;
    }

    public List<Item> getItemsUser(Long userId) {
        return items.stream().filter(item -> item.getUserId().equals(userId)).toList();
    }

    public Optional<Item> getItemId(Long itemId) {
        return items.stream().filter(item -> item.getItemId().equals(itemId)).findFirst();
    }

    public void delete(Long itemId) {
        items.removeIf(item -> item.getItemId().equals(itemId));
    }

    public List<Item> findSearchItems(Long userId, String text) {
        return getItemsUser(userId).stream()
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()) || item.getName()
                        .toLowerCase().contains(text.toLowerCase())).toList();
    }
}
