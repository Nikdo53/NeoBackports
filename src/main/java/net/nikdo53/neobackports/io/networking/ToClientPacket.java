package net.nikdo53.neobackports.io.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ToClientPacket extends CustomPacketPayload{
    @Override
    default void handle(IPayloadContext context){
        context.enqueueWork(() -> handleClean(context));

        context.original().setPacketHandled(true);
    };

    @OnlyIn(Dist.CLIENT)
    private void handleClean(IPayloadContext context){
        handleClient(context,  Minecraft.getInstance().level,  Minecraft.getInstance().player);
    }

    void handleClient(IPayloadContext context, Level level, Player player);
}
