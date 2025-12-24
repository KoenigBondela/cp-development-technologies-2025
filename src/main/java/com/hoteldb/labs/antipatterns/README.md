# Лабораторная работа: Антипаттерны программирования

## Описание

Данная лабораторная работа демонстрирует систему управления библиотекой, которая содержит **20 антипаттернов программирования**. Несмотря на наличие множества плохих практик, код является рабочим и выполняет свою основную функциональность.

Система позволяет:
- Добавлять книги в библиотеку
- Добавлять читателей
- Выдавать книги читателям
- Возвращать книги
- Искать книги
- Формировать отчеты

---

## Антипаттерны в коде

### 1. God Object / Blob (Божественный объект)

**Описание:** Класс, который делает слишком много вещей и знает слишком много о системе.

**Где:** `LibraryManager` класс

**Проблема:** Класс `LibraryManager` содержит всю логику системы: управление книгами, читателями, выдачу, возврат, поиск, отчеты. Он нарушает принцип единственной ответственности (SRP).

**Пример:**
```java
public class LibraryManager {
    // Управляет книгами
    private List<Book> books;
    // Управляет читателями
    private List<Reader> readers;
    // Обрабатывает все действия в одном методе
    public void processEverything(...) { ... }
    // И многое другое...
}
```

**Решение:** Разделить на отдельные классы: `BookRepository`, `ReaderRepository`, `BookBorrowingService`, `BookSearchService` и т.д.

---

### 2. Anemic Domain Model (Анемичная модель предметной области)

**Описание:** Объекты содержат только данные и не имеют поведения.

**Где:** Классы `Book` и `Reader`

**Проблема:** Классы `Book` и `Reader` содержат только публичные поля без методов. Вся логика находится в `LibraryManager`.

**Пример:**
```java
public class Book {
    public int id;
    public String title;
    public String author;
    public boolean isAvailable;
    // Нет методов, нет поведения!
}
```

**Решение:** Добавить методы в классы: `borrow()`, `return()`, `isAvailable()` и т.д.

---

### 3. Magic Numbers (Магические числа)

**Описание:** Использование чисел в коде без объяснения их значения.

**Где:** `LibraryManager.MAX_BOOKS = 100`, `LibraryManager.MAX_READERS = 50`

**Проблема:** Хотя значения объявлены как константы, они не имеют смыслового объяснения. Почему именно 100 и 50?

**Пример:**
```java
private static final int MAX_BOOKS = 100; // Откуда это число?
if (books.size() < MAX_BOOKS) { ... }
```

**Решение:** Добавить комментарии с объяснением, откуда взялось число, или использовать конфигурационные файлы.

---

### 4. Tight Coupling (Жесткая связанность)

**Описание:** Классы сильно зависят друг от друга, изменения в одном требуют изменений в других.

**Где:** `LibraryManager` напрямую зависит от `Book` и `Reader`

**Проблема:** `LibraryManager` использует конкретные классы `Book` и `Reader`, что делает систему негибкой.

**Пример:**
```java
private List<Book> books = new ArrayList<>(); // Прямая зависимость
private List<Reader> readers = new ArrayList<>(); // Прямая зависимость
```

**Решение:** Использовать интерфейсы и dependency injection.

---

### 5. Global State / Singleton Abuse (Злоупотребление Singleton)

**Описание:** Использование глобального состояния через Singleton паттерн, когда это не нужно.

**Где:** `LibraryManager.getInstance()`

**Проблема:** Singleton создает глобальное состояние, что усложняет тестирование и создает скрытые зависимости.

**Пример:**
```java
private static LibraryManager instance = null;
public static LibraryManager getInstance() {
    if (instance == null) {
        instance = new LibraryManager();
    }
    return instance;
}
```

**Решение:** Использовать dependency injection вместо Singleton.

---

### 6. Long Method (Длинный метод)

**Описание:** Метод слишком длинный и делает слишком много вещей.

**Где:** `LibraryManager.processEverything()`

**Проблема:** Метод `processEverything()` содержит более 50 строк и обрабатывает множество различных действий через if-else цепочку.

**Пример:**
```java
public void processEverything(String action, int bookId, int readerId, ...) {
    if (action.equals("addBook")) {
        // 10+ строк кода
    } else if (action.equals("addReader")) {
        // 10+ строк кода
    } else if (action.equals("borrowBook")) {
        // 10+ строк кода
    }
    // И так далее...
}
```

**Решение:** Разделить на отдельные методы: `addBook()`, `addReader()`, `borrowBook()` и т.д.

---

### 7. Feature Envy (Зависть к функциональности)

**Описание:** Метод обращается к данным другого объекта больше, чем к данным своего объекта.

**Где:** В методе `processEverything()` при обработке `borrowBook`

**Проблема:** Метод из `LibraryManager` обращается к полям `Book` и `Reader` напрямую, вместо того чтобы делегировать работу этим объектам.

**Пример:**
```java
Book book = findBookById(bookId);
Reader reader = findReaderById(readerId);
if (book.isAvailable) { // Доступ к полю другого объекта
    book.isAvailable = false; // Изменение поля другого объекта
    System.out.println("Reader " + reader.name + " borrowed..."); // Доступ к полю
}
```

**Решение:** Добавить методы `borrow()` и `return()` в класс `Book`.

---

### 8. Copy-Paste Programming (Копирование кода)

**Описание:** Дублирование кода вместо создания переиспользуемых методов.

**Где:** Методы `findBookById()` и `findReaderById()`

**Проблема:** Оба метода содержат идентичную логику поиска по ID, только работают с разными типами объектов.

**Пример:**
```java
private Book findBookById(int id) {
    for (int i = 0; i < books.size(); i++) {
        if (books.get(i).id == id) {
            return books.get(i);
        }
    }
    return null;
}

private Reader findReaderById(int id) {
    for (int i = 0; i < readers.size(); i++) {
        if (readers.get(i).id == id) {
            return readers.get(i);
        }
    }
    return null;
}
```

**Решение:** Использовать generics и создать общий метод `findById()`.

---

### 9. Data Clumps (Группы данных)

**Описание:** Группы данных, которые всегда передаются вместе, но не объединены в объект.

**Где:** Параметры метода `processEverything()`

**Проблема:** Множество параметров передается вместе (bookId, readerId, title, author, readerName, age), но они не объединены в структуру.

**Пример:**
```java
public void processEverything(String action, int bookId, int readerId, 
                              String title, String author, 
                              String readerName, int age) {
    // 7 параметров!
}
```

**Решение:** Создать объекты `BookData` и `ReaderData` или использовать Command паттерн.

---

### 10. Dead Code (Мертвый код)

**Описание:** Код, который никогда не выполняется и не используется.

**Где:** Метод `oldUnusedMethod()` в `LibraryManager`

**Проблема:** Метод определен, но никогда не вызывается.

**Пример:**
```java
@SuppressWarnings("unused")
private void oldUnusedMethod() {
    System.out.println("This method is never called");
    int x = 42;
    String s = "dead code";
}
```

**Решение:** Удалить неиспользуемый код.

---

### 11. Hard Coding (Жесткое кодирование)

**Описание:** Использование жестко закодированных значений вместо конфигурации.

**Где:** Метод `initializeDefaultData()`

**Проблема:** Данные захардкожены в коде, что затрудняет изменение без перекомпиляции.

**Пример:**
```java
public void initializeDefaultData() {
    processEverything("addBook", 1, 0, "Java for Dummies", "John Doe", null, 0);
    processEverything("addBook", 2, 0, "Python Basics", "Jane Smith", null, 0);
    // ...
}
```

**Решение:** Использовать конфигурационные файлы (JSON, XML, properties) или базу данных.

---

### 12. Magic Strings (Магические строки)

**Описание:** Использование строковых литералов без констант.

**Где:** Строковые литералы "addBook", "addReader", "borrowBook", "returnBook" в коде

**Проблема:** Строковые литералы используются для обозначения действий, что приводит к ошибкам опечаток.

**Пример:**
```java
if (action.equals("addBook")) { // Магическая строка
    // ...
}
```

**Решение:** Использовать enum или константы:
```java
public enum Action {
    ADD_BOOK, ADD_READER, BORROW_BOOK, RETURN_BOOK
}
```

---

### 13. Premature Optimization (Преждевременная оптимизация)

**Описание:** Оптимизация кода до того, как это действительно необходимо.

**Где:** Метод `optimizedSearch()`

**Проблема:** Метод создает массив символов для преобразования в нижний регистр, хотя `toLowerCase()` уже оптимален для этого.

**Пример:**
```java
public void optimizedSearch(String query) {
    char[] chars = query.toCharArray();
    for (int i = 0; i < chars.length; i++) {
        chars[i] = Character.toLowerCase(chars[i]);
    }
    String lowerQuery = new String(chars);
    // Обычный query.toLowerCase() был бы проще и быстрее
}
```

**Решение:** Использовать простые и понятные решения, оптимизировать только при необходимости.

---

### 14. Cargo Cult Programming (Карго-культ программирование)

**Описание:** Копирование кода или паттернов без понимания, зачем они нужны.

**Где:** Метод `complexAlgorithm()` в `LibraryManager`

**Проблема:** Код скопирован из другого места, но используется неправильно (массив создается, но не используется).

**Пример:**
```java
public void complexAlgorithm() {
    int[] array = new int[10];
    for (int i = 0; i < array.length; i++) {
        array[i] = i * 2;
    }
    // Но этот массив никогда не используется!
    System.out.println("Algorithm completed");
}
```

**Решение:** Понимать код перед копированием или переписать с нуля.

---

### 15. Spaghetti Code (Спагетти-код)

**Описание:** Код с запутанной структурой, множественными переходами и сложной логикой.

**Где:** Метод `getBookStatus()` в `BookService`

**Проблема:** Множество вложенных if-условий создают запутанную логику, которую сложно понять и поддерживать.

**Пример:**
```java
public String getBookStatus(int bookId) {
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
```

**Решение:** Использовать early return, guard clauses, или state machine.

---

### 16. Golden Hammer (Золотой молоток)

**Описание:** Использование одного решения для всех проблем.

**Где:** Класс `BookService` использует `Random` для всех операций

**Проблема:** `Random` используется везде, даже там, где это не имеет смысла (например, для определения статуса книги).

**Пример:**
```java
private Random random = new Random();
// Используется везде, даже там где не нужно
if (random.nextBoolean()) {
    return "available";
}
```

**Решение:** Выбирать правильные инструменты для каждой задачи.

---

### 17. Lava Flow (Лавовый поток)

**Описание:** Устаревший код, который никто не понимает, но все боятся удалить.

**Где:** Поля `legacyField1`, `legacyField2`, `legacyFlag` в `BookService`

**Проблема:** Устаревшие поля и методы остаются в коде, усложняя его понимание и поддержку.

**Пример:**
```java
private int legacyField1 = 42;
private String legacyField2 = "old_value";
private boolean legacyFlag = false;

public BookService() {
    // Никто не знает, зачем это нужно, но удалить страшно
    if (legacyField1 > 0) {
        legacyFlag = true;
    }
}
```

**Решение:** Проверить, действительно ли код используется, и удалить, если нет.

---

### 18. Reinventing the Wheel (Изобретение велосипеда)

**Описание:** Создание собственной реализации того, что уже существует в стандартной библиотеке.

**Где:** Поиск по коллекции через цикл вместо использования Stream API

**Проблема:** Вместо использования современных возможностей Java (Stream API, Optional) используется ручной перебор.

**Пример:**
```java
private Book findBookById(int id) {
    for (int i = 0; i < books.size(); i++) {
        if (books.get(i).id == id) {
            return books.get(i);
        }
    }
    return null;
}
// Вместо: books.stream().filter(b -> b.id == id).findFirst()
```

**Решение:** Использовать стандартные библиотеки и современные возможности языка.

---

### 19. Shotgun Surgery (Хирургия дробовиком)

**Описание:** Изменение в одной функциональности требует изменений в множестве мест.

**Где:** Добавление книги через `LibraryManager.processEverything()` и `BookService.addBookManually()`

**Проблема:** Логика добавления книги дублируется в разных местах. Изменение требует правок в нескольких местах.

**Пример:**
```java
// В LibraryManager
public void processEverything(...) {
    if (action.equals("addBook")) {
        // Логика добавления книги
    }
}

// В BookService - дублирование той же логики
public void addBookManually(...) {
    manager.processEverything("addBook", ...);
}
```

**Решение:** Вынести общую логику в один метод/класс.

---

### 20. Public Fields (Публичные поля)

**Описание:** Использование публичных полей вместо приватных с геттерами/сеттерами.

**Где:** Классы `Book` и `Reader`

**Проблема:** Публичные поля нарушают инкапсуляцию, не позволяют контролировать доступ и добавлять валидацию.

**Пример:**
```java
public class Book {
    public int id; // Нарушение инкапсуляции
    public String title;
    public String author;
    public boolean isAvailable;
}
```

**Решение:** Использовать приватные поля с публичными методами доступа.

---

## Запуск проекта

### Компиляция

```bash
mvn clean compile
```

### Запуск демонстрации

```bash
mvn exec:java -Dexec.mainClass="com.hoteldb.labs.antipatterns.AntiPatternsDemo"
```

---

## Структура проекта

```
src/main/java/com/hoteldb/labs/antipatterns/
├── LibraryManager.java      # Главный класс (God Object)
├── Book.java                 # Модель книги (Anemic Domain Model)
├── Reader.java               # Модель читателя (Anemic Domain Model)
├── BookService.java          # Сервис с различными антипаттернами
├── AntiPatternsDemo.java     # Демонстрационный класс
└── README.md                 # Этот файл
```

---

## Выводы

Данная лабораторная работа демонстрирует, что код может быть функциональным, но при этом содержать множество плохих практик программирования. Все представленные антипаттерны:

1. **Усложняют поддержку кода** - код становится труднее понимать и изменять
2. **Увеличивают вероятность ошибок** - магические числа, копирование кода приводят к багам
3. **Затрудняют тестирование** - жесткая связанность, глобальное состояние усложняют написание тестов
4. **Снижают гибкость** - изменения требуют правок в множестве мест

Понимание антипаттернов помогает писать более чистый, поддерживаемый и расширяемый код.

---

## Дополнительные ресурсы

- Martin Fowler - "Refactoring"
- "AntiPatterns: Refactoring Software, Architectures, and Projects in Crisis"
- "Code Smells" - список признаков плохого кода

