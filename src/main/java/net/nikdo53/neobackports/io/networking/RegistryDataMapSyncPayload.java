package net.nikdo53.neobackports.io.networking;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.nikdo53.neobackports.NeoBackports;
import net.nikdo53.neobackports.io.StreamCodec;
import net.nikdo53.neobackports.datamaps.DataMapType;
import net.nikdo53.neobackports.datamaps.DataMapsManager;
import net.nikdo53.neobackports.event.DataMapsUpdatedEvent;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@ApiStatus.Internal
@SuppressWarnings({ "unchecked", "rawtypes" })
public record RegistryDataMapSyncPayload<T>(ResourceKey<? extends Registry<T>> registryKey, Map<ResourceLocation, Map<ResourceKey<T>, ?>> dataMaps) implements ToClientPacket {

    public static final StreamCodec<RegistryDataMapSyncPayload> STREAM_CODEC = StreamCodec.ofMember(
            RegistryDataMapSyncPayload::write, RegistryDataMapSyncPayload::decode);

    public static <T> RegistryDataMapSyncPayload<T> decode(FriendlyByteBuf buf) {
        //noinspection RedundantCast javac complains about this cast
        final ResourceKey<Registry<T>> registryKey = (ResourceKey<Registry<T>>) (Object) readRegistryKey(buf);

        final Map<ResourceLocation, Map<ResourceKey<T>, ?>> attach = readMap(buf, FriendlyByteBuf::readResourceLocation, (b1, key) -> {
            final DataMapType<T, ?> dataMap = DataMapsManager.getDataMap(registryKey, key);
            return b1.readMap(bf -> bf.readResourceKey(registryKey), bf -> readJsonWithRegistryCodec(bf, dataMap.networkCodec()));
        });
        return new RegistryDataMapSyncPayload<>(registryKey, attach);
    }

    public static  <T> ResourceKey<? extends Registry<T>> readRegistryKey(FriendlyByteBuf buf) {
        ResourceLocation resourcelocation = buf.readResourceLocation();
        return ResourceKey.createRegistryKey(resourcelocation);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceKey(registryKey);
        writeMap(buf, dataMaps, FriendlyByteBuf::writeResourceLocation, (b1, key, attach) -> {
            final DataMapType<T, ?> dataMap = DataMapsManager.getDataMap(registryKey, key);

            b1.writeMap(attach, FriendlyByteBuf::writeResourceKey, (bf, value) -> writeJsonWithRegistryCodec(bf, (Codec) dataMap.networkCodec(), value));
        });
    }

    private static final Gson GSON = new Gson();

    private static <T> T readJsonWithRegistryCodec(FriendlyByteBuf buf, Codec<T> codec) {
        JsonElement jsonelement = GsonHelper.fromJson(GSON, buf.readUtf(), JsonElement.class);
        DataResult<T> dataresult = codec.parse(JsonOps.INSTANCE, jsonelement);
        return dataresult.getOrThrow(false, name -> new DecoderException("Failed to decode json: " + name));
    }

    private static <T> void writeJsonWithRegistryCodec(FriendlyByteBuf buf, Codec<T> codec, T value) {
        DataResult<JsonElement> dataresult = codec.encodeStart(JsonOps.INSTANCE, value);
        buf.writeUtf(GSON.toJson(dataresult.getOrThrow(false,message -> new EncoderException("Failed to encode: " + message + " " + value))));
    }

    public static  <K, V> Map<K, V> readMap(FriendlyByteBuf buf, Function<? super FriendlyByteBuf, K> keyReader, BiFunction<FriendlyByteBuf, K, V> valueReader) {
        final int size = buf.readVarInt();
        final Map<K, V> map = Maps.newHashMapWithExpectedSize(size);

        for (int i = 0; i < size; ++i) {
            final K k = keyReader.apply(buf);
            map.put(k, valueReader.apply(buf, k));
        }

        return map;
    }

    public static <K, V> void writeMap(FriendlyByteBuf buf, Map<K, V> map, BiConsumer<? super FriendlyByteBuf, K> keyWriter, TriConsumer<FriendlyByteBuf, K, V> valueWriter) {
        buf.writeVarInt(map.size());
        map.forEach((key, value) -> {
            keyWriter.accept(buf, key);
            valueWriter.accept(buf, key, value);
        });
    }

    @Override
    public void handleClient(IPayloadContext context, Level level, Player player) {
        context.enqueueWork(() -> {
            try {
                var regAccess = level.registryAccess();
                IForgeRegistry registry = RegistryManager.ACTIVE.getRegistry(registryKey());

                registry.getDataMaps().clear();

                this.dataMaps().forEach((attachKey, maps) -> registry.getDataMaps().put(DataMapsManager.getDataMap(this.registryKey(), attachKey), Collections.unmodifiableMap(maps)));
                MinecraftForge.EVENT_BUS.post(new DataMapsUpdatedEvent(regAccess, registry, DataMapsUpdatedEvent.UpdateCause.CLIENT_SYNC));
            } catch (Throwable t) {
                NeoBackports.LOGGER.error("Failed to handle registry data map sync: ", t);
                context.original().getNetworkManager().disconnect(Component.translatable("neoforge.network.data_maps.failed", this.registryKey().location().toString(), t.toString()));
            }
        });
    }

    public static final Type<RegistryDataMapSyncPayload> TYPE = new Type<>(NeoBackports.loc("registry_data_map_sync"), RegistryDataMapSyncPayload.class);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
