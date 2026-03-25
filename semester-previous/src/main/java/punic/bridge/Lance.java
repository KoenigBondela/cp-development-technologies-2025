package punic.bridge;

/**
 * Конкретное оружие — копье/пика.
 */
public class Lance implements Weapon {
    private final int damage;

    public Lance() {
        this.damage = 65;
    }

    public Lance(int damage) {
        this.damage = damage;
    }

    @Override
    public String getName() {
        return "Копье";
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void attack() {
        System.out.println("Наносит выпад копьем. Урон: " + damage);
    }
}

