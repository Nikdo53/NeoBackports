package net.nikdo53.neobackports.io.networking;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public interface ToServerPacket extends CustomPacketPayload {
    @Override
    default void handle(IPayloadContext context){
        context.enqueueWork(() -> handleServer(context, context.original().getSender(), context.original().getSender().serverLevel()));

        context.original().setPacketHandled(true);
    };

    void handleServer(IPayloadContext context, ServerPlayer player, ServerLevel level);
}
