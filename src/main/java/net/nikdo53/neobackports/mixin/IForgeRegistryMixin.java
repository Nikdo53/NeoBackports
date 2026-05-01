package net.nikdo53.neobackports.mixin;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.IForgeRegistry;
import net.nikdo53.neobackports.extensions.IForgeRegistryExtension;
import net.nikdo53.neobackports.extensions.IRegistryDataMapExtension;
import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Mixin(IForgeRegistry.class)
public interface IForgeRegistryMixin<T> extends IRegistryDataMapExtension<T>, IForgeRegistryExtension<T> {
    @Override
    default Map<DataMapType<T, ?>, Map<ResourceKey<T>, ?>> getDataMaps() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    default <A> Map<ResourceKey<T>, A> getDataMap(DataMapType<T, A> type) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    @Nullable
    default <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    default HolderLookup.RegistryLookup<T> getRegistryLookup() {
        throw new IllegalStateException("not implemented");
    }
}
