package net.nikdo53.neobackports.mixin;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistry;
import net.nikdo53.neobackports.extensions.IRegistryDataMapExtension;
import net.nikdo53.neobackports.registry.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> implements IRegistryDataMapExtension<T> {
    @Unique
    Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> neoBackports$dataMaps = new HashMap<>();

    @Override
    public Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps() {
        return neoBackports$dataMaps;
    }

    @Override
    public void setDataMaps(Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> maps) {
        neoBackports$dataMaps = maps;
    }

    @Override
    public <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> type) {
        return (Map<ResourceKey<T>, A>) neoBackports$dataMaps.getOrDefault(type, Map.of());
    }

    @Override
    public @Nullable <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        final var innerMap = neoBackports$dataMaps.get(type);
        return innerMap == null ? null : (A) innerMap.get(key);
    }

    @Injec
    private static void adasd(){

    }
}
