package com.aljun.uninfectedzone.core.command.debug;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.debug.Debug;
import com.aljun.uninfectedzone.core.network.ByteNetWorking;
import com.aljun.uninfectedzone.core.network.ChunkBorderCommandNetworking;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.TagUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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
        }))).then(Commands.literal("isChunkBordenDisplay").then(Commands.argument("boolean", BoolArgumentType.bool()).executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            ChunkBorderCommandNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), ChunkBorderCommandNetworking.createPack(context.getArgument("boolean", Boolean.class)));
            return 0;
        }))).then(Commands.literal("info").then(Commands.argument("id", StringArgumentType.string()).executes(context -> {
            Debug.info(context);
            return 0;
        }))).then(Commands.literal("reloadConfig").then(Commands.literal("client").executes((context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            ByteNetWorking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), ByteNetWorking.createPack(ByteNetWorking.RELOAD_ALL_CONFIG));
            return 0;
        }))).then(Commands.literal("server").executes(context -> {
            UninfectedZoneConfig.reloadWorld(context.getSource().getServer());
            return 0;
        })).then(Commands.literal("all").executes((context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            UninfectedZoneConfig.reloadWorld(context.getSource().getServer());
            ByteNetWorking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), ByteNetWorking.createPack(ByteNetWorking.RELOAD_ALL_CONFIG));
            return 0;
        })))).then(Commands.literal("test_client").then(Commands.argument("byte", IntegerArgumentType.integer(-128, 127)).executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            ByteNetWorking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), ByteNetWorking.createPack((byte) (int) context.getArgument("byte", Integer.class)));
            return 0;
        })));
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

        ItemStack test = new ItemStack(Items.STICK);
        test.setHoverName(ComponentUtils.literature("TestItem"));
        TagUtils.fastWrite(TagUtils.getRootOrCreate(test), Debug.TEST, true);
        player.addItem(test);

    }

    @Override
    public int run(CommandContext<CommandSourceStack> commandContext) {
        return 0;
    }
}
