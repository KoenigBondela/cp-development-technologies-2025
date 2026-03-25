package com.hoteldb.labs.antipatterns;

/**
 * Главный класс для демонстрации антипаттернов.
 * Демонстрирует работу системы управления библиотекой,
 * которая содержит множество антипаттернов, но при этом функциональна.
 */
public class AntiPatternsDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Демонстрация системы управления библиотекой ===\n");
        System.out.println("ВНИМАНИЕ: Этот код содержит множество антипаттернов!");
        System.out.println("Он работает, но демонстрирует плохие практики программирования.\n");
        
        // Использование Singleton (антипаттерн в данном контексте)
        LibraryManager library = LibraryManager.getInstance();
        
        // Инициализация данных
        System.out.println("--- Инициализация библиотеки ---");
        library.initializeDefaultData();
        System.out.println();
        
        // Добавление новых книг
        System.out.println("--- Добавление новых книг ---");
        library.processEverything("addBook", 4, 0, "Design Patterns", "Gang of Four", null, 0);
        library.processEverything("addBook", 5, 0, "Clean Code", "Robert Martin", null, 0);
        System.out.println();
        
        // Добавление нового читателя
        System.out.println("--- Добавление нового читателя ---");
        library.processEverything("addReader", 0, 3, null, null, "David", 28);
        System.out.println();
        
        // Выдача книг
        System.out.println("--- Выдача книг читателям ---");
        library.processEverything("borrowBook", 1, 1, null, null, null, 0);
        library.processEverything("borrowBook", 2, 2, null, null, null, 0);
        library.processEverything("borrowBook", 4, 3, null, null, null, 0);
        System.out.println();
        
        // Возврат книги
        System.out.println("--- Возврат книги ---");
        library.processEverything("returnBook", 1, 0, null, null, null, 0);
        System.out.println();
        
        // Поиск книг
        System.out.println("--- Поиск книг ---");
        library.optimizedSearch("Java");
        library.optimizedSearch("Pattern");
        System.out.println();
        
        // Отчет
        System.out.println("--- Отчет о библиотеке ---");
        library.printReport();
        System.out.println();
        
        // Демонстрация BookService
        System.out.println("--- Демонстрация BookService ---");
        BookService service = new BookService();
        System.out.println("Status for book 1: " + service.getBookStatus(1));
        System.out.println("Status for book 2: " + service.getBookStatus(2));
        System.out.println();
        
        System.out.println("=== Демонстрация завершена ===");
        System.out.println("\nОбратите внимание на следующие проблемы в коде:");
        System.out.println("1. God Object (LibraryManager делает слишком много)");
        System.out.println("2. Magic Numbers и Magic Strings");
        System.out.println("3. Long Methods");
        System.out.println("4. Copy-Paste Programming");
        System.out.println("5. Hard Coding");
        System.out.println("6. Anemic Domain Model");
        System.out.println("7. И многие другие...");
        System.out.println("\nСм. README.md для подробного описания всех антипаттернов.");
    }
}

