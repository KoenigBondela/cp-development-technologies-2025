package com.hoteldb.labs.jdbc;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionFailoverTest {

    @Test
    void testPropertiesFallbackWhenPrimaryMissing() throws SQLException {
        DatabaseConnection.ResourceProvider provider = name -> {
            if ("missing.properties".equals(name)) return null;
            if ("test-database.properties".equals(name)) {
                String props = """
                        db.driver=org.h2.Driver
                        db.url=jdbc:h2:mem:fallbackdb;DB_CLOSE_DELAY=-1;MODE=MySQL
                        db.username=sa
                        db.password=
                        """;
                return new ByteArrayInputStream(props.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        };

        DatabaseConnection db = new DatabaseConnection(provider, "missing.properties");
        try (Connection c = db.getConnection()) {
            assertNotNull(c);
            assertEquals(DatabaseConnection.DatabaseRole.PRIMARY, db.getActiveRole());
        }
    }

    @Test
    void testPropertiesMissingBothThrows() {
        DatabaseConnection.ResourceProvider provider = name -> null;
        RuntimeException ex = assertThrows(RuntimeException.class, () -> new DatabaseConnection(provider, "missing.properties"));
        assertTrue(ex.getMessage().toLowerCase().contains("не найден"));
    }

    @Test
    void testPropertiesInvalidStreamThrows() {
        DatabaseConnection.ResourceProvider provider = name -> new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }
        };
        RuntimeException ex = assertThrows(RuntimeException.class, () -> new DatabaseConnection(provider, "any.properties"));
        assertTrue(ex.getMessage().toLowerCase().contains("ошибка"));
    }

    @Test
    void testGetConnectionPrimarySuccessNoBackupConfigured() throws SQLException {
        Properties props = new Properties();
        props.setProperty("db.driver", "org.h2.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:primaryonly;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        assertFalse(db.isBackupConfigured());

        try (Connection c = db.getConnection()) {
            assertNotNull(c);
            assertEquals(DatabaseConnection.DatabaseRole.PRIMARY, db.getActiveRole());
        }
    }

    @Test
    void testGetConnectionPrimaryFailsBackupSucceeds() throws SQLException {
        Properties props = new Properties();
        props.setProperty("db.driver", "com.missing.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:shouldnotwork;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        props.setProperty("db.backup.driver", "org.h2.Driver");
        props.setProperty("db.backup.url", "jdbc:h2:mem:backupok;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.backup.username", "sa");
        props.setProperty("db.backup.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        try (Connection c = db.getConnection()) {
            assertNotNull(c);
            assertEquals(DatabaseConnection.DatabaseRole.BACKUP, db.getActiveRole());
        }
    }

    @Test
    void testGetConnectionPrimaryFailsBackupNotConfiguredThrows() {
        Properties props = new Properties();
        props.setProperty("db.driver", "com.missing.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:primaryfail;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        SQLException ex = assertThrows(SQLException.class, db::getConnection);
        assertTrue(ex.getMessage().toLowerCase().contains("резерв"));
    }

    @Test
    void testGetConnectionBothFailThrowsCombined() {
        Properties props = new Properties();
        props.setProperty("db.driver", "com.missing.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:primaryfail2;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        props.setProperty("db.backup.driver", "com.missing.Driver2");
        props.setProperty("db.backup.url", "jdbc:h2:mem:backupfail2;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.backup.username", "sa");
        props.setProperty("db.backup.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        SQLException ex = assertThrows(SQLException.class, db::getConnection);
        assertNotNull(ex.getCause());
        assertEquals(1, ex.getSuppressed().length);
    }

    @Test
    void testOpenBackupConnectionNotConfiguredThrows() {
        Properties props = new Properties();
        props.setProperty("db.driver", "org.h2.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:primary;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        assertThrows(SQLException.class, db::openBackupConnection);
    }

    @Test
    void testCloseConnectionNoConnectionDoesNotThrow() throws SQLException {
        Properties props = new Properties();
        props.setProperty("db.driver", "org.h2.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:noconn;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        db.closeConnection();
    }

    @Test
    void testTestConnectionFailureReturnsFalse() {
        Properties props = new Properties();
        props.setProperty("db.driver", "com.missing.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:fail;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        assertFalse(db.testConnection());
    }

    @Test
    void testBackupDriverAndCredentialsFallbackToPrimary() throws SQLException {
        Properties props = new Properties();
        props.setProperty("db.driver", "org.h2.Driver");
        props.setProperty("db.url", "jdbc:h2:mem:prim_fallback;DB_CLOSE_DELAY=-1;MODE=MySQL");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        props.setProperty("db.backup.url", "jdbc:h2:mem:back_fallback;DB_CLOSE_DELAY=-1;MODE=MySQL");
        // intentionally omit db.backup.driver/username/password to hit fallback logic

        DatabaseConnection db = new DatabaseConnection(props);
        try (Connection c = db.openBackupConnection()) {
            assertNotNull(c);
        }
    }

    @Test
    void testOpenConnectionBlankUrlOrDriverThrows() {
        Properties props = new Properties();
        props.setProperty("db.driver", "org.h2.Driver");
        props.setProperty("db.url", "");
        props.setProperty("db.username", "sa");
        props.setProperty("db.password", "");

        DatabaseConnection db = new DatabaseConnection(props);
        assertThrows(SQLException.class, db::openPrimaryConnection);
    }
}

