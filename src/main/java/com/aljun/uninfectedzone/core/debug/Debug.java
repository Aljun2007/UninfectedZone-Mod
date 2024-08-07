package com.aljun.uninfectedzone.core.debug;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.file.config.ConfigFileUtils;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.aljun.uninfectedzone.core.utils.VarSet;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class Debug {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final VarSet<Boolean> KILLER_ITEM = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).next("debug").defaultVar(false).create("killer");
    public static final VarSet<Boolean> SNATCHER_ITEM = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).next("debug").defaultVar(false).create("snatcher");
    public static final VarSet<Boolean> HEAL_ITEM = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).next("debug").defaultVar(false).create("heal");
    public static final VarSet<Boolean> DAY_ITEM = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).next("debug").defaultVar(false).create("day");
    public static final VarSet<Boolean> NIGHT_ITEM = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).next("debug").defaultVar(false).create("night");
    public static final VarSet<Boolean> TEST = VarSet.builder(UninfectedZone.MOD_ID, VarSet.VarType.BOOLEAN).next("debug").defaultVar(false).create("test");

    public static void info(CommandContext<CommandSourceStack> context) {
        String id = context.getArgument("id", String.class);
        if (id.equals("path")) {
            LOGGER.info("GamePath:{}\nWorldPath{}", ConfigFileUtils.gamePath(), ConfigFileUtils.worldPath(context.getSource().getServer()));
        }
    }

    public static void test(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            try {
                serverLevel.getServer().getPlayerList().broadcastMessage(ComponentUtils.literature("Testing..."), ChatType.CHAT, Util.NIL_UUID);

                serverLevel.getServer().getPlayerList().broadcastMessage(ComponentUtils.literature("Done!"), ChatType.CHAT, Util.NIL_UUID);
            } catch (Throwable throwable) {
                serverLevel.getServer().getPlayerList().broadcastMessage(ComponentUtils.literature("Failed!"), ChatType.CHAT, Util.NIL_UUID);
                serverLevel.getServer().getPlayerList().broadcastMessage(ComponentUtils.literature(throwable.toString()), ChatType.CHAT, Util.NIL_UUID);
            }
        }
    }
}
