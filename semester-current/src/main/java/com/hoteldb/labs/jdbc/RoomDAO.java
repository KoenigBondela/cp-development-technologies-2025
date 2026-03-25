package com.hoteldb.labs.jdbc;

import com.hoteldb.labs.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с номерами в базе данных
 */
public class RoomDAO {
    private static final Logger logger = LoggerFactory.getLogger(RoomDAO.class);
    private final DatabaseConnection dbConnection;

    public RoomDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        logger.debug("Создан экземпляр RoomDAO");
    }

    /**
     * Добавить новый номер в базу данных
     */
    public Room create(Room room) throws SQLException {
        if (room == null) {
            logger.error("Попытка создать номер с null значением");
            throw new IllegalArgumentException("Номер не может быть null");
        }
        
        logger.info("Создание нового номера: {}", room.getRoomNumber());
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, is_available) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            logger.debug("Выполнение SQL: {}", sql);
            logger.debug("Параметры: roomNumber={}, roomType={}, pricePerNight={}, isAvailable={}", 
                    room.getRoomNumber(), room.getRoomType(), room.getPricePerNight(), room.getIsAvailable());
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setBigDecimal(3, room.getPricePerNight());
            stmt.setBoolean(4, room.getIsAvailable());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                logger.error("Не удалось добавить номер: ни одна строка не была затронута");
                throw new SQLException("Не удалось добавить номер");
            }
            logger.debug("Затронуто строк: {}", affectedRows);

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    room.setId(generatedId);
                    logger.info("Номер успешно создан с ID: {} (номер: {})", generatedId, room.getRoomNumber());
                } else {
                    logger.error("Не удалось получить сгенерированный ID номера");
                    throw new SQLException("Не удалось получить ID номера");
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при создании номера {}: {}", room.getRoomNumber(), e.getMessage(), e);
            throw e;
        }
        return room;
    }

    /**
     * Найти номер по ID
     */
    public Room findById(Integer id) throws SQLException {
        if (id == null) {
            logger.error("Попытка найти номер с null ID");
            throw new IllegalArgumentException("ID номера не может быть null");
        }
        
        logger.debug("Поиск номера по ID: {}", id);
        String sql = "SELECT * FROM rooms WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            logger.debug("Выполнение SQL: {} с параметром id={}", sql, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Room room = mapResultSetToRoom(rs);
                    logger.info("Номер найден: ID={}, номер={}", id, room.getRoomNumber());
                    return room;
                } else {
                    logger.warn("Номер с ID={} не найден", id);
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при поиске номера по ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Получить все номера
     */
    public List<Room> findAll() throws SQLException {
        logger.debug("Получение списка всех номеров");
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            logger.debug("Выполнение SQL: {}", sql);
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            logger.info("Найдено номеров: {}", rooms.size());
        } catch (SQLException e) {
            logger.error("Ошибка при получении списка номеров: {}", e.getMessage(), e);
            throw e;
        }
        return rooms;
    }

    /**
     * Обновить информацию о номере
     */
    public Room update(Room room) throws SQLException {
        if (room == null) {
            logger.error("Попытка обновить номер с null значением");
            throw new IllegalArgumentException("Номер не может быть null");
        }
        if (room.getId() == null) {
            logger.error("Попытка обновить номер без ID");
            throw new IllegalArgumentException("ID номера не может быть null для обновления");
        }
        
        logger.info("Обновление номера с ID: {} (номер: {})", room.getId(), room.getRoomNumber());
        String sql = "UPDATE rooms SET room_number = ?, room_type = ?, price_per_night = ?, is_available = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            logger.debug("Выполнение SQL: {}", sql);
            logger.debug("Параметры: id={}, roomNumber={}, roomType={}, pricePerNight={}, isAvailable={}", 
                    room.getId(), room.getRoomNumber(), room.getRoomType(), 
                    room.getPricePerNight(), room.getIsAvailable());
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setBigDecimal(3, room.getPricePerNight());
            stmt.setBoolean(4, room.getIsAvailable());
            stmt.setInt(5, room.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                logger.error("Не удалось обновить номер с ID {}: номер не найден", room.getId());
                throw new SQLException("Не удалось обновить номер: номер с ID " + room.getId() + " не найден");
            }
            logger.info("Номер с ID {} успешно обновлен. Затронуто строк: {}", room.getId(), affectedRows);
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении номера с ID {}: {}", room.getId(), e.getMessage(), e);
            throw e;
        }
        return room;
    }

    /**
     * Удалить номер по ID
     */
    public boolean delete(Integer id) throws SQLException {
        if (id == null) {
            logger.error("Попытка удалить номер с null ID");
            throw new IllegalArgumentException("ID номера не может быть null");
        }
        
        logger.info("Удаление номера с ID: {}", id);
        String sql = "DELETE FROM rooms WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            logger.debug("Выполнение SQL: {} с параметром id={}", sql, id);
            
            int affectedRows = stmt.executeUpdate();
            boolean deleted = affectedRows > 0;
            
            if (deleted) {
                logger.info("Номер с ID {} успешно удален. Затронуто строк: {}", id, affectedRows);
            } else {
                logger.warn("Номер с ID {} не найден для удаления", id);
            }
            
            return deleted;
        } catch (SQLException e) {
            logger.error("Ошибка при удалении номера с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Преобразовать ResultSet в объект Room
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        try {
            Room room = new Room();
            room.setId(rs.getInt("id"));
            room.setRoomNumber(rs.getString("room_number"));
            room.setRoomType(rs.getString("room_type"));
            room.setPricePerNight(rs.getBigDecimal("price_per_night"));
            room.setIsAvailable(rs.getBoolean("is_available"));
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                room.setCreatedAt(createdAt.toLocalDateTime());
            }
            logger.trace("Преобразование ResultSet в Room: ID={}, номер={}", room.getId(), room.getRoomNumber());
            return room;
        } catch (SQLException e) {
            logger.error("Ошибка при преобразовании ResultSet в Room: {}", e.getMessage(), e);
            throw e;
        }
    }
}

