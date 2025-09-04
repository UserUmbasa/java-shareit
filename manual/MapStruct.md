## MapStruct: библиотека для маппинга объектов в Java

### Что такое MapStruct

**MapStruct** — это библиотека для Java, которая автоматизирует процесс преобразования (маппинга) 
объектов между различными классами. Она генерирует код во время компиляции, что делает процесс 
маппинга быстрым и эффективным.

### Основные преимущества

* **Производительность**: генерируемый код работает быстрее, чем рефлексия
* **Безопасность типов**: все преобразования проверяются на этапе компиляции
* **Простота использования**: декларативный подход к определению мапперов
* **Гибкость**: возможность настройки правил маппинга
* **Отсутствие зависимостей**: сгенерированный код не зависит от MapStruct

### Как работает MapStruct

MapStruct использует аннотации для определения правил маппинга. На основе этих аннотаций генерируется 
Java-код, который выполняет преобразования объектов.

### Установка

Для использования MapStruct необходимо добавить зависимости в проект:

```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.3.Final</version>
</dependency>

<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>1.5.3.Final</version>
    <scope>provided</scope>
</dependency>
```

### Создание маппера

Пример создания простого маппера:

```java
@Mapper
public interface UserMapper {
    UserDto toDto(User user);
    
    User toEntity(UserDto userDto);
}
```

### Основные концепции

#### 1. Определение маппера

```java
@Mapper
public interface ProductMapper {
    ProductDto toDto(Product product);
    
    Product toEntity(ProductDto productDto);
}
```

#### 2. Настройка маппинга

* **Игнорирование полей**:
```java
@Mapper
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto userDto);
}
```

* **Ручное маппинг**:
```java
@Mapper
public abstract class CustomMapper {
    @Mapping(target = "fullName", expression = "java(concatFullName(user))")
    abstract UserDto toDto(User user);
    
    protected String concatFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
```

### Продвинутые возможности

#### 1. Вложенные мапперы

```java
@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDto toDto(Address address);
}

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserMapper {
    UserDto toDto(User user);
}
```

#### 2. Условный маппинг

```java
@Mapper
public interface UserMapper {
    @Mapping(target = "status", qualifiedByName = "mapStatus")
    UserDto toDto(User user);
    
    @Named("mapStatus")
    default String mapStatus(UserStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }
}
```

### Интеграция с Spring

Для использования MapStruct в Spring-приложении необходимо:

1. Добавить компонентный модель:
```java
@Mapper(componentModel = "spring")
```

2. Автоматически подключать мапперы:
```java
@Component
public class Service {
    @Autowired
    private UserMapper userMapper;
}
```

### Основные аннотации

* **@Mapper** — определяет интерфейс маппера
* **@Mapping** — настраивает правила маппинга
* **@Mappings** — группа маппингов
* **@Named** — определяет именованную функцию маппинга
* **@InheritConfiguration** — наследует конфигурации маппинга

### Примеры использования

```java
@Mapper
public interface ProductMapper {
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "productPrice", source = "price")
    ProductDto toDto(Product product);
    
    @Mapping(target = "name", source = "productName")
    @Mapping(target = "price", source = "productPrice")
    Product toEntity(ProductDto productDto);
}
```