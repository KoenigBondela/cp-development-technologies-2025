package com.hoteldb.labs.pattern9;

import punic.bridge.ArcherUnit;
import punic.bridge.BattleUnit;
import punic.bridge.Bow;
import punic.bridge.HorsemanUnit;
import punic.bridge.InfantrymanUnit;
import punic.bridge.Lance;
import punic.bridge.Sword;
import punic.builder.WarriorBuilder;
import punic.core.Archer;
import punic.core.Horseman;
import punic.core.Infantryman;
import punic.core.Warrior;
import punic.iterator.WarriorCollection;
import punic.iterator.WarriorIterator;

/**
 * Лабораторная работа №9: Аспектно-ориентированные версии паттернов проектирования
 * 
 * Демонстрация применения аспектов к паттернам из лабораторных работ №4, №5, №6:
 * - Builder (порождающий паттерн)
 * - Bridge (структурный паттерн)
 * - Iterator (паттерн поведения)
 */
public class Lab9Main {
    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа №9: Аспектно-ориентированные паттерны ===\n");
        
        // Демонстрация аспектного Builder
        System.out.println("========== 1. Аспектный Builder ==========\n");
        demonstrateBuilder();
        
        System.out.println("\n========== 2. Аспектный Bridge ==========\n");
        demonstrateBridge();
        
        System.out.println("\n========== 3. Аспектный Iterator ==========\n");
        demonstrateIterator();
        
        System.out.println("\n=== Демонстрация завершена ===");
    }
    
    private static void demonstrateBuilder() {
        System.out.println("Создание стандартных воинов через Builder:\n");
        
        WarriorBuilder.buildStandardArcher().info();
        System.out.println();
        
        WarriorBuilder.buildStandardInfantryman().info();
        System.out.println();
        
        System.out.println("Создание кастомного воина через Builder:\n");
        Warrior eliteArcher = new WarriorBuilder()
                .setType("Archer")
                .setAppearance("Улучшенная кожаная броня")
                .setHealth(70)
                .setSpeed(50)
                .setProtection(35)
                .setCombatPower(80)
                .build();
        eliteArcher.info();
    }
    
    private static void demonstrateBridge() {
        BattleUnit archer = new ArcherUnit(null);
        BattleUnit infantry = new InfantrymanUnit(null);
        BattleUnit horseman = new HorsemanUnit(null);
        
        System.out.println("Исходное состояние юнитов:\n");
        archer.info();
        System.out.println();
        infantry.info();
        System.out.println();
        
        System.out.println("Смена оружия:\n");
        archer.changeWeapon(new Sword());
        infantry.changeWeapon(new Lance());
        
        archer.info();
        System.out.println();
        infantry.info();
    }
    
    private static void demonstrateIterator() {
        WarriorCollection army = new WarriorCollection();
        
        System.out.println("Формирование армии:\n");
        army.addWarrior(new Archer(60, 40, 30, 50, "Кожаная броня, лук"));
        army.addWarrior(new Infantryman(80, 30, 50, 60, "Металлическая броня, меч"));
        army.addWarrior(new Horseman(100, 50, 60, 70, "Тяжелая броня, копье"));
        army.addWarrior(new ArcherUnit(new Bow()));
        army.addWarrior(new InfantrymanUnit(new Sword()));
        
        System.out.println("\nОбход армии с помощью итератора:\n");
        WarriorIterator iterator = army.createIterator();
        
        int warriorNumber = 1;
        while (iterator.hasNext()) {
            Warrior warrior = iterator.next();
            System.out.println("Воин #" + warriorNumber + ":");
            warrior.info();
            System.out.println();
            warriorNumber++;
        }
        
        System.out.println("Сброс итератора:\n");
        iterator.reset();
    }
}

