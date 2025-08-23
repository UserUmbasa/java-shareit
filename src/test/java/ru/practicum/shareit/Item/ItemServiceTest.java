package ru.practicum.shareit.Item;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.storage.UserRepository;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
// !! аннотации ниже без спринг бута
//@TestPropertySource(properties = { "jdbc.url=jdbc:postgresql://localhost:5433/shareit"})
//@SpringJUnitConfig( {ItemMapper.class, UserRepository.class, ItemRepository.class, CommentRepository.class, BookingRepository.class}) // Замените на ваш пакет
public class ItemServiceTest {
    private ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    @Test
    void testSaveItem() {
        // реализация теста опущена для краткости
    }
}
