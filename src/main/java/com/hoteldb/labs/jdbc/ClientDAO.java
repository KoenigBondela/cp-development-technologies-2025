package com.hoteldb.labs.jdbc;

import com.hoteldb.labs.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с клиентами в базе данных
 */
public class ClientDAO {
    private static final Logger logger = LoggerFactory.getLogger(ClientDAO.class);
    private final DatabaseConnection dbConnection;

    public ClientDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        logger.debug("Создан экземпляр ClientDAO");
    }

    /**
     * Добавить нового клиента в базу данных
     */
    public Client create(Client client) throws SQLException {
        if (client == null) {
            logger.error("Попытка создать клиента с null значением");
            throw new IllegalArgumentException("Клиент не может быть null");
        }
        
        logger.info("Создание нового клиента: {} {}", client.getFirstName(), client.getLastName());
        String sql = "INSERT INTO clients (first_name, last_name, email, phone, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            logger.debug("Выполнение SQL: {}", sql);
            logger.debug("Параметры: firstName={}, lastName={}, email={}, phone={}, roomId={}", 
                    client.getFirstName(), client.getLastName(), client.getEmail(), 
                    client.getPhone(), client.getRoomId());
            
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
                logger.error("Не удалось добавить клиента: ни одна строка не была затронута");
                throw new SQLException("Не удалось добавить клиента");
            }
            logger.debug("Затронуто строк: {}", affectedRows);

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    client.setId(generatedId);
                    logger.info("Клиент успешно создан с ID: {}", generatedId);
                } else {
                    logger.error("Не удалось получить сгенерированный ID клиента");
                    throw new SQLException("Не удалось получить ID клиента");
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при создании клиента {} {}: {}", 
                    client.getFirstName(), client.getLastName(), e.getMessage(), e);
            throw e;
        }
        return client;
    }

    /**
     * Найти клиента по ID
     */
    public Client findById(Integer id) throws SQLException {
        if (id == null) {
            logger.error("Попытка найти клиента с null ID");
            throw new IllegalArgumentException("ID клиента не может быть null");
        }
        
        logger.debug("Поиск клиента по ID: {}", id);
        String sql = "SELECT * FROM clients WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            logger.debug("Выполнение SQL: {} с параметром id={}", sql, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = mapResultSetToClient(rs);
                    logger.info("Клиент найден: ID={}, {} {}", id, client.getFirstName(), client.getLastName());
                    return client;
                } else {
                    logger.warn("Клиент с ID={} не найден", id);
                    return null;
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при поиске клиента по ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Получить всех клиентов
     */
    public List<Client> findAll() throws SQLException {
        logger.debug("Получение списка всех клиентов");
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            logger.debug("Выполнение SQL: {}", sql);
            
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
            logger.info("Найдено клиентов: {}", clients.size());
        } catch (SQLException e) {
            logger.error("Ошибка при получении списка клиентов: {}", e.getMessage(), e);
            throw e;
        }
        return clients;
    }

    /**
     * Обновить информацию о клиенте
     */
    public Client update(Client client) throws SQLException {
        if (client == null) {
            logger.error("Попытка обновить клиента с null значением");
            throw new IllegalArgumentException("Клиент не может быть null");
        }
        if (client.getId() == null) {
            logger.error("Попытка обновить клиента без ID");
            throw new IllegalArgumentException("ID клиента не может быть null для обновления");
        }
        
        logger.info("Обновление клиента с ID: {}", client.getId());
        String sql = "UPDATE clients SET first_name = ?, last_name = ?, email = ?, phone = ?, room_id = ?, check_in_date = ?, check_out_date = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            logger.debug("Выполнение SQL: {}", sql);
            logger.debug("Параметры: id={}, firstName={}, lastName={}, email={}", 
                    client.getId(), client.getFirstName(), client.getLastName(), client.getEmail());
            
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
                logger.error("Не удалось обновить клиента с ID {}: клиент не найден", client.getId());
                throw new SQLException("Не удалось обновить клиента: клиент с ID " + client.getId() + " не найден");
            }
            logger.info("Клиент с ID {} успешно обновлен. Затронуто строк: {}", client.getId(), affectedRows);
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении клиента с ID {}: {}", client.getId(), e.getMessage(), e);
            throw e;
        }
        return client;
    }

    /**
     * Удалить клиента по ID
     */
    public boolean delete(Integer id) throws SQLException {
        if (id == null) {
            logger.error("Попытка удалить клиента с null ID");
            throw new IllegalArgumentException("ID клиента не может быть null");
        }
        
        logger.info("Удаление клиента с ID: {}", id);
        String sql = "DELETE FROM clients WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            logger.debug("Выполнение SQL: {} с параметром id={}", sql, id);
            
            int affectedRows = stmt.executeUpdate();
            boolean deleted = affectedRows > 0;
            
            if (deleted) {
                logger.info("Клиент с ID {} успешно удален. Затронуто строк: {}", id, affectedRows);
            } else {
                logger.warn("Клиент с ID {} не найден для удаления", id);
            }
            
            return deleted;
        } catch (SQLException e) {
            logger.error("Ошибка при удалении клиента с ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Преобразовать ResultSet в объект Client
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        try {
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
            logger.trace("Преобразование ResultSet в Client: ID={}, {} {}", 
                    client.getId(), client.getFirstName(), client.getLastName());
            return client;
        } catch (SQLException e) {
            logger.error("Ошибка при преобразовании ResultSet в Client: {}", e.getMessage(), e);
            throw e;
        }
    }
}

