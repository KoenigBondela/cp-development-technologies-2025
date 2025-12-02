# Быстрый старт

## Предварительные требования

1. **JDK 21** - установлен и настроен в PATH
2. **Maven 3.6+** - установлен и настроен в PATH
3. **MySQL 8.0+** (только для запуска Lab1 и Lab2, тесты используют H2)

## Быстрая настройка

### 1. Настройка базы данных MySQL

Создайте базу данных:
```sql
CREATE DATABASE hotel_db;
```

Измените настройки подключения в `src/main/resources/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=ваш_пароль
```

### 2. Сборка проекта

```bash
mvn clean compile
```

### 3. Запуск лабораторных работ

**Лабораторная работа №1 (JDBC):**
```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1Main"
```

**Лабораторная работа №2 (JPA/Hibernate):**
```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jpa.Lab2Main"
```

### 4. Запуск тестов (Лабораторная работа №3)

```bash
mvn test
```

Просмотр отчета о покрытии кода:
```bash
mvn jacoco:report
```

Отчет будет в `target/site/jacoco/index.html`

## Проверка покрытия кода

Для проверки, что покрытие составляет 100%:
```bash
mvn clean test jacoco:check
```

## Примечания

- Тесты используют H2 in-memory базу данных и **не требуют** установки MySQL
- Все зависимости автоматически загружаются Maven при первой сборке
- Проект полностью кроссплатформенный (Windows, Linux, macOS)

