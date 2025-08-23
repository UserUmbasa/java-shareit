package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 * Вещь
 */
@Entity
@Table(name = "items", schema = "public")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long requestId;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
}
