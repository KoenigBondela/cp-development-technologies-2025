package com.hoteldb.labs.jdbc;

import com.hoteldb.labs.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с клиентами в базе данных
 */
public class ClientDAO {
    private final DatabaseConnection dbConnection;

    public ClientDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Добавить нового клиента в базу данных
     */
    public Client create(Client client) throws SQLException {
        String sql = "INSERT INTO clients (first_name, last_name, email, phone, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhone());
            if (client.getRoomId() != null) {
                stmt.setInt(5, client.getRoomId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            if (client.getCheckInDate() != null) {
                stmt.setDate(6, Date.valueOf(client.getCheckInDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            if (client.getCheckOutDate() != null) {
                stmt.setDate(7, Date.valueOf(client.getCheckOutDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Не удалось добавить клиента");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Не удалось получить ID клиента");
                }
            }
        }
        return client;
    }

    /**
     * Найти клиента по ID
     */
    public Client findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToClient(rs);
                }
            }
        }
        return null;
    }

    /**
     * Получить всех клиентов
     */
    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }

    /**
     * Обновить информацию о клиенте
     */
    public Client update(Client client) throws SQLException {
        String sql = "UPDATE clients SET first_name = ?, last_name = ?, email = ?, phone = ?, room_id = ?, check_in_date = ?, check_out_date = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhone());
            if (client.getRoomId() != null) {
                stmt.setInt(5, client.getRoomId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            if (client.getCheckInDate() != null) {
                stmt.setDate(6, Date.valueOf(client.getCheckInDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            if (client.getCheckOutDate() != null) {
                stmt.setDate(7, Date.valueOf(client.getCheckOutDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            stmt.setInt(8, client.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Не удалось обновить клиента");
            }
        }
        return client;
    }

    /**
     * Удалить клиента по ID
     */
    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Преобразовать ResultSet в объект Client
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setFirstName(rs.getString("first_name"));
        client.setLastName(rs.getString("last_name"));
        client.setEmail(rs.getString("email"));
        client.setPhone(rs.getString("phone"));
        int roomId = rs.getInt("room_id");
        if (!rs.wasNull()) {
            client.setRoomId(roomId);
        }
        Date checkInDate = rs.getDate("check_in_date");
        if (checkInDate != null) {
            client.setCheckInDate(checkInDate.toLocalDate());
        }
        Date checkOutDate = rs.getDate("check_out_date");
        if (checkOutDate != null) {
            client.setCheckOutDate(checkOutDate.toLocalDate());
        }
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            client.setCreatedAt(createdAt.toLocalDateTime());
        }
        return client;
    }
}

