package net.nikdo53.neobackports.extensions;

import net.minecraft.resources.ResourceKey;
import net.nikdo53.neobackports.registry.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IRegistryDataMapExtension<T> extends IDataMapLookupExtension<T>{
    Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps();
    void setDataMaps(Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> maps);

    <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> type);
}
