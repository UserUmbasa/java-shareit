package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ExceptionServer;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private BookingRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingResponseDto addBooking(String userId, BookingRequestDto booking) {
        try {
            Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> new NotFoundException("такого айди нет"));
            if (!item.getAvailable()) {
                throw new ExceptionServer("вещь не доступна");
            }
            User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("такого айди нет"));
            Booking savedBooking = repository.save(bookingMapper.mapToBooking(booking, item, user));
            return bookingMapper.mapToBookingResponseDto(savedBooking);
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public BookingResponseDto updateBookingApproval(String userId, Long bookingId, boolean approved) {
        try {
            Booking booking = repository.findById(bookingId).orElseThrow(() -> new NotFoundException("такого айди нет"));
            if (booking.getItem().getOwnerId().equals(Long.parseLong(userId))) {
                booking.setStatus(approved ? BookingState.APPROVED : BookingState.REJECTED);
                Booking savedBooking = repository.save(booking);
                return bookingMapper.mapToBookingResponseDto(savedBooking);
            }
            throw new ValidationException("менять статус может только владелец");
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public BookingResponseDto findBookingId(String userId, Long bookingId) {
        try {
            Long id = Long.parseLong(userId);
            Booking booking = repository.findById(bookingId).orElseThrow(() -> new NotFoundException("такого айди нет"));
            if (booking.getBooker().getId().equals(id) || booking.getItem().getOwnerId().equals(id)) {
                return bookingMapper.mapToBookingResponseDto(booking);
            }
            throw new ValidationException("нет прав доступа");
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public List<BookingResponseDto> findBookingAllById(String userId, BookingState state) {
        try {
            Long id = Long.parseLong(userId);
            List<Booking> result = switch (state) {
                case ALL -> repository.findAllByBookerIdOrderByStartDesc(id);
                case WAITING, APPROVED, REJECTED -> repository.findAllByBookerIdAndStatusOrderByStartDesc(id, state);
                case FUTURE -> repository.findFutureBookingsByBookerId(LocalDateTime.now(), id);
                case PAST -> repository.findPastBookingsByBookerId(LocalDateTime.now(), id);
                default -> throw new ValidationException("не валидный state");
            };
            return result.stream().map(bookingMapper::mapToBookingResponseDto).toList();
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    public List<BookingResponseDto> findBookingsByItemsOwnerId(String userId, BookingState state) {
        try {
            Long ownerId = Long.parseLong(userId);
            List<Booking> result = switch (state) {
                case ALL -> repository.findAllByOwnerIdOrderByStartDesc(ownerId);
                case WAITING, APPROVED, REJECTED -> repository.findAllOwnerIdAndStatusOrderByStartDesc(ownerId, state);
                case FUTURE -> repository.findFutureBookingsByOwnerId(LocalDateTime.now(), ownerId);
                case PAST -> repository.findPastBookingsByOwnerId(LocalDateTime.now(), ownerId);
                default -> throw new ValidationException("не валидный state");
            };
            if (result.isEmpty()) {
                throw new ExceptionServer("Нет бронирований для данного владельца");
            }
            return result.stream().map(bookingMapper::mapToBookingResponseDto).toList();
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный запрос");
        }
    }
}
