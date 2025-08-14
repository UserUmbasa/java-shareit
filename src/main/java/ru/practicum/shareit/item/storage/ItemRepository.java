package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByOwnerId(Long l);

    @Query("SELECT i FROM Item i WHERE i.ownerId = :userId " +
            "AND i.available = true " +
            "AND (LOWER(i.name) LIKE %:searchTerm% OR LOWER(i.description) LIKE %:searchTerm%)")
    List<Item> findByUserIdAndSearchTermAndAvailableTrue(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);
}
