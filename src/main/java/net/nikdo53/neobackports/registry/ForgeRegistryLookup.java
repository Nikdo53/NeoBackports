package net.nikdo53.neobackports.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.ForgeRegistry;
import net.nikdo53.neobackports.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class ForgeRegistryLookup<T> implements HolderLookup.RegistryLookup<T> {
    public final ForgeRegistry<T> registry;

    public ForgeRegistryLookup(ForgeRegistry<T> registry) {
        this.registry = registry;
    }

    @Override
    public ResourceKey<? extends Registry<? extends T>> key() {
        return registry.getRegistryKey();
    }

    @Override
    public @Nullable <A> A getData(DataMapType<T, A> type, ResourceKey<T> key) {
        return registry.getData(type, key);
    }

    @Override
    public Lifecycle registryLifecycle() {
        return Lifecycle.stable();
    }

    @Override
    public Stream<Holder.Reference<T>> listElements() {
        return registry.getEntries().stream().map(entry -> get(entry.getKey())).filter(Optional::isPresent).map(Optional::get);
    }

    @Override
    public Stream<HolderSet.Named<T>> listTags() {
        throw forgeException();
    }

    @Override
    public Optional<Holder.Reference<T>> get(ResourceKey<T> resourceKey) {
        Optional<Holder<T>> holder = registry.getHolder(resourceKey.location());
        if (holder.isEmpty()) {
            return Optional.empty();
        }
        if (holder.get() instanceof Holder.Reference<T> reference) {
            return Optional.of(reference);
        }

        throw new IllegalStateException("Registry holder is not a reference, this should never happen");
    }

    @Override
    public Optional<HolderSet.Named<T>> get(TagKey<T> tagKey) {
        throw forgeException();
    }

    public static UnsupportedOperationException forgeException() {
        return new UnsupportedOperationException("Tried getting tags on a forge registry holder, this is not supported because forge registries suck");
    }
}
