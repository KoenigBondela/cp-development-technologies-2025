package punic.demo;

import punic.bridge.ArcherUnit;
import punic.bridge.BattleUnit;
import punic.bridge.Bow;
import punic.bridge.HorsemanUnit;
import punic.bridge.InfantrymanUnit;
import punic.bridge.Lance;
import punic.bridge.Sword;

/**
 * Демонстрация структурного паттерна Bridge:
 * Абстракция (BattleUnit) делегирует работу реализации (Weapon).
 */
public class BridgeDemo {
    public static void main(String[] args) {
        BattleUnit archer = new ArcherUnit(null);
        BattleUnit infantry = new InfantrymanUnit(null);
        BattleUnit horseman = new HorsemanUnit(null);

        archer.info();
        infantry.info();
        horseman.info();

        System.out.println();
        System.out.println("=== Замена оружия на лету ===");

        archer.changeWeapon(new Sword());
        infantry.changeWeapon(new Lance());
        horseman.changeWeapon(new Bow());

        archer.info();
        infantry.info();
        horseman.info();
    }
}

