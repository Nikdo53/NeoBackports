package net.nikdo53.neobackports.extensions;

import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

public interface IWithData<R> {
    /// {@return the data of the given type that is attached to this object, or `null` if one isn't}
    ///
    /// @param type the data type
    /// @param <T>  the type of the data
    @Nullable
    default <T> T getData(DataMapType<R, T> type) {
        return null;
    }

}
