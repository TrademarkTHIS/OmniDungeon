package me.arcademadness.omni_dungeon.attributes;

public final class Health extends SimpleAttribute<Integer> {

    double currentHealth;

    public Health(int baseHealth) {
        super(baseHealth);
        currentHealth = baseHealth;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = Math.min(currentHealth, getFinalValue());
    }

    public void damage(double amount) {
        currentHealth = Math.max(0, currentHealth - amount);
    }
}
