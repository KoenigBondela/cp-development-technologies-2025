package com.hoteldb.labs.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
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
    private static final String PROPERTIES_FILE = "database.properties";
    private static DatabaseConnection instance;
    private Connection connection;
    private Properties properties;

    private DatabaseConnection() {
        loadProperties();
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
     * Загрузить настройки подключения из файла
     */
    private void loadProperties() {
        logger.debug("Загрузка настроек подключения к базе данных");
        properties = new Properties();
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE);
        if (inputStream == null) {
            logger.warn("Файл {} не найден, попытка загрузить test-database.properties", PROPERTIES_FILE);
            inputStream = getClass().getClassLoader()
                    .getResourceAsStream("test-database.properties");
        }
        final InputStream stream = inputStream;
        if (stream == null) {
            logger.error("Не найден файл с настройками базы данных ({} или test-database.properties)", PROPERTIES_FILE);
            throw new RuntimeException("Не найден файл с настройками базы данных");
        }
        try (stream) {
            properties.load(stream);
            logger.info("Настройки подключения успешно загружены. URL: {}", properties.getProperty("db.url"));
        } catch (Exception e) {
            logger.error("Ошибка при загрузке настроек подключения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при загрузке настроек: " + e.getMessage(), e);
        }
    }

    /**
     * Получить соединение с базой данных
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.debug("Создание нового соединения с базой данных");
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");

            logger.debug("Параметры подключения: URL={}, Driver={}, Username={}", url, driver, username);

            try {
                logger.debug("Загрузка драйвера базы данных: {}", driver);
                Class.forName(driver);
                logger.debug("Драйвер успешно загружен");
            } catch (ClassNotFoundException e) {
                logger.error("Драйвер базы данных не найден: {}", driver, e);
                throw new SQLException("Драйвер базы данных не найден: " + driver, e);
            }

            try {
                connection = DriverManager.getConnection(url, username, password);
                logger.info("Соединение с базой данных успешно установлено: {}", url);
            } catch (SQLException e) {
                logger.error("Ошибка при установке соединения с базой данных: {}", e.getMessage(), e);
                throw new SQLException("Ошибка подключения к базе данных: " + e.getMessage(), e);
            }
        } else {
            logger.debug("Использование существующего соединения с базой данных");
        }
        return connection;
    }

    /**
     * Закрыть соединение с базой данных
     */
    public void closeConnection() throws SQLException {
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
}

