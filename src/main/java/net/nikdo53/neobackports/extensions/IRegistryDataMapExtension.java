package net.nikdo53.neobackports.extensions;

import net.minecraft.resources.ResourceKey;
import net.nikdo53.neobackports.registry.datamaps.DataMapType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IRegistryDataMapExtension<T> extends IDataMapLookupExtension<T>{
    default Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps(){
        throw new IllegalStateException("not implemented");
    }

    @ApiStatus.Internal
    default void setDataMaps(Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> maps){
        throw new IllegalStateException("not implemented");
    }

    default <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> type){
        throw new IllegalStateException("not implemented");
    }
}
