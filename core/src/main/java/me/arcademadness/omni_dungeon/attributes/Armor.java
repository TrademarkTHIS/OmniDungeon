package me.arcademadness.omni_dungeon.attributes;

public final class Armor extends SimpleAttribute<Integer> {

    double currentArmor;

    public Armor(int baseHealth) {
        super(baseHealth);
        currentArmor = baseHealth;
    }

    public double getCurrentArmor() {
        return Math.min(Math.max(currentArmor,0), getFinalValue());
    }

    public void setCurrentArmor(double currentArmor) {
        this.currentArmor = Math.min(currentArmor, getFinalValue());
    }

    public void damage(double amount) {
        currentArmor = Math.max(0, currentArmor - amount);
    }
}
