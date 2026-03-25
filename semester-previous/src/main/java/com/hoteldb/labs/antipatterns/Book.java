package com.hoteldb.labs.antipatterns;

/**
 * Класс книги.
 * АНТИПАТТЕРН: Anemic Domain Model - только данные, нет поведения
 * АНТИПАТТЕРН: Public Fields - публичные поля вместо инкапсуляции
 */
public class Book {
    // Публичные поля - нарушение инкапсуляции
    public int id;
    public String title;
    public String author;
    public boolean isAvailable;
    
    // Отсутствие конструкторов, геттеров, сеттеров - анемичная модель
}

