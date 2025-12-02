package com.hoteldb.labs.jdbc;

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
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Загрузить настройки подключения из файла
     */
    private void loadProperties() {
        properties = new Properties();
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE);
        if (inputStream == null) {
            inputStream = getClass().getClassLoader()
                    .getResourceAsStream("test-database.properties");
        }
        final InputStream stream = inputStream;
        if (stream == null) {
            throw new RuntimeException("Не найден файл с настройками базы данных");
        }
        try (stream) {
            properties.load(stream);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке настроек: " + e.getMessage());
        }
    }

    /**
     * Получить соединение с базой данных
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");

            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Драйвер базы данных не найден: " + driver, e);
            }

            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    /**
     * Закрыть соединение с базой данных
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Проверить подключение к базе данных
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            return false;
        }
    }
}

