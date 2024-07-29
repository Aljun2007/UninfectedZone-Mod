package com.aljun.uninfectedzone.core.network;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.client.ClientConfigUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ConfigServerToClientNetworking {
    public static final String NAME = "config_server_to_client_networking";
    public static final String VERSION = "1.0";
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(UninfectedZone.MOD_ID, NAME), () -> VERSION,
                (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
        INSTANCE.messageBuilder(PackJson.class, nextID()).encoder(PackJson::toBytes).decoder(
                PackJson::new).consumer(PackJson::handler).add();
    }

    private static int nextID() {
        return ID++;
    }

    public static PackJson createPack(JsonObject message) {
        return new PackJson(message);
    }

    public static class PackJson {

        private static final Gson GSON = new Gson();
        private final JsonObject configJson;

        public PackJson(JsonObject configJson) {
            this.configJson = configJson;
        }

        private PackJson(@NotNull FriendlyByteBuf buffer) {
            this.configJson = GSON.fromJson(buffer.readUtf(), JsonObject.class);
        }

        private static void handler(PackJson pack, @NotNull Supplier<NetworkEvent.Context> context) {
            context.get().enqueueWork(() -> {
                if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    pack.serverReceive();
                } else if (context.get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    pack.clientReceive();
                }
            });
            context.get().setPacketHandled(true);
        }

        private void serverReceive() {

        }

        private void clientReceive() {
            if (this.configJson == null) return;
            if (this.configJson.isJsonNull()) return;
            try {
                ClientConfigUtils.receive(this.configJson);
            } catch (NoClassDefFoundError ignore) {
            }
        }

        private void toBytes(@NotNull FriendlyByteBuf buf) {
            buf.writeUtf(GSON.toJson(this.configJson));
        }
    }
}
