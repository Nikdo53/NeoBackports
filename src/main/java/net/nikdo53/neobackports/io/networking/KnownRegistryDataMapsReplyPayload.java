package net.nikdo53.neobackports.io.networking;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.NetworkEvent;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;
import net.nikdo53.neobackports.registry.datamaps.DataMapsManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

@ApiStatus.Internal
public class KnownRegistryDataMapsReplyPayload implements IntSupplier {
    Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>> dataMaps;

    public static final StreamCodec<KnownRegistryDataMapsReplyPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(Maps::newHashMapWithExpectedSize, ByteBufCodecs.registryKey(), ByteBufCodecs.RESOURCE_LOCATION.apply(ByteBufCodecs.collection(ArrayList::new))),
                    load -> load.dataMaps,
                    KnownRegistryDataMapsReplyPayload::new
            );

    public KnownRegistryDataMapsReplyPayload(Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>> dataMaps) {
        this.dataMaps = dataMaps;
    }

    private int loginIndex;

    void setLoginIndex(final int loginIndex) {
        this.loginIndex = loginIndex;
    }

    int getLoginIndex() {
        return loginIndex;
    }

    @Override
    public int getAsInt() {
        return getLoginIndex();
    }


    static void handle(KnownRegistryDataMapsReplyPayload payload, Supplier<NetworkEvent.Context> context){
        context.get().attr(DataMapsManager.ATTRIBUTE_KNOWN_DATA_MAPS).set(payload.dataMaps);
        context.get().setPacketHandled(true);
    }
}
