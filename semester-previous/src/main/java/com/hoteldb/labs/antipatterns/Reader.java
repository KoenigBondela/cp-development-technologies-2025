package com.hoteldb.labs.antipatterns;

/**
 * Класс читателя.
 * АНТИПАТТЕРН: Anemic Domain Model - только данные, нет поведения
 * АНТИПАТТЕРН: Public Fields - публичные поля вместо инкапсуляции
 */
public class Reader {
    // Публичные поля - нарушение инкапсуляции
    public int id;
    public String name;
    public int age;
    
    // Отсутствие валидации, конструкторов, геттеров, сеттеров
}

