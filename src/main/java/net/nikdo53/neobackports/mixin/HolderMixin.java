package net.nikdo53.neobackports.mixin;

import net.minecraft.core.Holder;
import net.nikdo53.neobackports.extensions.IDataMapHolderExtension;
import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Holder.class)
public interface HolderMixin<T> extends IDataMapHolderExtension<T> {
    @Override
    @Nullable
    default <A> A getData(DataMapType<T, A> type) {
        return IDataMapHolderExtension.super.getData(type);
    }
}
