# Проект: Разработка технологий программирования

## Описание проекта

Данный проект представляет собой комплексную систему для изучения различных технологий программирования на Java. Проект включает в себя работу с базами данных (JDBC, JPA), паттерны проектирования, аспектно-ориентированное программирование, логирование и обработку ошибок. Все лабораторные работы реализованы на примере системы управления гостиницей и игры "Пунические войны".

---

## Содержание

1. [Лабораторная работа №1: Связь с БД при помощи JDBC](#лабораторная-работа-1-связь-с-бд-при-помощи-jdbc)
2. [Лабораторная работа №2: Связь с БД при помощи JPA](#лабораторная-работа-2-связь-с-бд-при-помощи-jpa)
3. [Лабораторная работа №4: Порождающие паттерны проектирования](#лабораторная-работа-4-порождающие-паттерны-проектирования)
4. [Лабораторная работа №5: Структурные паттерны проектирования](#лабораторная-работа-5-структурные-паттерны-проектирования)
5. [Лабораторная работа №6: Паттерны поведения](#лабораторная-работа-6-паттерны-поведения)
6. [Лабораторная работа №7: Hello, AspectJ](#лабораторная-работа-7-hello-aspectj)
7. [Лабораторная работа №8: Аспектная обработка БД](#лабораторная-работа-8-аспектная-обработка-бд)
8. [Лабораторная работа №9: Аспектно-ориентированные версии паттернов](#лабораторная-работа-9-аспектно-ориентированные-версии-паттернов)
9. [Лабораторная работа №10: Юнит-тестирование](#лабораторная-работа-10-юнит-тестирование)

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

# Лабораторная работа №1: Связь с БД при помощи JDBC

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

# Лабораторная работа №2: Связь с БД при помощи JPA

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

# Лабораторная работа №4: Порождающие паттерны проектирования

## Цель работы

Изучение и реализация порождающих паттернов проектирования на примере игры "Пунические войны".

## Реализованные паттерны

### Паттерн Builder (Порождающий)

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
└── ...
com.hoteldb.labs.pattern4/
└── creational/
    └── Lab4Main.java        # Демонстрация паттерна
```

## Запуск

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.pattern4.creational.Lab4Main"
```

---

# Лабораторная работа №5: Структурные паттерны проектирования

## Цель работы

Изучение и реализация структурных паттернов проектирования на примере игры "Пунические войны".

## Реализованные паттерны

### Паттерн Bridge (Структурный)

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
├── bridge/                  # Паттерн Bridge
│   ├── BattleUnit.java      # Abstraction
│   ├── Weapon.java          # Implementor
│   ├── Bow.java             # Concrete Implementor
│   ├── Sword.java           # Concrete Implementor
│   ├── Lance.java           # Concrete Implementor
│   ├── ArcherUnit.java      # Refined Abstraction
│   ├── InfantrymanUnit.java # Refined Abstraction
│   └── HorsemanUnit.java    # Refined Abstraction
└── ...
com.hoteldb.labs.pattern5/
└── structural/
    └── Lab5Main.java        # Демонстрация паттерна
```

## Запуск

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.pattern5.structural.Lab5Main"
```

---

# Лабораторная работа №6: Паттерны поведения

## Цель работы

Изучение и реализация поведенческих паттернов проектирования для обхода коллекций без раскрытия их внутренней структуры.

## Реализованные паттерны

### Паттерн Iterator (Поведенческий)

**Назначение:** Предоставляет механизм обхода элементов составных объектов (коллекций) не раскрывая их внутреннего представления.

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
└── ...
com.hoteldb.labs.pattern6/
└── behavioral/
    └── Lab6Main.java         # Демонстрация паттерна
```

## Запуск

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.pattern6.behavioral.Lab6Main"
```

---

# Лабораторная работа №7: Hello, AspectJ

## Цель работы

Изучение аспектно-ориентированного программирования (AOP) с использованием AspectJ. Реализация базовых аспектов для перехвата вызовов методов.

## Реализованные компоненты

### 1. HelloWorld.java

Класс содержит:
- Два поля: `Name` и `FamilyName`
- Геттеры и сеттеры для этих полей
- Метод `say(String message)` - воспроизведение фразы
- Метод `sayToPerson(String message, String name)` - обращение фразы к конкретному человеку

### 2. MainClass.java

Главный класс для демонстрации работы с экземплярами HelloWorld.

### 3. AspectManners.aj

Аспект, реализующий:

**Pointcuts:**
- `sayMethod()` - фильтрует все методы, содержащие "say" в названии
- `methodsWithoutSay()` - отсеивает методы без "say"
- `callSayMessageToPerson(String person)` - перехватывает вызов `sayToPerson` с аргументом person

**Advices:**
- `before(): methodsWithoutSay()` - логирование входа в методы без "say"
- `after(): methodsWithoutSay()` - логирование выхода из методов без "say"
- `around(String person): callSayMessageToPerson(person)` - добавление "-san" к имени (японская вежливость)

## Особенности реализации

- Использование AspectJ Maven Plugin для компиляции
- Weaving (вплетение) аспектов в байт-код на этапе компиляции
- Использование `execution` для перехвата выполнения методов
- Использование `call` для перехвата вызовов методов
- Модификация аргументов через `around()` advice

## Структура классов

```
com.hoteldb.labs.aspectj/
├── AspectManners.aj          # Аспект с логикой перехвата
├── HelloWorld.java           # Класс с методами для перехвата
└── MainClass.java            # Главный класс для демонстрации
```

## Запуск

**Важно:** Проект должен компилироваться через Maven для правильной работы аспектов.

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.aspectj.MainClass"
```

## Ожидаемый вывод

При правильной работе аспектов вывод должен содержать:
- Логирование входа и выхода из методов без "say" с временными метками
- Добавление "-san" к имени при вызове `sayToPerson`

Пример вывода:
```
How do you do?
Entering method without say execution(void HelloWorld.setName(String)) Timestamp:1453790224224
Leaving method without say execution(void HelloWorld.setName(String)) Timestamp:1453790224224
Entering method without say execution(void HelloWorld.setFamilyName(String)) Timestamp:1453790224224
Leaving method without say execution(void HelloWorld.setFamilyName(String)) Timestamp:1453790224224
Entering method without say execution(String HelloWorld.getName()) Timestamp:1453790224224
Leaving method without say execution(String HelloWorld.getName()) Timestamp:1453790224224
John-san, how do you do?
```

---

# Лабораторная работа №8: Аспектная обработка БД

## Цель работы

Реализация аспектной обработки операций с базой данных. Применение аспектов для логирования, обработки ошибок и мониторинга операций БД в лабораторной работе №1 (JDBC).

## Реализованные компоненты

### Логирование операций БД

Все операции с базой данных логируются через аспекты:
- Создание соединения
- Выполнение SQL запросов
- Обработка результатов
- Обработка ошибок
- Закрытие соединений

### Обработка ошибок

Аспекты перехватывают исключения и обеспечивают:
- Логирование ошибок с полным контекстом
- Корректное закрытие ресурсов
- Информативные сообщения об ошибках

## Особенности реализации

- Использование AspectJ для перехвата вызовов методов DAO
- Логирование всех операций с БД
- Автоматическая обработка ошибок
- Мониторинг производительности операций

## Запуск

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1Main"
```

Логи будут записываться в папку `logs/` в корне проекта.

---

# Лабораторная работа №9: Аспектно-ориентированные версии паттернов

## Цель работы

Разработка аспектных версий шаблонов проектирования из лабораторных работ №4, №5, №6 на основе теоретических материалов и статей Николаса Лесицки «AOP@Work: Улучшенные шаблоны проектирования AspectJ».

## Реализованные аспекты

### 1. BuilderAspect.aj

Аспект для паттерна Builder, добавляющий:
- Логирование процесса построения объектов
- Отслеживание вызовов методов `set*`
- Логирование создания стандартных воинов
- Обработка ошибок при построении

**Pointcuts:**
- `buildMethod()` - перехват метода `build()`
- `setterMethods()` - перехват методов `set*`
- `standardBuilders()` - перехват методов `buildStandard*`

**Advices:**
- `before(): buildMethod()` - логирование начала построения
- `after() returning: buildMethod()` - логирование успешного построения
- `after() throwing: buildMethod()` - обработка ошибок
- `before(): setterMethods()` - логирование установки параметров

### 2. BridgeAspect.aj

Аспект для паттерна Bridge, добавляющий:
- Логирование смены оружия
- Отслеживание вызовов метода `info()`
- Логирование атак оружием

**Pointcuts:**
- `changeWeapon(BattleUnit, Weapon)` - перехват смены оружия
- `infoMethod()` - перехват вызова `info()`
- `weaponAttack()` - перехват атаки оружием

**Advices:**
- `before(): changeWeapon()` - логирование смены оружия
- `after(): changeWeapon()` - подтверждение смены
- `before(): infoMethod()` - логирование вывода информации
- `before(): weaponAttack()` - логирование атаки

### 3. IteratorAspect.aj

Аспект для паттерна Iterator, добавляющий:
- Логирование операций итератора
- Статистику обхода коллекции
- Отслеживание добавления элементов

**Pointcuts:**
- `createIterator()` - перехват создания итератора
- `hasNext()` - перехват проверки наличия следующего элемента
- `next()` - перехват получения следующего элемента
- `reset()` - перехват сброса итератора
- `addWarrior(WarriorCollection, Warrior)` - перехват добавления воина

**Advices:**
- `after() returning: createIterator()` - логирование создания
- `after() returning: hasNext()` - подсчет итераций
- `after() returning: next()` - логирование получения элемента
- `before(): reset()` - вывод статистики перед сбросом
- `after(): addWarrior()` - логирование добавления

## Структура классов

```
com.hoteldb.labs.pattern9/
├── aspects/
│   ├── BuilderAspect.aj     # Аспект для Builder
│   ├── BridgeAspect.aj       # Аспект для Bridge
│   └── IteratorAspect.aj     # Аспект для Iterator
└── Lab9Main.java             # Демонстрация всех аспектов
```

## Запуск

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.pattern9.Lab9Main"
```

## Преимущества аспектного подхода

1. **Разделение ответственности:** Логирование и мониторинг вынесены из бизнес-логики
2. **Переиспользование:** Один аспект может применяться к множеству классов
3. **Гибкость:** Легко включать/выключать аспекты без изменения основного кода
4. **Чистота кода:** Бизнес-логика не загромождена кодом логирования

---

# Лабораторная работа №10: Юнит-тестирование

## Цель работы

Изучение принципов модульного тестирования с использованием JUnit 5 и Mockito. Достижение высокого покрытия кода тестами.

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

Проект настроен на требование высокого покрытия кода.

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
│   │   │   ├── aspectj/           # Лабораторная работа №7
│   │   │   ├── pattern4/          # Лабораторная работа №4
│   │   │   │   └── creational/
│   │   │   ├── pattern5/          # Лабораторная работа №5
│   │   │   │   └── structural/
│   │   │   ├── pattern6/          # Лабораторная работа №6
│   │   │   │   └── behavioral/
│   │   │   └── pattern9/           # Лабораторная работа №9
│   │   │       └── aspects/
│   │   └── punic/
│   │       ├── core/              # Базовые классы
│   │       ├── builder/           # Паттерн Builder
│   │       ├── bridge/            # Паттерн Bridge
│   │       ├── iterator/          # Паттерн Iterator
│   │       └── demo/              # Демонстрационные классы
│   └── resources/
│       ├── database.properties    # Конфигурация БД
│       ├── logback.xml            # Конфигурация логирования
│       ├── META-INF/
│       │   └── persistence.xml    # Конфигурация JPA
│       └── sql/
│           └── init.sql           # SQL скрипт инициализации
└── test/
    ├── java/                      # Лабораторная работа №10
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
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.pattern4.creational.Lab4Main"
```

**Лабораторная работа №5 (Bridge):**
```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.pattern5.structural.Lab5Main"
```

**Лабораторная работа №6 (Iterator):**
```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.pattern6.behavioral.Lab6Main"
```

**Лабораторная работа №7 (Hello, AspectJ):**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.aspectj.MainClass"
```

**Лабораторная работа №9 (Аспектные паттерны):**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.pattern9.Lab9Main"
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
- Применение паттернов проектирования (порождающих, структурных, поведенческих)
- Аспектно-ориентированное программирование
- Профессиональное логирование и обработка ошибок
- Модульное тестирование с высоким покрытием кода

Все лабораторные работы реализованы с соблюдением лучших практик разработки и готовы для демонстрации преподавателю.
