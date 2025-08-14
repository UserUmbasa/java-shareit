package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.start > :start AND b.booker.id = :bookerId")
    List<Booking> findFutureBookingsByBookerId(@Param("start") LocalDateTime start, @Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.start < :end AND b.booker.id = :bookerId")
    List<Booking> findPastBookingsByBookerId(@Param("end") LocalDateTime end, @Param("bookerId") Long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long id, BookingState state);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId")
    List<Booking> findAllByOwnerIdOrderByStartDesc(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId AND b.status = :state")
    List<Booking> findAllOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingState state);

    @Query("SELECT b FROM Booking b WHERE b.start > :start AND b.item.ownerId = :ownerId")
    List<Booking> findFutureBookingsByOwnerId(@Param("start") LocalDateTime start, @Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.start < :end AND b.item.ownerId = :ownerId")
    List<Booking> findPastBookingsByOwnerId(@Param("end") LocalDateTime end, @Param("ownerId") Long ownerId);

    @Query("SELECT b.start FROM Booking b WHERE b.item.ownerId = :ownerId AND b.item.Id = :itemId AND b.start > :time ORDER BY b.start ASC")
    Optional<LocalDateTime> findNextBookingStartTimeByOwnerIdAndItemID(@Param("ownerId") Long ownerId, @Param("itemId") Long itemId, @Param("time") LocalDateTime time);

    @Query("SELECT b.end FROM Booking b WHERE b.item.ownerId = :ownerId AND b.item.Id = :itemId AND b.end < :time ORDER BY b.end DESC")
    Optional<LocalDateTime> findLastBookingEndDateByOwnerIdAndItemID(@Param("ownerId") Long ownerId, @Param("itemId") Long itemId, @Param("time") LocalDateTime time);

    Booking findByBookerIdAndItemId(Long id, Long itemId);
}
