package net.nikdo53.neobackports.io.networking;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface ToServerPacket extends  CustomPacketPayload {
    @Override
    default void handle(Supplier<NetworkEvent.Context> ctx){
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> handleServer(context, context.getSender(), context.getSender().serverLevel()));

        context.setPacketHandled(true);
    };

    void handleServer(NetworkEvent.Context context, ServerPlayer player, ServerLevel level);
}
