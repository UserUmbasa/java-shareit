package ru.practicum.shareit.metadataRetriever;

import lombok.Builder;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class UrlMetadataImpl implements UrlMetadata {

    String normalUrl;
    String resolvedUrl;
    String mimeType;
    String title;
    boolean hasImage;
    boolean hasVideo;
    Instant dateResolved;

    @Override
    public UrlMetadata retrieve(String urlString) throws ItemRetrieverException {
        final URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            // Если адрес не соответствует правилам URI адресов, то генерируем исключение.
            throw new ItemRetrieverException("The URL is malformed: " + urlString);
        }

        HttpResponse<Void> resp = connect(uri, "HEAD", HttpResponse.BodyHandlers.discarding());

        String contentType = resp.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse("*");

        MediaType mediaType = MediaType.parseMediaType(contentType);

        final UrlMetadataImpl result;

        if (mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            result = handleText(resp.uri());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("image/*"))) {
            result = handleImage(resp.uri());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("video/*"))) {
            result = handleVideo(resp.uri());
        } else {
            throw new ItemRetrieverException("The content type [" + mediaType
                    + "] at the specified URL is not supported.");
        }

        return result.toBuilder()
                .normalUrl(urlString)
                .resolvedUrl(resp.uri().toString())
                .mimeType(mediaType.getType())
                .dateResolved(Instant.now())
                .build();
    }

    private <T> HttpResponse<T> connect(URI url,
                                        String method,
                                        HttpResponse.BodyHandler<T> responseBodyHandler) {
        //делаем запрос к данному url
        return null;
    }

    private UrlMetadataImpl handleText(URI url) {
        // Отправим get-запрос, чтобы получить содержимое
        HttpResponse<String> resp = connect(url, "GET", HttpResponse.BodyHandlers.ofString());

        // воспользуемся библиотекой Jsoup для парсинга содержимого
        Document doc = Jsoup.parse(resp.body());

        // ищем в полученном документе html-тэги, говорящие, что он
        // содержит видео или аудио информацию
        Elements imgElements = doc.getElementsByTag("img");
        Elements videoElements = doc.getElementsByTag("video");

        // добавляем полученные данные в ответ. В том числе находим заголовок
        // полученной страницы.
        return UrlMetadataImpl.builder()
                .title(doc.title())
                .hasImage(!imgElements.isEmpty())
                .hasVideo(!videoElements.isEmpty())
                .build();
    }

    private UrlMetadataImpl handleVideo(URI url) {
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasVideo(true)
                .build();
    }

    private UrlMetadataImpl handleImage(URI url) {
        String name = new File(url).getName();
        return UrlMetadataImpl.builder()
                .title(name)
                .hasImage(true)
                .build();
    }
}
