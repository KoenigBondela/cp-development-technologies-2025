package punic;

/**
 * Паттерн Builder для создания воинов
 * Позволяет поэтапно конструировать объекты воинов с различными характеристиками
 * 
 * Builder определяет процесс поэтапного конструирования сложного объекта,
 * в результате которого могут получаться разные представления этого объекта.
 */
public class WarriorBuilder {
    // Тип воина: "Archer", "Infantryman", "Horseman"
    private String warriorType;
    
    // Характеристики воина
    private int health = 0;
    private int speed = 0;
    private int protection = 0;
    private int combatPower = 0;
    private String appearance = "";

    /**
     * Конструктор Builder
     */
    public WarriorBuilder() {
    }

    /**
     * Устанавливает тип воина
     * @param type тип воина ("Archer", "Infantryman", "Horseman")
     * @return текущий экземпляр Builder для цепочки вызовов
     */
    public WarriorBuilder setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Тип воина не может быть пустым");
        }
        this.warriorType = type;
        return this;
    }

    /**
     * Устанавливает внешний вид воина
     * @param appearance описание внешнего вида
     * @return текущий экземпляр Builder для цепочки вызовов
     */
    public WarriorBuilder setAppearance(String appearance) {
        this.appearance = appearance != null ? appearance : "";
        return this;
    }

    /**
     * Устанавливает уровень здоровья воина
     * @param health уровень здоровья (должен быть >= 0)
     * @return текущий экземпляр Builder для цепочки вызовов
     */
    public WarriorBuilder setHealth(int health) {
        if (health < 0) {
            throw new IllegalArgumentException("Здоровье не может быть отрицательным");
        }
        this.health = health;
        return this;
    }

    /**
     * Устанавливает скорость передвижения воина
     * @param speed скорость передвижения (должна быть >= 0)
     * @return текущий экземпляр Builder для цепочки вызовов
     */
    public WarriorBuilder setSpeed(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("Скорость не может быть отрицательной");
        }
        this.speed = speed;
        return this;
    }

    /**
     * Устанавливает степень защиты воина
     * @param protection степень защиты (должна быть >= 0)
     * @return текущий экземпляр Builder для цепочки вызовов
     */
    public WarriorBuilder setProtection(int protection) {
        if (protection < 0) {
            throw new IllegalArgumentException("Защита не может быть отрицательной");
        }
        this.protection = protection;
        return this;
    }

    /**
     * Устанавливает боевую мощь воина
     * @param combatPower боевая мощь (должна быть >= 0)
     * @return текущий экземпляр Builder для цепочки вызовов
     */
    public WarriorBuilder setCombatPower(int combatPower) {
        if (combatPower < 0) {
            throw new IllegalArgumentException("Боевая мощь не может быть отрицательной");
        }
        this.combatPower = combatPower;
        return this;
    }

    /**
     * Создает и возвращает готовый объект воина
     * Выполняет валидацию и создает объект соответствующего типа
     * 
     * @return готовый объект Warrior
     * @throws IllegalStateException если тип воина не установлен
     * @throws IllegalArgumentException если тип воина неизвестен
     */
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
                throw new IllegalArgumentException("Неизвестный тип воина: " + warriorType + 
                    ". Доступные типы: Archer, Infantryman, Horseman");
        }

        return warrior;
    }

    /**
     * Создает стандартного лучника с предустановленными характеристиками
     * Лучники имеют низкое здоровье, среднюю скорость, низкую защиту и среднюю боевую мощь
     * 
     * @return стандартный объект лучника
     */
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

    /**
     * Создает стандартного пехотинца с предустановленными характеристиками
     * Пехота имеет высокое здоровье, низкую скорость, высокую защиту и высокую боевую мощь
     * 
     * @return стандартный объект пехотинца
     */
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

    /**
     * Создает стандартного всадника с предустановленными характеристиками
     * Конница имеет среднее здоровье, самую высокую скорость, среднюю защиту и высокую боевую мощь
     * 
     * @return стандартный объект всадника
     */
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

