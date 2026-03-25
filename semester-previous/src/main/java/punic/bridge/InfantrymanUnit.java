package punic.bridge;

/**
 * Конкретный юнит — пехотинец (абстракция Bridge).
 */
public class InfantrymanUnit extends BattleUnit {

    public InfantrymanUnit(Weapon weapon) {
        super(weapon != null ? weapon : new Sword());
    }

    @Override
    protected Weapon defaultWeapon() {
        return new Sword();
    }

    @Override
    protected String getUnitName() {
        return "Пехотинец (Bridge)";
    }
}

