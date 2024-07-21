package com.aljun.uninfectedzone.core.command;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.command.debug.ModDebugCommand;
import com.aljun.uninfectedzone.core.command.zombie.ZombieCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class UninfectedZoneCommands {


    public static LiteralArgumentBuilder<CommandSourceStack> ROOT = Commands.literal(UninfectedZone.MOD_ID);

    public static LiteralCommandNode<CommandSourceStack> registry(CommandDispatcher<CommandSourceStack> dispatcher) {
        ZombieCommand.register(ROOT);
        ModDebugCommand.register(ROOT);
        return dispatcher.register(ROOT);
    }
}
