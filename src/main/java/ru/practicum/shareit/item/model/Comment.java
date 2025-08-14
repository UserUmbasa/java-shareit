package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments", schema = "public")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    String authorName;
    LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    private String text;
}
