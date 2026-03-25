package punic.bridge;

/**
 * Конкретное оружие — меч.
 */
public class Sword implements Weapon {
    private final int damage;

    public Sword() {
        this.damage = 50;
    }

    public Sword(int damage) {
        this.damage = damage;
    }

    @Override
    public String getName() {
        return "Меч";
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void attack() {
        System.out.println("Рубит мечом. Урон: " + damage);
    }
}

