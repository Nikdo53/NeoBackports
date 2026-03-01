package net.nikdo53.neobackports.io.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.nikdo53.neobackports.io.StreamCodec;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class StreamNetworkingUtils {

    public static <MSG2> void registerMessage(
            SimpleChannel channel, int index, Class<MSG2> messageType,
            StreamCodec<MSG2> codec,
            BiConsumer<MSG2, Supplier<NetworkEvent.Context>> messageConsumer
    ) {
        channel.registerMessage(index, messageType, codec::encode, codec::decode, messageConsumer);
    }

    public static <MSG3> void registerMessageNoSup(
            SimpleChannel channel, int index, Class<MSG3> messageType,
            StreamCodec<MSG3> codec,
            BiConsumer<MSG3, NetworkEvent.Context> messageConsumer
    ) {
        channel.registerMessage(index, messageType, codec::encode, codec::decode, (msg, ctx) -> messageConsumer.accept(msg, ctx.get()));
    }


    public static void sendToAllPlayers(SimpleChannel channel, Object message){
        channel.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToServer(SimpleChannel channel,Object message){
        channel.sendToServer(message);
    }
    public static void sendToPlayer(SimpleChannel channel,Player player, Object message){
        if (!(player instanceof ServerPlayer serverPlayer))  throw new IllegalArgumentException("player is not a server player");

        channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
    }

}
