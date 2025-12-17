# Проект: Разработка технологий программирования

## Описание проекта

Данный проект представляет собой комплексную систему для изучения различных технологий программирования на Java. Проект включает в себя работу с базами данных (JDBC, JPA), паттерны проектирования, аспектно-ориентированное программирование, логирование и обработку ошибок. Все лабораторные работы реализованы на примере системы управления гостиницей и игры "Пунические войны".

---

## Содержание

1. [Лабораторная работа №1: JDBC](#лабораторная-работа-1-jdbc)
2. [Лабораторная работа №2: JPA/Hibernate](#лабораторная-работа-2-jpahibernate)
3. [Лабораторная работа №3: Unit тестирование](#лабораторная-работа-3-unit-тестирование)
4. [Лабораторная работа №4: Паттерны проектирования (Bridge, Builder)](#лабораторная-работа-4-паттерны-проектирования-bridge-builder)
5. [Лабораторная работа №6: Паттерн Iterator](#лабораторная-работа-6-паттерн-iterator)
6. [Лабораторная работа №7: AspectJ](#лабораторная-работа-7-aspectj)
7. [Лабораторная работа №8: Логирование и обработка ошибок](#лабораторная-работа-8-логирование-и-обработка-ошибок)

---

## Требования

- **JDK 21**
- **Maven 3.6+**
- **MySQL 8.0+** (для запуска основных приложений)
- **H2 Database** (используется автоматически для тестов)

---

## Настройка проекта

### 1. Настройка базы данных MySQL

Создайте базу данных:
```sql
CREATE DATABASE hotel_db;
```

Настройте подключение в `src/main/resources/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=your_password
db.driver=com.mysql.cj.jdbc.Driver
```

### 2. Сборка проекта

```bash
mvn clean compile
```

---

# Лабораторная работа №1: JDBC

## Цель работы

Изучение работы с базами данных через JDBC API. Реализация паттерна DAO (Data Access Object) для выполнения CRUD операций с данными гостиницы.

## Реализованные компоненты

### 1. DatabaseConnection
- **Паттерн:** Singleton
- **Функционал:**
  - Управление единственным соединением с базой данных
  - Загрузка настроек подключения из properties-файла
  - Проверка состояния соединения
  - Закрытие соединения

### 2. ClientDAO (Data Access Object)
- **Методы:**
  - `create(Client client)` - создание нового клиента
  - `findById(Integer id)` - поиск клиента по ID
  - `findAll()` - получение всех клиентов
  - `update(Client client)` - обновление информации о клиенте
  - `delete(Integer id)` - удаление клиента

### 3. RoomDAO (Data Access Object)
- **Методы:**
  - `create(Room room)` - создание нового номера
  - `findById(Integer id)` - поиск номера по ID
  - `findAll()` - получение всех номеров
  - `update(Room room)` - обновление информации о номере
  - `delete(Integer id)` - удаление номера

## Особенности реализации

- Использование `PreparedStatement` для защиты от SQL-инъекций
- Автоматическое управление ресурсами через try-with-resources
- Обработка NULL значений в базе данных
- Получение сгенерированных ключей после INSERT операций

## Запуск

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1Main"
```

## Структура классов

```
com.hoteldb.labs.jdbc/
├── DatabaseConnection.java    # Singleton для управления соединением
├── ClientDAO.java              # DAO для работы с клиентами
├── RoomDAO.java                # DAO для работы с номерами
└── Lab1Main.java               # Главный класс для демонстрации
```

---

# Лабораторная работа №2: JPA/Hibernate

## Цель работы

Изучение работы с базами данных через JPA (Java Persistence API) с использованием Hibernate как реализации. Демонстрация преимуществ ORM подхода по сравнению с JDBC.

## Реализованные компоненты

### 1. Entity классы

#### ClientEntity
- Аннотации JPA: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`
- Связь с RoomEntity через `@ManyToOne`
- Автоматическое управление жизненным циклом через EntityManager

#### RoomEntity
- Полная аннотация JPA
- Связь с ClientEntity через `@OneToMany`

### 2. Service классы

#### ClientService
- Использование EntityManager для CRUD операций
- Управление транзакциями
- Методы: `create()`, `findById()`, `findAll()`, `update()`, `delete()`

#### RoomService
- Аналогичная функциональность для работы с номерами

#### UniversalRelationService
- **Особенность:** Выполнение LEFT JOIN запроса для получения универсального отношения
- Объединение данных из таблиц `rooms` и `clients`
- Использование JPQL (Java Persistence Query Language)

## Особенности реализации

- Конфигурация через `persistence.xml`
- Использование EntityManagerFactory для создания EntityManager
- Управление транзакциями вручную
- Выполнение нативных SQL запросов через `createNativeQuery()`

## Запуск

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jpa.Lab2Main"
```

## Структура классов

```
com.hoteldb.labs.jpa/
├── entity/
│   ├── ClientEntity.java
│   └── RoomEntity.java
├── service/
│   ├── ClientService.java
│   ├── RoomService.java
│   └── UniversalRelationService.java
└── Lab2Main.java
```

---

# Лабораторная работа №3: Unit тестирование

## Цель работы

Изучение принципов модульного тестирования с использованием JUnit 5 и Mockito. Достижение 100% покрытия кода тестами.

## Реализованные тесты

### Тесты для JDBC слоя
- `DatabaseConnectionTest` - тестирование подключения к БД
- `ClientDAOTest` - тестирование всех CRUD операций для клиентов
- `RoomDAOTest` - тестирование всех CRUD операций для номеров

### Тесты для JPA слоя
- `ClientEntityTest` - тестирование работы с сущностями клиентов
- `RoomEntityTest` - тестирование работы с сущностями номеров
- `ClientServiceTest` - тестирование сервисного слоя для клиентов
- `RoomServiceTest` - тестирование сервисного слоя для номеров

### Тесты для моделей
- `ClientTest` - тестирование модели Client
- `RoomTest` - тестирование модели Room

## Особенности реализации

- Использование H2 in-memory базы данных для тестов
- Изоляция тестов друг от друга
- Использование Mockito для мокирования зависимостей
- Проверка покрытия кода через JaCoCo

## Запуск тестов

```bash
mvn test
```

## Просмотр отчета о покрытии

```bash
mvn jacoco:report
```

Отчет будет доступен в `target/site/jacoco/index.html`

## Проверка покрытия кода

```bash
mvn clean test jacoco:check
```

Проект настроен на требование 100% покрытия кода.

---

# Лабораторная работа №4: Паттерны проектирования (Bridge, Builder)

## Цель работы

Изучение и реализация порождающих и структурных паттернов проектирования на примере игры "Пунические войны".

## Реализованные паттерны

### 1. Паттерн Builder (Порождающий)

**Назначение:** Пошаговое создание сложных объектов. Позволяет создавать различные конфигурации объектов, используя один и тот же процесс конструирования.

**Реализация:** `WarriorBuilder`

**Особенности:**
- Fluent interface (цепочка вызовов методов)
- Методы для настройки всех параметров воина
- Статические методы для создания стандартных воинов:
  - `buildStandardArcher()` - стандартный лучник
  - `buildStandardInfantryman()` - стандартный пехотинец
  - `buildStandardHorseman()` - стандартный всадник
- Метод `build()` для создания финального объекта

**Пример использования:**
```java
Warrior eliteArcher = new WarriorBuilder()
    .setType("Archer")
    .setAppearance("Улучшенная кожаная броня")
    .setHealth(70)
    .setSpeed(50)
    .setProtection(35)
    .setCombatPower(80)
    .build();
```

### 2. Паттерн Bridge (Структурный)

**Назначение:** Разделение абстракции и реализации, чтобы они могли изменяться независимо друг от друга.

**Реализация:**
- **Abstraction:** `BattleUnit` (абстрактный класс)
- **Implementor:** `Weapon` (интерфейс)
- **Concrete Implementors:** `Bow`, `Sword`, `Lance`
- **Refined Abstractions:** `ArcherUnit`, `InfantrymanUnit`, `HorsemanUnit`

**Особенности:**
- Абстракция (`BattleUnit`) делегирует работу реализации (`Weapon`)
- Возможность замены оружия на лету через метод `changeWeapon()`
- Каждый тип юнита имеет оружие по умолчанию
- Разделение логики юнита и логики оружия

**Пример использования:**
```java
BattleUnit archer = new ArcherUnit(new Bow());
archer.info(); // Использует лук

archer.changeWeapon(new Sword()); // Замена оружия
archer.info(); // Теперь использует меч
```

## Структура классов

```
punic/
├── core/                    # Базовые классы воинов
│   ├── Warrior.java         # Интерфейс воина
│   ├── Archer.java
│   ├── Infantryman.java
│   └── Horseman.java
├── builder/                 # Паттерн Builder
│   └── WarriorBuilder.java
├── bridge/                  # Паттерн Bridge
│   ├── BattleUnit.java      # Abstraction
│   ├── Weapon.java          # Implementor
│   ├── Bow.java             # Concrete Implementor
│   ├── Sword.java           # Concrete Implementor
│   ├── Lance.java           # Concrete Implementor
│   ├── ArcherUnit.java      # Refined Abstraction
│   ├── InfantrymanUnit.java # Refined Abstraction
│   └── HorsemanUnit.java    # Refined Abstraction
└── demo/
    ├── Main.java            # Демо Builder
    └── BridgeDemo.java      # Демо Bridge
```

## Запуск демонстраций

**Builder:**
```bash
mvn exec:java -Dexec.mainClass="punic.demo.Main"
```

**Bridge:**
```bash
mvn exec:java -Dexec.mainClass="punic.demo.BridgeDemo"
```

---

# Лабораторная работа №6: Паттерн Iterator

## Цель работы

Изучение и реализация поведенческого паттерна Iterator для обхода коллекций без раскрытия их внутренней структуры.

## Реализованные компоненты

### 1. WarriorIterator (Интерфейс итератора)

**Методы:**
- `boolean hasNext()` - проверка наличия следующего элемента
- `Warrior next()` - получение следующего элемента
- `void reset()` - сброс итератора в начало коллекции

### 2. WarriorCollection (Коллекция воинов)

**Особенности:**
- Внутреннее хранение в массиве (скрыто от клиента)
- Динамическое расширение массива при необходимости
- Метод `createIterator()` для создания итератора
- Внутренний класс `WarriorIteratorImpl` реализует логику обхода

**Методы:**
- `addWarrior(Warrior warrior)` - добавление воина
- `getWarrior(int index)` - получение воина по индексу
- `size()` - размер коллекции
- `isEmpty()` - проверка на пустоту
- `createIterator()` - создание итератора

## Особенности реализации

- **Инкапсуляция:** Внутренняя структура (массив) полностью скрыта от клиента
- **Универсальность:** Итератор работает с любыми типами воинов (реализующими интерфейс `Warrior`)
- **Безопасность:** Проверки границ и null-значений
- **Гибкость:** Можно создавать несколько независимых итераторов для одной коллекции

## Пример использования

```java
WarriorCollection army = new WarriorCollection();

// Добавление воинов
army.addWarrior(new Archer(...));
army.addWarrior(new Infantryman(...));

// Обход коллекции через итератор
WarriorIterator iterator = army.createIterator();
while (iterator.hasNext()) {
    Warrior warrior = iterator.next();
    warrior.info();
}

// Сброс итератора для повторного обхода
iterator.reset();
```

## Структура классов

```
punic/iterator/
├── WarriorIterator.java      # Интерфейс итератора
├── WarriorCollection.java    # Коллекция с внутренним итератором
└── IteratorDemo.java        # Демонстрация работы паттерна
```

## Запуск демонстрации

```bash
mvn exec:java -Dexec.mainClass="punic.iterator.IteratorDemo"
```

---

# Лабораторная работа №7: AspectJ

## Цель работы

Изучение аспектно-ориентированного программирования (AOP) с использованием AspectJ. Реализация сквозной функциональности через аспекты.

## Реализованные аспекты

### AspectManners.aj

**Функционал:**
- Перехват вызовов методов с аннотацией `@Manners`
- Добавление приветствий "Good day!" и "Nice to meet you!" до и после выполнения метода
- Логирование входа и выхода из методов
- Перехват методов без аннотации `@Manners` с логированием

**Pointcuts:**
- `@annotation(com.hoteldb.labs.aspectj.Manners)` - методы с аннотацией @Manners
- `execution(* com.hoteldb.labs.aspectj.HelloWorld.*(..))` - все методы класса HelloWorld

**Advices:**
- `@Before` - выполнение перед методом
- `@After` - выполнение после метода
- `@Around` - обертка вокруг метода

## Особенности реализации

- Использование AspectJ Maven Plugin для компиляции
- Weaving (вплетение) аспектов в байт-код на этапе компиляции
- Использование аннотаций для маркировки методов
- Логирование с временными метками

## Структура классов

```
com.hoteldb.labs.aspectj/
├── AspectManners.aj          # Аспект с логикой перехвата
├── HelloWorld.java          # Класс с методами для перехвата
└── MainClass.java           # Главный класс для демонстрации
```

## Запуск

**Важно:** Проект должен компилироваться через Maven для правильной работы аспектов.

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.aspectj.MainClass"
```

## Ожидаемый вывод

При правильной работе аспектов вывод должен содержать:
- Приветствия "Good day!" и "Nice to meet you!" вокруг методов с аннотацией @Manners
- Логирование входа и выхода из всех методов
- Временные метки для каждого перехваченного вызова

---

# Лабораторная работа №8: Логирование и обработка ошибок

## Цель работы

Реализация профессионального логирования всех операций с базой данных и улучшенной обработки ошибок для лабораторной работы №1 (JDBC).

## Реализованные компоненты

### 1. Настройка логирования

**Библиотеки:**
- SLF4J API 2.0.9 (фасад для логирования)
- Logback Classic 1.4.14 (реализация)

**Конфигурация (`logback.xml`):**
- **Консольный вывод** - для разработки
- **Файловое логирование** - основной лог с ротацией по дням
- **Файл ошибок** - отдельный файл только для ERROR уровня
- Настройка уровней логирования для разных пакетов

### 2. Логирование в DatabaseConnection

**Логируемые события:**
- Загрузка настроек подключения
- Создание нового соединения
- Использование существующего соединения
- Ошибки подключения (с полным стектрейсом)
- Закрытие соединения

**Уровни:**
- `DEBUG` - детальная информация
- `INFO` - успешные операции
- `ERROR` - ошибки подключения

### 3. Логирование в ClientDAO

**Логируемые операции:**
- Создание клиента (с параметрами)
- Поиск клиента по ID
- Получение всех клиентов
- Обновление клиента
- Удаление клиента
- Результаты операций (количество затронутых строк)

**Уровни:**
- `DEBUG` - SQL запросы и параметры
- `INFO` - успешные операции
- `WARN` - предупреждения (объект не найден)
- `ERROR` - ошибки с полным контекстом

### 4. Логирование в RoomDAO

Аналогично ClientDAO - полное логирование всех CRUD операций.

### 5. Улучшенная обработка ошибок

**Реализовано:**
- Валидация входных параметров (проверка на null)
- Информативные сообщения об ошибках
- Логирование исключений с полным стектрейсом
- Правильная обработка SQL-исключений
- Использование try-catch-finally для гарантированного закрытия ресурсов

**Примеры обработки:**
```java
if (client == null) {
    logger.error("Попытка создать клиента с null значением");
    throw new IllegalArgumentException("Клиент не может быть null");
}
```

### 6. Обновленный Lab1Main

- Логирование основных этапов выполнения
- Демонстрация работы с DAO
- Улучшенная обработка ошибок с использованием try-catch-finally

## Структура логов

```
logs/
├── hotel-db.log              # Основной лог (все уровни)
├── hotel-db-error.log         # Только ошибки
├── hotel-db-2024-01-15.log   # Ротация по дням
└── hotel-db-error-2024-01-15.log
```

## Особенности реализации

- **Ротация логов:** Автоматическая ротация по дням, хранение за 30 дней
- **Разделение по уровням:** Отдельный файл для ошибок
- **Производительность:** Асинхронное логирование (настройка Logback)
- **Безопасность:** Логирование не содержит паролей

## Примеры логов

**Успешная операция:**
```
2024-01-15 10:30:45.123 [main] INFO  ClientDAO - Создание нового клиента: Иван Петров
2024-01-15 10:30:45.125 [main] DEBUG ClientDAO - Выполнение SQL: INSERT INTO clients...
2024-01-15 10:30:45.200 [main] INFO  ClientDAO - Клиент успешно создан с ID: 1
```

**Ошибка:**
```
2024-01-15 10:30:50.456 [main] ERROR ClientDAO - Ошибка при создании клиента Иван Петров: 
Duplicate entry 'ivan@example.com' for key 'email'
java.sql.SQLIntegrityConstraintViolationException: ...
    at com.hoteldb.labs.jdbc.ClientDAO.create(ClientDAO.java:45)
    ...
```

## Запуск

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1Main"
```

Логи будут записываться в папку `logs/` в корне проекта.

---

## Общая структура проекта

```
src/
├── main/
│   ├── java/
│   │   ├── com/hoteldb/labs/
│   │   │   ├── jdbc/              # Лабораторная работа №1, №8
│   │   │   ├── jpa/               # Лабораторная работа №2
│   │   │   ├── model/             # Модели данных
│   │   │   └── aspectj/           # Лабораторная работа №7
│   │   └── punic/
│   │       ├── core/              # Базовые классы
│   │       ├── builder/           # Лабораторная работа №4 (Builder)
│   │       ├── bridge/            # Лабораторная работа №4 (Bridge)
│   │       ├── iterator/          # Лабораторная работа №6
│   │       └── demo/              # Демонстрационные классы
│   └── resources/
│       ├── database.properties    # Конфигурация БД
│       ├── logback.xml            # Конфигурация логирования
│       ├── META-INF/
│       │   └── persistence.xml    # Конфигурация JPA
│       └── sql/
│           └── init.sql           # SQL скрипт инициализации
└── test/
    ├── java/                      # Лабораторная работа №3
    └── resources/
        ├── test-database.properties
        └── META-INF/
            └── persistence.xml
```

---

## Используемые технологии и библиотеки

### Базы данных
- **MySQL Connector/J 8.3.0** - драйвер для MySQL
- **H2 Database 2.2.224** - in-memory БД для тестов

### ORM
- **Jakarta Persistence API 3.1.0** - стандарт JPA
- **Hibernate 6.4.4.Final** - реализация JPA

### Тестирование
- **JUnit 5.10.1** - фреймворк для тестирования
- **Mockito 5.11.0** - библиотека для мокирования
- **JaCoCo 0.8.11** - инструмент для анализа покрытия кода

### Логирование
- **SLF4J 2.0.9** - фасад для логирования
- **Logback 1.4.14** - реализация логирования

### AOP
- **AspectJ 1.9.22** - аспектно-ориентированное программирование

---

## Команды для запуска

### Сборка проекта
```bash
mvn clean compile
```

### Запуск лабораторных работ

**Лабораторная работа №1 (JDBC):**
```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1Main"
```

**Лабораторная работа №2 (JPA):**
```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jpa.Lab2Main"
```

**Лабораторная работа №4 (Builder):**
```bash
mvn exec:java -Dexec.mainClass="punic.demo.Main"
```

**Лабораторная работа №4 (Bridge):**
```bash
mvn exec:java -Dexec.mainClass="punic.demo.BridgeDemo"
```

**Лабораторная работа №6 (Iterator):**
```bash
mvn exec:java -Dexec.mainClass="punic.iterator.IteratorDemo"
```

**Лабораторная работа №7 (AspectJ):**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.aspectj.MainClass"
```

### Запуск тестов
```bash
mvn test
```

### Просмотр отчета о покрытии
```bash
mvn jacoco:report
```

---

## Заключение

Проект демонстрирует комплексное изучение современных технологий разработки на Java:
- Работа с базами данных через JDBC и JPA
- Применение паттернов проектирования
- Аспектно-ориентированное программирование
- Профессиональное логирование и обработка ошибок
- Модульное тестирование с высоким покрытием кода

Все лабораторные работы реализованы с соблюдением лучших практик разработки и готовы для демонстрации преподавателю.

