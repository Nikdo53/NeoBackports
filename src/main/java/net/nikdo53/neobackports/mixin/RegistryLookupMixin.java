package net.nikdo53.neobackports.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.nikdo53.neobackports.extensions.IDataMapHolderExtension;
import net.nikdo53.neobackports.registry.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HolderLookup.RegistryLookup.class)
public class RegistryLookupMixin<T> implements IDataMapHolderExtension<T> {
    @Override
    public @Nullable <A> A getData(DataMapType<T, A> type) {
        return null;
    }
}
