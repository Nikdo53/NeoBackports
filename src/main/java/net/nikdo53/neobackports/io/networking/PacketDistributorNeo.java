package net.nikdo53.neobackports.io.networking;

import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.nikdo53.neobackports.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PacketDistributorNeo {
    /**
     * Send the given payload(s) to the server
     */
    public static void sendToServer(CustomPacketPayload payload, CustomPacketPayload... payloads) {
        Preconditions.checkState(FMLEnvironment.dist.isClient(), "Cannot send serverbound payloads on the server");

        String namespace = payload.type().id().getNamespace();
        SimpleChannel simpleChannel = getChannel(namespace);
        simpleChannel.sendToServer(payload);

        for (CustomPacketPayload otherPayload : payloads) {
            if (otherPayload.type().id().getNamespace().equals(namespace)) {
                simpleChannel.sendToServer(otherPayload);
            } else {
                throw new IllegalArgumentException("All payloads must be of the same namespace");
            }
        }
    }

    private static @NotNull SimpleChannel getChannel(String namespace) {
        SimpleChannel simpleChannel = RegisterPayloadHandlersEvent.REGISTERED_CHANNELS.get(namespace);
        if (simpleChannel == null) {
            throw new IllegalStateException("To use the neo packet distributor, use the event to create and register your channel");
        }
        return simpleChannel;
    }

    /**
     * Send the given payload(s) to the given player
     */
    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        String namespace = payload.type().id().getNamespace();
        SimpleChannel channel = getChannel(namespace);

        channel.send(PacketDistributor.PLAYER.with(() -> player), payload);

        for (CustomPacketPayload otherPayload : payloads) {
            if (otherPayload.type().id().getNamespace().equals(namespace)) {
                channel.send(PacketDistributor.PLAYER.with(() -> player), payload);
            } else {
                throw new IllegalArgumentException("All payloads must be of the same namespace");
            }
        }
    }

    /**
     * Send the given payload(s) to all players in the given dimension
     */
    public static void sendToPlayersInDimension(ServerLevel level, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        String namespace = payload.type().id().getNamespace();
        SimpleChannel channel = getChannel(namespace);

        channel.send(PacketDistributor.DIMENSION.with(level::dimension), payload);

        for (CustomPacketPayload otherPayload : payloads) {
            if (otherPayload.type().id().getNamespace().equals(namespace)) {
                channel.send(PacketDistributor.DIMENSION.with(level::dimension), payload);
            } else {
                throw new IllegalArgumentException("All payloads must be of the same namespace");
            }
        }
    }

    /**
     * Send the given payload(s) to all players in the area covered by the given radius around the given coordinates
     * in the given dimension, except the given excluded player if present
     */
    public static void sendToPlayersNear(
            ServerLevel level,
            @Nullable ServerPlayer excluded,
            double x,
            double y,
            double z,
            double radius,
            CustomPacketPayload payload,
            CustomPacketPayload... payloads) {

        String namespace = payload.type().id().getNamespace();
        SimpleChannel channel = getChannel(namespace);

        channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(excluded, x, y, z, radius, level.dimension())), payload);

        for (CustomPacketPayload otherPayload : payloads) {
            if (otherPayload.type().id().getNamespace().equals(namespace)) {
                channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(excluded, x, y, z, radius, level.dimension())), payload);
            } else {
                throw new IllegalArgumentException("All payloads must be of the same namespace");
            }
        }
    }

    /**
     * Send the given payload(s) to all players on the server
     */
    public static void sendToAllPlayers(CustomPacketPayload payload, CustomPacketPayload... payloads) {
        String namespace = payload.type().id().getNamespace();
        SimpleChannel channel = getChannel(namespace);

        channel.send(PacketDistributor.ALL.noArg(), payload);

        for (CustomPacketPayload otherPayload : payloads) {
            if (otherPayload.type().id().getNamespace().equals(namespace)) {
                channel.send(PacketDistributor.ALL.noArg(), payload);
            } else {
                throw new IllegalArgumentException("All payloads must be of the same namespace");
            }
        }
    }

    /**
     * Send the given payload(s) to all players tracking the given entity
     */
    public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        if (entity.level().isClientSide()) {
            throw new IllegalStateException("Cannot send clientbound payloads on the client");
        } else if (entity.level().getChunkSource() instanceof ServerChunkCache chunkCache) {

            String namespace = payload.type().id().getNamespace();
            SimpleChannel channel = getChannel(namespace);

            channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), payload);

            for (CustomPacketPayload otherPayload : payloads) {
                if (otherPayload.type().id().getNamespace().equals(namespace)) {
                    channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), payload);
                } else {
                    throw new IllegalArgumentException("All payloads must be of the same namespace");
                }
            }
        }
    }

    /**
     * Send the given payload(s) to all players tracking the given entity and the entity itself if it is a player
     */
    public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        if (entity.level().isClientSide()) {
            throw new IllegalStateException("Cannot send clientbound payloads on the client");
        } else if (entity.level().getChunkSource() instanceof ServerChunkCache chunkCache) {

            String namespace = payload.type().id().getNamespace();
            SimpleChannel channel = getChannel(namespace);

            channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), payload);

            for (CustomPacketPayload otherPayload : payloads) {
                if (otherPayload.type().id().getNamespace().equals(namespace)) {
                    channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), payload);
                } else {
                    throw new IllegalArgumentException("All payloads must be of the same namespace");
                }
            }
        }
        // Silently ignore custom Level implementations which may not return ServerChunkCache.
    }

    /**
     * Send the given payload(s) to all players tracking the chunk at the given position in the given level
     */
    public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        String namespace = payload.type().id().getNamespace();
        SimpleChannel channel = getChannel(namespace);

        LevelChunk chunk = level.getChunkAt(chunkPos.getWorldPosition());

        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), payload);

        for (CustomPacketPayload otherPayload : payloads) {
            if (otherPayload.type().id().getNamespace().equals(namespace)) {
                channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), payload);
            } else {
                throw new IllegalArgumentException("All payloads must be of the same namespace");
            }
        }    }
}
