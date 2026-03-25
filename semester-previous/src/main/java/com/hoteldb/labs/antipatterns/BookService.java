package com.hoteldb.labs.antipatterns;

import java.util.Random;

/**
 * Сервис для работы с книгами.
 * АНТИПАТТЕРН: Golden Hammer - использование одного решения везде (в данном случае Random)
 * АНТИПАТТЕРН: Spaghetti Code - запутанная логика
 */
public class BookService {
    
    // АНТИПАТТЕРН: Reinventing the Wheel - изобретение велосипеда вместо использования готовых решений
    private Random random = new Random();
    
    // АНТИПАТТЕРН: Lava Flow - устаревший код, который никто не понимает, но боится удалить
    private int legacyField1 = 42;
    private String legacyField2 = "old_value";
    private boolean legacyFlag = false;
    
    public BookService() {
        // Инициализация legacy полей (никто не знает зачем, но удалить страшно)
        if (legacyField1 > 0) {
            legacyFlag = true;
        }
    }
    
    // АНТИПАТТЕРН: Spaghetti Code - запутанная логика с множественными условиями
    public String getBookStatus(int bookId) {
        LibraryManager manager = LibraryManager.getInstance();
        
        // Вложенные условия, сложная логика
        if (bookId > 0) {
            if (bookId < 1000) {
                if (random.nextBoolean()) {
                    return "available";
                } else {
                    if (random.nextInt(10) > 5) {
                        return "checked_out";
                    } else {
                        return "reserved";
                    }
                }
            } else {
                return "invalid_id";
            }
        } else {
            return "error";
        }
    }
    
    // АНТИПАТТЕРН: Shotgun Surgery - изменение в одном месте требует изменений в других
    // Этот метод дублирует логику из LibraryManager
    public void addBookManually(int id, String title, String author) {
        LibraryManager manager = LibraryManager.getInstance();
        // Дублирование логики из processEverything
        manager.processEverything("addBook", id, 0, title, author, null, 0);
    }
    
    // Использование legacy полей (которые никто не понимает)
    @SuppressWarnings("unused")
    private void legacyMethod() {
        if (legacyFlag && legacyField1 == 42) {
            System.out.println("Legacy code path");
        }
    }
}

