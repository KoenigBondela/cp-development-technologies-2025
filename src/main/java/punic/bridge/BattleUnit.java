package punic.bridge;

import punic.core.Warrior;

/**
 * Abstraction в паттерне Bridge — боевая единица.
 * Делегирует атаку объекту Weapon (Implementor).
 */
public abstract class BattleUnit implements Warrior {
    private Weapon weapon;

    protected BattleUnit(Weapon weapon) {
        this.weapon = weapon != null ? weapon : defaultWeapon();
    }

    /**
     * Позволяет менять оружие на лету (демонстрация Bridge).
     */
    public void changeWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Оружие по умолчанию для конкретного юнита.
     */
    protected abstract Weapon defaultWeapon();

    /**
     * Имя юнита для вывода.
     */
    protected abstract String getUnitName();

    @Override
    public void info() {
        System.out.println("=== " + getUnitName() + " ===");
        System.out.println("Оружие: " + weapon.getName() + " (урон " + weapon.getDamage() + ")");
        System.out.print("Атака: ");
        weapon.attack();
    }
}

