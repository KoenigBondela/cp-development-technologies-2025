package punic.bridge;

/**
 * Конкретный юнит — всадник (абстракция Bridge).
 */
public class HorsemanUnit extends BattleUnit {

    public HorsemanUnit(Weapon weapon) {
        super(weapon != null ? weapon : new Lance());
    }

    @Override
    protected Weapon defaultWeapon() {
        return new Lance();
    }

    @Override
    protected String getUnitName() {
        return "Всадник (Bridge)";
    }
}

