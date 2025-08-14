package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingResponseDto addBooking(String userId, BookingRequestDto booking) {
        try {
            Item item = itemRepository.findById(booking.getItemId()).orElseThrow(() -> new NotFoundException("такого айди нет"));
            if (!item.getAvailable()) {
                throw new ExceptionServer("вещь не доступна");
            }
            User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new NotFoundException("такого айди нет"));
            Booking savedBooking = bookingRepository.save(bookingMapper.mapToBooking(booking, item, user));
            return bookingMapper.mapToBookingResponseDto(savedBooking);
        } catch (NumberFormatException e) {
            throw new ValidationException("не валидный Id");
        }
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingApproval(String userId, Long bookingId, boolean approved) {
        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("такого айди нет"));
            if (booking.getItem().getOwnerId().equals(Long.parseLong(userId))) {
                booking.setStatus(approved ? BookingState.APPROVED : BookingState.REJECTED);
                Booking savedBooking = bookingRepository.save(booking);
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
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("такого айди нет"));
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
                case ALL -> bookingRepository.findAllByBookerIdOrderByStartDesc(id);
                case WAITING, APPROVED, REJECTED -> bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(id, state);
                case FUTURE -> bookingRepository.findFutureBookingsByBookerId(LocalDateTime.now(), id);
                case PAST -> bookingRepository.findPastBookingsByBookerId(LocalDateTime.now(), id);
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
                case ALL -> bookingRepository.findAllByOwnerIdOrderByStartDesc(ownerId);
                case WAITING, APPROVED, REJECTED -> bookingRepository.findAllOwnerIdAndStatusOrderByStartDesc(ownerId, state);
                case FUTURE -> bookingRepository.findFutureBookingsByOwnerId(LocalDateTime.now(), ownerId);
                case PAST -> bookingRepository.findPastBookingsByOwnerId(LocalDateTime.now(), ownerId);
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
