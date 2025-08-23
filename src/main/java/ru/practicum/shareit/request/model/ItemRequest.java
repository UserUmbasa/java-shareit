package ru.practicum.shareit.request.model;

/**
 * TODO Sprint add-item-requests.
 */
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Table(name = "request", schema = "public")
@Data
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
}

