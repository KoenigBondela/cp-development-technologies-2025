package com.hoteldb.labs.pattern5.structural;

import punic.bridge.ArcherUnit;
import punic.bridge.BattleUnit;
import punic.bridge.Bow;
import punic.bridge.HorsemanUnit;
import punic.bridge.InfantrymanUnit;
import punic.bridge.Lance;
import punic.bridge.Sword;

/**
 * Лабораторная работа №5: Структурные паттерны проектирования
 * Демонстрация паттерна Bridge
 */
public class Lab5Main {
    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа №5: Паттерн Bridge ===\n");
        
        BattleUnit archer = new ArcherUnit(null);
        BattleUnit infantry = new InfantrymanUnit(null);
        BattleUnit horseman = new HorsemanUnit(null);

        archer.info();
        System.out.println();
        infantry.info();
        System.out.println();
        horseman.info();

        System.out.println();
        System.out.println("=== Замена оружия на лету ===");
        System.out.println();

        archer.changeWeapon(new Sword());
        infantry.changeWeapon(new Lance());
        horseman.changeWeapon(new Bow());

        archer.info();
        System.out.println();
        infantry.info();
        System.out.println();
        horseman.info();
    }
}

