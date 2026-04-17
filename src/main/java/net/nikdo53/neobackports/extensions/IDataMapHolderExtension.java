package net.nikdo53.neobackports.extensions;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

public interface IDataMapHolderExtension<T> {
    default  <A> @Nullable A getData(DataMapType<T, A> type){
        throw new IllegalStateException("not implemented");
    }

    default String getRegisteredName() {
        throw new IllegalStateException("not implemented");
    }

    @Nullable
    default ResourceKey<T> getKey() {
        throw new IllegalStateException("not implemented");
    }
   default boolean is(Holder<T> holder){
        throw new IllegalStateException("not implemented");
   }
}
