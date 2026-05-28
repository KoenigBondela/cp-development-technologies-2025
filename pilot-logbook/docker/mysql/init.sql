-- Инициализация БД при первом запуске Docker (UTF-8)
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE pilot_logbook;

CREATE TABLE IF NOT EXISTS pilot_users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(80)  NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(16)  NOT NULL,
    full_name       VARCHAR(150) NOT NULL,
    license_number  VARCHAR(40)  NULL,
    phone           VARCHAR(30)  NULL,
    email           VARCHAR(120) NULL,
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_role CHECK (role IN ('CHIEF', 'PILOT'))
);

CREATE TABLE IF NOT EXISTS aircraft (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_name            VARCHAR(80)  NOT NULL,
    registration_number  VARCHAR(20)  NOT NULL UNIQUE,
    active               BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS flight_routes (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    label   VARCHAR(150) NOT NULL,
    active  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS flight_entries (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    pilot_id            BIGINT         NOT NULL,
    flight_date         DATE           NOT NULL,
    aircraft_id         BIGINT         NULL,
    aircraft_type       VARCHAR(80)    NOT NULL,
    registration_number VARCHAR(20)    NOT NULL,
    route               VARCHAR(150)   NOT NULL,
    takeoff_time        TIME           NOT NULL,
    landing_time        TIME           NOT NULL,
    flight_hours        DECIMAL(6, 2)  NOT NULL,
    day_night           VARCHAR(10)    NOT NULL,
    notes               VARCHAR(2000)  NULL,
    CONSTRAINT fk_fe_pilot FOREIGN KEY (pilot_id) REFERENCES pilot_users(id),
    CONSTRAINT fk_fe_aircraft FOREIGN KEY (aircraft_id) REFERENCES aircraft(id),
    CONSTRAINT chk_day_night CHECK (day_night IN ('DAY', 'NIGHT'))
);

INSERT INTO pilot_users (username, password, role, full_name, license_number, active) VALUES
    ('chief', 'chief', 'CHIEF', 'Иванов Иван Иванович', NULL, TRUE),
    ('petrov', 'petrov', 'PILOT', 'Петров Пётр Петрович', 'РФ-123456', TRUE),
    ('sidorova', 'sidorova', 'PILOT', 'Сидорова Анна Сергеевна', 'РФ-654321', TRUE);

INSERT INTO aircraft (type_name, registration_number) VALUES
    ('Cessna 172', 'RA-67890'),
    ('Cessna 172', 'RA-67891'),
    ('Diamond DA40', 'RA-11223'),
    ('Як-52', 'RA-02852');

INSERT INTO flight_routes (label) VALUES
    ('Москва (Внуково) — Санкт-Петербург (Пулково)'),
    ('Москва (Внуково) — Казань'),
    ('Москва (Внуково) — локальный полёт'),
    ('Санкт-Петербург (Пулково) — Москва (Внуково)'),
    ('Казань — Москва (Внуково)');

INSERT INTO flight_entries (
    pilot_id, flight_date, aircraft_id, aircraft_type, registration_number, route,
    takeoff_time, landing_time, flight_hours, day_night, notes
) VALUES
    (2, '2025-03-10', 1, 'Cessna 172', 'RA-67890', 'Москва (Внуково) — Санкт-Петербург (Пулково)',
     '09:15:00', '10:45:00', 1.50, 'DAY', 'Тренировочный полёт'),
    (2, '2025-04-02', 1, 'Cessna 172', 'RA-67890', 'Санкт-Петербург (Пулково) — Москва (Внуково)',
     '14:00:00', '15:20:00', 1.33, 'DAY', NULL),
    (2, '2025-04-18', 3, 'Diamond DA40', 'RA-11223', 'Москва (Внуково) — локальный полёт',
     '18:30:00', '19:15:00', 0.75, 'NIGHT', 'Ночной налёт'),
    (3, '2025-05-05', 3, 'Diamond DA40', 'RA-11223', 'Москва (Внуково) — Казань',
     '11:00:00', '12:30:00', 1.50, 'DAY', 'Коммерческий рейс');
