-- Create database
CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;

-- Create rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL UNIQUE,
    room_type VARCHAR(50) NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create clients table
CREATE TABLE IF NOT EXISTS clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    room_id INT,
    check_in_date DATE,
    check_out_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL
);

-- Insert sample data
INSERT INTO rooms (room_number, room_type, price_per_night, is_available) VALUES
('101', 'Standard', 50.00, TRUE),
('102', 'Standard', 50.00, FALSE),
('103', 'Standard', 55.00, TRUE),
('104', 'Standard', 50.00, TRUE),
('201', 'Deluxe', 100.00, FALSE),
('202', 'Deluxe', 100.00, TRUE),
('203', 'Deluxe', 110.00, FALSE),
('301', 'Suite', 200.00, FALSE),
('302', 'Suite', 220.00, TRUE),
('303', 'Suite', 200.00, TRUE),
('401', 'Presidential', 500.00, TRUE),
('402', 'Presidential', 500.00, FALSE);

INSERT INTO clients (first_name, last_name, email, phone, room_id, check_in_date, check_out_date) VALUES
('Иван', 'Иванов', 'ivan.ivanov@example.com', '+7-900-123-4567', 2, '2025-12-01', '2025-12-05'),
('Мария', 'Петрова', 'maria.petrova@example.com', '+7-900-234-5678', 5, '2025-12-03', '2025-12-06'),
('Петр', 'Сидоров', 'petr.sidorov@example.com', '+7-900-345-6789', 7, '2025-12-10', '2025-12-13'),
('Анна', 'Козлова', 'anna.kozlova@example.com', '+7-900-456-7890', 8, '2025-11-15', '2025-11-20'),
('Дмитрий', 'Смирнов', 'dmitry.smirnov@example.com', '+7-900-567-8901', 3, '2025-12-02', '2025-12-04'),
('Елена', 'Волкова', 'elena.volkova@example.com', '+7-900-678-9012', NULL, NULL, NULL),
('Сергей', 'Новиков', 'sergey.novikov@example.com', '+7-900-789-0123', 12, '2025-12-05', '2025-12-08'),
('Ольга', 'Морозова', 'olga.morozova@example.com', '+7-900-890-1234', NULL, NULL, NULL),
('Алексей', 'Павлов', 'alexey.pavlov@example.com', '+7-900-901-2345', 1, '2025-12-15', '2025-12-18'),
('Татьяна', 'Лебедева', 'tatyana.lebedeva@example.com', '+7-900-012-3456', 4, '2025-12-20', '2025-12-25');

