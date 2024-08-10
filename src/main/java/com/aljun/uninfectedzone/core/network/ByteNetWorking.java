package com.aljun.uninfectedzone.core.network;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.client.ClientConfigUtils;
import com.aljun.uninfectedzone.core.client.gui.config.ConfigSetScreen;
import com.aljun.uninfectedzone.core.config.UninfectedZoneConfig;
import com.aljun.uninfectedzone.core.debug.Debug;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.resource.ResourcePackLoader;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ByteNetWorking {
    public static final String NAME = "byte2024_networking";
    public static final String VERSION = "1.0";
    public static final byte RELOAD_ALL_CONFIG = 0;
    private static final byte TEST_SCREEN_1 = 1;
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(UninfectedZone.MOD_ID, NAME), () -> VERSION,
                (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
        INSTANCE.messageBuilder(PackByte.class, nextID()).encoder(PackByte::toBytes).decoder(
                PackByte::new).consumer(PackByte::handler).add();
    }

    private static int nextID() {
        return ID++;
    }

    public static PackByte createPack(byte message) {
        return new PackByte(message);
    }

    public static class PackByte {

        private final byte testByte;

        public PackByte(byte testByte) {
            this.testByte = testByte;
        }

        private PackByte(@NotNull FriendlyByteBuf buffer) {
            this.testByte = buffer.readByte();
        }

        private static void handler(PackByte pack, @NotNull Supplier<NetworkEvent.Context> context) {
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
            if (this.testByte == RELOAD_ALL_CONFIG) {
                UninfectedZoneConfig.loadGlobal();
            }
        }

        private void clientReceive() {
            if (this.testByte == RELOAD_ALL_CONFIG) {
                ClientConfigUtils.loadGlobalClient();
            } else if (this.testByte == TEST_SCREEN_1) {
                Minecraft.getInstance().setScreen(new ConfigSetScreen());
            } else if (this.testByte == 2) {
                Debug.LOGGER.info(ResourcePackLoader.getPackNames().toString());
            }
        }


        private void toBytes(@NotNull FriendlyByteBuf buf) {
            buf.writeByte(this.testByte);
        }
    }
}
