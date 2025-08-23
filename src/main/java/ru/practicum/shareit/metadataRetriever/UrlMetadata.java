package ru.practicum.shareit.metadataRetriever;

import java.time.Instant;

interface UrlMetadata {
    String getNormalUrl();
    String getResolvedUrl();
    String getMimeType();
    String getTitle();
    boolean isHasImage();
    boolean isHasVideo();
    Instant getDateResolved();

    public UrlMetadata retrieve(String urlString) throws ItemRetrieverException;
}
