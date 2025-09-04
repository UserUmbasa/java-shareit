## Lombok: библиотека для упрощения Java-кода

### Что такое Lombok

**Lombok** — это библиотека для Java, которая позволяет сократить объем шаблонного кода в классах с 
помощью специальных аннотаций. Она генерирует код автоматически во время компиляции, что делает код 
более читаемым и поддерживаемым.

### Основные преимущества

* **Сокращение шаблонного кода**
* **Улучшение читаемости**
* **Уменьшение количества ошибок**
* **Упрощение разработки**
* **Автоматическое создание методов**

### Установка

Для использования Lombok необходимо добавить зависимость в проект:

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
    <scope>provided</scope>
</dependency>
```

### Основные аннотации

#### 1. @Getter и @Setter

Автоматически генерируют геттеры и сеттеры для полей класса:

```java
@Getter
@Setter
public class User {
    private String name;
    private int age;
}
```

#### 2. @ToString

Генерирует метод toString() для удобного вывода объекта:

```java
@ToString
public class User {
    private String name;
    private int age;
}
```

#### 3. @EqualsAndHashCode

Создает методы equals() и hashCode():

```java
@EqualsAndHashCode
public class User {
    private String name;
    private int age;
}
```

#### 4. @NoArgsConstructor

Создает конструктор без параметров:

```java
@NoArgsConstructor
public class User {
    private String name;
    private int age;
}
```

#### 5. @RequiredArgsConstructor

Создает конструктор с параметрами для всех final полей:

```java
@RequiredArgsConstructor
public class User {
    private final String name;
    private int age;
}
```

#### 6. @AllArgsConstructor

Создает конструктор со всеми полями класса:

```java
@AllArgsConstructor
public class User {
    private String name;
    private int age;
}
```

#### 7. @Data

Объединяет в себе @ToString, @EqualsAndHashCode, @Getter, @Setter и @RequiredArgsConstructor:

```java
@Data
public class User {
    private String name;
    private int age;
}
```

### Продвинутые возможности

#### 1. @Builder

Создает паттерн Builder для создания объектов:

```java
@Builder
public class User {
    private String name;
    private int age;
}

// Использование:
User user = User.builder()
    .name("Иван")
    .age(25)
    .build();
```

#### 2. @Value

Создает неизменяемые классы (final поля, final класс, без сеттеров):

```java
@Value
public class User {
    private String name;
    private int age;
}
```

#### 3. @Singular

Упрощает работу с коллекциями в конструкторе:

```java
@Data
@Builder
public class User {
    @Singular
    private Set<String> hobbies;
}

// Использование:
User user = User.builder()
    .hobby("футбол")
    .hobby("плавание")
    .build();
```

### Настройка Lombok

#### 1. Игнорирование полей

Можно указать, какие поля игнорировать при генерации методов:

```java
@Data
public class User {
    @Getter(AccessLevel.PACKAGE)
    private String password;
    
    @Setter(AccessLevel.PROTECTED)
    private String secret;
}
```

#### 2. Настройка toString()

Можно настроить формат вывода:

```java
@ToString(of = {"name", "age"})
public class User {
    private String name;
    private int age;
    private String email;
}
```

### Важные моменты

* **IDE-поддержка**: необходимо включить поддержку Lombok в вашей IDE
* **Компиляция**: Lombok работает на этапе компиляции
* **Тестирование**: сгенерированный код можно тестировать как обычный Java-код
* **Совместимость**: работает с большинством современных Java-фреймворков

### Примеры использования

```java
@Data
@Builder
@AllArgsConstructor
public class Product {
    private