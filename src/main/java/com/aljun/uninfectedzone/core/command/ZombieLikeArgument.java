package com.aljun.uninfectedzone.core.command;

import com.aljun.uninfectedzone.core.forgeRegister.UninfectedZoneRegistry;
import com.aljun.uninfectedzone.core.utils.ComponentUtils;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;

public class ZombieLikeArgument implements ArgumentType<ResourceLocation> {
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_ZOMBIE_LIKES = new DynamicCommandExceptionType((living) ->
            ComponentUtils.translate("uninfectedzone.zombieLike.notFound", String.valueOf(living))
    );
    public static final SuggestionProvider<CommandSourceStack> ZOMBIE_LIKES_ALL = SuggestionProviders.register(new ResourceLocation("zombie_like_all"),
            (p_212438_, p_212439_) -> SharedSuggestionProvider.suggestResource(UninfectedZoneRegistry.ZOMBIE_LIKES.get().getEntries().stream(), p_212439_, (a) -> a.getKey().location(), (v) -> ComponentUtils.translate(v.getValue().toString())));

    public static ZombieLikeArgument id() {
        return new ZombieLikeArgument();
    }

    public static ResourceLocation getZombieLike(CommandContext<CommandSourceStack> context, String key) throws CommandSyntaxException {
        return verifyCanSummon(context.getArgument(key, ResourceLocation.class));
    }

    private static ResourceLocation verifyCanSummon(ResourceLocation resourceLocation) throws CommandSyntaxException {
        if (UninfectedZoneRegistry.ZOMBIE_LIKES.get().containsKey(resourceLocation)) {
            return resourceLocation;
        }
        throw ERROR_UNKNOWN_ZOMBIE_LIKES.create(resourceLocation);
    }

    @Override
    public ResourceLocation parse(StringReader stringReader) throws CommandSyntaxException {
        return verifyCanSummon(ResourceLocation.read(stringReader));
    }


}
