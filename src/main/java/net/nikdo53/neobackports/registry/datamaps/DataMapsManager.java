package net.nikdo53.neobackports.registry.datamaps;

import com.mojang.logging.LogUtils;
import io.netty.util.AttributeKey;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.nikdo53.neobackports.io.networking.KnownRegistryDataMapsReplyPayload;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DataMapsManager {
    private static Map<ResourceKey<Registry<?>>, Map<ResourceLocation, DataMapType<?, ?>>> dataMaps = Map.of();

    @Nullable
    public static <R> DataMapType<R, ?> getDataMap(ResourceKey<? extends Registry<R>> registry, ResourceLocation key) {
        Map<ResourceKey<Registry<?>>, Map<ResourceLocation, DataMapType<?, ?>>> dataMaps1 = dataMaps;
        final var map = dataMaps1.get(registry);
        return map == null ? null : (DataMapType<R, ?>) map.get(key);
    }

    /**
     * {@return a view of all registered data maps}
     */
    public static Map<ResourceKey<Registry<?>>, Map<ResourceLocation, DataMapType<?, ?>>> getDataMaps() {
        return dataMaps;
    }

    public static void initDataMaps() {
        final Map<ResourceKey<Registry<?>>, Map<ResourceLocation, DataMapType<?, ?>>> dataMapTypes = new HashMap<>();
        FMLJavaModLoadingContext.get().getModEventBus().post(new RegisterDataMapTypesEvent(dataMapTypes));

        dataMaps = new IdentityHashMap<>();
        dataMapTypes.forEach((key, values) -> dataMaps.put(key, Collections.unmodifiableMap(values)));
        dataMaps = Collections.unmodifiableMap(dataMapTypes);
    }

    public static final AttributeKey<Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>>> ATTRIBUTE_KNOWN_DATA_MAPS = AttributeKey.valueOf("neoforge:known_data_maps");

}
