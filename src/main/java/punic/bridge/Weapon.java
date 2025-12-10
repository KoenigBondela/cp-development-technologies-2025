package punic.bridge;

/**
 * Implementor в паттерне Bridge — оружие.
 */
public interface Weapon {
    String getName();
    int getDamage();
    void attack();
}

