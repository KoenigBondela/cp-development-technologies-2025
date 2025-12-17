package punic.iterator;

import punic.bridge.ArcherUnit;
import punic.bridge.Bow;
import punic.bridge.HorsemanUnit;
import punic.bridge.InfantrymanUnit;
import punic.bridge.Lance;
import punic.bridge.Sword;
import punic.core.Archer;
import punic.core.Horseman;
import punic.core.Infantryman;
import punic.core.Warrior;

/**
 * Демонстрация паттерна Iterator.
 * Показывает, как можно обходить коллекцию воинов без знания
 * о внутренней структуре хранения данных.
 */
public class IteratorDemo {
    public static void main(String[] args) {
        System.out.println("=== Демонстрация паттерна Iterator ===\n");

        // Создаем коллекцию воинов
        WarriorCollection army = new WarriorCollection();

        // Добавляем различных воинов в коллекцию
        System.out.println("Формирование армии...\n");
        
        army.addWarrior(new Archer(60, 40, 30, 50, "Кожаная броня, лук"));
        army.addWarrior(new Infantryman(80, 30, 50, 60, "Металлическая броня, меч"));
        army.addWarrior(new Horseman(100, 50, 60, 70, "Тяжелая броня, копье"));
        army.addWarrior(new ArcherUnit(new Bow()));
        army.addWarrior(new InfantrymanUnit(new Sword()));
        army.addWarrior(new HorsemanUnit(new Lance()));
        army.addWarrior(new Archer(70, 45, 35, 55, "Улучшенная броня, усиленный лук"));
        army.addWarrior(new Infantryman(90, 35, 55, 65, "Тяжелая броня, длинный меч"));

        System.out.println("Армия сформирована. Всего воинов: " + army.size() + "\n");

        // Используем итератор для обхода коллекции
        System.out.println("=== Обход армии с помощью итератора ===\n");
        WarriorIterator iterator = army.createIterator();
        
        int warriorNumber = 1;
        while (iterator.hasNext()) {
            Warrior warrior = iterator.next();
            System.out.println("Воин #" + warriorNumber + ":");
            warrior.info();
            System.out.println();
            warriorNumber++;
        }

        // Демонстрация сброса итератора
        System.out.println("=== Повторный обход после сброса итератора ===\n");
        iterator.reset();
        
        System.out.println("Поиск лучников в армии:\n");
        warriorNumber = 1;
        int archerCount = 0;
        while (iterator.hasNext()) {
            Warrior warrior = iterator.next();
            if (warrior instanceof Archer || warrior.getClass().getSimpleName().contains("Archer")) {
                archerCount++;
                System.out.println("Найден лучник #" + archerCount + " (общий номер в армии: " + warriorNumber + "):");
                warrior.info();
                System.out.println();
            }
            warriorNumber++;
        }

        System.out.println("Всего найдено лучников: " + archerCount + "\n");

        // Демонстрация работы с пустой коллекцией
        System.out.println("=== Работа с пустой коллекцией ===\n");
        WarriorCollection emptyArmy = new WarriorCollection();
        WarriorIterator emptyIterator = emptyArmy.createIterator();
        
        if (!emptyIterator.hasNext()) {
            System.out.println("Коллекция пуста - нет элементов для обхода.");
        }

        System.out.println("\n=== Демонстрация завершена ===");
    }
}

