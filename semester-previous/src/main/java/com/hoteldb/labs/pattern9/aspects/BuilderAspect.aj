package com.hoteldb.labs.pattern9.aspects;

import punic.builder.WarriorBuilder;

/**
 * Аспект для паттерна Builder.
 * Добавляет логирование и валидацию процесса построения объектов.
 */
public aspect BuilderAspect {
    
    // Перехватываем вызов метода build()
    pointcut buildMethod(): execution(* WarriorBuilder.build());
    
    // Перехватываем вызовы методов set* в WarriorBuilder
    pointcut setterMethods(): execution(* WarriorBuilder.set*(..));
    
    // Перехватываем создание стандартных воинов
    pointcut standardBuilders(): execution(* WarriorBuilder.buildStandard*(..));
    
    before(): buildMethod() {
        System.out.println("[Aspect] Начало построения воина через Builder...");
    }
    
    after() returning(Object warrior): buildMethod() {
        System.out.println("[Aspect] Воин успешно построен: " + warrior.getClass().getSimpleName());
    }
    
    after() throwing(Exception e): buildMethod() {
        System.out.println("[Aspect] Ошибка при построении воина: " + e.getMessage());
    }
    
    before(): setterMethods() {
        String methodName = thisJoinPoint.getSignature().getName();
        System.out.println("[Aspect] Установка параметра: " + methodName);
    }
    
    before(): standardBuilders() {
        String methodName = thisJoinPoint.getSignature().getName();
        System.out.println("[Aspect] Создание стандартного воина через: " + methodName);
    }
    
    after() returning(Object warrior): standardBuilders() {
        System.out.println("[Aspect] Стандартный воин создан: " + warrior.getClass().getSimpleName());
    }
}

