package com.hoteldb.labs.jdbc;

import com.hoteldb.labs.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с номерами в базе данных
 */
public class RoomDAO {
    private final DatabaseConnection dbConnection;

    public RoomDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Добавить новый номер в базу данных
     */
    public Room create(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, is_available) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setBigDecimal(3, room.getPricePerNight());
            stmt.setBoolean(4, room.getIsAvailable());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Не удалось добавить номер");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Не удалось получить ID номера");
                }
            }
        }
        return room;
    }

    /**
     * Найти номер по ID
     */
    public Room findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }
        }
        return null;
    }

    /**
     * Получить все номера
     */
    public List<Room> findAll() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        }
        return rooms;
    }

    /**
     * Обновить информацию о номере
     */
    public Room update(Room room) throws SQLException {
        String sql = "UPDATE rooms SET room_number = ?, room_type = ?, price_per_night = ?, is_available = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType());
            stmt.setBigDecimal(3, room.getPricePerNight());
            stmt.setBoolean(4, room.getIsAvailable());
            stmt.setInt(5, room.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Не удалось обновить номер");
            }
        }
        return room;
    }

    /**
     * Удалить номер по ID
     */
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Преобразовать ResultSet в объект Room
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
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
        return room;
    }
}

