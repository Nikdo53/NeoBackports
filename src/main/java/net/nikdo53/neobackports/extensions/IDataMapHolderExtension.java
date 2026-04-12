package net.nikdo53.neobackports.extensions;

import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

public interface IDataMapHolderExtension<T> {
    default  <A> @Nullable A getData(DataMapType<T, A> type){
        throw new IllegalStateException("not implemented");
    }

    default String getRegisteredName() {
        throw new IllegalStateException("not implemented");
    }
}
