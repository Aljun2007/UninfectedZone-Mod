package com.aljun.uninfectedzone.core.command.zombie;

import com.aljun.uninfectedzone.core.command.ZombieLikeArgument;
import com.aljun.uninfectedzone.core.forgeRegister.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.zombie.like.ZombieLike;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class ZombieCommand implements Command<CommandSourceStack> {
    private static final SimpleCommandExceptionType NEW_ZOMBIE_ERROR_FAILED = new SimpleCommandExceptionType(ComponentUtils.translate("commands.summon.failed"));
    private static final SimpleCommandExceptionType NEW_ZOMBIE_ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(ComponentUtils.translate("commands.summon.failed.uuid"));
    private static final SimpleCommandExceptionType NEW_ZOMBIE_INVALID_POSITION = new SimpleCommandExceptionType(ComponentUtils.translate("commands.uninfectedzone.zombie.newZombie.invalidPosition"));

    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> zombieCommand = Commands.literal("zombie");
        load(zombieCommand);
        root.then(zombieCommand);
    }

    private static void load(LiteralArgumentBuilder<CommandSourceStack> command) {
        command.then(
                Commands.literal("newZombie").then(
                        Commands.argument("zombieLike", ZombieLikeArgument.id()).suggests(ZombieLikeArgument.ZOMBIE_LIKES_ALL).then(
                                Commands.argument("entityType", EntitySummonArgument.id()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                        .executes((stack) -> {
                                            ZombieLike zombieLike = UninfectedZoneRegistry.ZOMBIE_LIKES.get().getValue(stack.getArgument("zombieLike", ResourceLocation.class));
                                            EntityType<?> mobType = ForgeRegistries.ENTITIES.getValue(stack.getArgument("entityType", ResourceLocation.class));
                                            Mob mob = null;
                                            if (zombieLike != null && mobType != null) {
                                                try {
                                                    mob = zombieLike.newZombie(mobType, stack.getSource().getLevel());
                                                } catch (IllegalArgumentException exception) {
                                                    stack.getSource().sendFailure(ComponentUtils.literature(exception.getMessage()));
                                                }
                                                if (mob != null) {
                                                    if (Level.isInSpawnableBounds(new BlockPos(stack.getSource().getPosition()))) {
                                                        mob.moveTo(stack.getSource().getPosition());
                                                        if (mob instanceof PathfinderMob) {
                                                            Vec3 vec3 = DefaultRandomPos.getPos((PathfinderMob) mob, 2, 4);
                                                            if (vec3 == null) {
                                                                vec3 = DefaultRandomPos.getPos((PathfinderMob) mob, 2, 4);
                                                            }
                                                            if (vec3 == null) {
                                                                vec3 = DefaultRandomPos.getPos((PathfinderMob) mob, 2, 4);
                                                            }
                                                            if (vec3 != null) {
                                                                mob.moveTo(vec3);
                                                            }
                                                        }
                                                        stack.getSource().getLevel().addFreshEntity(mob);
                                                    } else throw NEW_ZOMBIE_INVALID_POSITION.create();
                                                }
                                            }
                                            return 0;
                                        }).then(
                                                Commands.argument("count", IntegerArgumentType.integer(1, 512)).executes(
                                                        (stack) -> {
                                                            ZombieLike zombieLike = UninfectedZoneRegistry.ZOMBIE_LIKES.get().getValue(stack.getArgument("zombieLike", ResourceLocation.class));
                                                            EntityType<?> mobType = ForgeRegistries.ENTITIES.getValue(stack.getArgument("entityType", ResourceLocation.class));
                                                            int count = stack.getArgument("count", Integer.class);
                                                            int r = (int) (1d + Math.ceil(Math.sqrt(count) / 2d));
                                                            try {
                                                                Mob mob = null;
                                                                for (int i = 1; i <= count; i++) {
                                                                    if (zombieLike != null && mobType != null) {
                                                                        mob = zombieLike.newZombie(mobType, stack.getSource().getLevel());
                                                                        if (mob != null) {
                                                                            if (Level.isInSpawnableBounds(new BlockPos(stack.getSource().getPosition()))) {
                                                                                mob.moveTo(stack.getSource().getPosition());
                                                                                if (mob instanceof PathfinderMob) {
                                                                                    Vec3 vec3 = DefaultRandomPos.getPos((PathfinderMob) mob, r, 4);
                                                                                    if (vec3 == null) {
                                                                                        vec3 = DefaultRandomPos.getPos((PathfinderMob) mob, r, 4);
                                                                                    }
                                                                                    if (vec3 == null) {
                                                                                        vec3 = DefaultRandomPos.getPos((PathfinderMob) mob, r, 4);
                                                                                    }
                                                                                    if (vec3 != null) {
                                                                                        mob.moveTo(vec3);
                                                                                    }
                                                                                    stack.getSource().getLevel().addFreshEntity(mob);
                                                                                }
                                                                            } else throw NEW_ZOMBIE_INVALID_POSITION.create();
                                                                        }
                                                                    }
                                                                }
                                                            } catch (IllegalArgumentException e) {
                                                                stack.getSource().sendFailure(ComponentUtils.literature(e.getMessage()));
                                                            }
                                                            return 0;
                                                        }
                                                )
                                        )
                        ))
        );
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
