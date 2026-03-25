package com.hoteldb.labs.pattern9.aspects;

import punic.iterator.WarriorCollection;
import punic.iterator.WarriorIterator;
import punic.core.Warrior;

/**
 * Аспект для паттерна Iterator.
 * Добавляет логирование операций итератора и статистику обхода.
 */
public aspect IteratorAspect {
    
    // Перехватываем создание итератора
    pointcut createIterator(): execution(* WarriorCollection.createIterator());
    
    // Перехватываем методы итератора
    pointcut hasNext(): execution(* WarriorIterator.hasNext());
    pointcut next(): execution(* WarriorIterator.next());
    pointcut reset(): execution(* WarriorIterator.reset());
    
    // Перехватываем добавление воина в коллекцию
    pointcut addWarrior(WarriorCollection collection, Warrior warrior): 
        execution(* WarriorCollection.addWarrior(Warrior)) && target(collection) && args(warrior);
    
    private int iterationCount = 0;
    private int totalIterations = 0;
    
    after() returning(WarriorIterator iterator): createIterator() {
        System.out.println("[Aspect] Итератор создан для коллекции");
        iterationCount = 0;
    }
    
    after() returning(boolean hasNext): hasNext() {
        if (hasNext) {
            iterationCount++;
            totalIterations++;
        }
    }
    
    after() returning(Warrior warrior): next() {
        String warriorType = warrior != null ? warrior.getClass().getSimpleName() : "null";
        System.out.println("[Aspect] Получен следующий воин (#" + iterationCount + "): " + warriorType);
    }
    
    before(): reset() {
        System.out.println("[Aspect] Сброс итератора. Всего итераций: " + iterationCount);
        iterationCount = 0;
    }
    
    after(WarriorCollection collection, Warrior warrior): addWarrior(collection, warrior) {
        String warriorType = warrior != null ? warrior.getClass().getSimpleName() : "null";
        System.out.println("[Aspect] Воин добавлен в коллекцию: " + warriorType + " (размер: " + collection.size() + ")");
    }
}

