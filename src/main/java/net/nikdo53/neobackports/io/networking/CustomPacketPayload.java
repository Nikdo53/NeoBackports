package net.nikdo53.neobackports.io.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface CustomPacketPayload {
    default void handle(Supplier<NetworkEvent.Context> ctx){
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> handle(new IPayloadContext(context)));
    }

    void handle(IPayloadContext context);

    record Type<T extends CustomPacketPayload>(ResourceLocation id, Class<T> type) { }

    /**
     * Unlike in neoforge, this is only used to get the correct channel to be used in {@link PacketDistributorNeo},
     * meaning only the modid is needed and that it can safely be null if you aren't using that class
     */
    @Nullable Type<? extends CustomPacketPayload> type();

}
