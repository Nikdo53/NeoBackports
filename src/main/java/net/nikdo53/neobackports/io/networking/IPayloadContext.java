package net.nikdo53.neobackports.io.networking;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.CompletableFuture;

public record IPayloadContext(NetworkEvent.Context original) {

    /**
     * Retrieves the packet listener associated with this original.
     */
    public PacketListener listener(){
       return original.getNetworkManager().getPacketListener();
    }

    /**
     * {@return the connection}
     */
    public Connection connection() {
        return original.getNetworkManager();
    }

    /**
     * Retrieves the player relevant to this payload. Players are only available in the {@link ConnectionProtocol#PLAY} phase.
     * <p>
     * For server-bound payloads, retrieves the sending {@link ServerPlayer}.
     * <p>
     * For client-bound payloads, retrieves the receiving {@link LocalPlayer}.
     *
     * @throws UnsupportedOperationException when called during the configuration phase.
     */
    public Player player() {
        ServerPlayer sender = original.getSender();
        if (sender != null) return sender;

        return ClientPlayerGetter.getPlayer();
    }

    @OnlyIn(Dist.CLIENT)
    public static class ClientPlayerGetter {
        public static Player getPlayer() {
            return Minecraft.getInstance().player;
        }
    }

/*
    */
/**
     * Sends the given payload back to the sender.
     *
     * @param payload The payload to send.
     *//*

    public void reply(CustomPacketPayload payload) {
        throw new UnsupportedOperationException("Replying to a payload is not supported yet");
       // connection().send(Packet);
    }
*/

    /**
     * Disconnects the player from the network.
     */
    public void disconnect(Component reason) {
        this.listener().onDisconnect(reason);
    }

    /**
     * For handlers running on the network thread, submits the given task to be run on the main thread of the game.
     * <p>
     * For handlers running on the main thread, immediately executes the task.
     * <p>
     * On the network thread, the future will be automatically guarded against exceptions using {@link CompletableFuture#exceptionally}.
     * If you need to catch your own exceptions, use a try/catch block within your task.
     *
     * @param task The task to run.
     */
    public CompletableFuture<Void> enqueueWork(Runnable task){
        return original().enqueueWork(task);
    };

    /**
     * {@return the flow of the received payload}
     */
    public NetworkDirection flow(){
        return original.getDirection();
    };

/*

    public ConnectionProtocol protocol() {
        return ;
    }

    public void handle(CustomPacketPayload payload){
        original.getPacketDispatcher().sendPacket(new ResourceLocation(payload.type().id(), "channel"), b);
    }

    public void finishCurrentTask(ConfigurationTask.Type type){

    }
*/

    /**
     * {@return the channel handler original}
     */
    public ChannelHandlerContext channelHandlerContext() {
        return connection().channel().pipeline().lastContext();
    }

}
