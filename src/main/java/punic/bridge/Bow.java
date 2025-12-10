package punic.bridge;

/**
 * Конкретное оружие — лук.
 */
public class Bow implements Weapon {
    private final int damage;

    public Bow() {
        this.damage = 30;
    }

    public Bow(int damage) {
        this.damage = damage;
    }

    @Override
    public String getName() {
        return "Лук";
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void attack() {
        System.out.println("Стреляет из лука. Урон: " + damage);
    }
}

