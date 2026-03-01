package net.nikdo53.neobackports.io.networking;

import com.google.common.collect.Maps;
import com.mojang.datafixers.types.Type;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.io.utils.ByteBufCodecs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public record KnownRegistryDataMapsReplyPayload(Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>> dataMaps) implements ToServerPacket {
    public static final StreamCodec<KnownRegistryDataMapsReplyPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    Maps::newHashMapWithExpectedSize,
                    ByteBufCodecs.RESOURCE_LOCATION.remap(ResourceKey::createRegistryKey, ResourceKey::location),
                    ByteBufCodecs.RESOURCE_LOCATION.apply(ByteBufCodecs.collection(ArrayList::new))),
            KnownRegistryDataMapsReplyPayload::dataMaps,
            KnownRegistryDataMapsReplyPayload::new);


    @Override
    public void handleServer(NetworkEvent.Context context, ServerPlayer player, ServerLevel level) {
       // context.channelHandlerContext().attr(ATTRIBUTE_KNOWN_DATA_MAPS).set(payload.dataMaps());
    }
}
