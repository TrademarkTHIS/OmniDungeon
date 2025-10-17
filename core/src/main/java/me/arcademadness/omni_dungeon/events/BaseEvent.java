package me.arcademadness.omni_dungeon.events;

public abstract class BaseEvent implements Event {
    private boolean canceled = false;

    protected abstract void execute();

    @Override
    public void cancel() { this.canceled = true; }

    @Override
    public boolean isCanceled() { return canceled; }

    @Override
    public void setCanceled(boolean canceled) { this.canceled = canceled; }
}

