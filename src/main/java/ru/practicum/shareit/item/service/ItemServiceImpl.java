package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public ItemResponseDto addItem(String ownerId, ItemRequestDto item) {
        try {
            Long id = Long.parseLong(ownerId);
            if (!userRepository.existsById(id)) {
                throw new NotFoundException("такого пользователя нет");
            }
            item.setOwnerId(id);
            Item result = itemRepository.save(itemMapper.mapToItem(item));
            return itemMapper.mapToItemResponseDto(result);
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public ItemResponseDto updateItem(String userId, Long itemId, ItemRequestDto item) {
        try {
            if (!userRepository.existsById(Long.parseLong(userId))) {
                throw new NotFoundException("такого пользователя нет");
            }
            Item result = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("такого айди нет"));
            if (result.getOwnerId().equals(Long.parseLong(userId))) {
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
            return itemMapper.mapToItemResponseDto(itemRepository.save(result));
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public List<ItemCommentResponseDto> findItemsUser(String userId) {
        try {
            List<ItemCommentResponseDto> result = new ArrayList<>();
            List<Item> items = itemRepository.findItemsByOwnerId(Long.parseLong(userId));
            for (Item item : items) {
                ItemCommentResponseDto itemCommentResponseDto = findItemId(userId, item.getId());
                result.add(itemCommentResponseDto);
            }
            return result;
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public ItemCommentResponseDto findItemId(String userId, Long itemId) {
        try {
            Long id = Long.parseLong(userId);
            if (!userRepository.existsById(id)) {
                throw new NotFoundException("такого пользователя нет");
            }
            Item result = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("такого айди нет"));
            List<Comment> comments = commentRepository.findAllByItem_Id(itemId);
            ItemCommentResponseDto itemComment = itemMapper.mapToItemCommentResponseDto(result, comments);

            //хвост последнего (ласт) и начало следующего (некст)
            if (result.getOwnerId().equals(id)) {
                itemComment.setNextBooking(bookingRepository.findNextBookingStartTimeByOwnerIdAndItemID(id, itemId, LocalDateTime.now()).orElse(null));
                itemComment.setLastBooking(bookingRepository.findLastBookingEndDateByOwnerIdAndItemID(id, itemId, LocalDateTime.now()).orElse(null));
            }
            return itemComment;
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public List<ItemResponseDto> findSearchItems(String userId, String text) {
        try {
            if (!userRepository.existsById(Long.parseLong(userId))) {
                throw new NotFoundException("такого пользователя нет");
            }
            List<Item> result = itemRepository.findByUserIdAndSearchTermAndAvailableTrue(Long.parseLong(userId), text.toLowerCase());
            return result.stream().map(itemMapper::mapToItemResponseDto).toList();
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public void deleteItem(String userId, Long itemId) {
        try {
            if (userRepository.existsById(Long.parseLong(userId))) {
                throw new NotFoundException("такого пользователя нет");
            }
            itemRepository.deleteById(itemId);
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public Comment addItemComment(String userId, Long itemId, Comment comment) {
        try {
            Long id = Long.parseLong(userId);
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalStateException("такой вещи нет"));
            User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("такого пользователя нет"));
            Booking booking = bookingRepository.findByBookerIdAndItemId(id, itemId);
            if (!booking.getStatus().equals(BookingState.APPROVED) || LocalDateTime.now().isBefore(booking.getEnd())) {
                throw new ValidationException("аренда не найдена");
            }
            comment.setItem(item);
            comment.setAuthorName(user.getName());
            comment.setCreated(LocalDateTime.now());
            return commentRepository.save(comment);
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }
}
