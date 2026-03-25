package com.hoteldb.labs.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Лабораторная работа №1: Подключение к базе данных через JDBC
 * Лабораторная работа №8: Логирование и обработка ошибок
 */
public class Lab1Main {
    private static final Logger logger = LoggerFactory.getLogger(Lab1Main.class);

    public static void main(String[] args) {
        logger.info("=== Лабораторная работа №1: Подключение к БД через JDBC ===");
        logger.info("=== Лабораторная работа №8: Логирование и обработка ошибок ===");
        
        DatabaseConnection dbConnection = null;
        try {
            dbConnection = DatabaseConnection.getInstance();
            logger.info("Экземпляр DatabaseConnection получен");
            
            boolean connected = dbConnection.testConnection();
            
            if (connected) {
                logger.info("Подключение к базе данных успешно установлено!");
                System.out.println("Подключение к базе данных успешно установлено!");
            } else {
                logger.error("Ошибка подключения к базе данных!");
                System.err.println("Ошибка подключения к базе данных!");
                return;
            }

            // Демонстрация работы с DAO
            demonstrateDAOOperations();
            
        } catch (Exception e) {
            logger.error("Критическая ошибка в приложении: {}", e.getMessage(), e);
            System.err.println("Критическая ошибка: " + e.getMessage());
        } finally {
            if (dbConnection != null) {
                try {
                    dbConnection.closeConnection();
                    logger.info("Соединение с базой данных успешно закрыто!");
                    System.out.println("Соединение с базой данных успешно закрыто!");
                } catch (SQLException e) {
                    logger.error("Ошибка при закрытии соединения: {}", e.getMessage(), e);
                    System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Демонстрация операций с DAO
     */
    private static void demonstrateDAOOperations() {
        logger.info("=== Демонстрация операций с DAO ===");
        
        try {
            // Демонстрация работы с номерами
            RoomDAO roomDAO = new RoomDAO();
            logger.info("Получение списка всех номеров...");
            var rooms = roomDAO.findAll();
            logger.info("Найдено номеров: {}", rooms.size());
            
            // Демонстрация работы с клиентами
            ClientDAO clientDAO = new ClientDAO();
            logger.info("Получение списка всех клиентов...");
            var clients = clientDAO.findAll();
            logger.info("Найдено клиентов: {}", clients.size());
            
        } catch (SQLException e) {
            logger.error("Ошибка при выполнении операций с DAO: {}", e.getMessage(), e);
            System.err.println("Ошибка при работе с базой данных: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка: {}", e.getMessage(), e);
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}

