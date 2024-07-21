package com.aljun.uninfectedzone.core.command.debug;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.debug.Debug;
import com.aljun.uninfectedzone.core.network.ChunkBorderCommandNetworking;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.TagUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.PacketDistributor;

public class ModDebugCommand implements Command<CommandSourceStack> {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> debugCommand = Commands.literal("debug").requires(context -> UninfectedZone.debugMode);
        load(debugCommand);
        root.then(debugCommand);
    }

    private static void load(LiteralArgumentBuilder<CommandSourceStack> command) {
        command.then(Commands.literal("debug_items").executes((context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    debugItems(player);
                    return 0;
                }))
        ).then(Commands.literal("isChunkBordenDisplay").then(
                Commands.argument("boolean", BoolArgumentType.bool()).executes(
                        (context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ChunkBorderCommandNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                                    ChunkBorderCommandNetworking.createPack(context.getArgument("boolean", Boolean.class)));
                            return 0;
                        })
                )));
    }

    private static void debugItems(ServerPlayer player) {
        ItemStack killer = new ItemStack(Items.BLAZE_ROD);
        killer.setHoverName(ComponentUtils.literature("KillerItem"));
        TagUtils.fastWrite(TagUtils.getRootOrCreate(killer), Debug.KILLER_ITEM, true);
        player.addItem(killer);

        ItemStack snatcher = new ItemStack(Items.TROPICAL_FISH);
        snatcher.setHoverName(ComponentUtils.literature("SnatcherItem"));
        TagUtils.fastWrite(TagUtils.getRootOrCreate(snatcher), Debug.SNATCHER_ITEM, true);
        player.addItem(snatcher);

        ItemStack heal = new ItemStack(Items.RED_DYE);
        heal.setHoverName(ComponentUtils.literature("HealItem"));
        TagUtils.fastWrite(TagUtils.getRootOrCreate(heal), Debug.HEAL_ITEM, true);
        player.addItem(heal);

        ItemStack day = new ItemStack(Items.GOLD_NUGGET);
        day.setHoverName(ComponentUtils.literature("DayItem"));
        TagUtils.fastWrite(TagUtils.getRootOrCreate(day), Debug.DAY_ITEM, true);
        player.addItem(day);

        ItemStack night = new ItemStack(Items.COAL);
        night.setHoverName(ComponentUtils.literature("NightItem"));
        TagUtils.fastWrite(TagUtils.getRootOrCreate(night), Debug.NIGHT_ITEM, true);
        player.addItem(night);

    }

    @Override
    public int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return 0;
    }
}
