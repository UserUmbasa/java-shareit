## H2 Database: Полное руководство

### Что такое H2 Database

**H2 Database** — это встраиваемая и серверная СУБД с открытым исходным кодом, написанная на Java. 
Она позволяет создавать базы данных как в памяти, так и на диске.

### Основные особенности

* **Встраиваемый режим** — работает внутри Java-приложения
* **Серверная версия** — доступна через TCP/IP или Web-сокет
* **Поддержка SQL** — ANSI SQL-99 и SQL-2016
* **Высокая производительность**
* **Компактность** — размер всего несколько мегабайт
* **Шифрование данных**
* **Поддержка транзакций**

### Сценарии использования

* **Тестирование** приложений
* **Разработка** — быстрое прототипирование
* **Встраиваемые** приложения
* **Временное хранение** данных
* **Миграция** между базами данных

### Установка и настройка

#### Зависимости Maven

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.1.214</version>
    <scope>test</scope>
</dependency>
```

#### Зависимости Gradle

```groovy
testImplementation 'com.h2database:h2:2.1.214'
```

### Конфигурация подключения

Основные URL для подключения:

* **В памяти**: `jdbc:h2:mem:testdb`
* **На диске**: `jdbc:h2:file:./data/test`
* **Сервер**: `jdbc:h2:tcp://localhost/~/test`

### Основные команды

```sql
-- Создание базы данных
CREATE DATABASE IF NOT EXISTS test;

-- Подключение к базе
CONNECT TO DATABASE test;

-- Создание таблицы
CREATE TABLE users (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);
```

### Работа с H2 в Spring Boot

#### application.properties

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### Консоль управления

H2 предоставляет веб-консоль для управления базой данных:
* Доступ через браузер по адресу: `http://localhost:8080/h2-console`
* Имя пользователя: `sa`
* Пароль: пустой

### Преимущества H2

* **Простота установки**
* **Низкая нагрузка** на систему
* **Быстрое развертывание**
* **Поддержка JDBC**
* **Встроенный SQL-редактор**

### Ограничения

* **Не для production** — не рекомендуется для продакшен-среды
* **Масштабируемость** — ограничения при работе с большими объемами данных
* **Производительность** — может уступать специализированным СУБД

### Примеры использования

```java
// Создание подключения
try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb")) {
    Statement stmt = conn.createStatement();
    stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(100))");
    
    PreparedStatement ps = conn.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)");
    ps.setInt(1, 1);
    ps.setString(2, "John Doe");
    ps.executeUpdate();
}
```

### Рекомендации по использованию

* **Используйте для тестирования**
* **Храните конфигурации** в application.properties
* **Следите за версиями** зависимостей
* **Не используйте** в production-среде без тщательного анализа

H2 Database — отличный инструмент для разработки и тестирования Java-приложений, позволяющий быстро 
начать работу с базой данных без сложной настройки инфраструктуры.