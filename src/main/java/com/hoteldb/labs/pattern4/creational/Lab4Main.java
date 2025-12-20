package com.hoteldb.labs.pattern4.creational;

import punic.builder.WarriorBuilder;
import punic.core.Warrior;

/**
 * Лабораторная работа №4: Порождающие паттерны проектирования
 * Демонстрация паттерна Builder
 */
public class Lab4Main {
    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа №4: Паттерн Builder ===\n");
        
        WarriorBuilder.buildStandardArcher().info();
        System.out.println();

        WarriorBuilder.buildStandardInfantryman().info();
        System.out.println();

        WarriorBuilder.buildStandardHorseman().info();
        System.out.println();

        Warrior eliteArcher = new WarriorBuilder()
                .setType("Archer")
                .setAppearance("Улучшенная кожаная броня с металлическими вставками, усиленный лук")
                .setHealth(70)
                .setSpeed(50)
                .setProtection(35)
                .setCombatPower(80)
                .build();
        eliteArcher.info();
        System.out.println();

        Warrior lightInfantryman = new WarriorBuilder()
                .setType("Infantryman")
                .setAppearance("Легкая броня, короткий меч")
                .setHealth(60)
                .setSpeed(50)
                .setProtection(40)
                .setCombatPower(50)
                .build();
        lightInfantryman.info();
        System.out.println();

        Warrior heavyHorseman = new WarriorBuilder()
                .setType("Horseman")
                .setAppearance("Тяжелая броня, длинное копье, мощный конь")
                .setHealth(120)
                .setSpeed(60)
                .setProtection(90)
                .setCombatPower(100)
                .build();
        heavyHorseman.info();
    }
}

