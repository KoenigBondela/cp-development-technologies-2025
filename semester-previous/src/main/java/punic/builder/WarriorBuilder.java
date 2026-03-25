package punic.builder;

import punic.core.Archer;
import punic.core.Horseman;
import punic.core.Infantryman;
import punic.core.Warrior;

/**
 * Паттерн Builder для создания воинов.
 */
public class WarriorBuilder {
    private String warriorType;
    private int health = 0;
    private int speed = 0;
    private int protection = 0;
    private int combatPower = 0;
    private String appearance = "";

    public WarriorBuilder setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Тип воина не может быть пустым");
        }
        this.warriorType = type;
        return this;
    }

    public WarriorBuilder setAppearance(String appearance) {
        this.appearance = appearance != null ? appearance : "";
        return this;
    }

    public WarriorBuilder setHealth(int health) {
        if (health < 0) {
            throw new IllegalArgumentException("Здоровье не может быть отрицательным");
        }
        this.health = health;
        return this;
    }

    public WarriorBuilder setSpeed(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Скорость не может быть отрицательной");
        }
        this.speed = speed;
        return this;
    }

    public WarriorBuilder setProtection(int protection) {
        if (protection < 0) {
            throw new IllegalArgumentException("Защита не может быть отрицательной");
        }
        this.protection = protection;
        return this;
    }

    public WarriorBuilder setCombatPower(int combatPower) {
        if (combatPower < 0) {
            throw new IllegalArgumentException("Боевая мощь не может быть отрицательной");
        }
        this.combatPower = combatPower;
        return this;
    }

    public Warrior build() {
        if (warriorType == null || warriorType.isEmpty()) {
            throw new IllegalStateException("Тип воина не установлен. Используйте setType() перед вызовом build()");
        }

        Warrior warrior;
        switch (warriorType) {
            case "Archer":
                warrior = new Archer(health, speed, protection, combatPower, appearance);
                break;
            case "Infantryman":
                warrior = new Infantryman(health, speed, protection, combatPower, appearance);
                break;
            case "Horseman":
                warrior = new Horseman(health, speed, protection, combatPower, appearance);
                break;
            default:
                throw new IllegalArgumentException(
                        "Неизвестный тип воина: " + warriorType + ". Доступные типы: Archer, Infantryman, Horseman");
        }
        return warrior;
    }

    public static Warrior buildStandardArcher() {
        return new WarriorBuilder()
                .setType("Archer")
                .setAppearance("Легкая кожаная броня, лук и колчан со стрелами")
                .setHealth(50)
                .setSpeed(40)
                .setProtection(20)
                .setCombatPower(60)
                .build();
    }

    public static Warrior buildStandardInfantryman() {
        return new WarriorBuilder()
                .setType("Infantryman")
                .setAppearance("Тяжелая броня, меч и щит")
                .setHealth(100)
                .setSpeed(20)
                .setProtection(80)
                .setCombatPower(70)
                .build();
    }

    public static Warrior buildStandardHorseman() {
        return new WarriorBuilder()
                .setType("Horseman")
                .setAppearance("Средняя броня, копье, конь")
                .setHealth(80)
                .setSpeed(100)
                .setProtection(50)
                .setCombatPower(85)
                .build();
    }
}

