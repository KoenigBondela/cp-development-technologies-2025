package punic.demo;

import punic.builder.WarriorBuilder;
import punic.core.Warrior;

/**
 * Демонстрация порождающего паттерна Builder.
 */
public class Main {
    public static void main(String[] args) {
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

