## RestClient в Java: подробное руководство

### Что такое RestClient

**RestClient** — это синхронный HTTP-клиент, представленный в Spring Boot 3.2, который предоставляет современный API 
для работы с REST-сервисами. Он служит абстракцией над HTTP-библиотеками и упрощает преобразование Java-объектов 
в HTTP-запросы и обратно.

### Основные возможности

* **Синхронная работа** с REST API
* **Удобное построение запросов** через fluent-интерфейс
* **Автоматическое преобразование** объектов в JSON и обратно
* **Работа с заголовками** и параметрами запроса
* **Обработка ошибок** и статусов ответа

### Создание RestClient

Существует несколько способов создания:

```java
// Простой способ
RestClient defaultClient = RestClient.create();

// С настройкой через билдер
RestClient customClient = RestClient.builder()
    .baseUrl("https://example.com")
    .defaultHeader("Authorization", "Bearer token")
    .build();
```

### Основные методы работы

#### HTTP-методы
* `get()`
* `post()`
* `put()`
* `patch()`
* `delete()`

#### Настройка запроса
* `uri()` — установка URL
* `header()` — добавление заголовков
* `contentType()` — установка типа контента
* `body()` — установка тела запроса

#### Получение ответа
* `retrieve()` — получение ответа
* `toEntity()` — получение ResponseEntity
* `body()` — получение тела ответа

### Примеры использования

**GET-запрос**
```java
String result = restClient.get()
    .uri("https://api.example.com/data")
    .retrieve()
    .body(String.class);
```

**POST-запрос**
```java
MyObject response = restClient.post()
    .uri("https://api.example.com/create")
    .contentType(MediaType.APPLICATION_JSON)
    .body(requestObject)
    .retrieve()
    .body(MyObject.class);
```
**POST-контроллер**
```java
@PostMapping
public ResponseEntity<Object> addUser(@Validated(Marker.OnCreate.class) @RequestBody UserDto user) {
    return restClient.post()
            .body(user)
            .retrieve()
            .toEntity(Object.class);
}
```

### Обработка ошибок

RestClient автоматически обрабатывает ошибки HTTP-статусов 4xx и 5xx. Можно настроить собственную обработку:

```java
String result = restClient.get()
    .uri("https://api.example.com/data")
    .retrieve()
    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
        // Обработка ошибки
    })
    .body(String.class);
```

### Дополнительные возможности

* **Настройка конвертеров** сообщений
* **Регистрация перехватов** (interceptors)
* **Работа с cookie**
* **Обработка URI-шаблонов**

### Преимущества RestClient

* **Современный API** с fluent-синтаксисом
* **Встроенная поддержка** JSON-сериализации
* **Многопоточная безопасность**
* **Гибкая настройка** через билдер

RestClient является отличным выбором для работы с REST API в Spring-приложениях, особенно если вам нужен простой и понятный способ выполнения HTTP-запросов с автоматической обработкой JSON.