-- Опционально: колонка users.deleted добавляется автоматически при старте Spring (UsersDeletedColumnMigration).
-- Выполните вручную только если миграция при старте недоступна:
USE hotel_db;

ALTER TABLE users
    ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;
