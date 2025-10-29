package me.arcademadness.omni_dungeon.components;

import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.entities.MobEntity;

import java.util.HashSet;
import java.util.Set;

public class MobGroup {
    private final Set<MobEntity> members = new HashSet<>();
    private Entity target;
    public static final int MAX_GROUP_SIZE = 100;

    public boolean addMember(MobEntity mob) {
        if (members.size() >= MAX_GROUP_SIZE) return false;
        members.add(mob);
        mob.setGroup(this);
        return true;
    }

    public void removeMember(MobEntity mob) {
        members.remove(mob);
        mob.setGroup(null);
    }

    public boolean canMerge(MobGroup other) {
        return members.size() + other.members.size() <= MAX_GROUP_SIZE;
    }

    public void merge(MobGroup other) {
        if (!canMerge(other)) return;
        for (MobEntity mob : other.members) {
            addMember(mob);
        }
        other.members.clear();
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Set<MobEntity> getMembers() {
        return members;
    }

    public void clear() {
        members.clear();
    }
}
