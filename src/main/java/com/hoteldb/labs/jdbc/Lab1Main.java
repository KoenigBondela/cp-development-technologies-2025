package com.hoteldb.labs.jdbc;

import java.sql.SQLException;

/**
 * Лабораторная работа №1: Подключение к базе данных через JDBC
 */
public class Lab1Main {
    public static void main(String[] args) {
        System.out.println("Лабораторная работа №1: Подключение к БД через JDBC");
        
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        boolean connected = dbConnection.testConnection();
        
        if (connected) {
            System.out.println("Подключение к базе данных успешно установлено!");
        } else {
            System.out.println("Ошибка подключения к базе данных!");
            return;
        }

        try {
            dbConnection.closeConnection();
            System.out.println("Соединение с базой данных успешно закрыто!");
        } catch (SQLException e) {
            System.out.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
}

