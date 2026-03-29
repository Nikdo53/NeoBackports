package net.nikdo53.neobackports.io.networking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.StreamNetworkingUtils;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NBNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            NeoBackports.loc("channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    static int id = 0;

    public static void init() {
        id = 0;

        register(
                SyncAttachmentPayload.class,
                SyncAttachmentPayload.STREAM_CODEC,
                SyncAttachmentPayload::handle
        );

        register(
                RegistryDataMapSyncPayload.class,
                RegistryDataMapSyncPayload.STREAM_CODEC,
                RegistryDataMapSyncPayload::handle
        );


        CHANNEL.messageBuilder(KnownRegistryDataMapsReplyPayload.class, id++, NetworkDirection.PLAY_TO_SERVER).
               // loginIndex(KnownRegistryDataMapsReplyPayload::getLoginIndex, KnownRegistryDataMapsReplyPayload::setLoginIndex).
                decoder(KnownRegistryDataMapsReplyPayload.STREAM_CODEC::decode).
                encoder(KnownRegistryDataMapsReplyPayload.STREAM_CODEC::encode).
                consumerNetworkThread(KnownRegistryDataMapsReplyPayload::handle).
                add();



    }

    private static <T> void register(Class<T> msg, StreamCodec<T> streamCodec, BiConsumer<T, Supplier<NetworkEvent.Context>> consumer){
        StreamNetworkingUtils.registerMessage(CHANNEL, id++,
                msg,
                streamCodec,
                consumer
        );

    }

    public static void sendToAllPlayers(Object message){
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToServer(Object message){
        CHANNEL.sendToServer(message);
    }
    public static void sendToPlayer(Player player, Object message){
        if (!(player instanceof ServerPlayer serverPlayer))  throw new IllegalArgumentException("player is not a server player");

        CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
    }
}
