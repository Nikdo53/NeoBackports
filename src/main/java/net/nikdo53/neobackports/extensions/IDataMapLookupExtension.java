package net.nikdo53.neobackports.extensions;

import net.minecraft.resources.ResourceKey;
import net.nikdo53.neobackports.registry.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

public interface IDataMapLookupExtension<T> {
    default  <A> @Nullable A getData(DataMapType<T, A> type, ResourceKey<T> key){
        throw new IllegalStateException("not implemented");
    }
}
