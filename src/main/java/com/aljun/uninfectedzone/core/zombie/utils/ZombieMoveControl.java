package com.aljun.uninfectedzone.core.zombie.utils;


import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public abstract class ZombieMoveControl {
    protected final Mob ZOMBIE;
    protected final ZombieMainGoal MAIN_GOAL;
    protected final double MOVEMENT_SPEED;
    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;

    protected Path path;
    protected PathState pathState = PathState.DONE;
    protected boolean available = true;

    public ZombieMoveControl(ZombieMainGoal mainGoal) {
        this.ZOMBIE = mainGoal.getZombie();
        this.MAIN_GOAL = mainGoal;
        this.MOVEMENT_SPEED = this.MAIN_GOAL.getZombie().getAttributes().getBaseValue(Attributes.MOVEMENT_SPEED);
    }

    protected void receivePathOrAbsent(@Nullable Path path) {
        if (path != null) {
            this.path = path;
            this.pathState = PathState.CREATED;
        }
    }

    public ZombieMainGoal getMainGoal() {
        return MAIN_GOAL;
    }

    public void setWantedPos(double wantedX, double wantedY, double wantedZ) {
        this.wantedX = wantedX;
        this.wantedY = wantedY;
        this.wantedZ = wantedZ;
    }

    public void tick() {
        if (this.available) {
            if (this.pathState.is(PathState.CREATED)) {
                this.startMove();
            } else if (this.pathState == PathState.RUNNING) {
                if (this.getZombie().getNavigation().isDone()) this.pathState = PathState.DONE;
            }
        }
    }

    protected void startMove() {
        this.getZombie().getNavigation().stop();
        this.getZombie().getNavigation().moveTo(this.path, this.MOVEMENT_SPEED);
    }

    public Mob getZombie() {
        return ZOMBIE;
    }

    public double getMovementSpeed() {
        return this.MOVEMENT_SPEED;
    }

    public double getWantedX() {
        return wantedX;
    }

    public double getWantedY() {
        return wantedY;
    }

    public double getWantedZ() {
        return wantedZ;
    }

    public Vec3 getWantedPos() {
        return new Vec3(this.wantedX, this.wantedY, wantedZ);
    }

    public void setWantedPos(Vec3 position) {
        this.wantedX = position.x;
        this.wantedY = position.y;
        this.wantedZ = position.z;
    }

    public enum PathState {
        CREATED(0), RUNNING(1), DONE(2);

        public final int ID;

        PathState(int id) {
            this.ID = id;
        }

        public boolean is(PathState state) {
            return state.ID == this.ID;
        }
    }
}
