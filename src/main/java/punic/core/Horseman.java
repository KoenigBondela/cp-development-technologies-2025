package punic.core;

/**
 * Класс всадника.
 */
public class Horseman implements Warrior {
    private int health;
    private int speed;
    private int protection;
    private int combatPower;
    private String appearance;

    public Horseman() {
        // Конструктор по умолчанию
    }

    public Horseman(int health, int speed, int protection, int combatPower, String appearance) {
        this.health = health;
        this.speed = speed;
        this.protection = protection;
        this.combatPower = combatPower;
        this.appearance = appearance;
    }

    @Override
    public void info() {
        System.out.println("=== Конница ===");
        System.out.println("Внешний вид: " + appearance);
        System.out.println("Здоровье: " + health);
        System.out.println("Скорость: " + speed);
        System.out.println("Защита: " + protection);
        System.out.println("Боевая мощь: " + combatPower);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getProtection() {
        return protection;
    }

    public void setProtection(int protection) {
        this.protection = protection;
    }

    public int getCombatPower() {
        return combatPower;
    }

    public void setCombatPower(int combatPower) {
        this.combatPower = combatPower;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }
}

