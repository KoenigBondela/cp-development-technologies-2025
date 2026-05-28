# CP Development Technologies 2025 (Java)

Этот репозиторий содержит лабораторные работы **прошлого** и **текущего** семестров, разнесённые по директориям, но собираемые **одним корневым `pom.xml`**.

## Структура проекта (по семестрам)

- **`semester-current/`** — текущий семестр: JDBC/JPA/WEB (JSP/Servlet), unit-тесты, failover/backup
- **`semester-previous/`** — прошлый семестр: паттерны, AspectJ, антипаттерны
- **`spring-app/`** — **Лаб5–Лаб9**: отдельное **Spring Boot** приложение (Лаб4+REST, Security, CRUD, Docker). Собирается **своим** `spring-app/pom.xml`.
- **`pilot-logbook/`** — **курсовой проект**: веб-приложение «Электронная лётная книжка пилота» (CRUD полётов, агрегация налёта). Отдельный `pilot-logbook/pom.xml`, порт **8082**. Docker: `cd pilot-logbook && docker compose up --build`. См. [`pilot-logbook/README.md`](pilot-logbook/README.md), [`pilot-logbook/DOCKER.md`](pilot-logbook/DOCKER.md).

Корневой `pom.xml` в корне репозитория — **общий** для servlet-части: он подключает исходники из `semester-current/` и `semester-previous/` и собирает один `war`. Spring-приложение в корень не включён (чтобы не смешивать два стека в одной сборке).

### Сводная таблица (текущий семестр)

| Лаб | Тема | Где в репозитории | Запуск |
|-----|------|-------------------|--------|
| 1 | JDBC, DAO, failover/backup | `semester-current/.../jdbc/` | `mvn` + `Lab1Main` |
| 2 | JPA, универсальное отношение | `semester-current/.../jpa/` | `mvn` + `Lab2Main` |
| 3 | JSP/Servlet: логин, регистрация | `semester-current/.../web/`, `webapp/` | Tomcat, WAR |
| 4 | JSP/Servlet: роли, разные таблицы | `WelcomeServlet`, `welcome.jsp` | Tomcat, WAR |
| 5 | Spring Boot (аналог Лаб4) | `spring-app/` | IDEA / `mvn spring-boot:run` |
| 6 | REST API поверх Лаб5 | `spring-app/.../api/` | HTTP Basic, порт 8081 |
| 7 | Spring Security | `SecurityConfig`, `DatabaseUserDetailsService` | то же приложение |
| 8 | Bootstrap + CRUD | `spring-app/.../admin/`, шаблоны Thymeleaf | `/admin/rooms`, `/admin/users` |
| 9 | Docker (app + MySQL) | `Dockerfile`, `docker-compose.yml` | `docker compose up --build` |

> **Важно:** ниже, начиная с раздела «Проект: Разработка технологий программирования», подробно описаны лабораторные **прошлого семестра** (паттерны, AspectJ). Там другая нумерация (Лаб4 = Builder, Лаб7 = AspectJ и т.д.) — не путать с таблицей выше.

## Содержание (текущий семестр)

1. [Структура и сборка](#структура-проекта-по-семестрам)
2. [Где какая лабораторная](#где-какая-лабораторная)
3. [Настройка БД](#настройка-бд-для-текущего-семестра)
4. [Запуск Лаб1–2 (CLI)](#запуск-лабораторных-cli)
5. [Запуск Лаб3–4 (Tomcat)](#запуск-web-лаб34)
6. [Запуск Лаб5-9 (Spring)](#запуск-spring-лаб5-9)
7. [Тесты и JaCoCo](#тесты-и-покрытие)
8. [Прошлый семестр (паттерны, AspectJ)](#проект-разработка-технологий-программирования)

## Быстрый старт (сборка)

Собрать всё (оба семестра):

```bash
mvn clean test
```

Отдельной сборки “только текущий/только прошлый” сейчас нет — сборка общая (один `pom.xml`).

> Проект собирается на **JDK 21+**, но `source/target` выставлены в **17** из-за ограничений AspectJ (ajc).

Если нужно собрать AspectJ-часть (прошлый семестр, файлы `*.aj`), используйте профиль:

```bash
mvn -Paspectj clean compile
```

> Если команда `mvn` не находится в системе — установите Maven или добавьте его в PATH.

## Где какая лабораторная

### Текущий семестр (`semester-current`)

- **Лаб1 (повторение, БД по вариантам без AOP)**
  - JDBC + DAO: `semester-current/src/main/java/com/hoteldb/labs/jdbc/`
  - Конфиг БД (primary + backup): `semester-current/src/main/resources/database.properties`
  - Скрипты инициализации:
    - MySQL primary: `semester-current/src/main/resources/sql/init.sql`
    - PostgreSQL backup: `semester-current/src/main/resources/sql/init-backup.sql`
  - Unit-тесты: `semester-current/src/test/java/` (JaCoCo настроен на 100% покрытие нетривиального кода)

- **Лаб2 (JPA)**
  - JPA сущности/сервисы: `semester-current/src/main/java/com/hoteldb/labs/jpa/`
  - `persistence.xml`: `semester-current/src/main/resources/META-INF/persistence.xml`

- **Лаб3 (JSP/Servlet 1)**
  - Логин по таблице `users` (username+password) → переход на `welcome.jsp`: `semester-current/src/main/java/com/hoteldb/labs/web/LoginServlet.java`
  - Регистрация в `users`: `semester-current/src/main/java/com/hoteldb/labs/web/RegisterServlet.java`
  - JSP страницы: `semester-current/src/main/webapp/`
  - “Красивое” оформление: Bootstrap подключён в JSP

- **Лаб4 (JSP/Servlet 2)**
  - Роли `USER`/`ADMIN`: `semester-current/src/main/java/com/hoteldb/labs/jpa/entity/UserRole.java`
  - Разные таблицы для ролей (через JPA):
    - ADMIN видит `rooms`
    - USER видит **только свою строку** в `users`
  - Реализация: `semester-current/src/main/java/com/hoteldb/labs/web/WelcomeServlet.java` + `semester-current/src/main/webapp/welcome.jsp`

#### Spring Boot (`spring-app/`) — Лаб5–Лаб9

Общее для модуля:

- **Spring Boot 3.2**, Java **17**, порт **8081** (Tomcat для servlet-лаб остаётся на **8080**)
- Та же БД **`hotel_db`**, таблицы `users`, `rooms` (скрипт `semester-current/.../sql/init.sql`)
- Точка входа: `com.hoteldb.spring.SpringLabsApplication`
- Локальные настройки: `spring-app/src/main/resources/application.properties`
- При старте: `UsersDeletedColumnMigration` (добавляет `users.deleted`, если колонки нет), `AdminUserBootstrap` (создаёт `admin/admin`, если нет)

- **Лаб5 (Spring — веб, аналог Лаб4)**
  - Thymeleaf: `/login`, `/register`, редирект после входа на `/welcome`
  - Роли: ADMIN → панель `/admin`; USER → `/welcome/user` (своя строка в `users`)
  - Ключевые классы: `PageController`, `WelcomeController`, `UserAccountService`

- **Лаб6 (REST API)**
  - Отдельная цепочка Security для `/api/**` (HTTP Basic, stateless, CSRF выкл.)
  - `POST /api/v1/register` — регистрация (без авторизации)
  - `GET /api/v1/me` — текущий пользователь (JSON)
  - `GET /api/v1/rooms` — номера (только **ADMIN**)
  - Контроллеры: `RegisterRestController`, `CurrentUserRestController`, `RoomRestController`

- **Лаб7 (Spring Security)**
  - `SecurityConfig`: `DaoAuthenticationProvider`, `@EnableMethodSecurity`
  - Веб: form-login; `/admin/**` → **ADMIN**; `/profile/**` → **USER**
  - `DatabaseUserDetailsService`: вход только для `deleted = false`
  - Пароли: `LabPasswordEncoder` (plain text, совместимо с servlet-БД)

- **Лаб8 (Bootstrap + CRUD)**
  - Оформление: Bootstrap 5 в `templates/fragments/layout.html`
  - ADMIN: CRUD номеров `/admin/rooms` (физическое удаление из БД допустимо)
  - ADMIN: CRUD пользователей `/admin/users`; **удаление** = `users.deleted = 1` (строка скрывается, `DELETE` из MySQL запрещён)
  - USER: редактирование своего пароля `/profile/edit`
  - Кнопки «Редактировать» / «Удалить» **в строке таблицы**; при создании **id не вводится**
  - Сервисы: `RoomManagementService`, `UserManagementService`

- **Лаб9 (Docker)**
  - `spring-app/Dockerfile` — сборка JAR внутри образа (Maven + JRE 17)
  - `spring-app/docker-compose.yml` — сервисы **mysql** + **app**
  - Профиль `docker`: `application-docker.properties` (хост БД `mysql`)
  - MySQL готовится до старта Spring: `healthcheck` + `depends_on: condition: service_healthy`
  - Порты: приложение **8081**, MySQL с хоста **3307**
  - Инициализация БД: `spring-app/docker/mysql/init.sql`
  - Подробно: [`spring-app/DOCKER.md`](spring-app/DOCKER.md)

### Прошлый семестр (`semester-previous`)

- Паттерны/AspectJ/антипаттерны: `semester-previous/src/main/java/`
  - паттерны `punic/*`, `com/hoteldb/labs/pattern*`
  - AspectJ: `com/hoteldb/labs/aspectj`, `com/hoteldb/labs/pattern9/aspects`
  - антипаттерны: `com/hoteldb/labs/antipatterns`

## Настройка БД (для текущего семестра)

### MySQL (primary)

1) Создайте БД:

```sql
CREATE DATABASE hotel_db;
```

2) Примените скрипт `semester-current/src/main/resources/sql/init.sql` (любой удобный способ: MySQL Workbench / консоль).

3) Проверьте `semester-current/src/main/resources/database.properties`:

- `db.url`, `db.username`, `db.password`, `db.driver`

### MySQL для Spring (Лаб5–Лаб8, локальный запуск)

Та же БД `hotel_db`. Настройте `spring-app/src/main/resources/application.properties` (логин/пароль как у servlet-части).

При ошибке `Unknown column 'deleted'` колонка добавляется автоматически при старте (`UsersDeletedColumnMigration`) или вручную: `spring-app/src/main/resources/sql/alter-users-deleted.sql`.

### PostgreSQL (backup для Лаб1)

1) Создайте backup БД и примените `semester-current/src/main/resources/sql/init-backup.sql`.

2) Проверьте `semester-current/src/main/resources/database.properties`:

- `db.backup.url`, `db.backup.username`, `db.backup.password`, `db.backup.driver`

## Запуск лабораторных (CLI)

Все команды ниже запускайте **из корня репозитория**.

### Лаб1: JDBC (основная БД + failover)

```bash
mvn -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1Main" exec:java
```

### Лаб1: резервная копия primary → backup (другая СУБД)

```bash
mvn -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1BackupMain" exec:java
```

### Лаб2: JPA (вывод “универсального отношения”)

```bash
mvn -Dexec.mainClass="com.hoteldb.labs.jpa.Lab2Main" exec:java
```

## Запуск WEB (Лаб3/Лаб4)

Проект собирается как **`war`**. Дальше его нужно задеплоить в сервлет-контейнер (например, Tomcat).

**IntelliJ IDEA + Tomcat (кратко):**

1. `mvn clean package` в корне репозитория.
2. Run → Edit Configurations → **Tomcat Server** → Local.
3. Deployment: артефакт `hotel-db-labs:war exploded` или WAR из `target/hotel-db-labs-1.0-SNAPSHOT.war`.
4. Application context: `/hotel-db-labs` (или как в настройках Tomcat).
5. Перед запуском: MySQL + `init.sql`, в `database.properties` верные `db.username` / `db.password`.

1) Собрать WAR:

```bash
mvn clean package
```

2) Взять файл:

- `target/hotel-db-labs-1.0-SNAPSHOT.war`

3) Положить в `webapps/` Tomcat и открыть (если war назовёте `hotel-db-labs-1.0-SNAPSHOT.war`):

- `http://localhost:8080/hotel-db-labs/`

### Учётки по умолчанию (из init.sql/init-backup.sql)

- `admin / admin` (роль ADMIN)
- `user / user` (роль USER)

## Запуск Spring (Лаб5-9)

Модуль: **`spring-app/`** (отдельный `pom.xml`, не входит в корневую `mvn package` для WAR).

### Лаб5–Лаб8: локально (IDEA или Maven)

**IntelliJ IDEA (рекомендуется для демонстрации):**

1. File → Open → каталог `spring-app/` (или весь репозиторий, тогда укажите модуль `spring-labs`).
2. Run → Edit Configurations → **+** → **Spring Boot**.
3. Main class: `com.hoteldb.spring.SpringLabsApplication`.
4. Working directory: `spring-app`.
5. Убедитесь, что MySQL запущен и применён `init.sql`.
6. Run — в браузере: `http://localhost:8081/`.

**Maven (из каталога `spring-app/`):**

```bash
cd spring-app
mvn spring-boot:run
```

Учётки по умолчанию: **`admin` / `admin`**, **`user` / `user`** (из `init.sql`). Если `admin` отсутствует — создаётся при старте (`AdminUserBootstrap`).

### Лаб5 (веб после входа)

| Роль | URL | Что показать |
|------|-----|----------------|
| ADMIN | `http://localhost:8081/admin` | панель администратора |
| USER | `http://localhost:8081/welcome/user` | своя запись в `users` |

Регистрация: `http://localhost:8081/register`.

### Лаб6 (REST)

В PowerShell для Basic Auth удобнее **`curl.exe`**, не алиас `curl`:

```powershell
curl.exe -s -X POST http://localhost:8081/api/v1/register `
  -H "Content-Type: application/json" `
  -d '{\"username\":\"apiuser\",\"password\":\"secret\"}'

curl.exe -s -u admin:admin http://localhost:8081/api/v1/me
curl.exe -s -u admin:admin http://localhost:8081/api/v1/rooms
```

### Лаб7 (Spring Security — сценарий демонстрации)

1. Войти как **admin** → открываются `/admin/**`; попытка открыть `/profile/edit` под admin — отказ (403).
2. Войти как **user** → доступны `/welcome/user`, `/profile/edit`; `/admin` — 403.
3. REST: без Basic Auth `GET /api/v1/me` → 401; с `user:user` запрос `GET /api/v1/rooms` → 403.

### Лаб8 (Bootstrap + CRUD)

**ADMIN** (`admin/admin`):

- `http://localhost:8081/admin/rooms` — добавить номер (без id), редактировать/удалить кнопками **в строке**.
- `http://localhost:8081/admin/users` — добавить пользователя; «Удалить» ставит `deleted=1` (в MySQL строка остаётся).

**USER:**

- `http://localhost:8081/welcome/user` → «Редактировать» в своей строке → смена пароля.

### Лаб9 (Docker)

Нужен только **Docker Desktop** (Maven/MySQL на хосте не обязательны).

```bash
cd spring-app
docker compose up --build
```

- Приложение: `http://localhost:8081/`
- MySQL с хоста (опционально): `localhost:3307`, пароль root: `hotel_pass` (см. `docker-compose.yml`)

**Развёртывание на другом ПК:**

```bash
git clone <URL-репозитория>
cd cp-development-technologies-2025/spring-app
docker compose up --build -d
```

**Порты заняты?** Скопируйте `.env.example` → `.env`, измените `APP_HOST_PORT` / `MYSQL_HOST_PORT`.

**Сброс данных БД в контейнере:**

```bash
docker compose down -v
docker compose up --build
```

Полная инструкция: [`spring-app/DOCKER.md`](spring-app/DOCKER.md).

### Тесты `spring-app`

```bash
cd spring-app
mvn test
```

Интеграционные тесты API: `ApiSecurityIT` (профиль `test`, H2 in-memory).

## Тесты и покрытие

Запуск тестов для текущего семестра:

```bash
mvn test
```

Отчёт JaCoCo:

```bash
mvn jacoco:report
```

HTML-отчёт:

- `semester-current/target/site/jacoco/index.html`

Примечание: так как сборка общая, отчёт JaCoCo лежит в:

- `target/site/jacoco/index.html`

---

# Проект: Разработка технологий программирования

> **Этот раздел — прошлый семестр** (`semester-previous/`): паттерны GoF, AspectJ, антипаттерны.  
> Нумерация лабораторных **не совпадает** с текущим семестром (см. [сводную таблицу](#сводная-таблица-текущий-семестр) в начале README).  
> Текущий семестр: Лаб1–4 в `semester-current/`, Лаб5–9 в `spring-app/`.

## Описание проекта

Данный проект представляет собой комплексную систему для изучения различных технологий программирования на Java. Проект включает в себя работу с базами данных (JDBC, JPA), веб (JSP/Servlet, Spring Boot), паттерны проектирования, аспектно-ориентированное программирование, Docker и модульное тестирование. Примеры — система управления гостиницей и игра «Пунические войны».

---

## Содержание (прошлый семестр — паттерны и AspectJ)

1. [Лабораторная работа №1: JDBC](#лабораторная-работа-1-связь-с-бд-при-помощи-jdbc) — также Лаб1 текущего семестра
2. [Лабораторная работа №2: JPA](#лабораторная-работа-2-связь-с-бд-при-помощи-jpa) — также Лаб2 текущего семестра
3. [Лабораторная работа №4: Порождающие паттерны](#лабораторная-работа-4-порождающие-паттерны-проектирования)
4. [Лабораторная работа №5: Структурные паттерны](#лабораторная-работа-5-структурные-паттерны-проектирования)
5. [Лабораторная работа №6: Паттерны поведения](#лабораторная-работа-6-паттерны-поведения)
6. [Лабораторная работа №7: Hello, AspectJ](#лабораторная-работа-7-hello-aspectj)
7. [Лабораторная работа №8: Аспектная обработка БД](#лабораторная-работа-8-аспектная-обработка-бд)
8. [Лабораторная работа №9: Аспектные паттерны](#лабораторная-работа-9-аспектно-ориентированные-версии-паттернов)
9. [Лабораторная работа №10: Юнит-тестирование](#лабораторная-работа-10-юнит-тестирование)

---

## Требования

- **JDK 17+** (сборка; рекомендуется JDK 21)
- **Maven 3.6+**
- **MySQL 8.0+** (Лаб1–4 локально, Лаб5–8; в Лаб9 — MySQL в Docker)
- **Tomcat 10+** (Лаб3–4, servlet WAR)
- **Docker Desktop / Docker Compose v2** (Лаб9)
- **H2** — автоматически для unit-тестов Spring (`spring-app`, профиль `test`)

---

## Структура по семестрам

Проект теперь **разделён на 2 директории по семестрам**:

- `semester-current/` — лабораторные текущего семестра (JDBC/JPA/WEB + тесты)
- `semester-previous/` — лабораторные прошлого семестра (паттерны/AspectJ/антипаттерны)

Собирать всё сразу:

```bash
mvn clean test
```

Сборка выполняется одним корневым `pom.xml` (один `war`), поэтому отдельно по директориям не собираем.

---

## Настройка проекта

### 1. Настройка базы данных MySQL

Создайте базу данных:
```sql
CREATE DATABASE hotel_db;
```

Настройте подключение в `semester-current/src/main/resources/database.properties`:
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
cp-development-technologies-2025/
├── pom.xml                        # WAR: semester-current + semester-previous
├── semester-current/              # Текущий семестр: Лаб1–4
│   └── src/main/java/com/hoteldb/labs/
│       ├── jdbc/                  # Лаб1
│       ├── jpa/                   # Лаб2 (+ сущности для Лаб4)
│       └── web/                   # Лаб3–4 (Servlet, JSP)
│   └── src/main/webapp/           # login.jsp, welcome.jsp, Bootstrap
├── semester-previous/             # Прошлый семестр: паттерны, AspectJ
│   └── src/main/java/             # pattern*, aspectj, antipatterns, punic
└── spring-app/                    # Текущий семестр: Лаб5–9
    ├── pom.xml
    ├── Dockerfile                 # Лаб9
    ├── docker-compose.yml
    ├── DOCKER.md
    └── src/main/java/com/hoteldb/spring/
        ├── config/                # SecurityConfig (Лаб7)
        ├── api/                   # REST (Лаб6)
        ├── web/admin/             # CRUD (Лаб8)
        ├── bootstrap/             # миграция deleted, admin
        └── resources/templates/   # Thymeleaf + Bootstrap
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

### Spring (текущий семестр, `spring-app/`)
- **Spring Boot 3.2.5**, **Spring Security 6**, **Spring Data JPA**
- **Thymeleaf** + **Bootstrap 5** (Лаб8)
- **MySQL Connector/J 8.3** (runtime), **H2** (тесты)

### DevOps
- **Docker**, **Docker Compose** (Лаб9)

---

## Команды для запуска

### Текущий семестр (кратко)

| Лаб | Команда |
|-----|---------|
| 1 JDBC | `mvn -Dexec.mainClass="com.hoteldb.labs.jdbc.Lab1Main" exec:java` |
| 2 JPA | `mvn -Dexec.mainClass="com.hoteldb.labs.jpa.Lab2Main" exec:java` |
| 3–4 WEB | `mvn clean package` → WAR в Tomcat |
| 5–8 Spring | `cd spring-app && mvn spring-boot:run` или Run в IDEA |
| 9 Docker | `cd spring-app && docker compose up --build` |

Подробные сценарии — в [начале README](#запуск-spring-лаб5-9).

### Прошлый семестр (паттерны, AspectJ)

**Лабораторная работа №4 (Builder):**
```bash
mvn -Paspectj exec:java -Dexec.mainClass="com.hoteldb.labs.pattern4.creational.Lab4Main"
```

**Лабораторная работа №5 (Bridge):**
```bash
mvn -Paspectj exec:java -Dexec.mainClass="com.hoteldb.labs.pattern5.structural.Lab5Main"
```

**Лабораторная работа №6 (Iterator):**
```bash
mvn -Paspectj exec:java -Dexec.mainClass="com.hoteldb.labs.pattern6.behavioral.Lab6Main"
```

**Лабораторная работа №7 (Hello, AspectJ):**
```bash
mvn -Paspectj clean compile
mvn -Paspectj exec:java -Dexec.mainClass="com.hoteldb.labs.aspectj.MainClass"
```

**Лабораторная работа №9 (Аспектные паттерны):**
```bash
mvn -Paspectj clean compile
mvn -Paspectj exec:java -Dexec.mainClass="com.hoteldb.labs.pattern9.Lab9Main"
```

### Запуск тестов
```bash
mvn test                    # servlet + semester-previous
cd spring-app && mvn test   # Spring (ApiSecurityIT)
```

### Просмотр отчета о покрытии
```bash
mvn jacoco:report
```

---

## Заключение

Репозиторий объединяет два семестра:

**Текущий:** JDBC/JPA, JSP/Servlet с ролями, Spring Boot (Security, CRUD, REST), контейнеризация Docker.

**Прошлый:** паттерны проектирования, AspectJ, антипаттерны, юнит-тесты с JaCoCo.

Все лабораторные готовы к демонстрации; для Spring и Docker см. разделы [Запуск Spring (Лаб5-9)](#запуск-spring-лаб5-9) и [`spring-app/DOCKER.md`](spring-app/DOCKER.md).
