package com.aljun.uninfectedzone.core.network;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ConfigEnquiringNetworking {
    public static final String NAME = "config_enquiring_networking";
    public static final String VERSION = "1.0";
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(UninfectedZone.MOD_ID, NAME), () -> VERSION,
                version -> version.equals(VERSION), version -> version.equals(VERSION));
        INSTANCE.messageBuilder(PackVoid.class, nextID()).encoder(PackVoid::toBytes).decoder(
                PackVoid::new).consumer(PackVoid::handler).add();
    }

    private static int nextID() {
        return ID++;
    }

    public static PackVoid createPack() {
        return new PackVoid();
    }

    public static class PackVoid {


        public PackVoid() {
        }

        private PackVoid(@NotNull FriendlyByteBuf buffer) {

        }

        private static void handler(PackVoid pack, @NotNull Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    pack.serverReceive(context.get().getSender());
                } else if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    pack.clientReceive();
                }
            });
            context.get().setPacketHandled(true);
        }

        private void serverReceive(ServerPlayer player) {
            UninfectedZoneConfig.sendToClient(player);
        }

        private void clientReceive() {

        }

        private void toBytes(@NotNull FriendlyByteBuf buf) {
        }
    }
}
