package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL,
    WAITING, // ожидающие подтверждения
    APPROVED, // подтвержденный
    REJECTED, // отклоненный
    PAST, // завершенные
    FUTURE //будущие
}
