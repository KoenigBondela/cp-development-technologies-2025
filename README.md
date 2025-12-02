# Hotel Database Labs

Проект содержит три лабораторные работы по работе с базами данных в Java на примере системы управления гостиницей.

## Требования

- JDK 21
- Maven 3.6+
- MySQL 8.0+ (для запуска основных приложений)
- H2 Database (используется автоматически для тестов)

## Структура проекта

```
src/
├── main/
│   ├── java/com/hoteldb/labs/
│   │   ├── jdbc/          # Лабораторная работа №1: JDBC
│   │   ├── jpa/            # Лабораторная работа №2: JPA/Hibernate
│   │   └── model/         # Модели данных
│   └── resources/
│       ├── database.properties    # Конфигурация БД для JDBC
│       ├── META-INF/persistence.xml  # Конфигурация JPA
│       └── sql/init.sql   # SQL скрипт для инициализации БД
└── test/
    ├── java/               # Unit тесты (Лабораторная работа №3)
    └── resources/
        ├── test-database.properties  # Конфигурация БД для тестов
        └── META-INF/persistence.xml  # Конфигурация JPA для тестов
```

## Настройка базы данных

1. Установите MySQL и создайте базу данных:
```sql
CREATE DATABASE hotel_db;
```

2. Настройте подключение в `src/main/resources/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=your_password
```

3. (Опционально) Выполните SQL скрипт для создания таблиц и тестовых данных:
```bash
mysql -u root -p hotel_db < src/main/resources/sql/init.sql
```

## Сборка проекта

```bash
mvn clean compile
```

## Запуск лабораторных работ

### Лабораторная работа №1: JDBC

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1Main"
```

Демонстрирует:
- Подключение к БД через JDBC
- CRUD операции с номерами и клиентами
- Проверку успешного подключения

### Лабораторная работа №2: JPA/Hibernate

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jpa.Lab2Main"
```

Демонстрирует:
- Подключение к БД через JPA (Hibernate)
- CRUD операции с использованием JPA
- Вывод универсального отношения (JOIN таблиц rooms и clients)

### Лабораторная работа №3: Unit тестирование

```bash
mvn test
```

Запускает все unit тесты с проверкой покрытия кода.

Просмотр отчета о покрытии:
```bash
mvn jacoco:report
```

Отчет будет доступен в `target/site/jacoco/index.html`

## Покрытие кода

Проект настроен на требование 100% покрытия кода. JaCoCo проверяет покрытие при выполнении тестов:

```bash
mvn clean test jacoco:check
```

## Кроссплатформенность

Проект полностью кроссплатформенный:
- Все зависимости строго указаны в `pom.xml`
- Конфигурация БД вынесена в properties файлы
- Тесты используют H2 in-memory базу данных (не требуют установки MySQL)
- Пути к ресурсам используют classpath (не зависят от ОС)

## Зависимости

Все зависимости управляются через Maven и указаны в `pom.xml`:
- MySQL Connector/J 8.3.0
- H2 Database 2.2.224 (для тестов)
- Jakarta Persistence API 3.1.0
- Hibernate 6.4.4.Final
- JUnit 5.10.1
- Mockito 5.11.0
- JaCoCo 0.8.11

## Примечания

- Для запуска основных приложений (Lab1, Lab2) требуется MySQL
- Тесты работают с H2 in-memory базой и не требуют MySQL
- Все пути и конфигурации настроены для кроссплатформенной работы

