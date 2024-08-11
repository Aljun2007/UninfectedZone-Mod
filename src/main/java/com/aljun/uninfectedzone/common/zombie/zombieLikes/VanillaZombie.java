package com.aljun.uninfectedzone.common.zombie.zombieLikes;

import com.aljun.uninfectedzone.api.zombie.zombielike.ZombieLike;
import com.aljun.uninfectedzone.core.zombie.goal.ZombieMainGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class VanillaZombie extends ZombieLike {
    @Override
    protected ZombieMainGoal createMainGoal(Mob mob) {
        return new ZombieMainGoal(mob, this);
    }

    @Override
    protected void registerAbilities(ZombieMainGoal zombieMainGoal) {
    }

    @Override
    protected void registerGoals(ZombieMainGoal zombieMainGoal) {
        zombieMainGoal.getZombie().goalSelector.addGoal(8, new LookAtPlayerGoal(zombieMainGoal.getZombie(), Player.class, 8.0F));
        zombieMainGoal.getZombie().goalSelector.addGoal(8, new RandomLookAroundGoal(zombieMainGoal.getZombie()));
        zombieMainGoal.getZombie().goalSelector.addGoal(2, new ZombieAttackGoal((Zombie) zombieMainGoal.getZombie(), 1.0D, false));
        zombieMainGoal.getZombie().targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(zombieMainGoal.getZombie(), Player.class, true));
        zombieMainGoal.getZombie().targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombieMainGoal.getZombie(), AbstractVillager.class, false));
        zombieMainGoal.getZombie().targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombieMainGoal.getZombie(), IronGolem.class, true));
        zombieMainGoal.getZombie().targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(zombieMainGoal.getZombie(), Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
        if (zombieMainGoal.getZombie() instanceof PathfinderMob mob) {
            zombieMainGoal.getZombie().goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(mob, 1.0D));
            zombieMainGoal.getZombie().targetSelector.addGoal(1, new HurtByTargetGoal(mob).setAlertOthers(ZombifiedPiglin.class));
            zombieMainGoal.getZombie().goalSelector.addGoal(4, new ZombieAttackTurtleEggGoal(mob, 1.0D, 3));
        }
        if (zombieMainGoal.getZombie() instanceof Zombie zombie) {
            zombieMainGoal.getZombie().goalSelector.addGoal(6, new MoveThroughVillageGoal(zombie, 1.0D, true, 4, zombie::canBreakDoors));
        }
    }

    static class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
        ZombieAttackTurtleEggGoal(PathfinderMob p_34344_, double p_34345_, int p_34346_) {
            super(Blocks.TURTLE_EGG, p_34344_, p_34345_, p_34346_);
        }

        public void playDestroyProgressSound(LevelAccessor p_34351_, BlockPos p_34352_) {
            p_34351_.playSound((Player) null, p_34352_, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + this.mob.getRandom().nextFloat() * 0.2F);
        }

        public void playBreakSound(Level p_34348_, BlockPos p_34349_) {
            p_34348_.playSound((Player) null, p_34349_, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + p_34348_.random.nextFloat() * 0.2F);
        }

        public double acceptedDistance() {
            return 1.14D;
        }
    }
}
