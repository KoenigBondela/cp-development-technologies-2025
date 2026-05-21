# Лаб9 — Docker (Spring Labs, Лаб5–8)

Контейнеры: **MySQL 8** + **Spring Boot** (Thymeleaf, Security, CRUD).

## Требования

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (Windows/macOS) или Docker Engine + Compose v2 (Linux)
- Свободные порты на хосте (по умолчанию): **8081** (приложение), **3307** (MySQL снаружи)

Проверка портов (Windows PowerShell):

```powershell
netstat -ano | findstr ":8081"
netstat -ano | findstr ":3307"
```

Если заняты — скопируйте `.env.example` в `.env` и измените, например: `APP_HOST_PORT=9081`, `MYSQL_HOST_PORT=3308`.

## Запуск на этом ПК

```bash
cd spring-app
docker compose up --build
```

Первый запуск: сборка JAR внутри Docker (~2–5 мин), инициализация MySQL из `docker/mysql/init.sql`.

Приложение: **http://localhost:8081/** (логин `admin` / `admin`).

Остановка:

```bash
docker compose down
```

Полный сброс БД (удалить volume):

```bash
docker compose down -v
docker compose up --build
```

## Запуск на другом ПК (через GitHub)

1. Установить Docker.
2. Клонировать репозиторий:

```bash
git clone <URL-вашего-репозитория>
cd cp-development-technologies-2025/spring-app
```

3. Запустить:

```bash
docker compose up --build -d
```

4. Открыть `http://localhost:8081/`.

Локальная установка Maven/MySQL на втором ПК **не нужна**.

## Почему Spring не падает при старте MySQL

В `docker-compose.yml` у сервиса `app` указано:

```yaml
depends_on:
  mysql:
    condition: service_healthy
```

MySQL считается готовым только после успешного `mysqladmin ping`. Дополнительно в профиле `docker` увеличены таймауты HikariCP.

## Полезные команды

```bash
docker compose ps
docker compose logs -f app
docker compose logs -f mysql
```

## Ссылки (референсы)

- https://www.bezkoder.com/docker-compose-spring-boot-mysql/
- https://www.javaguides.net/2022/12/deploy-spring-boot-mysql-application-to-docker.html
