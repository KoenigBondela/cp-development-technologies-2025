package com.hoteldb.labs.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Резервная копия БД: копирование данных из основной БД в резервную (другая СУБД).
 * Реализация сделана на JDBC, чтобы работать одинаково для MySQL/PostgreSQL и т.д.
 */
public class DatabaseBackupService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseBackupService.class);

    private final DatabaseConnection databaseConnection;

    public record BackupReport(int roomsCopied, int clientsCopied) { }

    public DatabaseBackupService(DatabaseConnection databaseConnection) {
        if (databaseConnection == null) {
            throw new IllegalArgumentException("databaseConnection cannot be null");
        }
        this.databaseConnection = databaseConnection;
    }

    public BackupReport backupPrimaryToBackup() throws SQLException {
        if (!databaseConnection.isBackupConfigured()) {
            throw new IllegalStateException("Backup DB is not configured");
        }

        try (Connection primary = databaseConnection.openPrimaryConnection();
             Connection backup = databaseConnection.openBackupConnection()) {
            backup.setAutoCommit(false);
            try {
                ensureSchemaExists(backup);
                clearBackupTables(backup);

                int roomsCopied = copyRooms(primary, backup);
                int clientsCopied = copyClients(primary, backup);

                backup.commit();
                logger.info("Backup completed успешно. rooms={}, clients={}", roomsCopied, clientsCopied);
                return new BackupReport(roomsCopied, clientsCopied);
            } catch (SQLException e) {
                try {
                    backup.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            } finally {
                try {
                    backup.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // ignore
                }
            }
        }
    }

    private void ensureSchemaExists(Connection backup) throws SQLException {
        try (Statement stmt = backup.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS rooms (" +
                            "id INT PRIMARY KEY, " +
                            "room_number VARCHAR(10) NOT NULL, " +
                            "room_type VARCHAR(50) NOT NULL, " +
                            "price_per_night DECIMAL(10, 2) NOT NULL, " +
                            "is_available BOOLEAN, " +
                            "created_at TIMESTAMP" +
                            ")"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS clients (" +
                            "id INT PRIMARY KEY, " +
                            "first_name VARCHAR(100) NOT NULL, " +
                            "last_name VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(255), " +
                            "phone VARCHAR(20), " +
                            "room_id INT, " +
                            "check_in_date DATE, " +
                            "check_out_date DATE, " +
                            "created_at TIMESTAMP" +
                            ")"
            );
        }
    }

    private void clearBackupTables(Connection backup) throws SQLException {
        try (Statement stmt = backup.createStatement()) {
            stmt.executeUpdate("DELETE FROM clients");
            stmt.executeUpdate("DELETE FROM rooms");
        }
    }

    private int copyRooms(Connection primary, Connection backup) throws SQLException {
        String selectSql = "SELECT id, room_number, room_type, price_per_night, is_available, created_at FROM rooms";
        String insertSql = "INSERT INTO rooms (id, room_number, room_type, price_per_night, is_available, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        int copied = 0;
        try (Statement select = primary.createStatement();
             ResultSet rs = select.executeQuery(selectSql);
             PreparedStatement insert = backup.prepareStatement(insertSql)) {
            while (rs.next()) {
                insert.setInt(1, rs.getInt("id"));
                insert.setString(2, rs.getString("room_number"));
                insert.setString(3, rs.getString("room_type"));
                insert.setBigDecimal(4, rs.getBigDecimal("price_per_night"));
                insert.setBoolean(5, rs.getBoolean("is_available"));
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    insert.setTimestamp(6, createdAt);
                } else {
                    insert.setNull(6, Types.TIMESTAMP);
                }

                copied += insert.executeUpdate();
            }
        }
        return copied;
    }

    private int copyClients(Connection primary, Connection backup) throws SQLException {
        String selectSql = "SELECT id, first_name, last_name, email, phone, room_id, check_in_date, check_out_date, created_at FROM clients";
        String insertSql = "INSERT INTO clients (id, first_name, last_name, email, phone, room_id, check_in_date, check_out_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int copied = 0;
        try (Statement select = primary.createStatement();
             ResultSet rs = select.executeQuery(selectSql);
             PreparedStatement insert = backup.prepareStatement(insertSql)) {
            while (rs.next()) {
                insert.setInt(1, rs.getInt("id"));
                insert.setString(2, rs.getString("first_name"));
                insert.setString(3, rs.getString("last_name"));
                insert.setString(4, rs.getString("email"));
                insert.setString(5, rs.getString("phone"));

                int roomId = rs.getInt("room_id");
                if (!rs.wasNull()) {
                    insert.setInt(6, roomId);
                } else {
                    insert.setNull(6, Types.INTEGER);
                }

                Date checkIn = rs.getDate("check_in_date");
                if (checkIn != null) {
                    insert.setDate(7, checkIn);
                } else {
                    insert.setNull(7, Types.DATE);
                }

                Date checkOut = rs.getDate("check_out_date");
                if (checkOut != null) {
                    insert.setDate(8, checkOut);
                } else {
                    insert.setNull(8, Types.DATE);
                }

                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    insert.setTimestamp(9, createdAt);
                } else {
                    insert.setNull(9, Types.TIMESTAMP);
                }

                copied += insert.executeUpdate();
            }
        }
        return copied;
    }
}

