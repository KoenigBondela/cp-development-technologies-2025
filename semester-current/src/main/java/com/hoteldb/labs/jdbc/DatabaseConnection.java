package com.hoteldb.labs.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Класс для подключения к базе данных через JDBC
 * Реализует паттерн Singleton
 */
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String DEFAULT_PROPERTIES_FILE = "database.properties";
    private static final String FALLBACK_TEST_PROPERTIES_FILE = "test-database.properties";
    private static final String SYS_PROP_PROPERTIES_FILE = "db.properties.file";

    private static final String KEY_DRIVER = "db.driver";
    private static final String KEY_URL = "db.url";
    private static final String KEY_USERNAME = "db.username";
    private static final String KEY_PASSWORD = "db.password";

    private static final String KEY_BACKUP_DRIVER = "db.backup.driver";
    private static final String KEY_BACKUP_URL = "db.backup.url";
    private static final String KEY_BACKUP_USERNAME = "db.backup.username";
    private static final String KEY_BACKUP_PASSWORD = "db.backup.password";

    private static DatabaseConnection instance;
    private Connection connection;
    private final Properties properties;
    private final ResourceProvider resourceProvider;
    private final String propertiesFileName;
    private DatabaseRole activeRole = DatabaseRole.PRIMARY;

    enum DatabaseRole { PRIMARY, BACKUP }

    @FunctionalInterface
    interface ResourceProvider {
        InputStream open(String resourceName);
    }

    private DatabaseConnection() {
        this(DatabaseConnection::openFromClassPath, resolvePropertiesFileName());
    }

    DatabaseConnection(ResourceProvider resourceProvider, String propertiesFileName) {
        this.resourceProvider = resourceProvider;
        this.propertiesFileName = propertiesFileName;
        this.properties = loadProperties(resourceProvider, propertiesFileName);
    }

    DatabaseConnection(Properties properties) {
        this.resourceProvider = DatabaseConnection::openFromClassPath;
        this.propertiesFileName = "<in-memory>";
        this.properties = properties;
    }

    /**
     * Получить единственный экземпляр класса
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            logger.debug("Создание нового экземпляра DatabaseConnection");
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Сбросить singleton (нужно для unit-тестов)
     */
    static synchronized void resetForTests() {
        instance = null;
    }

    public DatabaseRole getActiveRole() {
        return activeRole;
    }

    public boolean isBackupConfigured() {
        return isNotBlank(properties.getProperty(KEY_BACKUP_URL));
    }

    /**
     * Получить соединение с базой данных
     */
    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.debug("Создание нового соединения с базой данных");
            try {
                connection = openConnection(DatabaseRole.PRIMARY);
                activeRole = DatabaseRole.PRIMARY;
                logger.info("Соединение с ОСНОВНОЙ базой данных успешно установлено: {}", getPrimaryUrl());
            } catch (SQLException primaryException) {
                logger.error("Ошибка подключения к ОСНОВНОЙ БД: {}", primaryException.getMessage(), primaryException);
                if (!isBackupConfigured()) {
                    throw new SQLException("Ошибка подключения к основной БД, а резервная БД не настроена", primaryException);
                }
                try {
                    connection = openConnection(DatabaseRole.BACKUP);
                    activeRole = DatabaseRole.BACKUP;
                    logger.warn("Переключение на РЕЗЕРВНУЮ БД выполнено успешно: {}", getBackupUrl());
                } catch (SQLException backupException) {
                    logger.error("Ошибка подключения к РЕЗЕРВНОЙ БД: {}", backupException.getMessage(), backupException);
                    SQLException combined = new SQLException("Не удалось подключиться ни к основной, ни к резервной БД", primaryException);
                    combined.addSuppressed(backupException);
                    throw combined;
                }
            }
        } else {
            logger.debug("Использование существующего соединения с базой данных");
        }
        return connection;
    }

    public Connection openPrimaryConnection() throws SQLException {
        return openConnection(DatabaseRole.PRIMARY);
    }

    public Connection openBackupConnection() throws SQLException {
        if (!isBackupConfigured()) {
            throw new SQLException("Резервная БД не настроена (нет свойства " + KEY_BACKUP_URL + ")");
        }
        return openConnection(DatabaseRole.BACKUP);
    }

    /**
     * Закрыть соединение с базой данных
     */
    public synchronized void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            logger.debug("Закрытие соединения с базой данных");
            try {
                connection.close();
                logger.info("Соединение с базой данных успешно закрыто");
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии соединения с базой данных: {}", e.getMessage(), e);
                throw e;
            }
        } else {
            logger.debug("Соединение уже закрыто или не было установлено");
        }
    }

    /**
     * Проверить подключение к базе данных
     */
    public boolean testConnection() {
        logger.info("Проверка подключения к базе данных");
        try {
            Connection conn = getConnection();
            boolean isConnected = conn != null && !conn.isClosed();
            if (isConnected) {
                logger.info("Подключение к базе данных успешно проверено");
            } else {
                logger.warn("Соединение с базой данных не установлено");
            }
            return isConnected;
        } catch (SQLException e) {
            logger.error("Ошибка при проверке подключения к базе данных: {}", e.getMessage(), e);
            return false;
        }
    }

    private Connection openConnection(DatabaseRole role) throws SQLException {
        String url = role == DatabaseRole.PRIMARY ? getPrimaryUrl() : getBackupUrl();
        String username = role == DatabaseRole.PRIMARY ? getPrimaryUsername() : getBackupUsername();
        String password = role == DatabaseRole.PRIMARY ? getPrimaryPassword() : getBackupPassword();
        String driver = role == DatabaseRole.PRIMARY ? getPrimaryDriver() : getBackupDriver();

        logger.debug("Параметры подключения ({}): URL={}, Driver={}, Username={}",
                role, url, driver, username);

        if (isBlank(url)) {
            throw new SQLException("Не задан URL подключения для " + role);
        }
        if (isBlank(driver)) {
            throw new SQLException("Не задан JDBC-драйвер для " + role);
        }

        try {
            logger.debug("Загрузка драйвера базы данных ({}): {}", role, driver);
            Class.forName(driver);
            logger.debug("Драйвер успешно загружен ({})", role);
        } catch (ClassNotFoundException e) {
            logger.error("Драйвер базы данных не найден ({}): {}", role, driver, e);
            throw new SQLException("Драйвер базы данных не найден: " + driver, e);
        }

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SQLException("Ошибка подключения к " + role + " БД: " + e.getMessage(), e);
        }
    }

    private String getPrimaryDriver() {
        return properties.getProperty(KEY_DRIVER);
    }

    private String getPrimaryUrl() {
        return properties.getProperty(KEY_URL);
    }

    private String getPrimaryUsername() {
        return properties.getProperty(KEY_USERNAME);
    }

    private String getPrimaryPassword() {
        return properties.getProperty(KEY_PASSWORD);
    }

    private String getBackupDriver() {
        String driver = properties.getProperty(KEY_BACKUP_DRIVER);
        return isNotBlank(driver) ? driver : getPrimaryDriver();
    }

    private String getBackupUrl() {
        return properties.getProperty(KEY_BACKUP_URL);
    }

    private String getBackupUsername() {
        String username = properties.getProperty(KEY_BACKUP_USERNAME);
        return isNotBlank(username) ? username : getPrimaryUsername();
    }

    private String getBackupPassword() {
        String password = properties.getProperty(KEY_BACKUP_PASSWORD);
        return password != null ? password : getPrimaryPassword();
    }

    private static String resolvePropertiesFileName() {
        String override = System.getProperty(SYS_PROP_PROPERTIES_FILE);
        if (isNotBlank(override)) {
            return override.trim();
        }
        return DEFAULT_PROPERTIES_FILE;
    }

    private static InputStream openFromClassPath(String resourceName) {
        InputStream classpathStream = DatabaseConnection.class.getClassLoader().getResourceAsStream(resourceName);
        if (classpathStream != null) {
            return classpathStream;
        }

        // Fallback: allow running from a working directory that contains the properties file
        try {
            Path localPath = Path.of(resourceName);
            if (Files.exists(localPath) && Files.isRegularFile(localPath)) {
                return Files.newInputStream(localPath);
            }
        } catch (Exception ignored) {
            // ignore and return null below
        }

        return null;
    }

    private static Properties loadProperties(ResourceProvider resourceProvider, String primaryFileName) {
        logger.debug("Загрузка настроек подключения к базе данных (файл: {})", primaryFileName);
        Properties props = new Properties();

        InputStream streamToUse = resourceProvider.open(primaryFileName);
        if (streamToUse == null && !FALLBACK_TEST_PROPERTIES_FILE.equals(primaryFileName)) {
            logger.warn("Файл {} не найден, попытка загрузить {}", primaryFileName, FALLBACK_TEST_PROPERTIES_FILE);
            streamToUse = resourceProvider.open(FALLBACK_TEST_PROPERTIES_FILE);
        }

        if (streamToUse == null) {
            logger.error("Не найден файл с настройками базы данных ({} или {})", primaryFileName, FALLBACK_TEST_PROPERTIES_FILE);
            throw new RuntimeException("Не найден файл с настройками базы данных");
        }

        try (InputStream inputStream = streamToUse) {
            props.load(inputStream);
            logger.info("Настройки подключения успешно загружены. URL: {}", props.getProperty(KEY_URL));
            return props;
        } catch (Exception e) {
            logger.error("Ошибка при загрузке настроек подключения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при загрузке настроек: " + e.getMessage(), e);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static boolean isNotBlank(String value) {
        return !isBlank(value);
    }
}

