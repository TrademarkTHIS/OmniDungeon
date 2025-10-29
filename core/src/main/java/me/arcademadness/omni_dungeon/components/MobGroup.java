package me.arcademadness.omni_dungeon.components;

import me.arcademadness.omni_dungeon.attributes.Attribute;
import me.arcademadness.omni_dungeon.controllers.GoalController;
import me.arcademadness.omni_dungeon.controllers.goals.GroupFindTargetGoal;
import me.arcademadness.omni_dungeon.entities.Entity;
import me.arcademadness.omni_dungeon.entities.MobEntity;
import me.arcademadness.omni_dungeon.events.EventListener;
import me.arcademadness.omni_dungeon.events.Subscribe;
import me.arcademadness.omni_dungeon.events.entity.EntityDeathEvent;

import java.util.*;

public class MobGroup implements EventListener  {
    private final Set<MobEntity> members = new HashSet<>();
    private Entity target;
    private final int MAX_GROUP_SIZE;

    private MobEntity queen;

    public MobGroup(int maxGroupSize) {
        MAX_GROUP_SIZE = maxGroupSize;
    }

    @Subscribe
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof MobEntity victim) {
            if (members.contains(victim)) {
                removeMember(victim);
            }
        }
    }

    public boolean addMember(MobEntity mob) {
        if (members.size() >= MAX_GROUP_SIZE) return false;
        members.add(mob);
        mob.setGroup(this);

        if (queen == null) {
            setQueen(mob);
        }

        return true;
    }

    public void setQueen(MobEntity mob) {
        if (this.queen != null) {
            removeQueenGoal(this.queen);
        }

        this.queen = mob;

        if (mob != null) {
            addQueenGoal(mob);
        }
    }

    private void addQueenGoal(MobEntity mob) {
        if (mob.getController() instanceof GoalController<?> controller) {
            @SuppressWarnings("unchecked")
            GoalController<MobEntity> gc = (GoalController<MobEntity>) controller;
            if (!gc.hasGoal(GroupFindTargetGoal.class)) {
                gc.addGoal(new GroupFindTargetGoal<>(3, 8));
            }
        }
    }

    private void removeQueenGoal(MobEntity mob) {
        if (mob.getController() instanceof GoalController<?> controller) {
            @SuppressWarnings("unchecked")
            GoalController<MobEntity> gc = (GoalController<MobEntity>) controller;
            gc.removeGoal(GroupFindTargetGoal.class);
        }
    }

    public void removeMember(MobEntity mob) {
        members.remove(mob);
        mob.setGroup(null);

        if (mob == queen) {
            setQueen(findNewQueen());
        }
    }

    private MobEntity findNewQueen() {
        return members.stream().findFirst().orElse(null);
    }

    public boolean canMerge(MobGroup other) {
        return members.size() + other.members.size() <= MAX_GROUP_SIZE;
    }

    public void merge(MobGroup other) {
        if (!canMerge(other)) return;

        for (MobEntity mob : other.members) {
            addMember(mob);
        }

        other.clear();
    }

    public Entity getTarget() {
        if (target!=null && target.getHealth().getCurrentHealth() <= 0) target = null;
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Set<MobEntity> getMembers() {
        return members;
    }

    public void clear() {
        setQueen(null);
        members.clear();
        target = null;
    }

    private boolean isDead(Entity entity) {
        return entity == null || entity.getEnvironment() == null || entity.getHealth().getCurrentHealth()<=0;
    }

    public MobEntity getQueen() {
        if (isDead(queen)) {
            validateMembers();
            setQueen(selectRandomLivingMember());
        }
        return queen;
    }

    public void validateMembers() {
        members.removeIf(this::isDead);
    }

    private MobEntity selectRandomLivingMember() {
        if (members.isEmpty()) {
            return null;
        }

        int randomIndex = new Random().nextInt(members.size());
        int i = 0;
        for (MobEntity member : members) {
            if (i == randomIndex) {
                return member;
            }
            i++;
        }

        return null;
    }
}
