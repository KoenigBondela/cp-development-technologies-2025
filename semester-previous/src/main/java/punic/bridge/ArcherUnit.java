package punic.bridge;

/**
 * Конкретный юнит — лучник (абстракция Bridge).
 */
public class ArcherUnit extends BattleUnit {

    public ArcherUnit(Weapon weapon) {
        super(weapon != null ? weapon : new Bow());
    }

    @Override
    protected Weapon defaultWeapon() {
        return new Bow();
    }

    @Override
    protected String getUnitName() {
        return "Лучник (Bridge)";
    }
}

