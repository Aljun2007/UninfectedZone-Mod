package com.aljun.uninfectedzone.core.network;

import com.aljun.uninfectedzone.UninfectedZone;
import com.aljun.uninfectedzone.core.client.debug.ClientDebug;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ChunkBorderCommandNetworking {
    public static final String NAME = "chunk_border_command_networking";
    public static final String VERSION = "1.0";
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(UninfectedZone.MOD_ID, NAME), () -> VERSION,
                (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
        INSTANCE.messageBuilder(PackBoolean.class, nextID()).encoder(PackBoolean::toBytes).decoder(
                PackBoolean::new).consumer(PackBoolean::handler).add();
    }

    private static int nextID() {
        return ID++;
    }

    public static PackBoolean createPack(boolean message) {
        return new PackBoolean(message);
    }

    public static class PackBoolean {
        private final boolean isChunkBordenDisplay;

        public PackBoolean(boolean isChunkBordenDisplay) {
            this.isChunkBordenDisplay = isChunkBordenDisplay;
        }

        private PackBoolean(@NotNull FriendlyByteBuf buffer) {
            this.isChunkBordenDisplay = buffer.readBoolean();
        }

        private static void handler(PackBoolean pack, @NotNull Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(pack::receive);
            ctx.get().setPacketHandled(true);
        }

        // When receive Pack

        private void receive() {
            try {
                this.modify();
            } catch (ClassNotFoundException ignore) {
            }
        }

        private void modify() throws ClassNotFoundException {
            ClientDebug.isChunkBordenDisplay = this.isChunkBordenDisplay;
        }

        private void toBytes(@NotNull FriendlyByteBuf buf) {
            buf.writeBoolean(this.isChunkBordenDisplay);
        }
    }
}
