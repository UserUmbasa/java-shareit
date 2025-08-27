package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Marker;

@RestController
@RequestMapping("/items")
public class ItemGatewayController {
    private final RestClient restClient;
    private static final String HEADER = "X-Sharer-User-Id";

    public ItemGatewayController(@Qualifier("itemRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(value = HEADER, required = false) String ownerId,
                                          @Validated(Marker.OnCreate.class) @RequestBody ItemDto item) {
        return restClient.post()
                .header(HEADER, ownerId)
                .body(item)
                .retrieve()
                .toEntity(Object.class);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addItemComment(@RequestHeader(value = HEADER, required = false) String userId,
                                                 @PathVariable Long itemId,
                                                 @Validated(Marker.OnCreate.class) @RequestBody CommentDto comment) {
        return restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(String.valueOf(itemId))
                        .pathSegment("comment")
                        .build())
                .header(HEADER, userId)
                .body(comment)
                .retrieve()
                .toEntity(Object.class);
    }

    @CacheEvict(value = {"itemCache", "itemSearchCache"}, allEntries = true)
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(value = HEADER, required = false) String userId,
                                             @PathVariable Long itemId, @RequestBody ItemDto item) {
        return restClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(String.valueOf(itemId))
                        .build())
                .header(HEADER, userId)
                .body(item)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "itemCache", key = "#userId")
    @GetMapping
    public ResponseEntity<Object> findItemsUser(@RequestHeader(HEADER) String userId) {
        return restClient.get()
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "itemCache", key = "#userId + '_' + #itemId")
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemId(@RequestHeader(HEADER) String userId, @PathVariable Long itemId) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(String.valueOf(itemId))
                        .build())
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }

    @Cacheable(value = "itemSearchCache", key = "#userId + '_' + (#text != null ? #text : '')")
    @GetMapping("/search")
    public ResponseEntity<Object> findSearchItems(@RequestHeader(HEADER) String userId,
                                                  @RequestParam(name = "text", required = false) String text) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("search")
                        .queryParam("text", text)
                        .build())
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }

    @CacheEvict(value = {"itemCache", "itemSearchCache"}, allEntries = true)
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(HEADER) String userId, @PathVariable Long itemId) {
        return restClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(String.valueOf(itemId))
                        .build())
                .header(HEADER, userId)
                .retrieve()
                .toEntity(Object.class);
    }
}
