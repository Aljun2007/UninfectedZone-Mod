package com.aljun.uninfectedzone.core.network;

import com.aljun.uninfectedzone.UninfectedZone;
import com.google.gson.Gson;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class UninfectedChuckNetworking {
    public static final String NAME = "uninfected_chuck_networking";
    public static final String VERSION = "1.0";
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(UninfectedZone.MOD_ID, NAME), () -> VERSION,
                (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
        INSTANCE.messageBuilder(PackHashMap.class, nextID()).encoder(PackHashMap::toBytes).decoder(
                PackHashMap::new).consumer(PackHashMap::handler).add();
    }

    private static int nextID() {
        return ID++;
    }

    public static PackHashMap createPack(List<ChunkPos> internalZone, List<ChunkPos> boundaryZone) {
        return new PackHashMap(internalZone, boundaryZone);
    }

    public static class PackHashMap {

        private static final Gson GSON = new Gson();


        private final Map<Integer, ChunkPos> internalZone;
        private final Map<Integer, ChunkPos> boundaryZone;

        public PackHashMap(List<ChunkPos> internalZone, List<ChunkPos> boundaryZone) {
            this.internalZone = new HashMap<>();
            this.boundaryZone = new HashMap<>();

            internalZone.forEach(chuckPos -> {
                this.internalZone.put(this.internalZone.size(), chuckPos);
            });
            boundaryZone.forEach(chuckPos -> {
                this.boundaryZone.put(this.boundaryZone.size(), chuckPos);
            });
        }

        private PackHashMap(@NotNull FriendlyByteBuf buffer) {
            this.internalZone = buffer.readMap(FriendlyByteBuf::readInt, FriendlyByteBuf::readChunkPos);
            this.boundaryZone = buffer.readMap(FriendlyByteBuf::readInt, FriendlyByteBuf::readChunkPos);
        }

        private static void handler(PackHashMap pack, @NotNull Supplier<NetworkEvent.Context> context) {
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

        }

        private void toBytes(@NotNull FriendlyByteBuf buf) {
            buf.writeMap(this.internalZone, FriendlyByteBuf::writeInt, FriendlyByteBuf::writeChunkPos);
            buf.writeMap(this.boundaryZone, FriendlyByteBuf::writeInt, FriendlyByteBuf::writeChunkPos);
        }
    }
}
