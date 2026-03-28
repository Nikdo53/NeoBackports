package net.nikdo53.neobackports.extensions;

import net.minecraft.resources.ResourceKey;
import net.nikdo53.neobackports.registry.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface IDataMapHolderExtension<T> {
    default  <A> @Nullable A getData(DataMapType<T, A> type){
        throw new IllegalStateException("not implemented");
    }
}
