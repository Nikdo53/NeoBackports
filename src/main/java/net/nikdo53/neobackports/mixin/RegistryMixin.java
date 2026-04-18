package net.nikdo53.neobackports.mixin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryManager;
import net.nikdo53.neobackports.datamaps.DataMapType;
import net.nikdo53.neobackports.extensions.IRegistryDataMapExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(Registry.class)
public interface RegistryMixin<T> extends IRegistryDataMapExtension<T> {
    @Shadow
    ResourceKey<? extends Registry<T>> key();

    @Override
    default Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps() {
        return RegistryManager.ACTIVE.getRegistry(key()).getDataMaps();
    }

    @Override
    default <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> type) {
        return RegistryManager.ACTIVE.getRegistry(key()).getDataMap(type);
    }

    @Override
    default @Nullable <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        return RegistryManager.ACTIVE.getRegistry(key()).getData(type, key);
    }
}
