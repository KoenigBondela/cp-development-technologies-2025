package com.hoteldb.labs.pattern9.aspects;

import punic.bridge.BattleUnit;
import punic.bridge.Weapon;

/**
 * Аспект для паттерна Bridge.
 * Добавляет логирование смены оружия и атак.
 */
public aspect BridgeAspect {
    
    // Перехватываем смену оружия
    pointcut changeWeapon(BattleUnit unit, Weapon weapon): 
        execution(* BattleUnit.changeWeapon(Weapon)) && target(unit) && args(weapon);
    
    // Перехватываем вызов метода info()
    pointcut infoMethod(): execution(* BattleUnit.info());
    
    // Перехватываем атаку оружием
    pointcut weaponAttack(): execution(* Weapon.attack());
    
    before(BattleUnit unit, Weapon weapon): changeWeapon(unit, weapon) {
        String unitName = unit.getClass().getSimpleName();
        String weaponName = weapon != null ? weapon.getClass().getSimpleName() : "null";
        System.out.println("[Aspect] Смена оружия для " + unitName + " на " + weaponName);
    }
    
    after(BattleUnit unit, Weapon weapon): changeWeapon(unit, weapon) {
        System.out.println("[Aspect] Оружие успешно заменено");
    }
    
    before(): infoMethod() {
        System.out.println("[Aspect] Вывод информации о боевой единице...");
    }
    
    after(): infoMethod() {
        System.out.println("[Aspect] Информация выведена");
    }
    
    before(): weaponAttack() {
        Weapon weapon = (Weapon) thisJoinPoint.getTarget();
        System.out.println("[Aspect] Атака оружием: " + weapon.getName() + " (урон: " + weapon.getDamage() + ")");
    }
}

