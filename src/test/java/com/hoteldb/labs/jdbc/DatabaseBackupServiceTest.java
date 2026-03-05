package com.hoteldb.labs.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseBackupServiceTest {

    @Test
    void testConstructorNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new DatabaseBackupService(null));
    }

    @Test
    void testBackupPrimaryToBackupCopiesData() throws Exception {
        Properties props = new Properties();
        props.setProperty("db.driver", "org.h2.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:primary_backup_ok;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        props.setProperty("db.backup.driver", "org.h2.Driver");
        props.setProperty("db.backup.url", "jdbc:h2:mem:backup_backup_ok;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.backup.username", "sa");
        props.setProperty("db.backup.password", "");

        DatabaseConnection db = new DatabaseConnection(props);

        // schema + data in PRIMARY
        try (Connection primary = db.openPrimaryConnection();
             Statement stmt = primary.createStatement()) {
            stmt.execute("CREATE TABLE rooms (" +
                    "id INT PRIMARY KEY, " +
                    "room_number VARCHAR(10) NOT NULL, " +
                    "room_type VARCHAR(50) NOT NULL, " +
                    "price_per_night DECIMAL(10, 2) NOT NULL, " +
                    "is_available BOOLEAN, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            stmt.execute("CREATE TABLE clients (" +
                    "id INT PRIMARY KEY, " +
                    "first_name VARCHAR(100) NOT NULL, " +
                    "last_name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(255), " +
                    "phone VARCHAR(20), " +
                    "room_id INT, " +
                    "check_in_date DATE, " +
                    "check_out_date DATE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            stmt.execute("INSERT INTO rooms (id, room_number, room_type, price_per_night, is_available) VALUES (1, '101', 'Standard', 50.00, TRUE)");
            stmt.execute("INSERT INTO clients (id, first_name, last_name, email, phone, room_id, check_in_date, check_out_date) VALUES " +
                    "(10, 'John', 'Doe', 'john@example.com', '+123', 1, DATE '" + LocalDate.now() + "', DATE '" + LocalDate.now().plusDays(2) + "')");
        }

        DatabaseBackupService backupService = new DatabaseBackupService(db);
        DatabaseBackupService.BackupReport report = backupService.backupPrimaryToBackup();
        assertEquals(1, report.roomsCopied());
        assertEquals(1, report.clientsCopied());

        // verify in BACKUP
        try (Connection backup = db.openBackupConnection();
             Statement stmt = backup.createStatement()) {
            var rsRooms = stmt.executeQuery("SELECT COUNT(*) FROM rooms");
            assertTrue(rsRooms.next());
            assertEquals(1, rsRooms.getInt(1));

            var rsClients = stmt.executeQuery("SELECT COUNT(*) FROM clients");
            assertTrue(rsClients.next());
            assertEquals(1, rsClients.getInt(1));
        }
    }

    @Test
    void testBackupNotConfiguredThrows() {
        Properties props = new Properties();
        props.setProperty("db.driver", "org.h2.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:primary_only;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        DatabaseBackupService backupService = new DatabaseBackupService(db);
        assertThrows(IllegalStateException.class, backupService::backupPrimaryToBackup);
    }

    @Test
    void testBackupRollbackOnFailure() throws SQLException {
        Properties props = new Properties();
        props.setProperty("db.driver", "org.h2.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:primary_backup_fail;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        props.setProperty("db.backup.driver", "org.h2.Driver");
        props.setProperty("db.backup.url", "jdbc:h2:mem:backup_backup_fail;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.backup.username", "sa");
        props.setProperty("db.backup.password", "");

        DatabaseConnection db = new DatabaseConnection(props);

        // Intentionally DO NOT create rooms/clients in PRIMARY -> copy should fail
        DatabaseBackupService backupService = new DatabaseBackupService(db);
        assertThrows(SQLException.class, backupService::backupPrimaryToBackup);
    }
}

