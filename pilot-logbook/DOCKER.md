# Pilot Logbook — Docker

Контейнеры: **MySQL 8** + **Spring Boot** (Thymeleaf, JPA, CRUD, статистика налёта).

## Требования

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) или Docker Engine + Compose v2
- Свободные порты на хосте (по умолчанию): **8082** (приложение), **3308** (MySQL снаружи)

Проверка портов (Windows PowerShell):

```powershell
netstat -ano | findstr ":8082"
netstat -ano | findstr ":3308"
```

Если заняты — скопируйте `.env.example` в `.env` и измените, например: `APP_HOST_PORT=9082`, `MYSQL_HOST_PORT=3309`.

## Запуск

```bash
cd pilot-logbook
docker compose up --build
```

Первый запуск: сборка JAR внутри Docker (~2–5 мин), инициализация MySQL из `docker/mysql/init.sql`.

Если вместо русских букв видны символы вроде `ÐŸÐµÑ‚Ñ€Ð¾Ð²` — пересоздайте БД (старые данные были записаны в неверной кодировке):

```bash
docker compose down -v
docker compose up --build
```

Приложение: **http://localhost:8082/flights**

Остановка:

```bash
docker compose down
```

Полный сброс БД (удалить volume):

```bash
docker compose down -v
docker compose up --build
```

## Запуск в фоне

```bash
docker compose up --build -d
```

## Запуск на другом ПК

```bash
git clone <URL-репозитория>
cd cp-development-technologies-2025/pilot-logbook
docker compose up --build -d
```

Maven и MySQL на хосте **не нужны**.

## Почему Spring не падает при старте MySQL

В `docker-compose.yml` у сервиса `app`:

```yaml
depends_on:
  mysql:
    condition: service_healthy
```

MySQL считается готовым после `mysqladmin ping`. В профиле `docker` увеличены таймауты HikariCP.

## Полезные команды

```bash
docker compose ps
docker compose logs -f app
docker compose logs -f mysql
```

Пароль root MySQL по умолчанию: `pilot_pass` (см. `docker-compose.yml` и `.env.example`).
