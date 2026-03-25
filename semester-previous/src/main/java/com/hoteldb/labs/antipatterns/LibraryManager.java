package com.hoteldb.labs.antipatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс-менеджер библиотеки.
 * АНТИПАТТЕРН #1: God Object / Blob - этот класс делает слишком много вещей
 * АНТИПАТТЕРН #2: Anemic Domain Model - классы Book и Reader не имеют поведения
 */
public class LibraryManager {
    
    // АНТИПАТТЕРН #3: Magic Numbers - числа без объяснений
    private static final int MAX_BOOKS = 100;
    private static final int MAX_READERS = 50;
    
    // АНТИПАТТЕРН #4: Tight Coupling - прямая зависимость от конкретных классов
    private List<Book> books = new ArrayList<>();
    private List<Reader> readers = new ArrayList<>();
    
    // АНТИПАТТЕРН #5: Global State - глобальное состояние через статическую переменную
    private static LibraryManager instance = null;
    
    // АНТИПАТТЕРН #6: Singleton Abuse - неправильное использование Singleton
    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }
    
    // АНТИПАТТЕРН #7: Long Method - очень длинный метод, делающий слишком много
    public void processEverything(String action, int bookId, int readerId, String title, String author, String readerName, int age) {
        // Огромный метод, который обрабатывает все возможные действия
        if (action.equals("addBook")) {
            if (books.size() < MAX_BOOKS) {
                Book b = new Book();
                b.id = bookId;
                b.title = title;
                b.author = author;
                b.isAvailable = true;
                books.add(b);
                System.out.println("Book added: " + title);
            } else {
                System.out.println("Library is full!");
            }
        } else if (action.equals("addReader")) {
            if (readers.size() < MAX_READERS) {
                Reader r = new Reader();
                r.id = readerId;
                r.name = readerName;
                r.age = age;
                readers.add(r);
                System.out.println("Reader added: " + readerName);
            } else {
                System.out.println("Too many readers!");
            }
        } else if (action.equals("borrowBook")) {
            // АНТИПАТТЕРН #8: Feature Envy - метод обращается к данным другого объекта больше, чем к своим
            Book book = findBookById(bookId);
            Reader reader = findReaderById(readerId);
            if (book != null && reader != null) {
                if (book.isAvailable) {
                    book.isAvailable = false;
                    // АНТИПАТТЕРН #9: Data Clumps - группы данных передаются вместе без структуры
                    System.out.println("Reader " + reader.name + " (ID: " + reader.id + ", Age: " + reader.age + ") borrowed book " + book.title);
                } else {
                    System.out.println("Book is not available");
                }
            }
        } else if (action.equals("returnBook")) {
            Book book = findBookById(bookId);
            if (book != null) {
                book.isAvailable = true;
                System.out.println("Book returned: " + book.title);
            }
        }
    }
    
    // АНТИПАТТЕРН #10: Copy-Paste Programming - дублирование кода
    private Book findBookById(int id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).id == id) {
                return books.get(i);
            }
        }
        return null;
    }
    
    // Дублирование той же логики
    private Reader findReaderById(int id) {
        for (int i = 0; i < readers.size(); i++) {
            if (readers.get(i).id == id) {
                return readers.get(i);
            }
        }
        return null;
    }
    
    // АНТИПАТТЕРН #11: Hard Coding - жестко закодированные значения
    public void initializeDefaultData() {
        processEverything("addBook", 1, 0, "Java for Dummies", "John Doe", null, 0);
        processEverything("addBook", 2, 0, "Python Basics", "Jane Smith", null, 0);
        processEverything("addBook", 3, 0, "C++ Advanced", "Bob Johnson", null, 0);
        processEverything("addReader", 0, 1, null, null, "Alice", 25);
        processEverything("addReader", 0, 2, null, null, "Charlie", 30);
    }
    
    // АНТИПАТТЕРН #12: Dead Code - неиспользуемый метод
    @SuppressWarnings("unused")
    private void oldUnusedMethod() {
        System.out.println("This method is never called");
        int x = 42;
        String s = "dead code";
    }
    
    // АНТИПАТТЕРН #13: Magic Strings - строки без констант
    public void printReport() {
        System.out.println("=== Library Report ===");
        System.out.println("Total books: " + books.size());
        System.out.println("Total readers: " + readers.size());
        
        int available = 0;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).isAvailable) {
                available++;
            }
        }
        System.out.println("Available books: " + available);
    }
    
    // АНТИПАТТЕРН #14: Premature Optimization - ненужная оптимизация
    public void optimizedSearch(String query) {
        // "Оптимизация" которая на самом деле усложняет код
        char[] chars = query.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = Character.toLowerCase(chars[i]);
        }
        String lowerQuery = new String(chars);
        
        // Обычный поиск был бы проще
        for (Book book : books) {
            if (book.title.toLowerCase().contains(lowerQuery)) {
                System.out.println("Found: " + book.title);
            }
        }
    }
    
    // АНТИПАТТЕРН #15: Cargo Cult Programming - копирование кода без понимания
    public void complexAlgorithm() {
        // Копировано из какого-то другого проекта, но используется неправильно
        int[] array = new int[10];
        for (int i = 0; i < array.length; i++) {
            array[i] = i * 2;
        }
        // Но этот массив никогда не используется!
        System.out.println("Algorithm completed");
    }
}

